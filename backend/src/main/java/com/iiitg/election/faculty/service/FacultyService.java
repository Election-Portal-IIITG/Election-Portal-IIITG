package com.iiitg.election.faculty.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.iiitg.election.faculty.Faculty;
import com.iiitg.election.faculty.jpa.FacultySpringDataJpaRepository;
import com.iiitg.election.jwt.JwtService;

import jakarta.validation.Valid;

@Service
public class FacultyService {

	FacultySpringDataJpaRepository facRepo;
	private BCryptPasswordEncoder encoder;
	private AuthenticationManager authManager;
	private JwtService jwtService;

	public FacultyService(FacultySpringDataJpaRepository facRepo, BCryptPasswordEncoder encoder, AuthenticationManager authManager, JwtService jwtService) {
		super();
		this.facRepo = facRepo;
		this.encoder = encoder;
		this.authManager = authManager;
		this.jwtService = jwtService;
	}
	
	public Faculty registerFaculty(@Valid Faculty faculty) {
		faculty.setPassword(encoder.encode(faculty.getPassword()));
		faculty.setAvailable(true);
		return facRepo.save(faculty);
	}
	
	public String verify(String facultyEmailId, String password) throws Exception {
		try {
			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(facultyEmailId, password));
			if (authentication.isAuthenticated()) {
				return jwtService.generateToken(facultyEmailId);
			}	
		}
		catch (AuthenticationException e){
			throw new Exception("Invalid email or password");
		}
		return null;
	}
	
	
	
}
