package com.iiitg.election.student;

import com.iiitg.election.annotations.ValidEmail;
import com.iiitg.election.faculty.Faculty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity(name = "candidate")
public class Candidate {
	
	@Id
	@Email(message = "Invalid email format")
	@ValidEmail
	@NotNull(message = "Email ID cannot be null")
    @Column(name = "student_email_id")
    private String studentEmailId;
	
	@NotNull(message = "Programme cannot be null")
	@Column(name = "programme", nullable = false)
	private String programme;
	
	@NotNull(message = "Graduating Year cannot be null")
	@Column(name = "graduating_year", nullable = false)
	private int graduatingYear;
	
	@NotNull(message = "Student Image cannot be null")
	@Column(name = "student_image", nullable = false)
	private String studentImage;
	
	@NotNull(message = "About cannot be null")
	@Column(name = "student_about", nullable = false)
	private String about;
	
	@NotNull(message = "Eligibility cannot be null")
	@Column(name = "is_eligible", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
	private boolean isEligible;
	
	@Column(name = "manifesto")
	private String manifesto;
	
	@OneToOne
	@MapsId
	@JoinColumn(name = "student_email_id")
	private Student student;
	
	@ManyToOne
    @JoinColumn(name = "nominated_by", referencedColumnName = "student_email_id", nullable = false)
    private Student nominatedBy;
	
	@ManyToOne
	@JoinColumn(name = "approved_by", referencedColumnName = "email_id")
	private Faculty approvedBy;

	
	public Candidate() {
        super();
    }


		
}
