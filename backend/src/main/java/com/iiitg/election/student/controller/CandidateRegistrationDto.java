package com.iiitg.election.student.controller;

import org.springframework.web.multipart.MultipartFile;

public class CandidateRegistrationDto {
	
	private String programme;
	private int graduatingYear;
	private String about;
	private String contestingPositionName;
	private MultipartFile profileImage;
	
	// Getters and Setters
	public String getProgramme() {
		return programme;
	}
	
	public void setProgramme(String programme) {
		this.programme = programme;
	}
	
	public int getGraduatingYear() {
		return graduatingYear;
	}
	
	public void setGraduatingYear(int graduatingYear) {
		this.graduatingYear = graduatingYear;
	}
	
	public String getAbout() {
		return about;
	}
	
	public void setAbout(String about) {
		this.about = about;
	}
	
	public String getContestingPositionName() {
		return contestingPositionName;
	}
	
	public void setContestingPositionName(String contestingPositionName) {
		this.contestingPositionName = contestingPositionName;
	}
	
	public MultipartFile getProfileImage() {
		return profileImage;
	}
	
	public void setProfileImage(MultipartFile profileImage) {
		this.profileImage = profileImage;
	}
}
