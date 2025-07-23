package com.iiitg.election.student.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iiitg.election.jwt.JwtService;
import com.iiitg.election.student.Candidate;
import com.iiitg.election.student.service.CandidateService;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;
    
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> registerCandidate(
            @RequestHeader("Authorization") String authToken,
            @ModelAttribute CandidateRegistrationDto candidateRequest) throws IOException {
        
        // Extract student Email ID from JWT for unique filename
        String studentEmailId = jwtService.extractUserName(authToken.replace("Bearer ", "").trim());
            
        Candidate registeredCandidate = candidateService.registerCandidate(studentEmailId, candidateRequest);
        return ResponseEntity.ok(registeredCandidate);
    }
    
    @PostMapping("/request-nomination/{nominatorEmailId}")
    public ResponseEntity<String> requestNomination(
            @RequestHeader("Authorization") String authToken,
            @PathVariable("nominatorEmailId") String nominatorEmailId) {

        // Extract candidate Email ID from JWT
        String candidateEmailId = jwtService.extractUserName(authToken.replace("Bearer ", "").trim());
            
        String result = candidateService.requestNomination(candidateEmailId, nominatorEmailId);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/nomination-response")
    public ResponseEntity<String> handleNominationResponse(
            @RequestParam String token,
            @RequestParam boolean accept) {
    	
        String result = candidateService.handleNominationResponse(token, accept);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/request-approval/{facultyEmailId}")
    public ResponseEntity<String> requestApproval(
            @RequestHeader("Authorization") String authToken,
            @PathVariable("facultyEmailId") String facultyEmailId) {

        // Extract candidate Email ID from JWT
        String candidateEmailId = jwtService.extractUserName(authToken.replace("Bearer ", "").trim());
            
        String result = candidateService.requestApproval(candidateEmailId, facultyEmailId);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/approval-response")
    public ResponseEntity<String> handleApprovalResponse(
            @RequestParam String token,
            @RequestParam boolean approve) {
    	
        String result = candidateService.handleApprovalResponse(token, approve);
        return ResponseEntity.ok(result);
    }
}
