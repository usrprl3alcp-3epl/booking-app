package com.assignment.integration.dao;

import static com.assignment.util.ReservationUtils.beginningOfTheWorkDay;
import static com.assignment.util.ReservationUtils.endOfTheWorkDay;
import static com.assignment.util.ReservationUtils.generateReservations;
import static com.assignment.util.ReservationUtils.getRandomId;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import com.assignment.dao.EmployeeRepository;
import com.assignment.dao.ReservationRepository;
import com.assignment.dao.RoomRepository;
import com.assignment.domain.Employee;
import com.assignment.domain.Reservation;
import com.assignment.domain.Room;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public class ReservationRepositoryTest extends AbstractRepositoryTest {

  private static final long FIRST_ROOM_ID = 1L;
  private static final long FIRST_EMPLOYEE_ID = 1L;

  @Autowired
  private ReservationRepository reservationRepository;
  @Autowired
  private RoomRepository roomRepository;
  @Autowired
  private EmployeeRepository employeeRepository;

  @Override
  public JpaRepository getRepository() {
    return reservationRepository;
  }

  @Override
  @Before
  public void setUp() {
    super.setUp();
    prepareTestData();
  }


  @Test
  public void findByRoomIdOrderByStartDateAsc_roomAndReservationsExist_successfully() {
    List<Reservation> reservations = reservationRepository
        .findByRoomIdOrderByStartDateAsc(FIRST_ROOM_ID);
    assertThat(reservations, not(empty()));
    assertThatReservationsInChronologicalOrder(reservations);
  }

  @Test
  public void findByRoomIdOrderByStartDateAsc_roomNotExist_emptyList() {
    List<Reservation> reservations = reservationRepository
        .findByRoomIdOrderByStartDateAsc(getRandomId());
    assertThat(reservations, is(empty()));
  }

  @Test
  public void findByRoomIdAndStartDateBetween_roomAndReservationsExist_successfully()
      throws Exception {
    LocalDateTime start = beginningOfTheWorkDay().plusHours(1);
    LocalDateTime end = start.plusHours(4);
    List<Reservation> reservations = reservationRepository
        .findByRoomIdAndStartDateBetween(FIRST_ROOM_ID, start, end);
    assertThatReservationsBetweenStartAndEndDates(reservations, start, end);
  }

  private void assertThatReservationsBetweenStartAndEndDates(List<Reservation> reservations,
      LocalDateTime start, LocalDateTime end) {
    reservations.forEach(reservation -> {
      assertThat(reservation.getStartDate(), lessThan(end));
      assertThat(reservation.getStartDate(), greaterThanOrEqualTo(start));
    });
  }

  private void assertThatReservationsInChronologicalOrder(List<Reservation> reservations) {
    Reservation previousReservation = null;
    for (Reservation reservation : reservations) {
      if (previousReservation == null) {
        previousReservation = reservation;
        continue;
      }
      assertThat(reservation.getStartDate(), is(greaterThan(previousReservation.getStartDate())));
      previousReservation = reservation;
    }
  }

  private void prepareTestData() {
    Employee employee = employeeRepository.findOne(FIRST_EMPLOYEE_ID);
    Room room = roomRepository.findOne(FIRST_ROOM_ID);
    reservationRepository
        .save(generateReservations(employee, room, beginningOfTheWorkDay(), endOfTheWorkDay()));
    generateReservations(employee, room, beginningOfTheWorkDay().plusDays(3),
        endOfTheWorkDay().plusDays(3));
    generateReservations(employee, room, beginningOfTheWorkDay().plusMonths(2),
        endOfTheWorkDay().plusMonths(2));
  }

}
