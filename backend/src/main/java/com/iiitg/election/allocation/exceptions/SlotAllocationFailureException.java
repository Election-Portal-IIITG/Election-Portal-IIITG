package com.iiitg.election.allocation.exceptions;

public class SlotAllocationFailureException extends RuntimeException {
    public SlotAllocationFailureException(String message) {
        super(message);
    }
}