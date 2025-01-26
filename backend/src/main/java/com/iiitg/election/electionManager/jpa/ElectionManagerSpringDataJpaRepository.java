package com.iiitg.election.electionManager.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iiitg.election.electionManager.ElectionManager;

public interface ElectionManagerSpringDataJpaRepository extends JpaRepository<ElectionManager, String> {
	ElectionManager findByManagerEmailId(String emailId);
}
