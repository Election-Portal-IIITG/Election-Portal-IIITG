package com.iiitg.election.services.FileService.Exceptions;

public class InvalidFileTypeException extends RuntimeException {

	public InvalidFileTypeException(String message) {
        super(message);
    }

    public InvalidFileTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
