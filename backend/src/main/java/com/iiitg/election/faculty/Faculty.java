package com.iiitg.election.faculty;

import java.util.List;

import com.iiitg.election.annotations.ValidEmail;
import com.iiitg.election.student.Candidate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity(name = "faculty")
public class Faculty {
	
	@Id
	@Email(message = "Invalid email format")
	@ValidEmail
	@NotNull(message = "Email ID cannot be null")
	@Column(name = "email_id", nullable = false, unique = true)
	private String emailId;
	
	
	@NotNull(message = "Firstname cannot be null")
	@Column(name = "first_name", nullable = false)
	private String firstName;
	
	@Column(name = "last_name", nullable = true)
	private String lastName;
	
	@NotNull(message = "Password cannot be null")
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "is_available", columnDefinition = "BOOLEAN DEFAULT false")
	private boolean isAvailable;
	
	@OneToMany(mappedBy = "approvedBy")
	private List<Candidate> approvedCandidates;

	public Faculty(
			@Email(message = "Invalid email format") @NotNull(message = "Email ID cannot be null") String emailId,
			@NotNull(message = "Firstname cannot be null") String firstName, String lastName,
			@NotNull(message = "Password cannot be null") String password, boolean isAvailable,
			List<Candidate> approvedCandidates) {
		super();
		this.emailId = emailId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.isAvailable = isAvailable;
		this.approvedCandidates = approvedCandidates;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public List<Candidate> getApprovedCandidates() {
		return approvedCandidates;
	}

	public void setApprovedCandidates(List<Candidate> approvedCandidates) {
		this.approvedCandidates = approvedCandidates;
	}

	@Override
	public String toString() {
		return "Faculty [emailId=" + emailId + ", firstName=" + firstName + ", lastName=" + lastName + ", password="
				+ password + ", isAvailable=" + isAvailable + ", approvedCandidates=" + approvedCandidates + "]";
	}
	
	
	
}
