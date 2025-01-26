package com.iiitg.election.allocation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iiitg.election.allocation.Classroom;
import com.iiitg.election.allocation.service.ClassroomService;

@RestController
@RequestMapping("/classrooms")
public class ClassroomController {
	
	private ClassroomService classroomService;
	
	public ClassroomController(ClassroomService classroomService) {
		super();
		this.classroomService = classroomService;
	}
	
	@GetMapping("/list")
	public List<Classroom> listClassroom() {
		return classroomService.listAllClassrooms();
	}
	
}
