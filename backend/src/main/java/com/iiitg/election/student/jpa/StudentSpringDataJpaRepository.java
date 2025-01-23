package com.iiitg.election.student.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iiitg.election.student.Student;

public interface StudentSpringDataJpaRepository extends JpaRepository<Student, String> {

}
