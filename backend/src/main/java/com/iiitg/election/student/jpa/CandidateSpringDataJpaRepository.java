package com.iiitg.election.student.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iiitg.election.student.Candidate;

public interface CandidateSpringDataJpaRepository extends JpaRepository<Candidate, String> {
	
	Candidate findByStudent_StudentEmailId(String studentEmailId);
	Candidate findByStudent_RollNumber(String rollNumber);
	
	List<Candidate> findByContestingPosition_PositionName(String positionName);
	
	List<Candidate> findByIsEligibleTrue();
	List<Candidate> findByIsEligibleFalse();
	
	List<Candidate> findByIsNominatedTrue();
	List<Candidate> findByIsNominatedFalse();
	
	List<Candidate> findByIsApprovedTrue();
	List<Candidate> findByIsApprovedFalse();
	
	boolean existsByStudent_StudentEmailId(String studentEmailId);
}
