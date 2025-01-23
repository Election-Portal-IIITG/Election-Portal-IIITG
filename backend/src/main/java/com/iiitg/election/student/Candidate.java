package com.iiitg.election.student;

import com.iiitg.election.annotations.ValidEmail;
import com.iiitg.election.core.Position;
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
    @Column(name = "student_email_id", unique = true)
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
	@Column(name = "is_eligible")
	private Boolean isEligible;
	
	@Column(name = "manifesto")
	private String manifesto;
	
	@OneToOne
	@MapsId
	@JoinColumn(name = "student_email_id", referencedColumnName = "student_email_id")
	private Student student;
	
	@ManyToOne
    @JoinColumn(name = "nominated_by", referencedColumnName = "student_email_id")
    private Student nominatedBy;
	
	@ManyToOne
	@JoinColumn(name = "approved_by", referencedColumnName = "email_id")
	private Faculty approvedBy;
	
	@ManyToOne
    @JoinColumn(name = "position_id", referencedColumnName = "position_id")
    private Position position;
	

	
	public Candidate() {
        super();
    }
	
	public Candidate(Student student, String programme, int graduatingYear, 
            String studentImage, String about, Position position) {
		super();
		this.student = student;
		this.studentEmailId = student.getStudentEmailId();
		this.programme = programme;
		this.graduatingYear = graduatingYear;
		this.studentImage = studentImage;
		this.about = about;
		this.position = position;
		this.isEligible = null;
		this.manifesto = null;
		this.nominatedBy = null;
		this.approvedBy = null;
	}

	public String getStudentEmailId() {
		return studentEmailId;
	}

	public void setStudentEmailId(String studentEmailId) {
		this.studentEmailId = studentEmailId;
	}

	public String getProgramme() {
		return programme;
	}

	public void setProgramme(String programme) {
		this.programme = programme;
	}

	public int getGraduatingYear() {
		return graduatingYear;
	}

	public void setGraduatingYear(int graduatingYear) {
		this.graduatingYear = graduatingYear;
	}

	public String getStudentImage() {
		return studentImage;
	}

	public void setStudentImage(String studentImage) {
		this.studentImage = studentImage;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public Boolean getIsEligible() {
		return isEligible;
	}

	public void setIsEligible(Boolean isEligible) {
		this.isEligible = isEligible;
	}

	public String getManifesto() {
		return manifesto;
	}

	public void setManifesto(String manifesto) {
		this.manifesto = manifesto;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Student getNominatedBy() {
		return nominatedBy;
	}

	public void setNominatedBy(Student nominatedBy) {
		this.nominatedBy = nominatedBy;
	}

	public Faculty getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Faculty approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "Candidate [studentEmailId=" + studentEmailId + ", programme=" + programme + ", graduatingYear="
				+ graduatingYear + ", studentImage=" + studentImage + ", about=" + about + ", isEligible=" + isEligible
				+ ", manifesto=" + manifesto + ", student=" + student + ", nominatedBy=" + nominatedBy + ", approvedBy="
				+ approvedBy + ", position=" + position + "]";
	}
	
	
}
