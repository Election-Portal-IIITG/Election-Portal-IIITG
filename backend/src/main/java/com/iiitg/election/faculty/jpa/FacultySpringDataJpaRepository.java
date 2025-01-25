package com.iiitg.election.faculty.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iiitg.election.allocation.Slot;
import com.iiitg.election.faculty.Faculty;

public interface FacultySpringDataJpaRepository extends JpaRepository<Faculty, String> {
	
	Faculty findByFacultyEmailId(String facultyEmailId);
	
	List<Faculty> findByIsAvailableTrue();
	List<Faculty> findByIsAvailableFalse();
	
	Faculty findByAssignedClassroom_ClassroomName(String classroomName);

	List<Faculty> findByAssignedClassroom_ClassroomSlots_Slot(Slot slot);
}
