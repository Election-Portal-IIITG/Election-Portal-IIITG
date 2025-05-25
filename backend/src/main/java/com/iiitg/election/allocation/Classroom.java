package com.iiitg.election.allocation;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.iiitg.election.faculty.Faculty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "classroom")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Classroom {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	@NotNull(message = "Classroom Name cannot be null")
	@Column(name = "classroom_name", unique = true)
	private String classroomName;
	
	@NotNull(message = "Capacity cannot be null")
	@Column(name = "capacity")
	private int capacity;
	
	@NotNull(message = "Availability cannnot be null")
	@Column(name = "is_available")
	private boolean isAvailable;
	
	@OneToMany(mappedBy = "classroom")
	private List<SlotClassroom> classroomSlots = new ArrayList<>();
	
	
	public Classroom() {
		super();
	}

	public Classroom(@NotNull(message = "Classroom Name cannot be null") String classroomName,
			@NotNull(message = "Capacity cannot be null") int capacity,
			@NotNull(message = "Availability cannnot be null") boolean isAvailable) {
		super();
		this.classroomName = classroomName;
		this.capacity = capacity;
		this.isAvailable = isAvailable;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassroomName() {
		return classroomName;
	}

	public void setClassroomName(String classroomName) {
		this.classroomName = classroomName;
	}

	public int getCapacity() {
		return capacity;
	}


	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}


	public boolean isAvailable() {
		return isAvailable;
	}


	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public List<SlotClassroom> getClassroomSlots() {
		return classroomSlots;
	}

	public void setClassroomSlots(List<SlotClassroom> classroomSlots) {
		this.classroomSlots = classroomSlots;
	}
	
	

	@Override
	public String toString() {
		return "Classroom [id=" + id + ", classroomName=" + classroomName + ", capacity=" + capacity + ", isAvailable="
				+ isAvailable + "]";
	}


	
}
