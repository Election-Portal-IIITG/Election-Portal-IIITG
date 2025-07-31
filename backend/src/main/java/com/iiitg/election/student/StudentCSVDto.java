package com.iiitg.election.student;

import com.iiitg.election.annotations.ValidEmail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class StudentCSVDto {
	@NotNull(message = "Email ID cannot be null")
    @Email(message = "Invalid email format")
    @ValidEmail
    private String studentEmailId;

    @NotNull(message = "Firstname cannot be null")
    private String firstName;

    private String lastName;

    @NotNull(message = "Roll Number cannot be null")
    private String rollNumber;

    private String password;

    @NotNull(message = "On Campus cannot be null")
    private Boolean onCampus;

    private Boolean hasVoted;

    private Boolean isActive;
    
    private int rowNumber;
    
    public Student toStudent() {
    	return Student.builder()
    			.studentEmailId(this.studentEmailId)
    			.firstName(this.firstName.trim())
    			.lastName(this.lastName)
    			.rollNumber(this.rollNumber)
    			.onCampus(this.onCampus)
    			.hasVoted(false)
    			.isActive(false)
    			.build();
    }
}
