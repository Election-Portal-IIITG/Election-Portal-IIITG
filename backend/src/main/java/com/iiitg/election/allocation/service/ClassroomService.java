package com.iiitg.election.allocation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.iiitg.election.allocation.Classroom;
import com.iiitg.election.allocation.jpa.ClassroomSpringDataJpaRepository;

@Service
public class ClassroomService {
	private ClassroomSpringDataJpaRepository classroomRepository;

	public ClassroomService(ClassroomSpringDataJpaRepository classroomRepository) {
		super();
		this.classroomRepository = classroomRepository;
	}
	
	public List<Classroom> listAllClassrooms() {
		return classroomRepository.findAll();
	}
}
