package com.iiitg.election.core;

import com.iiitg.election.annotations.ValidEmail;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity(name = "winner")
public class Winner {
	
	@Id
	@Email(message = "Invalid Email format")
	@ValidEmail
	@NotNull(message = "Email ID cannot be null")
	@Column(name = "email_id", nullable = false, unique = true)
	private String emailId;
	
	@NotNull(message = "Roll Number cannot be null")
	@Column(name = "roll_no", nullable = false, unique = true)
	private String rollNo;
	
	@NotNull(message = "Firstname cannot be null")
	@Column(name = "first_name", nullable = false)
	private String firstName;
	
	@Column(name = "last_name", nullable = true)
	private String lastName;
	
	@NotNull(message = "Image cannot be null")
	@Column(name = "image", nullable = false)
	private String image;
	
	@NotNull(message = "Programme cannot be null")
	@Column(name = "programme", nullable = false)
	private String programme;
	
	@OneToOne
    @JoinColumn(name = "position_id", referencedColumnName = "position_id", nullable = false)
    private Position position;
}
