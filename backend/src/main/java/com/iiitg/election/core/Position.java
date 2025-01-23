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
	
	@OneToMany(mappedBy = "position")
    private List<Candidate> candidate;
	
	@OneToOne(mappedBy = "position")
    private Winner winner;
	
	@OneToMany(mappedBy = "position")
    private List<Result> results;
	
	public Position() {
		super();
	}

	public Position(@NotNull(message = "Position Id cannot be null") String positionId,
			@NotNull(message = "Position Name cannot be null") String positionName, List<Candidate> candidate,
			Winner winner, List<Result> results) {
		super();
		this.positionId = positionId;
		this.positionName = positionName;
		this.candidate = candidate;
		this.winner = winner;
		this.results = results;
	}

	
	
	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public List<Candidate> getCandidate() {
		return candidate;
	}

	public void setCandidate(List<Candidate> candidate) {
		this.candidate = candidate;
	}

	public Winner getWinner() {
		return winner;
	}

	public void setWinner(Winner winner) {
		this.winner = winner;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "Position [positionId=" + positionId + ", positionName=" + positionName + ", candidate=" + candidate
				+ ", winner=" + winner + ", results=" + results + "]";
	}
}
