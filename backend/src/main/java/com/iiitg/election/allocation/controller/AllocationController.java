package com.iiitg.election.allocation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iiitg.election.allocation.dto.AllocationDto;
import com.iiitg.election.allocation.service.AllocationService;

@RestController
@RequestMapping("/api/allocations")
public class AllocationController {
	
	private AllocationService allocationService;

	public AllocationController(AllocationService allocationService) {
		super();
		this.allocationService = allocationService;
	}
	
	@GetMapping("")
	public List<AllocationDto> listAllocations(@RequestParam(value = "classroomName", required = false) String classroomName) {
	    if (classroomName != null) {
	        return allocationService.getAllocationsByClassroom(classroomName);
	    }
	    return allocationService.getAllocations();
	}
}
