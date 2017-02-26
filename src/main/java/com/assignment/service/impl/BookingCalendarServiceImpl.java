package com.assignment.service.impl;

import com.assignment.dao.ReservationRepository;
import com.assignment.dao.RoomRepository;
import com.assignment.domain.Room;
import com.assignment.dto.BookingCalendar;
import com.assignment.exception.RoomNotFoundException;
import com.assignment.service.BookingCalendarService;
import java.util.ArrayList;
import java.util.List;
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
  public BookingCalendar buildBookingCalendar(Long roomId) throws RoomNotFoundException {
    if (roomRepository.findOne(roomId) == null) {
      throw new RoomNotFoundException();
    }
    return buildBookingCalendarInternal(roomId);
  }

  @Override
  public List<BookingCalendar> buildBookingCalendars() {
    final ArrayList<BookingCalendar> calendars = new ArrayList<>();
    final List<Room> rooms = roomRepository.findAll();

    rooms.forEach(room -> {
      BookingCalendar calendar = buildBookingCalendarInternal(room.getId());
      calendars.add(calendar);
    });

    return calendars;
  }

  private BookingCalendar buildBookingCalendarInternal(Long roomId) {
    final BookingCalendar bookingCalendar = new BookingCalendar();
    bookingCalendar.setRoomId(roomId);
    reservationRepository.findByRoomIdOrderByStartDateAsc(roomId)
        .forEach(bookingCalendar::putReservation);
    return bookingCalendar;
  }

}
