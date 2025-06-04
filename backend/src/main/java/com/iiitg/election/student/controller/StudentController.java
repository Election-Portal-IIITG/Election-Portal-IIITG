package com.iiitg.election.student.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iiitg.election.jwt.JwtService;
import com.iiitg.election.student.Candidate;
import com.iiitg.election.student.Student;
import com.iiitg.election.student.service.StudentService;
import com.iiitg.election.validation.authentication.LoginValidation;
import com.iiitg.election.validation.candidate.CandidatureValidation;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/students")
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	
    @Autowired
    private JwtService jwtService;

	@PostMapping("/login")
	public ResponseEntity<String> loginStudent(@RequestBody @Validated(LoginValidation.class) Student student) {
	    String token = studentService.verify(student);
	    return ResponseEntity.ok(token);
	}
	
	@PostMapping("/nominate")
	@PreAuthorize("hasRole('STUDENT')")
	public ResponseEntity<Student> nominate(@RequestBody @Validated(CandidatureValidation.class) Candidate candidate, HttpServletRequest httpRequest){
		
        // Extract JWT token from Authorization header
        String authHeader = httpRequest.getHeader("Authorization");

        String token = authHeader.substring(7);
        String candidateEmail = jwtService.extractUserName(token);
        
        if (candidateEmail == null || candidateEmail.isEmpty()) {
            throw new IllegalArgumentException("Invalid token or email not found");
        }
        
        Student nominee = studentService.submitCandidature(candidateEmail, candidate);
        
		return ResponseEntity.ok(nominee);
	}
}
