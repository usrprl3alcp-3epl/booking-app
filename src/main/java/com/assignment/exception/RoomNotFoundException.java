package com.assignment.exception;

public class RoomNotFoundException extends BookingException {

  public RoomNotFoundException() {
  }

  public RoomNotFoundException(String message) {
    super(message);
  }
}
