package com.iiitg.election.allocation.dto;

import java.time.LocalDateTime;
import java.util.List;


public class AllocationDto {
	private ClassroomInfo classroom;
	private FacultyInfo faculty;
	private List<ElectionSlotsInfo> electionSlots;
	
	public static class ClassroomInfo {
		private String classroomName;
		private int capacity;
		
		//Getters and Setters for ClassroomInfo
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
	}
	
	public static class FacultyInfo {
	    private String firstName;
	    private String lastName;
	    private String facultyEmailId;
	    
	    //Getters and Setters for FacultyInfo
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getFacultyEmailId() {
			return facultyEmailId;
		}
		public void setFacultyEmailId(String facultyEmailId) {
			this.facultyEmailId = facultyEmailId;
		}
	}
	
	public static class ElectionSlotsInfo {
		private LocalDateTime slotStartTime;
		private LocalDateTime slotEndTime;
		private List<StudentInfo> voters;
		
		public static class StudentInfo {
			private String studentEmailId;
			private String rollNumber;
			private String firstName;
			private String lastName;
			
			//Getters and Setters for StudentInfo
			public String getStudentEmailId() {
				return studentEmailId;
			}
			public void setStudentEmailId(String studentEmailId) {
				this.studentEmailId = studentEmailId;
			}
			public String getRollNumber() {
				return rollNumber;
			}
			public void setRollNumber(String rollNumber) {
				this.rollNumber = rollNumber;
			}
			public String getFirstName() {
				return firstName;
			}
			public void setFirstName(String firstName) {
				this.firstName = firstName;
			}
			public String getLastName() {
				return lastName;
			}
			public void setLastName(String lastName) {
				this.lastName = lastName;
			}
		}

		//Getters and Setters for ElectionSlotsInfo
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

		public List<StudentInfo> getVoters() {
			return voters;
		}

		public void setVoters(List<StudentInfo> voters) {
			this.voters = voters;
		}
	}

	//	Getters and Setters for AllocationDto
	public ClassroomInfo getClassroom() {
		return classroom;
	}

	public void setClassroom(ClassroomInfo classroom) {
		this.classroom = classroom;
	}

	public FacultyInfo getFaculty() {
		return faculty;
	}

	public void setFaculty(FacultyInfo faculty) {
		this.faculty = faculty;
	}

	public List<ElectionSlotsInfo> getElectionSlots() {
		return electionSlots;
	}

	public void setElectionSlots(List<ElectionSlotsInfo> electionSlots) {
		this.electionSlots = electionSlots;
	}	
}

