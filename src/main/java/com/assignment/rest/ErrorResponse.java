package com.assignment.rest;

public class ErrorResponse<T> {

    private T error;

    public ErrorResponse() {
    }

    public ErrorResponse(T error) {
        this.error = error;
    }

    public T getError() {
        return error;
    }

    public void setError(T error) {
        this.error = error;
    }

    public static ErrorResponse<String> anErrorMessage(Throwable throwable) {
        ErrorResponse<String> errorResponse = new ErrorResponse<>();
        errorResponse.setError(throwable.getMessage());
        return errorResponse;
    }

    public static ErrorResponse<String> anErrorMessage(String message) {
        return new ErrorResponse<>(message);
    }
}
