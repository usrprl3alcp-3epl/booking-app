package com.assignment.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

  @Column(nullable = false)
  @NotNull(message = "validation.Reservation.startDate.notnull")
  private LocalDateTime startDate;

  @Column(nullable = false)
  @NotNull(message = "validation.Reservation.duration.notnull")
  private LocalTime duration;

  @ManyToOne(optional = false)
  @NotNull(message = "validation.Reservation.employee.notnull")
  private Employee employee;

  @ManyToOne(optional = false)
  @NotNull(message = "validation.Reservation.room.notnull")
  private Room room;

  private LocalDateTime submissionDate;

  public Reservation() {
    submissionDate = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
    if (startDate == null || duration == null) {
      return null;
    }
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
