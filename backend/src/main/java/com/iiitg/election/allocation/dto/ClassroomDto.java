package com.iiitg.election.allocation.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ClassroomDto {
 private String id;
 private String classroomName;
 private Integer capacity;
 private Boolean isAvailable;
 private List<SlotInfo> slots;


 // A simple inner class for Slot
 public static class SlotInfo {
	    private String id;
	    private LocalDateTime slotStartTime;
	    private LocalDateTime slotEndTime;
	    
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
 }
 
 // Getters and Setters for ClassroomResponseDTO
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
	
	public Integer getCapacity() {
		return capacity;
	}
	
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	
	public Boolean getIsAvailable() {
		return isAvailable;
	}
	
	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	public List<SlotInfo> getSlots() {
		return slots;
	}
	
	public void setSlots(List<SlotInfo> slots) {
		this.slots = slots;
	}
}