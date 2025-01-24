package com.iiitg.election.student;

import com.iiitg.election.core.Position;
import com.iiitg.election.faculty.Faculty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "candidate")
public class Candidate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	@NotNull(message = "Programme cannot be null")
	@Column(name = "programme", nullable = false)
	private String programme;
	
	@NotNull(message = "Graduating Year cannot be null")
	@Column(name = "graduating_year", nullable = false)
	private int graduatingYear;
	
	@NotNull(message = "Student Image cannot be null")
	@Column(name = "student_image_url", nullable = false)
	private String studentImageURL;
	
	@NotNull(message = "About cannot be null")
	@Column(name = "student_about", nullable = false)
	private String about;
	
	@Column(name = "is_eligible")
	private Boolean isEligible;
	
	@Column(name = "manifesto_url")
	private String manifestoURL;
	
//	Relationship with Student table
//	One to one realtionship with student using student_id
//	Many to one realtionship with student using nominator_id
//	One student can nominate many candidates
	
	@OneToOne
	@JoinColumn(name = "student_id", unique = true)
	private Student student;
	
	@ManyToOne
	@JoinColumn(name = "nominator_id")
	private Student nominatedBy;
	
//	Relationship with Faculty table
//	Many to one realtionship with faculty using faculty_id
//	One faculty can approve many candidates

	@ManyToOne
	@JoinColumn(name = "approver_id")
	private Faculty approvedBy;
	
//	Relationship with Faculty table
//	Many to one relationship with position using position_id
//	One position can have many candidates
	@ManyToOne
	@JoinColumn(name = "position_id")
	private Position contestingPosition;
	
	
	public Candidate() {
        super();
    }
	
	public Candidate(@NotNull(message = "Programme cannot be null") String programme,
			@NotNull(message = "Graduating Year cannot be null") int graduatingYear,
			@NotNull(message = "Student Image cannot be null") String studentImageURL,
			@NotNull(message = "About cannot be null") String about, Boolean isEligible, String manifestoURL) {
		super();
		this.programme = programme;
		this.graduatingYear = graduatingYear;
		this.studentImageURL = studentImageURL;
		this.about = about;
		this.isEligible = isEligible;
		this.manifestoURL = manifestoURL;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getStudentImageURL() {
		return studentImageURL;
	}

	public void setStudentImageURL(String studentImageURL) {
		this.studentImageURL = studentImageURL;
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

	public String getManifestoURL() {
		return manifestoURL;
	}

	public void setManifestoURL(String manifestoURL) {
		this.manifestoURL = manifestoURL;
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

	public Position getContestingPosition() {
		return contestingPosition;
	}

	public void setContestingPosition(Position contestingPosition) {
		this.contestingPosition = contestingPosition;
	}

	@Override
	public String toString() {
		return "Candidate [id=" + id + ", programme=" + programme + ", graduatingYear=" + graduatingYear
				+ ", studentImageURL=" + studentImageURL + ", about=" + about + ", isEligible=" + isEligible
				+ ", manifestoURL=" + manifestoURL + "]";
	}
	
	
}
