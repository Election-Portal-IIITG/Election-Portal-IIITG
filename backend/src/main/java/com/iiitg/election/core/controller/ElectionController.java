package com.iiitg.election.core.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iiitg.election.core.Election;
import com.iiitg.election.core.service.ElectionService;

@RestController
@RequestMapping("/api/elections")
public class ElectionController {
	
	@Autowired
	private ElectionService electionService;
	
	/**
	 * Handles POST requests to create a new election.
	 *
	 * <p>This endpoint accepts an Election object in the request body, extracts the election date,
	 * and delegates the creation process to the election service. Upon successful creation,
	 * it returns the newly created Election object with an HTTP status of 201 (CREATED).
	 *
	 * @param election the Election object containing the election date. Must not be null.
	 * @return a ResponseEntity containing the created Election object and an HTTP status of CREATED.
	 *
	 * @author Ishaan Das
	 */
    @PostMapping("/create-new-election")
    public ResponseEntity<Election> createElection(@RequestBody Election election) {
        Election savedElection = electionService.createElection(election.getElectionDate());
        return new ResponseEntity<>(savedElection, HttpStatus.CREATED);
    }
    
    /**
     * Handles PATCH requests to update the date of an existing election.
     *
     * <p>This endpoint accepts a path variable representing the election ID and a query parameter
     * for the new election date. It delegates the update process to the election service and
     * returns the updated Election object with an HTTP status of 200 (OK) upon success.
     *
     * @param id           the ID of the election to update. Must not be null.
     * @param electionDate  the new election date in the format "yyyy-MM-dd". Must not be null.
     * @return a ResponseEntity containing the updated Election object and an HTTP status of OK.
     *
     * @author Ishaan Das
     */
    @PatchMapping("/change-date/{id}")
    public ResponseEntity<Election> updateElectionDate(
            @PathVariable("id") String id,
            @RequestParam("electionDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate electionDate) {
        
        Election updatedElection = electionService.updateElectionDate(id, electionDate);
        return ResponseEntity.ok(updatedElection);
    }
}
