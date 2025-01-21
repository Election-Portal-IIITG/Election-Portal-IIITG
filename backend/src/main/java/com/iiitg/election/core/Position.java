package com.iiitg.election.core;

import java.util.List;

import com.iiitg.election.student.Candidate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@Entity(name = "position")
public class Position {

	@Id
	@NotNull(message = "Position Id cannot be null")
	@Column(name = "position_id", unique = true)
	private String positionId;
	
	@NotNull(message = "Position Name cannot be null")
	@Column(name = "position_name", unique = true)
	private String positionName;
	
	@OneToOne(mappedBy = "position")
    private Candidate candidate;
	
	@OneToOne(mappedBy = "position")
    private Winner winner;
	
	@OneToMany(mappedBy = "position")
    private List<Result> results;
	
	
}
