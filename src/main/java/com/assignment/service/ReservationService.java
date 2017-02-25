package com.assignment.service;

import com.assignment.domain.Reservation;
import com.assignment.exception.BookingException;
import javax.validation.Valid;

public interface ReservationService {

  Reservation save(@Valid Reservation reservation) throws BookingException;

  Reservation get(Long id);
}
