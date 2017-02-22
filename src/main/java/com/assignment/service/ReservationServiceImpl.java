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

import static org.apache.commons.lang3.Validate.notNull;

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
    public Reservation create(Reservation reservation) throws BookingException {
        validate(reservation);
        List<Reservation> reservations = fetchReservations(reservation);
        checkThatReservationFitsExistingRestrictions(reservation, reservations);

        return reservationRepository.save(reservation);
    }

    private void checkThatReservationFitsExistingRestrictions(Reservation reservation, List<Reservation> reservations)
            throws BookingException {
        Room room = roomRepository.findOne(reservation.getRoom().getId());
        checkThatReservationFitsRoomWorkingTime(reservation, room);
        checkThatReservationFitsWithOtherReservations(reservation, reservations);
    }

    private void checkThatReservationFitsWithOtherReservations(Reservation newReservation, List<Reservation> reservations)
            throws BookingException {
        String exceptionMessage = "New reservation overlaps with existing reservation";
        LocalDateTime newReservationStart = newReservation.getStartDate();
        LocalDateTime newReservationEnd = newReservation.getEndDate();

        for (Reservation reservation : reservations) {
            LocalDateTime reservationStart = reservation.getStartDate();
            LocalDateTime reservationEnd = reservation.getEndDate();
            if (reservationStart.isAfter(newReservationStart)
                    && reservationStart.isBefore(newReservationEnd)) {
                throw new BookingException(exceptionMessage);
            }
            if (reservationEnd.isAfter(newReservationStart)
                    && reservationEnd.isBefore(newReservationEnd)) {
                throw new BookingException(exceptionMessage);
            }
        }
    }

    private void checkThatReservationFitsRoomWorkingTime(Reservation reservation, Room room) throws BookingException {
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

    private void validate(Reservation reservation) {
        notNull(reservation.getRoom());
        notNull(reservation.getEmployee());
        // TODO implement other validation cases
    }
}
