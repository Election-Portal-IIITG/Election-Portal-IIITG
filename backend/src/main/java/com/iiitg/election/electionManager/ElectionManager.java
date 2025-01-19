package com.iiitg.election.electionManager;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "election_manager")
public class ElectionManager {

	@Id
	private String emailId;
	
	private String password;
	
	public ElectionManager(String emailId, String password) {
		super();
		this.emailId = emailId;
		this.password = password;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "ElectionManager [emailId=" + emailId + ", password=" + password + "]";
	}	
}
