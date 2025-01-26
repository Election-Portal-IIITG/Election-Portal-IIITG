package com.iiitg.election.electionManager;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.iiitg.election.electionManager.jpa.ElectionManagerSpringDataJpaRepository;
import com.iiitg.election.jwt.JwtService;

import jakarta.validation.Valid;

@Service
public class ElectionManagerService {
	
	private ElectionManagerSpringDataJpaRepository electionManagerRepo;
	private BCryptPasswordEncoder encoder;
	private AuthenticationManager authManager;
	private JwtService jwtService;
	
	public ElectionManagerService(ElectionManagerSpringDataJpaRepository electionManagerRepo, BCryptPasswordEncoder encoder, AuthenticationManager authManager, JwtService jwtService) {
		super();
		this.electionManagerRepo = electionManagerRepo;
		this.encoder = encoder;
		this.authManager = authManager;
		this.jwtService = jwtService;
	}



	public ElectionManager register(@Valid ElectionManager manager) {
		manager.setPassword(encoder.encode(manager.getPassword()));
		return electionManagerRepo.save(manager);
	}



	public String verify(@Valid ElectionManager manager) {
		Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(manager.getManagerEmailId(), manager.getPassword()));
		if(authentication.isAuthenticated()) {
			return jwtService.generateToken(manager.getManagerEmailId());
		}
		return "failure";
	}
}
