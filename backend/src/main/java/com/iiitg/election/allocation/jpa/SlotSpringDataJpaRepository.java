package com.iiitg.election.allocation.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iiitg.election.allocation.Slot;

public interface SlotSpringDataJpaRepository extends JpaRepository<Slot, String>{
	
	List<Slot> findByClassroomSlots_Classroom_ClassroomName(String classroomName);
	List<Slot> findByClassroomSlots_Classroom_AssignedFaculty_FacultyEmailId(String facultyEmailId);
}
