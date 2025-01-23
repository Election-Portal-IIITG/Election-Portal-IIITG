package com.iiitg.election.student;

import java.util.ArrayList;
import java.util.List;

import com.iiitg.election.annotations.ValidEmail;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity(name = "student")
public class Student {

	@Id
	@Email(message = "Invalid email format")
	@ValidEmail
	@NotNull(message = "Email ID cannot be null")
	@Column(name = "student_email_id", nullable = false, unique = true)
	private String studentEmailId;

	@NotNull(message = "Firstname cannot be null")
	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name")
	private String lastName;
	
	@NotNull(message = "Roll Number cannot be null")
	@Column(name = "roll_number", nullable = false, unique = true)
	private String rollNumber;

	@NotNull(message = "Password cannot be null")
	@Column(name = "password", nullable = false)
	private String password;

	@NotNull(message = "On Campus cannot be null")
	@Column(name = "on_campus", nullable = false)
	private boolean onCampus;

	@NotNull(message = "Has Voted cannot be null")
	@Column(name = "has_voted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
	private boolean hasVoted;

	@OneToOne(mappedBy = "student", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Candidate candidateProfile;

	@OneToMany(mappedBy = "nominatedBy", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Candidate> nominatedCandidates = new ArrayList<>();

	public Student() {
		super();
	}

	public Student(
			@Email(message = "Invalid email format") @NotNull(message = "Email ID cannot be null") String studentEmailId,
			@NotNull(message = "Firstname cannot be null") String firstName, String lastName,
			@NotNull(message = "Roll Number cannot be null") String rollNumber,
			@NotNull(message = "Password cannot be null") String password,
			@NotNull(message = "On Campus cannot be null") boolean onCampus,
			@NotNull(message = "Has Voted cannot be null") boolean hasVoted, Candidate candidateProfile) {
		super();
		this.studentEmailId = studentEmailId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.rollNumber = rollNumber;
		this.password = password;
		this.onCampus = onCampus;
		this.hasVoted = hasVoted;
		this.candidateProfile = candidateProfile;
	}

	public String getStudentEmailId() {
		return studentEmailId;
	}

	public void setStudentEmailId(String studentEmailId) {
		this.studentEmailId = studentEmailId;
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

	public String getRollNumber() {
		return rollNumber;
	}

	public void setRollNumber(String rollNo) {
		this.rollNumber = rollNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isOnCampus() {
		return onCampus;
	}

	public void setOnCampus(boolean onCampus) {
		this.onCampus = onCampus;
	}

	public boolean isHasVoted() {
		return hasVoted;
	}

	public void setHasVoted(boolean hasVoted) {
		this.hasVoted = hasVoted;
	}

	public Candidate getCandidateProfile() {
		return candidateProfile;
	}

	public void setCandidateProfile(Candidate candidateProfile) {
		this.candidateProfile = candidateProfile;
	}

	public List<Candidate> getNominatedCandidates() {
		return nominatedCandidates;
	}

	public void setNominatedCandidates(List<Candidate> nominatedCandidates) {
		this.nominatedCandidates = nominatedCandidates;
	}

	@Override
	public String toString() {
		return "Student [studentEmailId=" + studentEmailId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", Roll Number=" + rollNumber + ", password=" + password + ", onCampus=" + onCampus + ", hasVoted=" + hasVoted
				+ ", candidateProfile=" + candidateProfile + ", nominatedCandidates=" + nominatedCandidates + "]";
	}

		
}
