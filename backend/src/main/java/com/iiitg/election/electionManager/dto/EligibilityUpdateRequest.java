package com.iiitg.election.electionManager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EligibilityUpdateRequest {
    @JsonProperty("candidateEmailId")
    private String candidateEmailId;

    @JsonProperty("isEligible")
    private Boolean isEligible;
}
