package com.iiitg.election.electionManager.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iiitg.election.electionManager.ElectionManager;
import com.iiitg.election.electionManager.jpa.ElectionManagerSpringDataJpaRepository;
import com.iiitg.election.jwt.JwtService;
import com.iiitg.election.student.jpa.StudentSpringDataJpaRepository;

import jakarta.validation.Valid;

@Service
public class ElectionManagerService {

	private ElectionManagerSpringDataJpaRepository electionManagerRepo;
	private BCryptPasswordEncoder encoder;
	private AuthenticationManager authManager;
	private JwtService jwtService;
	private StudentSpringDataJpaRepository studentRepo;

	public ElectionManagerService(ElectionManagerSpringDataJpaRepository electionManagerRepo,
			StudentSpringDataJpaRepository studentRepo, BCryptPasswordEncoder encoder,
			AuthenticationManager authManager, JwtService jwtService) {
		super();
		this.electionManagerRepo = electionManagerRepo;
		this.encoder = encoder;
		this.authManager = authManager;
		this.jwtService = jwtService;
		this.studentRepo = studentRepo;
	}

	public ElectionManager register(@Valid ElectionManager manager) {
		manager.setPassword(encoder.encode(manager.getPassword()));
		return electionManagerRepo.save(manager);
	}

//	public String verify(@Valid ElectionManager manager) {
//		Authentication authentication = authManager.authenticate(
//				new UsernamePasswordAuthenticationToken(manager.getManagerEmailId(), manager.getPassword()));
//		if (authentication.isAuthenticated()) {
//			return jwtService.generateToken(manager.getManagerEmailId());
//		}
//		return "failure";
//	}
	
	public String verify(@Valid ElectionManager manager) {
	    try {
	        Authentication authentication = authManager.authenticate(
	                new UsernamePasswordAuthenticationToken(manager.getManagerEmailId(), manager.getPassword()));
	        if (authentication.isAuthenticated()) {
	            return jwtService.generateToken(manager.getManagerEmailId());
	        }
	        // This shouldn't happen, but just in case
	        throw new BadCredentialsException("Authentication failed");
	    } catch (AuthenticationException e) {
	        throw new BadCredentialsException("Invalid email or password");
	    }
	}

	public void processVoterList(MultipartFile file) throws IOException {
		String filename = file.getOriginalFilename();
		if (filename == null || !filename.toLowerCase().endsWith(".csv")) {
			throw new IllegalArgumentException("Please upload a CSV file.");
		}
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
				CSVParser csvParser = new CSVParser(reader,
						CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build())) {
			Set<String> requiredHeaders = new HashSet<>(
					Arrays.asList("name", "email", "roll_number", "has_voted", "on_campus"));
			Set<String> actualHeaders = new HashSet<>(csvParser.getHeaderNames());

			if (!actualHeaders.containsAll(requiredHeaders)) {
				Set<String> missingHeaders = new HashSet<>(requiredHeaders);
				missingHeaders.removeAll(actualHeaders);
				throw new IllegalArgumentException("Missing required headers: " + missingHeaders);
			}
			StringBuilder preview = new StringBuilder("First 10 records:\n");
			int count = 0;
			for (CSVRecord record : csvParser) {
				if (count >= 10)
					break;

				preview.append(String.format("Record %d: %s, Email: %s, Roll: %s, Voted: %s, On Campus: %s%n",
						count + 1, record.get("name"), record.get("email"),
						record.get("roll_number"), record.get("has_voted"), record.get("on_campus")));
				count++;
			}
		}
	}
}
