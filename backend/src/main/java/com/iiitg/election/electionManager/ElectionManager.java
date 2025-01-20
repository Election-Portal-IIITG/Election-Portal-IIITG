package com.iiitg.election.electionManager;

import com.iiitg.election.annotations.ValidEmail;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity(name = "election_manager")
public class ElectionManager {

	@Id
	@Email(message = "Invalid email format")
	@ValidEmail
	@NotNull(message = "Email ID cannot be null")
	@Column(name = "manager_email_id", nullable = false, unique = true)
	private String managerEmailId;
	
	private String password;
	
	public ElectionManager(String emailId, String password) {
		super();
		this.managerEmailId = emailId;
		this.password = password;
	}

	public String getEmailId() {
		return managerEmailId;
	}

	public void setEmailId(String emailId) {
		this.managerEmailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "ElectionManager [emailId=" + managerEmailId + ", password=" + password + "]";
	}	
}
