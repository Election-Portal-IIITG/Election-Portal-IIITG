package com.iiitg.election.core;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

//@Entity(name = "election")
public class Election {

	@Id
	@NotNull(message = "Election ID cannot be null")
	@Column(name = "election_id", nullable = false, unique = true)
	private String election_id;
	
	@Column(name = "election_date", nullable = false)
	private LocalDate electionDate;
	
	@Column(name = "election_timestamp", nullable = false)
    private LocalDateTime electionTimestamp;
	
	@OneToMany(mappedBy = "election")
	private List<Result> results;

}
