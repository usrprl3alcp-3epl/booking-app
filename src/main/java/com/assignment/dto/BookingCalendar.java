package com.assignment.dto;

import com.assignment.domain.Reservation;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BookingCalendar {

  private Map<LocalDate, List<Reservation>> bookings;

  private Long roomId;

  public BookingCalendar() {
    this.bookings = new TreeMap<>();
  }

  public void putReservation(Reservation reservation) {
    LocalDate date = reservation.getStartDate()
        .toLocalDate();
    List<Reservation> reservationList = bookings.computeIfAbsent(date, k -> new ArrayList<>());
    reservationList.add(reservation);
  }

  public Map<LocalDate, List<Reservation>> getBookings() {
    return bookings;
  }

  public void setBookings(Map<LocalDate, List<Reservation>> bookings) {
    this.bookings = bookings;
  }

  public Long getRoomId() {
    return roomId;
  }

  public void setRoomId(Long roomId) {
    this.roomId = roomId;
  }
}
