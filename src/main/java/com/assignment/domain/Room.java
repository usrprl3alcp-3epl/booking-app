package com.assignment.domain;

import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
public class Room {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @Column(nullable = false)
  @NotNull(message = "validation.Room.code.notnull")
  private String code;

  @Column(nullable = false)
  @NotNull(message = "validation.Room.startTime.notnull")
  private LocalTime startTime;

  @NotNull(message = "validation.Room.endTime.notnull")
  @Column(nullable = false)
  private LocalTime endTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalTime startTime) {
    this.startTime = startTime;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalTime endTime) {
    this.endTime = endTime;
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
