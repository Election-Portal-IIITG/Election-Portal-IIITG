package com.iiitg.election.core;

import java.util.ArrayList;
import java.util.List;

import com.iiitg.election.student.Candidate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "election_position")
public class Position {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	@NotNull(message = "Position Name cannot be null")
	@Column(name = "position_name", unique = true)
	private String positionName;
	
	@OneToMany(mappedBy = "contestingPosition")
	private List<Candidate> contestingCandidates = new ArrayList<>();
	
	@OneToMany(mappedBy = "winningPosition")
	private List<Winner> winningCandidates = new ArrayList<>();
	
	@OneToMany(mappedBy = "position")
	private List<Result> results = new ArrayList<>();
	
	public Position() {
		super();
	}


	public Position(String id, @NotNull(message = "Position Name cannot be null") String positionName) {
		super();
		this.id = id;
		this.positionName = positionName;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getPositionName() {
		return positionName;
	}


	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}


	public List<Candidate> getContestingCandidates() {
		return contestingCandidates;
	}


	public void setContestingCandidates(List<Candidate> contestingCandidates) {
		this.contestingCandidates = contestingCandidates;
	}


	public List<Winner> getWinningCandidates() {
		return winningCandidates;
	}


	public void setWinningCandidates(List<Winner> winningCandidates) {
		this.winningCandidates = winningCandidates;
	}


	public List<Result> getResults() {
		return results;
	}


	public void setResults(List<Result> results) {
		this.results = results;
	}


	@Override
	public String toString() {
		return "Position [id=" + id + ", positionName=" + positionName + "]";
	}
	
	

}
