package com.assignment.service;

import com.assignment.dto.BookingCalendar;
import com.assignment.exception.RoomNotFoundException;

public interface BookingCalendarService {

  BookingCalendar buildBookingCalendar(Long roomId) throws RoomNotFoundException;

}