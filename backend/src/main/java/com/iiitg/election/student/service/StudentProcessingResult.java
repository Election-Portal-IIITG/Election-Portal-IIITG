package com.iiitg.election.student.service;

import java.time.Duration;
import java.util.List;

import com.iiitg.election.student.Student;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentProcessingResult {
    private int totalProcessed;
    private int successCount;
    private int failureCount;
    private List<StudentValidationError> errors;
    private List<Student> createdStudents;
    private Duration processingTime;
    
    @Data
    @Builder
    public static class StudentValidationError {
        private int rowNumber;
        private String rollNumber;
        private String email;
        private List<String> validationErrors;
        private String errorType; // VALIDATION, DUPLICATE, SYSTEM_ERROR
    }
}