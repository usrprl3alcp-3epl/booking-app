package com.assignment.service.impl;

import com.assignment.dao.ReservationRepository;
import com.assignment.dao.RoomRepository;
import com.assignment.dto.BookingCalendar;
import com.assignment.exception.RoomNotFoundException;
import com.assignment.service.BookingCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingCalendarServiceImpl implements BookingCalendarService {

  private final RoomRepository roomRepository;
  private final ReservationRepository reservationRepository;

  @Autowired
  public BookingCalendarServiceImpl(RoomRepository roomRepository,
      ReservationRepository reservationRepository) {
    this.roomRepository = roomRepository;
    this.reservationRepository = reservationRepository;
  }

  @Override
  // TODO grouped chronologically by day
  public BookingCalendar buildBookingCalendar(Long roomId) throws RoomNotFoundException {
    if (roomRepository.findOne(roomId) == null) {
      throw new RoomNotFoundException();
    }
    BookingCalendar bookingCalendar = new BookingCalendar();
    bookingCalendar.setRoomId(roomId);
    reservationRepository.findByRoomId(roomId)
        .forEach(bookingCalendar::putReservation);
    return bookingCalendar;
  }

}
