package com.iiitg.election.core.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iiitg.election.core.Winner;

public interface WinnerSpringDataJpaRepository extends JpaRepository<Winner, String>{
	
	Winner findByEmailId(String emailId);
	Winner findByRollNumber(String rollNumber);
	List<Winner> findByProgramme(String programme);
	List<Winner> findByWinningPosition_PositionName(String positionName);
}
