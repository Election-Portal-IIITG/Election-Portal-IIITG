package com.iiitg.election.allocation;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.iiitg.election.faculty.Faculty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "slot_classroom")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SlotClassroom {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "classroom_id")
	private Classroom classroom;
	
	@ManyToOne
	@JoinColumn(name = "slot_id")
	private Slot slot;
	
	@ManyToOne
	@JoinColumn(name = "assigned_faculty_id")
	private Faculty assignedFaculty;

	public SlotClassroom() {
		super();
	}

	public SlotClassroom(Classroom classroom, Slot slot) {
		super();
		this.classroom = classroom;
		this.slot = slot;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Classroom getClassroom() {
		return classroom;
	}

	public void setClassroom(Classroom classroom) {
		this.classroom = classroom;
	}

	public Slot getSlot() {
		return slot;
	}

	public void setSlot(Slot slot) {
		this.slot = slot;
	}
	
	public Faculty getAssignedFaculty() {
		return assignedFaculty;
	}

	public void setAssignedFaculty(Faculty assignedFaculty) {
		this.assignedFaculty = assignedFaculty;
	}

	@Override
	public String toString() {
		return "SlotClassroom [id=" + id + ", classroom=" + classroom + ", slot=" + slot + ", assignedFaculty="
				+ assignedFaculty + "]";
	}
}
