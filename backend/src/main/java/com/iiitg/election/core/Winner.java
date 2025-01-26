package com.iiitg.election.core;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
	@Column(name = "roll_number", nullable = false, unique = true)
	private String rollNumber;
	
	@NotNull(message = "Name cannot be null")
	@Column(name = "name", nullable = false)
	private String name;
	
	
	@NotNull(message = "Image URLcannot be null")
	@Column(name = "image_url", nullable = false)
	private String imageURL;
	
	@NotNull(message = "Programme cannot be null")
	@Column(name = "programme", nullable = false)
	private String programme;
	
	
//	Relationship with Position table
//	Many to one relationship with position using position_id
//	One position can have many winners
	@ManyToOne
	@JoinColumn(name = "position_id")
	private Position winningPosition;

	@ManyToOne
	@JoinColumn(name = "election_id")
	private Election winningElection;

	public Winner() {
		super();
	}


	public Winner(@Email(message = "Invalid Email format") @NotNull(message = "Email ID cannot be null") String emailId,
			@NotNull(message = "Roll Number cannot be null") String rollNo,
			@NotNull(message = "Name cannot be null") String name,
			@NotNull(message = "Image URLcannot be null") String imageURL,
			@NotNull(message = "Programme cannot be null") String programme, Position winningPosition) {
		super();
		this.emailId = emailId;
		this.rollNumber = rollNo;
		this.name = name;
		this.imageURL = imageURL;
		this.programme = programme;
		this.winningPosition = winningPosition;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getRollNo() {
		return rollNumber;
	}

	public void setRollNo(String rollNo) {
		this.rollNumber = rollNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getProgramme() {
		return programme;
	}

	public void setProgramme(String programme) {
		this.programme = programme;
	}

	public Position getWinningPosition() {
		return winningPosition;
	}

	public void setWinningPosition(Position winningPosition) {
		this.winningPosition = winningPosition;
	}

	public String getRollNumber() {
		return rollNumber;
	}


	public void setRollNumber(String rollNumber) {
		this.rollNumber = rollNumber;
	}


	public Election getWinningElection() {
		return winningElection;
	}


	public void setWinningElection(Election winningElection) {
		this.winningElection = winningElection;
	}


	@Override
	public String toString() {
		return "Winner [id=" + id + ", emailId=" + emailId + ", rollNo=" + rollNumber + ", name=" + name + ", imageURL="
				+ imageURL + ", programme=" + programme + ", winningPosition=" + winningPosition + "]";
	}
}


