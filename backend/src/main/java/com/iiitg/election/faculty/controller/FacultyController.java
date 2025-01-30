package com.iiitg.election.faculty.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iiitg.election.faculty.Faculty;
import com.iiitg.election.faculty.dto.FacultyLoginRequest;
import com.iiitg.election.faculty.service.FacultyService;

import jakarta.validation.Valid;

@RestController
public class FacultyController {
	
	FacultyService facultyService;

	public FacultyController(FacultyService facultyService) {
		super();
		this.facultyService = facultyService;
	}

	@PostMapping("register-faculty")
	public Faculty registerFaculty(@RequestBody @Valid Faculty faculty) {
		return facultyService.registerFaculty(faculty);
	}
	
	@PostMapping("login-faculty")
	public ResponseEntity<String> loginFaculty(@RequestBody FacultyLoginRequest facultyRequest) {
		try {
			String token = facultyService.verify(facultyRequest.getFacultyEmailId(), facultyRequest.getPassword());
			return ResponseEntity.ok(token);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
}
