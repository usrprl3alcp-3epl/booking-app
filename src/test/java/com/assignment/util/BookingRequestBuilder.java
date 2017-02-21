package com.assignment.util;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.assignment.domain.BookingRequest;
import com.assignment.domain.Employee;
import com.assignment.domain.Room;

public final class BookingRequestBuilder {
    private Long id;
    private LocalDateTime submissionDate;
    private LocalDateTime startDate;
    private LocalTime duration;
    private Employee employee;
    private Room room;

    private BookingRequestBuilder() {
    }

    public static BookingRequestBuilder aBookingRequest() {
        return new BookingRequestBuilder();
    }

    public static BookingRequestBuilder aBookingRequestWithDefaults() {
        return new BookingRequestBuilder().withSubmissionDate(LocalDateTime.now())
                .withStartDate(LocalDateTime.now()
                        .plusHours(1))
                .withDuration(LocalTime.of(0, 30));
    }

    public BookingRequestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public BookingRequestBuilder withSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
        return this;
    }

    public BookingRequestBuilder withStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public BookingRequestBuilder withDuration(LocalTime duration) {
        this.duration = duration;
        return this;
    }

    public BookingRequestBuilder withEmployee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public BookingRequestBuilder withRoom(Room room) {
        this.room = room;
        return this;
    }

    public BookingRequest build() {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setId(id);
        bookingRequest.setSubmissionDate(submissionDate);
        bookingRequest.setStartDate(startDate);
        bookingRequest.setDuration(duration);
        bookingRequest.setEmployee(employee);
        bookingRequest.setRoom(room);
        return bookingRequest;
    }
}
