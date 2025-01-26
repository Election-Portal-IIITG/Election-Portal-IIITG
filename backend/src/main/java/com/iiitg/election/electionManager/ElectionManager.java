package com.iiitg.election.electionManager;

import com.iiitg.election.annotations.ValidEmail;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity(name = "election_manager")
public class ElectionManager {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	@Email(message = "Invalid email format")
	@ValidEmail
	@NotNull(message = "Email ID cannot be null")
	@Column(name = "manager_email_id", unique = true)
	private String managerEmailId;
	
	@NotNull(message = "password cannot be null")
	@Column(name = "password")
	private String password;
	
	public ElectionManager() {
		super();
	}
	
	public ElectionManager(String emailId, String password) {
		super();
		this.managerEmailId = emailId;
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getManagerEmailId() {
		return managerEmailId;
	}

	public void setManagerEmailId(String managerEmailId) {
		this.managerEmailId = managerEmailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "ElectionManager [id=" + id + ", managerEmailId=" + managerEmailId + ", password=" + password + "]";
	}

		
}
