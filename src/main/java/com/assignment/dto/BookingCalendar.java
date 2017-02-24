package com.assignment.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.assignment.domain.Reservation;

public class BookingCalendar {

    private Map<LocalDate, List<Reservation>> bookings;

    public BookingCalendar() {
        this.bookings = new TreeMap<>();
    }

    public void putReservation(Reservation reservation) {
        LocalDate date = reservation.getStartDate()
                .toLocalDate();
        List<Reservation> list = bookings.get(date);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(reservation);
        bookings.put(date, list);
    }

    public Map<LocalDate, List<Reservation>> getBookings() {
        return bookings;
    }

    public void setBookings(Map<LocalDate, List<Reservation>> bookings) {
        this.bookings = bookings;
    }

}
