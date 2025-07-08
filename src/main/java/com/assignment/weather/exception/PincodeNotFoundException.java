package com.assignment.weather.exception;

public class PincodeNotFoundException extends RuntimeException {
    public PincodeNotFoundException(String message) {
        super(message);
    }
} 