package com.iiitg.election.core;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.NotNull;

//@Entity(name = "result")
public class Result {
	
	@EmbeddedId
	private ResultId id;
	
	@NotNull(message = "Roll Number cannot be null")
	@Column(name = "roll_number", nullable = false, unique = true)
	private String rollNumber;
	
	@NotNull(message = "Firstname cannot be null")
	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name")
	private String lastName;
	
	@NotNull(message = "Votes cannot be null")
	private int votes;
	
	@ManyToOne
    @MapsId("position_id")
    @JoinColumn(name = "position_id")
    private Position position;
    
    @ManyToOne
    @MapsId("election_id")
    @JoinColumn(name = "election_id")
    private Election election;
	
	public Result() {
		super();
	}

	public Result(ResultId id, @NotNull(message = "Roll Number cannot be null") String rollNumber,
			@NotNull(message = "Firstname cannot be null") String firstName, String lastName,
			@NotNull(message = "Votes cannot be null") int votes) {
		super();
		this.id = id;
		this.rollNumber = rollNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.votes = votes;
	}

	public ResultId getId() {
		return id;
	}

	public void setId(ResultId id) {
		this.id = id;
	}

	public String getRollNumber() {
		return rollNumber;
	}

	public void setRollNumber(String rollNumber) {
		this.rollNumber = rollNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}
	
	
	
	
}
