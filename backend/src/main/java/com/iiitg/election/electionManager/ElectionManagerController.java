package com.iiitg.election.electionManager;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	public String uploadVoterList(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
            return "Please upload a valid Excel file";
        }

        try {
            // Process the Excel file
            // You can add logic here to read the file contents
            byte[] bytes = file.getBytes();
            
            // Example: Print file details (replace with actual processing)
            System.out.println("File uploaded successfully: " + file.getOriginalFilename() + " (Size: " + bytes.length + " bytes)");
            return "File uploaded successfully: " + 
                   file.getOriginalFilename() + 
                   " (Size: " + bytes.length + " bytes)";

        } catch (IOException e) {
            return "Error uploading file: " + e.getMessage();
        }
		
	}
	
	
	@PostMapping("register-manager")
	public ResponseEntity<ElectionManager> registerElectionManager(@RequestBody @Valid ElectionManager manager) {
		return ResponseEntity.ok(electionManagerService.register(manager));
	}
	
	@PostMapping("login-manager")
	public String loginElectionManager(@RequestBody @Valid ElectionManager manager) {
		return electionManagerService.verify(manager);
	}
}
