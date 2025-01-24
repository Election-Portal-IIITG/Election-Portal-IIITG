package com.iiitg.election.core.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iiitg.election.core.Winner;

public interface WinnerSpringDataJpaRepository extends JpaRepository<Winner, String>{
	
	Winner findByEmailId(String emailId);
	Winner findByRollNumber(String rollNumber);
	Winner findByProgramme(String programme);
	Winner findByWinningPosition_PositionName(String positionName);
}
