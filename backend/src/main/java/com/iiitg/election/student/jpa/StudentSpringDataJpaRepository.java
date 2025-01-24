package com.iiitg.election.student.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iiitg.election.student.Student;

public interface StudentSpringDataJpaRepository extends JpaRepository<Student, String> {
	
	Student findByStudentEmailId(String studentEmailId);
	Student findByRollNumber(String rollNumber);
	
    List<Student> findByOnCampusTrue();
    List<Student> findByOnCampusFalse();
    
    List<Student> findByHasVotedTrue();
    List<Student> findByHasVotedFalse();
}
