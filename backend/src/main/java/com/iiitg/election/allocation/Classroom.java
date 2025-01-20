package com.iiitg.election.allocation;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@Entity(name = "classroom")
public class Classroom {
	
	@Id
	@NotNull(message = "Classroom ID cannot be null")
	private String classroomId;
	
	@NotNull(message = "Capacity cannot be null")
	private int capacity;
	
	@NotNull(message = "Availability cannnot be null")
	private boolean isAvailable;
	
	@OneToOne(mappedBy = "classroom")
	private FacultyClassroom facultyClassroom;
	
	@OneToMany(mappedBy = "classroom")
	private List<StudentClassroom> studentClassrooms;
	
	public Classroom() {
		super();
	}
	
	public Classroom(String classroomId, int capacity, boolean is_available) {
		super();
		this.classroomId = classroomId;
		this.capacity = capacity;
		this.isAvailable = is_available;
	}

	public String getClassroomId() {
		return classroomId;
	}

	public void setClassroomId(String classroomId) {
		this.classroomId = classroomId;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public boolean isIs_available() {
		return isAvailable;
	}

	public void setIs_available(boolean is_available) {
		this.isAvailable = is_available;
	}
	
	public FacultyClassroom getFacultyClassroom() {
        return facultyClassroom;
    }

    public void setFacultyClassroom(FacultyClassroom facultyClassroom) {
        this.facultyClassroom = facultyClassroom;
    }

	@Override
	public String toString() {
		return "Classroom [classroomId=" + classroomId + ", is_available=" + isAvailable +  ", capacity=" + capacity + "]";
	}
	
	
}
