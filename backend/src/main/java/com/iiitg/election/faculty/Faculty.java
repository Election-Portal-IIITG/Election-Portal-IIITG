package com.iiitg.election.faculty;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.iiitg.election.allocation.Classroom;
import com.iiitg.election.annotations.ValidEmail;
import com.iiitg.election.student.Candidate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "faculty")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Faculty {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	@Email(message = "Invalid email format")
	@ValidEmail
	@NotNull(message = "Email ID cannot be null")
	@Column(name = "faculty_email_id", unique = true)
	private String facultyEmailId;
	
	
	@NotNull(message = "Firstname cannot be null")
	@Column(name = "first_name", nullable = false)
	private String firstName;
	
	@Column(name = "last_name", nullable = true)
	private String lastName;
	
	@NotNull(message = "Password cannot be null")
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "is_available", columnDefinition = "BOOLEAN DEFAULT true")
	private boolean isAvailable;
	
	@OneToMany(mappedBy = "approvedBy")
	private List<Candidate> approvedCandidates;

	public Faculty() {
		super();
	}

	public Faculty(
			@Email(message = "Invalid email format") @NotNull(message = "Email ID cannot be null") String facultyEmailId,
			@NotNull(message = "Firstname cannot be null") String firstName, String lastName,
			@NotNull(message = "Password cannot be null") String password, boolean isAvailable) {
		super();
		this.facultyEmailId = facultyEmailId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.isAvailable = isAvailable;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getFacultyEmailId() {
		return facultyEmailId;
	}


	public void setFacultyEmailId(String facultyEmailId) {
		this.facultyEmailId = facultyEmailId;
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
		return "Faculty [id=" + id + ", facultyEmailId=" + facultyEmailId + ", firstName=" + firstName + ", lastName="
				+ lastName + ", password=" + password + "]";
	}
}
