package com.iiitg.election.allocation.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iiitg.election.allocation.Classroom;

public interface ClassroomSpringDataJpaRepository extends JpaRepository<Classroom, String> {

}
