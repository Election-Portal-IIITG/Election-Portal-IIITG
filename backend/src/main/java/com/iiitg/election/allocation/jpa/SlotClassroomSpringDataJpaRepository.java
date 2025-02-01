package com.iiitg.election.allocation.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iiitg.election.allocation.Classroom;
import com.iiitg.election.allocation.SlotClassroom;

public interface SlotClassroomSpringDataJpaRepository extends JpaRepository<SlotClassroom, String>{
	
    @Query("SELECT sc FROM Student s "
            + "JOIN s.slotClassroom sc "
            + "WHERE s.studentEmailId = :studentEmailId")
    SlotClassroom findByStudentEmailId(@Param("studentEmailId") String studentEmailId);
    
    List<SlotClassroom> findByClassroom(Classroom classroom);
	
	List<SlotClassroom> findByClassroom_AssignedFaculty_FacultyEmailId(String facultyEmailId);
	
}