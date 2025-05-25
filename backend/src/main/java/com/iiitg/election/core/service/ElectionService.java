package com.iiitg.election.core.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.iiitg.election.core.Election;
import com.iiitg.election.core.jpa.ElectionSpringDataJpaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ElectionService {

	@Autowired
	private ElectionSpringDataJpaRepository electionRepository;
	
	/**
	 * Creates a new {@link Election} scheduled for the specified date.
	 *
	 * <p>This method performs several validations before creating a new election:
	 * <ul>
	 *   <li>Checks for data integrity by ensuring no other election is scheduled on the same date.</li>
	 *   <li>Validates that the election date is not in the past.</li>
	 * </ul>
	 * If any of these validations fail, an appropriate exception is thrown.
	 *
	 * @param electionDate the date for the new election. Must not be null and must be a future date.
	 * @return the newly created Election object.
	 * @throws DataIntegrityViolationException if an election already exists on the specified date.
	 * @throws IllegalArgumentException if the election date is in the past.
	 *
	 * @author Ishaan Das
	 */
	public Election createElection(LocalDate electionDate) {
		
		// Data integrity check
	    if (electionRepository.existsByElectionDate(electionDate)) {
	        throw new DataIntegrityViolationException("An election already exists on this date");
	    }
	    
	    // Back-date validation
	    if (electionDate.isBefore(LocalDate.now())) {
	        throw new IllegalArgumentException("Election date cannot be in the past");
	    }
	    
	    // Create and save
		Election election = new Election(electionDate);
		electionRepository.save(election);
		
		return election;
	}
	
	/**
	 * Updates the date of an existing {@link Election} identified by its ID.
	 *
	 * <p>This method performs several validations before updating the election date:
	 * <ul>
	 *   <li>Ensures the election exists by retrieving it from the repository.</li>
	 *   <li>Checks that the election is not already marked as completed.</li>
	 *   <li>Validates data integrity by ensuring no other election is scheduled on the new date.</li>
	 *   <li>Ensures the new election date is not in the past.</li>
	 * </ul>
	 * If any of these validations fail, an appropriate exception is thrown.
	 *
	 * @param id              the ID of the election to update. Must not be null.
	 * @param newElectionDate the new date for the election. Must not be null and must be a future date.
	 * @return the updated Election object.
	 * @throws EntityNotFoundException if no election is found with the specified ID.
	 * @throws IllegalStateException if the election is already completed.
	 * @throws DataIntegrityViolationException if an election already exists on the new date.
	 * @throws IllegalArgumentException if the new election date is in the past.
	 *
	 * @author Ishaan Das
	 */
	public Election updateElectionDate(String id, LocalDate newElectionDate) {
	    // Proper exception handling
	    Election election = electionRepository.findById(id)
	        .orElseThrow(() -> new EntityNotFoundException("Election not found with id: " + id));
	    
	    // Business logic validation
	    if (Boolean.TRUE.equals(election.getIsComplete())) {
	        throw new IllegalStateException("Cannot update date for completed election");
	    }
	    
	    // Data integrity check
	    if (electionRepository.existsByElectionDateAndIdNot(newElectionDate, id)) {
	        throw new DataIntegrityViolationException("An election already exists on this date");
	    }
	    
	    // Back-date validation
	    if (newElectionDate.isBefore(LocalDate.now())) {
	        throw new IllegalArgumentException("Election date cannot be in the past");
	    }
	    
	    // Update and save
	    election.setElectionDate(newElectionDate);
	    return electionRepository.save(election);
	}
	
}
