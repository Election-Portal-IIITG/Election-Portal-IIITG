package com.iiitg.election.electionManager.controller;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iiitg.election.electionManager.ElectionManager;
import com.iiitg.election.electionManager.service.ElectionManagerService;
import com.iiitg.election.services.EmailService.EmailRequest;
import com.iiitg.election.services.EmailService.EmailService;
import com.iiitg.election.services.FileService.FileService;
import com.iiitg.election.services.FileService.FileType;

import jakarta.validation.Valid;

@RestController
public class ElectionManagerController {

	private ElectionManagerService electionManagerService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private EmailService emailService;

	public ElectionManagerController(ElectionManagerService electionManagerService) {
		super();
		this.electionManagerService = electionManagerService;
	}

	@GetMapping("/get")
	public void get() {

	}

	@PostMapping("upload-voter-list")
	public ResponseEntity<String> uploadVoterList(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a valid file.");
        }

        try {
//            electionManagerService.processVoterList(file);
        	fileService.uploadFile(file, FileType.CSV, "abc");
            return ResponseEntity.ok("File processed successfully!\n");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

	}
	
	@DeleteMapping("/files/{fileId}")
	public ResponseEntity<String> deleteFile(@PathVariable("fileId") String fileId) throws FileNotFoundException {
		boolean deleted = fileService.deleteFile(fileId);
	    return ResponseEntity.ok("File deleted successfully.");
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
	
	@GetMapping("api/manager/test")
	@PreAuthorize("hasRole('ELECTION_MANAGER')")
	public String test() {
		return "secured";
	}
}
