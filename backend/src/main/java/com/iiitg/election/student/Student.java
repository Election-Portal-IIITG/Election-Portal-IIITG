package com.iiitg.election.student;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.iiitg.election.allocation.SlotClassroom;
import com.iiitg.election.annotations.ValidEmail;
import com.iiitg.election.validation.authentication.LoginValidation;
import com.iiitg.election.validation.authentication.RegisterValidation;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "student")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Student {

	public Student(
			@ValidEmail @Email(message = "Invalid email format") @NotNull(message = "Email ID cannot be null") String studentEmailId,
			@NotNull(message = "Firstname cannot be null") String firstName, String lastName,
			@NotNull(message = "Roll Number cannot be null") String rollNumber,
			@NotNull(message = "Password cannot be null") String password) {
		super();
		this.studentEmailId = studentEmailId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.rollNumber = rollNumber;
		this.password = password;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	@ValidEmail
	@Email(message = "Invalid email format")
	@NotNull(message = "Email ID cannot be null", groups = {RegisterValidation.class, LoginValidation.class})
	@Column(name = "student_email_id", unique = true)
	@NonNull
	private String studentEmailId;

	@NotNull(message = "Firstname cannot be null", groups = RegisterValidation.class)
	@Column(name = "first_name", nullable = false)
	@NonNull
	private String firstName;

	@Column(name = "last_name")
	private String lastName;
	
	@NotNull(message = "Roll Number cannot be null", groups = RegisterValidation.class)
	@Column(name = "roll_number", unique = true)
	@NonNull
	private String rollNumber;

	@NotNull(message = "Password cannot be null", groups = {RegisterValidation.class, LoginValidation.class})
	@Column(name = "password", nullable = false)
	private String password;

	@NotNull(message = "On Campus cannot be null")
	@Column(name = "on_campus", nullable = false)
	@NonNull
	private Boolean onCampus;

	@NotNull(message = "Has Voted cannot be null")
	@Column(name = "has_voted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
	@NonNull
	private Boolean hasVoted;
	
	@NotNull(message = "Account status cannot be null")
	@Column(name = "is_active")
	@NonNull
	private Boolean isActive;

	@OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
	private Candidate candidate;
	
	@OneToMany(mappedBy = "nominatedBy")
	private List<Candidate> nominatedCandidates = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name = "slot_classroom_id")
	private SlotClassroom slotClassroom;
}
