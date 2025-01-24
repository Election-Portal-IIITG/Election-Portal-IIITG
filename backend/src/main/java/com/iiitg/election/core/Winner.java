package com.iiitg.election.core;

import com.iiitg.election.annotations.ValidEmail;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "winner")
public class Winner {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	
	@Email(message = "Invalid Email format")
	@ValidEmail
	@NotNull(message = "Email ID cannot be null")
	@Column(name = "email_id", nullable = false, unique = true)
	private String emailId;
	
	@NotNull(message = "Roll Number cannot be null")
	@Column(name = "roll_no", nullable = false, unique = true)
	private String rollNo;
	
	@NotNull(message = "Name cannot be null")
	@Column(name = "name", nullable = false)
	private String name;
	
	
	@NotNull(message = "Image URLcannot be null")
	@Column(name = "image_url", nullable = false)
	private String imageURL;
	
	@NotNull(message = "Programme cannot be null")
	@Column(name = "programme", nullable = false)
	private String programme;
	
	
//	Relationship with Positio table
//	Many to one relationship with position using position_id
//	One position can have many winners
	@ManyToOne
	@JoinColumn(name = "position_id")
	private Position winningPosition;
	
	
}
