package com.iiitg.election.electionManager.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iiitg.election.electionManager.ElectionManager;
import com.iiitg.election.electionManager.service.ElectionManagerService;

import jakarta.validation.Valid;

@RestController
public class ElectionManagerController {

	private ElectionManagerService electionManagerService;

	public ElectionManagerController(ElectionManagerService electionManagerService) {
		super();
		this.electionManagerService = electionManagerService;
	}

	@GetMapping("/get")
	public void get() {

	}

	@PostMapping("/upload-voter-list")
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
		try {
			String token = electionManagerService.verify(manager);
			return ResponseEntity.ok(token);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("api/manager/test")
	@PreAuthorize("hasRole('ELECTION_MANAGER')")
	public String test() {
		return "secured";
	}
}
