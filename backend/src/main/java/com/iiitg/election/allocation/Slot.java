package com.iiitg.election.allocation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "slot")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Slot {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	@Column(name = "slot_start_time")
	@NotNull(message = "Slot start time is required")
	private LocalDateTime slotStartTime;
	
	
	@Column(name = "slot_end_time")
	@NotNull(message = "Slot end time is required")
	private LocalDateTime slotEndTime;
	
	@OneToMany(mappedBy = "slot")
	private List<SlotClassroom> classroomSlots = new ArrayList<>();
	
	public Slot() {
		super();
	}
	
	public Slot( @NotNull(message = "Slot start time is required") LocalDateTime slotStartTime,
			@NotNull(message = "Slot end time is required") LocalDateTime slotEndTime) {
		super();
		this.slotStartTime = slotStartTime;
		this.slotEndTime = slotEndTime;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		return "Slot [id=" + id + ", slotStartTime=" + slotStartTime + ", slotEndTime=" + slotEndTime + "]";
	}
	
}
