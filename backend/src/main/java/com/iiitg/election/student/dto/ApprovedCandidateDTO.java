package com.iiitg.election.student.dto;

import com.iiitg.election.faculty.dto.ApproverDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovedCandidateDTO {
    private String studentEmailId;
    private String firstName;
    private String lastName;
    private String rollNumber;
    private String programme;
    private int graduatingYear;
    private String studentImageURL;  // Send the URL as-is, frontend will handle image loading
    private NominatorDTO nominatedBy;
    private ApproverDTO approvedBy;
    private String contestingPosition;
    private Boolean isEligible;
}