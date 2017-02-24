package com.assignment.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.assignment.dao.EmployeeRepository;
import com.assignment.dao.ReservationRepository;
import com.assignment.dao.RoomRepository;
import com.assignment.domain.Reservation;
import com.assignment.domain.Room;
import com.assignment.exception.BookingException;

@Service
@Validated
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, RoomRepository roomRepository,
            EmployeeRepository employeeRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Reservation get(Long id) {
        return reservationRepository.findOne(id);
    }

    @Override
    public Reservation save(final Reservation reservation) throws BookingException {
        checkThatRoomAndEmployeeExist(reservation);
        checkThatReservationFitsRoomWorkingTime(reservation);
        checkThatReservationFitsWithOtherReservations(reservation);
        return reservationRepository.save(reservation);
    }

    private void checkThatRoomAndEmployeeExist(Reservation reservation) throws BookingException {
        String employeeErrorMessage = "Employee actually doesn't exist";
        String roomErrorMessage = "Room actually doesn't exist";
        checkThatEntityExist(employeeRepository, reservation.getEmployee()
                .getId(), employeeErrorMessage);
        checkThatEntityExist(roomRepository, reservation.getRoom()
                .getId(), roomErrorMessage);
    }

    private void checkThatEntityExist(JpaRepository<?, Long> repository, Long id, String message)
            throws BookingException {
        if (repository.findOne(id) == null) {
            throw new BookingException(message);
        }
    }

    private void checkThatReservationFitsWithOtherReservations(final Reservation reservation) throws BookingException {
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
        return !(reservation.getId() == null || existingReservation.getId() == null) && existingReservation.getId()
                .equals(reservation.getId());
    }

    private void checkThatReservationFitsRoomWorkingTime(Reservation reservation) throws BookingException {
        Room room = roomRepository.findOne(reservation.getRoom()
                .getId());
        LocalTime reservationStart = reservation.getStartDate()
                .toLocalTime();
        LocalTime roomStart = room.getStartTime();
        LocalTime reservationEnd = reservation.getEndDate()
                .toLocalTime();
        LocalTime roomEnd = room.getEndTime();

        if (reservationStart.isBefore(roomStart) || reservationStart.isAfter(roomEnd)
                || reservationEnd.isBefore(roomStart) || reservationEnd.isAfter(roomEnd)) {
            throw new BookingException("Reservation dates don't fit room restrictions");
        }
    }

    private List<Reservation> fetchReservations(Reservation reservation) {
        return reservationRepository.findOverlapped(reservation.getRoom()
                .getId(), reservation.getStartDate(), reservation.getEndDate());
    }

}
