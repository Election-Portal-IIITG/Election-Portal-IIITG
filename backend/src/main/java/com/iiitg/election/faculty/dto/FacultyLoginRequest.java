package com.iiitg.election.faculty.dto;

public class FacultyLoginRequest {
	
	private String facultyEmailId;
    private String password;
    
    
	public FacultyLoginRequest() {
		super();
	}

	public FacultyLoginRequest(String facultyEmailId, String password) {
		super();
		this.facultyEmailId = facultyEmailId;
		this.password = password;
	}

	public String getFacultyEmailId() {
		return facultyEmailId;
	}

	public void setFacultyEmailId(String facultyEmailId) {
		this.facultyEmailId = facultyEmailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
    
}
