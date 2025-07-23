package com.iiitg.election.jwt;

public class TokenData {
    private String type;
    private String candidateEmailId;
    private String studentEmailId;
    private String facultyEmailId;
    
    

	public TokenData(String type, String candidateEmailId, String studentEmailId, String facultyEmailId) {
		this.type = type;
		this.candidateEmailId = candidateEmailId;
		this.studentEmailId = studentEmailId;
		this.facultyEmailId = facultyEmailId;
	}



	public String getType() {
		return type;
	}



	public String getCandidateEmailId() {
		return candidateEmailId;
	}



	public String getStudentEmailId() {
		return studentEmailId;
	}



	public String getFacultyEmailId() {
		return facultyEmailId;
	}
	
	@Override
	public String toString() {
		return "TokenData [type=" + type + ", candidateEmailId=" + candidateEmailId + ", studentEmailId="
				+ studentEmailId + ", facultyEmailId=" + facultyEmailId + "]";
	}
}
