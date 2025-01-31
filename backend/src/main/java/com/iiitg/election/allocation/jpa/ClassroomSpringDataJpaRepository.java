package com.iiitg.election.allocation.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iiitg.election.allocation.Classroom;

public interface ClassroomSpringDataJpaRepository extends JpaRepository<Classroom, String> {
	Classroom findByClassroomName(String classroomName);
	
	List<Classroom> findByIsAvailable(Boolean isAvailable);

	boolean existsByClassroomName(String classroomName);
}
