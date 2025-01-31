package com.iiitg.election.allocation.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.iiitg.election.allocation.Classroom;
import com.iiitg.election.allocation.dto.ClassroomDto;
import com.iiitg.election.allocation.dto.ClassroomDtoMapper;
import com.iiitg.election.allocation.exceptions.DuplicateClassroomNameException;
import com.iiitg.election.allocation.jpa.ClassroomSpringDataJpaRepository;
import com.iiitg.election.exceptions.ResourceNotFoundException;

@Service
public class ClassroomService {
	private ClassroomSpringDataJpaRepository classroomRepository;

	public ClassroomService(ClassroomSpringDataJpaRepository classroomRepository) {
		super();
		this.classroomRepository = classroomRepository;
	}
	
	public List<ClassroomDto> listAllClassrooms(Boolean available) {
        List<Classroom> classrooms;
        
        if (available == null) {
            classrooms = classroomRepository.findAll();
        } else {
            classrooms = classroomRepository.findByIsAvailable(available);
        }
        
        return classrooms.stream()
                .map(ClassroomDtoMapper::toDto)
                .collect(Collectors.toList());
    }
	
    public ClassroomDto createClassroom(String classroomName, Integer capacity) {
        if (classroomRepository.existsByClassroomName(classroomName)) {
            throw new DuplicateClassroomNameException("Classroom name '" + classroomName + "' already exists.");
        }
        
        Classroom classroom = new Classroom();
        classroom.setClassroomName(classroomName);
        classroom.setCapacity(capacity);

        classroom.setAvailable(false);
            
        Classroom savedClassroom = classroomRepository.save(classroom);
        return ClassroomDtoMapper.toDto(savedClassroom);
    }
    
    public ClassroomDto updateAvailability(String classroomName, boolean available) {
        Classroom classroom = classroomRepository.findByClassroomName(classroomName);
        
        if (classroom == null) {
            throw new ResourceNotFoundException("Classroom not found with name: " + classroomName);
        }

        classroom.setAvailable(available);
        Classroom updatedClassroom = classroomRepository.save(classroom);
        return ClassroomDtoMapper.toDto(updatedClassroom);
    }
    
    public void deleteClassroom(String classroomName) throws ResourceNotFoundException {
        Classroom classroom = classroomRepository.findByClassroomName(classroomName);
        
        if (classroom == null) {
            throw new ResourceNotFoundException("Classroom not found with name: " + classroomName);
        }
        classroomRepository.delete(classroom);
    }
}
