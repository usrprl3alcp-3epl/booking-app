package com.assignment.rest;

public class ErrorResponse {

    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(Throwable throwable) {
        this.message = throwable.getMessage();
    }

    public static ErrorResponse anErrorResponse(Throwable throwable) {
        return new ErrorResponse(throwable);
    }

    public static ErrorResponse anErrorResponse(String message) {
        return new ErrorResponse(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
