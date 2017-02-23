package com.assignment.service;

import com.assignment.dao.ReservationRepository;
import com.assignment.dao.RoomRepository;
import com.assignment.domain.Reservation;
import com.assignment.domain.Room;
import com.assignment.exception.BookingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public Reservation save(final Reservation reservation) throws BookingException {
        checkThatReservationFitsRoomWorkingTime(reservation);
        checkThatReservationFitsWithOtherReservations(reservation);
        return reservationRepository.save(reservation);
    }

    private void checkThatReservationFitsWithOtherReservations(final Reservation reservation)
            throws BookingException {
        List<Reservation> reservations = fetchReservations(reservation);

        if (reservations.isEmpty()) {
            return;
        }
        if (isReservationOneAndItself(reservation, reservations)) {
            return;
        }
        throw new BookingException("New reservation overlaps with existing reservation");
    }

    private boolean isReservationOneAndItself(Reservation reservation, List<Reservation> reservations) {
        if (reservations.size() > 1) {
            return false;
        }
        Reservation existingReservation = reservations.get(0);
        return !(reservation.getId() == null
                || existingReservation.getId() == null) && existingReservation.getId().equals(reservation.getId());
    }

    private void checkThatReservationFitsRoomWorkingTime(Reservation reservation) throws BookingException {
        Room room = roomRepository.findOne(reservation.getRoom().getId());
        LocalTime reservationStart = reservation.getStartDate().toLocalTime();
        LocalTime roomStart = room.getStartTime();
        LocalTime reservationEnd = reservation.getEndDate().toLocalTime();
        LocalTime roomEnd = room.getEndTime();

        if (reservationStart.isBefore(roomStart)
                || reservationStart.isAfter(roomEnd)
                || reservationEnd.isBefore(roomStart)
                || reservationEnd.isAfter(roomEnd)) {
            throw new BookingException("Reservation dates don't fit room restrictions");
        }
    }

    private List<Reservation> fetchReservations(Reservation reservation) {
        return reservationRepository.findOverlapped(reservation.getRoom().getId(),
                reservation.getStartDate(), reservation.getEndDate());
    }

}
