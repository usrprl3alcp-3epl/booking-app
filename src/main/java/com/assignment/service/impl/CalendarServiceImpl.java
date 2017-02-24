package com.assignment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.dao.ReservationRepository;
import com.assignment.dao.RoomRepository;
import com.assignment.dto.BookingCalendar;
import com.assignment.exception.RoomNotFoundException;
import com.assignment.service.CalendarService;

@Service
public class CalendarServiceImpl implements CalendarService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public CalendarServiceImpl(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public BookingCalendar buildBookingCalendar(Long roomId) throws RoomNotFoundException {
        if (roomRepository.findOne(roomId) == null) {
            throw new RoomNotFoundException();
        }
        BookingCalendar bookingCalendar = new BookingCalendar();
        reservationRepository.findByRoomId(roomId)
                .forEach(bookingCalendar::putReservation);
        return bookingCalendar;
    }

}
