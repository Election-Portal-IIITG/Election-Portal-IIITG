package com.iiitg.election.student;

import java.util.ArrayList;
import java.util.List;

import com.iiitg.election.allocation.SlotClassroom;
import com.iiitg.election.annotations.ValidEmail;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "student")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	@ValidEmail
	@Email(message = "Invalid email format")
	@NotNull(message = "Email ID cannot be null")
	@Column(name = "student_email_id", unique = true)
	private String studentEmailId;

	@NotNull(message = "Firstname cannot be null")
	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name")
	private String lastName;
	
	@NotNull(message = "Roll Number cannot be null")
	@Column(name = "roll_number", unique = true)
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

	@OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
	private Candidate candidate;
	
	@OneToMany(mappedBy = "nominatedBy")
	private List<Candidate> nominatedCandidates = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name = "slot_classroom_id")
	private SlotClassroom slotClassroom;

	public Student() {
		super();
	}

	public Student(
			@ValidEmail @Email(message = "Invalid email format") @NotNull(message = "Email ID cannot be null") String studentEmailId,
			@NotNull(message = "Firstname cannot be null") String firstName, String lastName,
			@NotNull(message = "Roll Number cannot be null") String rollNumber,
			@NotNull(message = "Password cannot be null") String password,
			@NotNull(message = "On Campus cannot be null") boolean onCampus,
			@NotNull(message = "Has Voted cannot be null") boolean hasVoted) {
		super();
		this.studentEmailId = studentEmailId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.rollNumber = rollNumber;
		this.password = password;
		this.onCampus = onCampus;
		this.hasVoted = hasVoted;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public void setRollNumber(String rollNumber) {
		this.rollNumber = rollNumber;
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

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public List<Candidate> getNominatedCandidates() {
		return nominatedCandidates;
	}

	public void setNominatedCandidates(List<Candidate> nominatedCandidates) {
		this.nominatedCandidates = nominatedCandidates;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", studentEmailId=" + studentEmailId + ", firstName=" + firstName + ", lastName="
				+ lastName + ", rollNumber=" + rollNumber + ", password=" + password + ", onCampus=" + onCampus
				+ ", hasVoted=" + hasVoted + "]";
	}

			
}
