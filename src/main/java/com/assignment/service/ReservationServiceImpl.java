package com.assignment.service;

import com.assignment.dao.ReservationRepository;
import com.assignment.dao.RoomRepository;
import com.assignment.domain.Reservation;
import com.assignment.domain.Room;
import com.assignment.exception.BookingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        String exceptionMessage = "New reservation overlaps with existing reservation";
        LocalDateTime reservationStart = reservation.getStartDate();
        LocalDateTime reservationEnd = reservation.getEndDate();

        for (Reservation existingReservation : reservations) {
            if (isReservationItself(reservation, existingReservation)) {
                continue;
            }

            LocalDateTime existingReservationStart = existingReservation.getStartDate();
            LocalDateTime existingReservationEnd = existingReservation.getEndDate();
            if (existingReservationStart.isAfter(reservationStart)
                    && existingReservationStart.isBefore(reservationEnd)) {
                throw new BookingException(exceptionMessage);
            }
            if (existingReservationEnd.isAfter(reservationStart)
                    && existingReservationEnd.isBefore(reservationEnd)) {
                throw new BookingException(exceptionMessage);
            }
            if (existingReservationStart.isEqual(reservationStart)
                    || existingReservationEnd.isEqual(reservationEnd)) {
                throw new BookingException(exceptionMessage);
            }
        }
    }

    private boolean isReservationItself(Reservation reservation, Reservation existingReservation) {
        if (reservation.getId() == null
                || existingReservation.getId() == null) {
            return false;
        }
        return existingReservation.getId().equals(reservation.getId());
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
        return reservationRepository.findByRoomIdAndStartDateBetween(reservation.getRoom().getId(),
                reservation.getStartDate(), reservation.getEndDate());
    }

}
