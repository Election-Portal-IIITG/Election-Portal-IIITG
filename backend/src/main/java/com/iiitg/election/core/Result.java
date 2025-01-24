package com.iiitg.election.core;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.NotNull;

@Entity
public class Result {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	@NotNull(message = "Roll Number cannot be null")
	@Column(name = "roll_number", unique = true)
	private String rollNumber;
	
	@NotNull(message = "Name cannot be null")
	@Column(name = "name")
	private String name;
	
	@NotNull(message = "Votes cannot be null")
	@Column(name = "vote_count")
	private int voteCount;
	
	@ManyToOne
	@JoinColumn(name = "position_id")
	private Position position;
	
	@ManyToOne
	@JoinColumn(name = "election_id")
	private Election election;
	
	public Result() {
		super();
	}

	public Result(@NotNull(message = "Roll Number cannot be null") String rollNumber,
			@NotNull(message = "Name cannot be null") String name,
			@NotNull(message = "Votes cannot be null") int voteCount) {
		super();
		this.rollNumber = rollNumber;
		this.name = name;
		this.voteCount = voteCount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRollNumber() {
		return rollNumber;
	}

	public void setRollNumber(String rollNumber) {
		this.rollNumber = rollNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Election getElection() {
		return election;
	}

	public void setElection(Election election) {
		this.election = election;
	}

	@Override
	public String toString() {
		return "Result [id=" + id + ", rollNumber=" + rollNumber + ", name=" + name + ", voteCount=" + voteCount + "]";
	}
	
	
	
}
