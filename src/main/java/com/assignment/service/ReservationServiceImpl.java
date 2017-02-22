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
    public Reservation save(final Reservation reservation) throws BookingException {
        // TODO switch to JSR-303 validation
        validate(reservation);
        checkThatReservationFitsRoomWorkingTime(reservation);
        checkThatReservationFitsWithOtherReservations(reservation);
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation update(final Reservation reservation) throws BookingException {
        // TODO implement common exception handler
        try {
            notNull(reservation.getId(), "Reservation Id is null");
        } catch (Exception e) {
            throw new BookingException(e);
        }
        return save(reservation);
    }

    private void checkThatReservationFitsWithOtherReservations(Reservation newReservation)
            throws BookingException {
        List<Reservation> reservations = fetchReservations(newReservation);
        String exceptionMessage = "New reservation overlaps with existing reservation";
        LocalDateTime newReservationStart = newReservation.getStartDate();
        LocalDateTime newReservationEnd = newReservation.getEndDate();

        for (Reservation reservation : reservations) {
            if (reservation.getId().equals(newReservation.getId())) {
                continue;
            }
            
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
            if (reservationStart.isEqual(newReservationStart)
                    || reservationEnd.isEqual(newReservationEnd)) {
                throw new BookingException(exceptionMessage);
            }
        }
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

    private void validate(Reservation reservation) throws BookingException {
        try {
            notNull(reservation.getRoom(), "Room is null or doesn't exist");
            notNull(reservation.getEmployee(), "Employee is null or doesn't exist");
            // TODO implement other validation cases
        } catch (Exception e) {
            throw new BookingException(e);
        }

    }
}
