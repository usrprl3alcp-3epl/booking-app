package com.assignment.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Access(AccessType.FIELD)
@NamedQuery(name = "Reservation.findOverlapped",
        query = "select r from Reservation r where (r.room.id = :roomId)" +
                "and ((r.startDate >= :startDate and r.startDate <= :endDate) or" +
                "(r.endDate > :startDate and r.endDate <= :endDate))")
public class Reservation {

    @Id
    @GeneratedValue
    private Long id;

    // TODO should be generated ?
    private LocalDateTime submissionDate;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalTime duration;

    @ManyToOne(optional = false)
    @NotNull
    private Employee employee;

    @ManyToOne(optional = false)
    @NotNull
    private Room room;

    public Long getId() {
        return id;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Access(AccessType.PROPERTY)
    public LocalDateTime getEndDate() {
        return startDate.plusHours(duration.getHour())
                .plusMinutes(duration.getMinute());
    }

    public void setEndDate(LocalDateTime endDate) {
        // nothing to do
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
