package com.iiitg.election.allocation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "slot_classroom")
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
	
}
