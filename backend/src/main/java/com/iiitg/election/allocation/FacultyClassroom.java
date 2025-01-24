package com.iiitg.election.allocation;

import com.iiitg.election.annotations.ValidEmail;
import com.iiitg.election.faculty.Faculty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

//@Entity(name = "faculty_classroom")
public class FacultyClassroom {
	
	@Id
	@Email(message = "Invalid email format")
	@ValidEmail
	@NotNull(message = "Email ID cannot be null")
	@Column(name = "faculty_email_id", nullable = false, unique = true)
	private String facultyEmailId;
	
	@OneToOne
    @JoinColumn(name = "classroom_id", referencedColumnName = "classroomId", nullable = false)
    private Classroom classroom;
	
	public FacultyClassroom() {
		super();
    }

	public FacultyClassroom(Faculty faculty, Classroom classroom) {
		super();
		this.facultyEmailId = faculty.getEmailId();
		this.classroom = classroom;
	}

	public String getFacultyEmailId() {
		return facultyEmailId;
	}

	public void setFacultyEmailId(String facultyEmailId) {
		this.facultyEmailId = facultyEmailId;
	}

	public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

	@Override
	public String toString() {
		return "FacultyClassroom [facultyEmailId=" + facultyEmailId + ", classroom=" + classroom + "]";
	}

	
	
}
