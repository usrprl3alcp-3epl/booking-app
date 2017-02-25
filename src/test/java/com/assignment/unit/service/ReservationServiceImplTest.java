package com.assignment.unit.service;

import static com.assignment.util.ReservationBuilder.aReservationWithDefaults;
import static com.assignment.util.ReservationUtils.beginningOfTheWorkDay;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.assignment.dao.EmployeeRepository;
import com.assignment.dao.ReservationRepository;
import com.assignment.dao.RoomRepository;
import com.assignment.domain.Employee;
import com.assignment.domain.Reservation;
import com.assignment.domain.Room;
import com.assignment.exception.BookingException;
import com.assignment.service.ReservationService;
import com.assignment.service.impl.ReservationServiceImpl;
import com.assignment.util.EmployeeBuilder;
import com.assignment.util.RoomBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceImplTest {

  @Mock
  private ReservationRepository reservationRepository;
  @Mock
  private RoomRepository roomRepository;
  @Mock
  private EmployeeRepository employeeRepository;

  private ReservationService reservationService;

  @Before
  public void setUp() throws Exception {
    reservationService = new ReservationServiceImpl(reservationRepository, roomRepository,
        employeeRepository);
  }

  @Test
  public void create_fitWithOtherReservations_Successfully() throws Exception {
    Employee employee = givenEmployee();
    Room room = givenRoom();
    Reservation reservation = givenReservation(room, employee);
    givenReservationFitsOtherReservations();
    givenReservationPersisted(reservation);
    Reservation createdReservation = whenCreateReservation(reservation);
    thenReservationCreatedSuccessfully(createdReservation);
  }

  @Test(expected = BookingException.class)
  public void create_overlapWithOtherReservations_Exception() throws Exception {
    Employee employee = givenEmployee();
    Room room = givenRoom();
    Reservation reservation = givenReservation(room, employee);
    givenReservationOverlapsOtherReservationsByStartTime(reservation, room);
    whenCreateReservation(reservation);
  }

  @Test(expected = BookingException.class)
  public void create_outOfRoomWorkingTime_Exception() throws Exception {
    Employee employee = givenEmployee();
    Room room = givenRoom();
    Reservation reservation = givenReservation(room, employee);
    givenReservationOutOfRoomWorkingTime(reservation);
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

  private void givenReservationFitsOtherReservations() {
    when(reservationRepository.findOverlapped(any(), any(), any()))
        .thenReturn(Collections.emptyList());
  }

  private void givenReservationOverlapsOtherReservationsByStartTime(Reservation reservation,
      Room room) {
    reservation.setStartDate(beginningOfTheWorkDay().plusHours(4)
        .plusMinutes(30));
    reservation.setDuration(LocalTime.of(2, 0));

    Reservation firstReservation =
        aReservationWithDefaults()
            .withStartDate(LocalDateTime.of(LocalDate.now(), room.getStartTime()
                .plusHours(2)))
            .withDuration(LocalTime.of(3, 0))
            .withRoom(room)
            .build();
    Reservation secondReservation =
        aReservationWithDefaults()
            .withStartDate(LocalDateTime.of(LocalDate.now(), room.getStartTime()
                .plusHours(5)))
            .withDuration(LocalTime.of(0, 15))
            .withRoom(room)
            .build();

    List<Reservation> reservations = Arrays.asList(firstReservation, secondReservation);
    when(reservationRepository.findOverlapped(any(), any(), any())).thenReturn(reservations);
  }

  /**
   * The room's working hours 8:00 - 20:00
   */
  private Room givenRoom() {
    Room room = RoomBuilder.aRoomWithDefaults()
        .build();
    when(roomRepository.findOne(any(Long.class))).thenReturn(room);
    return room;
  }

  private Employee givenEmployee() {
    Employee employee = EmployeeBuilder.anEmployeeWithDefaults()
        .build();
    when(employeeRepository.findOne(any(Long.class))).thenReturn(employee);
    return employee;
  }

  private void givenReservationPersisted(final Reservation reservation) {
    when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
  }

  private Reservation givenReservation(Room room, Employee employee) {
    return aReservationWithDefaults().withRoom(room)
        .withEmployee(employee)
        .build();
  }

}