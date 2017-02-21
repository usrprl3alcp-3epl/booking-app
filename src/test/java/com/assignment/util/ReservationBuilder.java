package com.assignment.util;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.assignment.domain.Reservation;
import com.assignment.domain.Employee;
import com.assignment.domain.Room;

public final class ReservationBuilder {
    private Long id;
    private LocalDateTime submissionDate;
    private LocalDateTime startDate;
    private LocalTime duration;
    private Employee employee;
    private Room room;

    private ReservationBuilder() {
    }

    public static ReservationBuilder aReservation() {
        return new ReservationBuilder();
    }

    public static ReservationBuilder aReservationWithDefaults() {
        return new ReservationBuilder().withSubmissionDate(LocalDateTime.now())
                .withStartDate(LocalDateTime.now()
                        .plusHours(1))
                .withDuration(LocalTime.of(0, 30));
    }

    public ReservationBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ReservationBuilder withSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
        return this;
    }

    public ReservationBuilder withStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public ReservationBuilder withDuration(LocalTime duration) {
        this.duration = duration;
        return this;
    }

    public ReservationBuilder withEmployee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public ReservationBuilder withRoom(Room room) {
        this.room = room;
        return this;
    }

    public Reservation build() {
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setSubmissionDate(submissionDate);
        reservation.setStartDate(startDate);
        reservation.setDuration(duration);
        reservation.setEmployee(employee);
        reservation.setRoom(room);
        return reservation;
    }
}
