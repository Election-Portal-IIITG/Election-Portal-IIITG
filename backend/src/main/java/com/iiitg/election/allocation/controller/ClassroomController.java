package com.iiitg.election.allocation.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iiitg.election.allocation.dto.ClassroomDto;
import com.iiitg.election.allocation.exceptions.DuplicateClassroomNameException;
import com.iiitg.election.allocation.service.ClassroomService;
import com.iiitg.election.exceptions.ResourceNotFoundException;
import com.iiitg.election.payload.response.ErrorResponse;

@RestController
@RequestMapping("/classrooms")
public class ClassroomController {
	
	private ClassroomService classroomService;
	
	public ClassroomController(ClassroomService classroomService) {
		super();
		this.classroomService = classroomService;
	}
	
	@GetMapping("/list")
    public List<ClassroomDto> listClassroom(@RequestParam(value = "available", required = false) Boolean available) {
        return classroomService.listAllClassrooms(available);
    }
	
    @PostMapping("/add")
    public ResponseEntity<?> createClassroom(@RequestBody ClassroomDto classroomDto) {
        try {
            ClassroomDto createdClassroom = classroomService.createClassroom(
                classroomDto.getClassroomName(), 
                classroomDto.getCapacity()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdClassroom);
        } catch (DuplicateClassroomNameException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }
    
    @PatchMapping("/{classroomName}/update-availability")
    public ResponseEntity<?> updateAvailability(@PathVariable("classroomName") String classroomName, @RequestBody Map<String, Object> payload) {
        try {
            boolean available = Boolean.parseBoolean(payload.get("available").toString());
            
            ClassroomDto updatedClassroom = classroomService.updateAvailability(classroomName, available);
            return ResponseEntity.ok(updatedClassroom);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!"));
        }
    }
    
    @DeleteMapping("/{classroomName}")
    public ResponseEntity<?> deleteClassroom(@PathVariable("classroomName") String classroomName){
    	try {
    		classroomService.deleteClassroom(classroomName);
    		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    	} catch(ResourceNotFoundException e) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage()));
    	} catch (Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!"));
    	}
    }

}
