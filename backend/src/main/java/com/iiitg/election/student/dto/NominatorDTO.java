package com.iiitg.election.student.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NominatorDTO {
    private String firstName;
    private String lastName;
    private String rollNumber;
    private String studentEmailId;
}
