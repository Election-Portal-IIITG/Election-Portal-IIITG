package com.iiitg.election.core.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iiitg.election.core.Election;

public interface ElectionSpringDataJpaRepository extends JpaRepository<Election, String>{
	
	@Query("SELECT e FROM Election e WHERE YEAR(e.electionDate) = :year")
    List<Election> findByYear(@Param("year") int year);
}
