package com.assignment.service;

import com.assignment.dto.BookingCalendar;
import com.assignment.exception.RoomNotFoundException;
import java.util.List;

public interface BookingCalendarService {

  BookingCalendar buildBookingCalendar(Long roomId) throws RoomNotFoundException;

  List<BookingCalendar> buildBookingCalendars();

}