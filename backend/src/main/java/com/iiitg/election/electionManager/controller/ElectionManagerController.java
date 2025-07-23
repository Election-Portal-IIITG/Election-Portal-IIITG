package com.iiitg.election.electionManager.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iiitg.election.electionManager.ElectionManager;
import com.iiitg.election.electionManager.dto.EligibilityUpdateRequest;
import com.iiitg.election.electionManager.service.ElectionManagerService;
import com.iiitg.election.services.EmailService.EmailRequest;
import com.iiitg.election.services.EmailService.EmailService;
import com.iiitg.election.student.dto.ApprovedCandidateDTO;
import com.iiitg.election.student.service.CandidateService;

import jakarta.validation.Valid;

@RestController
public class ElectionManagerController {
	
	@Autowired
	private ElectionManagerService electionManagerService;
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private CandidateService candidateService;

	@GetMapping("/get")
	public void get() {

	}

	@PostMapping("upload-voter-list")
	public ResponseEntity<String> uploadVoterList(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a valid file.");
        }

        try {
            electionManagerService.processVoterList(file);
            return ResponseEntity.ok("File processed successfully!\n");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing file: " + e.getMessage());
        }

	}

	@PostMapping("register-manager")
	public ResponseEntity<ElectionManager> registerElectionManager(@RequestBody @Valid ElectionManager manager) {
		return ResponseEntity.ok(electionManagerService.register(manager));
	}

	@PostMapping("login-manager")
	public ResponseEntity<String> loginElectionManager(@RequestBody @Valid ElectionManager manager) {
	    String token = electionManagerService.verify(manager);
	    return ResponseEntity.ok(token);
	}
	
	@PostMapping("send-mail")
	public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
		try {
			emailService.sendEmail(emailRequest);
			return ResponseEntity.ok("Mail sent");
		}
		catch (Exception e) {
			return ResponseEntity.internalServerError()
                    .body("Failed to send message: " + e.getMessage());
		}
	}
	
	@GetMapping("/candidates-list")
	public ResponseEntity<List<ApprovedCandidateDTO>> getAllApprovedCandidates() {
	    List<ApprovedCandidateDTO> approvedCandidates = candidateService.getAllApprovedCandidates();
	    return ResponseEntity.ok(approvedCandidates);
	}
	
	@PatchMapping("/candidates-list/update-eligibility")
	public ResponseEntity<?> updateCandidateEligibility(
	        @RequestBody EligibilityUpdateRequest request) {

	    candidateService.updateCandidateEligibility(
	        request.getCandidateEmailId(), 
	        request.getIsEligible());

	    Map<String, Object> response = new HashMap<>();
	    response.put("status", "SUCCESS");
	    response.put("message", "Candidate eligibility updated successfully");
	    response.put("emailId", request.getCandidateEmailId());
	    response.put("isEligible", request.getIsEligible());

	    return ResponseEntity.ok(response);
	}


	
	@GetMapping("api/manager/test")
	@PreAuthorize("hasRole('ELECTION_MANAGER')")
	public String test() {
		return "secured";
	}
}
