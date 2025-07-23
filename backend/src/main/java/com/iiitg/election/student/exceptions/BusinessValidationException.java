package com.iiitg.election.student.exceptions;

public class BusinessValidationException extends RuntimeException {
    public BusinessValidationException(String message) {
        super(message);
    }
}