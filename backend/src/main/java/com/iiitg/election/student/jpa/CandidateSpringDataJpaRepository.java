package com.iiitg.election.student.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iiitg.election.student.Candidate;

public interface CandidateSpringDataJpaRepository extends JpaRepository<Candidate, String> {

}
