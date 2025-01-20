package com.iiitg.election.allocation;

import com.iiitg.election.annotations.ValidEmail;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity(name = "student_classroom")
public class StudentClassroom {
	
	@Id
	@Email(message = "Invalid email format")
	@ValidEmail
	@NotNull(message = "Email ID cannot be null")
	@Column(name = "student_email_id", nullable = false, unique = true)
	private String studentEmailId;
	
	@ManyToOne
    @JoinColumn(name = "classroom_id", referencedColumnName = "classroomId", nullable = false)
    private Classroom classroom;
	
	public StudentClassroom() {
		super();
    }

	public String getStudentEmailId() {
		return studentEmailId;
	}

	public void setStudentEmailId(String studentEmailId) {
		this.studentEmailId = studentEmailId;
	}

	public Classroom getClassroom() {
		return classroom;
	}

	public void setClassroom(Classroom classroom) {
		this.classroom = classroom;
	}

	@Override
	public String toString() {
		return "StudentClassroom [studentEmailId=" + studentEmailId + ", classroom=" + classroom + "]";
	}
	
	
}