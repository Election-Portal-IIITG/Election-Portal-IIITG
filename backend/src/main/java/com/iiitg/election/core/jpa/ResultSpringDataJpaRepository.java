package com.iiitg.election.core.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iiitg.election.core.Result;

public interface ResultSpringDataJpaRepository extends JpaRepository<Result, String> {
	List<Result> findByPosition_PositionName(String positionName);
	
    @Query("SELECT r FROM Result r WHERE FUNCTION('YEAR', r.election.electionDate) = :year")
    List<Result> findByElectionYear(@Param("year") int year);
    
    @Query("SELECT r FROM Result r WHERE FUNCTION('YEAR', r.election.electionDate) = :year AND r.position.positionName = :positionName")
    List<Result> findByElectionYearAndPositionName(@Param("year") int year, @Param("positionName") String positionName);
}
