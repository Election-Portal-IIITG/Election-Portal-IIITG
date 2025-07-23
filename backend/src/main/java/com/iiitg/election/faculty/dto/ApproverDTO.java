package com.iiitg.election.faculty.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApproverDTO {
    private String firstName;
    private String lastName;
    private String facultyEmailId;
}