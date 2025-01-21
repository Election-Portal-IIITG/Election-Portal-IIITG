package com.iiitg.election.core;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class ResultId implements Serializable {

	private String position_id;
	private String election_id;
	
	public ResultId() {
		super();
	}

	public ResultId(String position_id, String election_id) {
		super();
		this.position_id = position_id;
		this.election_id = election_id;
	}

	public String getPosition_id() {
		return position_id;
	}

	public void setPosition_id(String position_id) {
		this.position_id = position_id;
	}

	public String getElection_id() {
		return election_id;
	}

	public void setElection_id(String election_id) {
		this.election_id = election_id;
	}

	@Override
	public String toString() {
		return "ResultId [position_id=" + position_id + ", election_id=" + election_id + "]";
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultId resultId = (ResultId) o;
        return Objects.equals(position_id, resultId.position_id) &&
               Objects.equals(election_id, resultId.election_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position_id, election_id);
    }
}
