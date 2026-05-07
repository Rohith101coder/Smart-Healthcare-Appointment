package com.example.SmartConsult.exception;

public class DuplicateBookingException extends RuntimeException {
    public DuplicateBookingException(String message) {
        super(message);
    }

    public DuplicateBookingException(String message, Throwable cause) {
        super(message, cause);
    }
}
