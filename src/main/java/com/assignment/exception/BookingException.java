package com.assignment.exception;

public class BookingException extends Exception {

    public BookingException() {
    }

    public BookingException(String message) {
        super(message);
    }

    public BookingException(String message, Throwable cause) {
        super(message, cause);
    }
}
