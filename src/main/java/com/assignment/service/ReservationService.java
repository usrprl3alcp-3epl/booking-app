package com.assignment.service;

import com.assignment.domain.Reservation;
import com.assignment.exception.BookingException;

public interface ReservationService {

    Reservation save(Reservation reservation) throws BookingException;

}
