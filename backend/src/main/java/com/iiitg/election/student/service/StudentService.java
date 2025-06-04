package com.iiitg.election.student.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.iiitg.election.jwt.JwtService;
import com.iiitg.election.student.Candidate;
import com.iiitg.election.student.Student;
import com.iiitg.election.student.jpa.CandidateSpringDataJpaRepository;
import com.iiitg.election.student.jpa.StudentSpringDataJpaRepository;
import com.iiitg.election.validation.authentication.LoginValidation;

import jakarta.persistence.EntityNotFoundException;

@Service
public class StudentService {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private StudentSpringDataJpaRepository studentRepository;
	
	@Autowired
	private CandidateSpringDataJpaRepository candidateRepository;
	
	@Autowired
	private JwtService jwtService;
	
	public String verify(@Validated(LoginValidation.class) Student student) {
	    // Fast check
	    if (!studentRepository.existsByStudentEmailId(student.getStudentEmailId())) {
	        throw new BadCredentialsException("No student account found with this email.");
	    }

	    // Attempt authentication (only if student exists)
	    try {
	        Authentication authentication = authManager.authenticate(
	            new UsernamePasswordAuthenticationToken(
	            		student.getStudentEmailId(),
	            		student.getPassword()
	            )
	        );
	        
	        if (authentication.isAuthenticated()) {
	        	return jwtService.generateToken(student.getStudentEmailId());
	        }
	        
	        throw new BadCredentialsException("Authentication failed");
	        
	    } catch (AuthenticationException e) {
	    	System.err.println(e);
	        throw new BadCredentialsException("Invalid email or password.");
	    }
	}
	
	@Transactional
	public Student submitCandidature(String contestantEmailId, Candidate candidateBody) {
        Student contestant = studentRepository.findByStudentEmailId(contestantEmailId);
        
        if(contestant == null) {
        	new EntityNotFoundException("No student found with emailId: "+contestantEmailId);
        }
        
        if(contestant.getCandidate() != null) {
        	throw new IllegalStateException("Candidate is already nominated");
        }
        
        Candidate newCandidate = new Candidate(candidateBody.getProgramme(),
        		candidateBody.getGraduatingYear(),
        		candidateBody.getStudentImageURL(),
        		candidateBody.getAbout(),
        		candidateBody.getContestingPosition());
        
        candidateRepository.save(newCandidate);
        
        contestant.setCandidate(newCandidate);
        studentRepository.save(contestant);
        
        return contestant;
	}
}
