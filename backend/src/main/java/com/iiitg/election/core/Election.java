package com.iiitg.election.core;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "election")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Election {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	@Column(name = "election_date", nullable = false)
	private LocalDate electionDate;
	
	@Column(name = "is_complete")
	private Boolean isComplete;
	
	@OneToMany(mappedBy = "election")
	private List<Result> results = new ArrayList<>();
	
	@OneToMany(mappedBy = "winningElection")
	private List<Winner> winners = new ArrayList<>();

	public Election() {
		super();
	}

	public Election(LocalDate electionDate, Boolean isComplete) {
		super();
		this.electionDate = electionDate;
		this.isComplete = isComplete;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDate getElectionDate() {
		return electionDate;
	}

	public void setElectionDate(LocalDate electionDate) {
		this.electionDate = electionDate;
	}

	public Boolean getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	public List<Winner> getWinners() {
		return winners;
	}

	public void setWinners(List<Winner> winners) {
		this.winners = winners;
	}

	@Override
	public String toString() {
		return "Election [id=" + id + ", electionDate=" + electionDate + ", isComplete=" + isComplete + "]";
	}
	
	
	

}
