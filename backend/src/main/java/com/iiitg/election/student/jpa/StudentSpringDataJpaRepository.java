package com.iiitg.election.student.jpa;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iiitg.election.allocation.Slot;
import com.iiitg.election.student.Student;

public interface StudentSpringDataJpaRepository extends JpaRepository<Student, String> {
	
	Student findByStudentEmailId(String studentEmailId);
	Student findByRollNumber(String rollNumber);
	
    List<Student> findByOnCampus(boolean onCampus);
    
    List<Student> findByHasVotedTrue();
    List<Student> findByHasVotedFalse();
    
    List<Student> findBySlotClassroom_Classroom_ClassroomName(String classroomName);
    List<Student> findBySlotClassroom_Slot(Slot slot);
    List<Student> findBySlotClassroom_Classroom_ClassroomNameAndSlotClassroom_Slot(String classroomName, Slot slot);
    
    @Query("SELECT s.studentEmailId from Student s")
    Set<String> findAllEmails();
    
    @Query("SELECT s.rollNumber FROM Student s")
    Set<String> findAllRollNumbers();
}
