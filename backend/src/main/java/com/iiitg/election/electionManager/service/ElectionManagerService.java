package com.iiitg.election.electionManager.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.iiitg.election.student.jpa.CandidateSpringDataJpaRepository;
import com.iiitg.election.student.jpa.StudentSpringDataJpaRepository;

import jakarta.validation.Valid;

@Service
public class ElectionManagerService {
	@Autowired
	private ElectionManagerSpringDataJpaRepository electionManagerRepo;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private StudentSpringDataJpaRepository studentRepo;
	
	@Autowired
	private CandidateSpringDataJpaRepository candidateRepo;
	
	@Autowired
	private StudentUploadService studentUploadService;

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
			Path tempFile = null;
			try {
				tempFile = Files.createTempFile("voters-", ".csv");
				Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
				studentUploadService.processFileAndCreateStudentsAsync(tempFile, filename);

			} catch (IOException e) {
				if(tempFile != null) {
					try {
						Files.deleteIfExists(tempFile);
					}
					catch (IOException ex){
						
					}
				}
				throw new IOException("Could not save file or start processing: " + e.getMessage(), e);
			}
		}
	}
}
