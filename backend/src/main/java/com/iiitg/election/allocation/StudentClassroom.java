package com.iiitg.election.allocation;

import java.time.LocalDateTime;

import com.iiitg.election.annotations.ValidEmail;
import com.iiitg.election.student.Student;

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
	
	@NotNull(message = "Slot start time cannot be null")
	@Column(name = "slot_start_time", nullable = false, unique = false)
	private LocalDateTime slotStartTime;
	
	@NotNull(message = "Slot end time cannot be null")
	@Column(name = "slot_end_time", nullable = false, unique = false)
	private LocalDateTime slotEndTime;
	
	public StudentClassroom() {
		super();
    }
	

    public StudentClassroom(Student student, Classroom classroom, LocalDateTime slotStartTime, LocalDateTime slotEndTime) {
        super();
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        this.studentEmailId = student.getStudentEmailId();
        this.classroom = classroom;
        this.slotStartTime = slotStartTime;
        this.slotEndTime = slotEndTime;
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


	public LocalDateTime getSlotStartTime() {
		return slotStartTime;
	}


	public void setSlotStartTime(LocalDateTime slotStartTime) {
		this.slotStartTime = slotStartTime;
	}


	public LocalDateTime getSlotEndTime() {
		return slotEndTime;
	}


	public void setSlotEndTime(LocalDateTime slotEndTime) {
		this.slotEndTime = slotEndTime;
	}


	@Override
	public String toString() {
		return "StudentClassroom [studentEmailId=" + studentEmailId + ", classroom=" + classroom + "]";
	}
	
	
}