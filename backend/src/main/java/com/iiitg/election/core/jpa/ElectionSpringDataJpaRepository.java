package com.iiitg.election.core.jpa;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iiitg.election.core.Election;

public interface ElectionSpringDataJpaRepository extends JpaRepository<Election, String>{
	
	@Query("SELECT e FROM Election e WHERE YEAR(e.electionDate) = :year")
    List<Election> findByYear(@Param("year") int year);
	
	/**
	 * Checks for an {@link Election} entity with the given election date and a different ID.
	 *
	 * @param electionDate the election date to check. Must not be null.
	 * @param id           the ID to exclude from the check. Must not be null.
	 * @return true if such an entity exists, false otherwise.
	 */
	boolean existsByElectionDateAndIdNot(LocalDate electionDate, String id);
	
	/**
	 * Checks for an {@link Election} entity with the given election date.
	 *
	 * @param electionDate the election date to check. Must not be null.
	 * @return true if such an entity exists, false otherwise.
	 */
	boolean existsByElectionDate(LocalDate electionDate);
}
