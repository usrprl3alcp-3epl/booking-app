package com.assignment.service;

import javax.validation.Valid;

import com.assignment.domain.Reservation;
import com.assignment.exception.BookingException;

public interface ReservationService {

    Reservation save(@Valid Reservation reservation) throws BookingException;

    Reservation get(Long id);
}
