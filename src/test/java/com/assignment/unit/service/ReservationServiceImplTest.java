package com.assignment.unit.service;

import com.assignment.dao.ReservationRepository;
import com.assignment.dao.RoomRepository;
import com.assignment.domain.Employee;
import com.assignment.domain.Reservation;
import com.assignment.domain.Room;
import com.assignment.exception.BookingException;
import com.assignment.service.ReservationService;
import com.assignment.service.ReservationServiceImpl;
import com.assignment.util.EmployeeBuilder;
import com.assignment.util.RoomBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static com.assignment.util.ReservationBuilder.aReservationWithDefaults;
import static com.assignment.util.ReservationUtils.beginningOfTheWorkDay;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    private ReservationService reservationService;

    @Before
    public void setUp() throws Exception {
        reservationService = new ReservationServiceImpl(reservationRepository, roomRepository);
    }

    @Test
    public void create_fitWithOtherReservations_Successfully() throws Exception {
        Employee employee = givenEmployee();
        Room room = givenRoom();
        Reservation reservation = givenReservation(room, employee);
        givenExistingReservations(room);
        givenReservationFitsOtherReservations(reservation);
        givenReservationPersisted(reservation);
        Reservation createdReservation = whenCreateReservation(reservation);
        thenReservationCreatedSuccessfully(createdReservation);
    }

    @Test(expected = BookingException.class)
    public void create_overlapWithOtherReservationsByStartTime_Exception() throws Exception {
        Employee employee = givenEmployee();
        Room room = givenRoom();
        Reservation reservation = givenReservation(room, employee);
        givenExistingReservations(room);
        givenReservationOverlapsOtherReservationsByStartTime(reservation);
        givenReservationPersisted(reservation);
        whenCreateReservation(reservation);
    }

    @Test(expected = BookingException.class)
    public void create_overlapWithOtherReservationsByEndTime_Exception() throws Exception {
        Employee employee = givenEmployee();
        Room room = givenRoom();
        Reservation reservation = givenReservation(room, employee);
        givenExistingReservations(room);
        givenReservationOverlapsOtherReservationsByEndTime(reservation);
        givenReservationPersisted(reservation);
        whenCreateReservation(reservation);
    }

    @Test(expected = BookingException.class)
    public void create_outOfRoomWorkingTime_Exception() throws Exception {
        Employee employee = givenEmployee();
        Room room = givenRoom();
        Reservation reservation = givenReservation(room, employee);
        givenExistingReservations(room);
        givenReservationOutOfRoomWorkingTime(reservation);
        givenReservationPersisted(reservation);
        whenCreateReservation(reservation);
    }

    private void thenReservationCreatedSuccessfully(Reservation reservation) {
        assertThat(reservation, notNullValue());
    }

    private Reservation whenCreateReservation(Reservation reservation) throws BookingException {
        return reservationService.save(reservation);
    }

    /**
     * Reservation start time: 6:00, duration: 2 hours
     */
    private void givenReservationOutOfRoomWorkingTime(Reservation reservation) {
        reservation.setStartDate(beginningOfTheWorkDay().minusHours(2));
        reservation.setDuration(LocalTime.of(2, 0));
    }

    /**
     * Reservation start time: 13:30, duration: 2 hours
     */
    private void givenReservationFitsOtherReservations(Reservation reservation) {
        reservation.setStartDate(beginningOfTheWorkDay().plusHours(5).plusMinutes(30));
        reservation.setDuration(LocalTime.of(2, 0));
    }

    /**
     * Reservation start time: 10:30, duration: 2 hours
     */
    private void givenReservationOverlapsOtherReservationsByStartTime(Reservation reservation) {
        reservation.setStartDate(beginningOfTheWorkDay().plusHours(4).plusMinutes(30));
        reservation.setDuration(LocalTime.of(2, 0));
    }

    /**
     * Reservation start time: 8:00, duration: 2 hours 30 minutes, end time: 10:30
     */
    private void givenReservationOverlapsOtherReservationsByEndTime(Reservation reservation) {
        reservation.setStartDate(beginningOfTheWorkDay());
        reservation.setDuration(LocalTime.of(2, 30));
    }

    /**
     * The room reservations: 10:00-13:00
     */
    private void givenExistingReservations(Room room) {
        Reservation firstReservation = aReservationWithDefaults()
                .withStartDate(LocalDateTime.of(LocalDate.now(), room.getStartTime().plusHours(2)))
                .withDuration(LocalTime.of(3, 0))
                .withRoom(room).build();
        Reservation secondReservation = aReservationWithDefaults()
                .withStartDate(LocalDateTime.of(LocalDate.now(), room.getStartTime().plusHours(5)))
                .withDuration(LocalTime.of(0, 15))
                .withRoom(room).build();

        List<Reservation> reservations = Arrays.asList(firstReservation, secondReservation);
        when(reservationRepository.findByRoomIdAndStartDateBetween(any(), any(), any()))
                .thenReturn(reservations);
    }

    /**
     * The room's working hours 8:00 - 20:00
     */
    private Room givenRoom() {
        Room room = RoomBuilder.aRoomWithDefaults().build();
        when(roomRepository.findOne(any(Long.class))).thenReturn(room);
        return room;
    }

    private Employee givenEmployee() {
        return EmployeeBuilder.anEmployeeWithDefaults().build();
    }

    private void givenReservationPersisted(final Reservation reservation) {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
    }

    private Reservation givenReservation(Room room, Employee employee) {
        return aReservationWithDefaults()
                .withRoom(room)
                .withEmployee(employee).build();
    }

}