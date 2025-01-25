package com.iiitg.election.allocation.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iiitg.election.allocation.SlotClassroom;

public interface SlotClassroomSpringDataJpaRepository extends JpaRepository<SlotClassroom, String>{
	
    @Query("SELECT sc FROM Student s "
            + "JOIN s.slotClassroom sc "
            + "WHERE s.studentEmailId = :studentEmailId")
    SlotClassroom findByStudentEmailId(String studentEmailId);
	
	List<SlotClassroom> findByClassroom_AssignedFaculty_FacultyEmailId(String facultyEmailId);
	
}