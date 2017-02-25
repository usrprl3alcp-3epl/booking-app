package com.assignment.unit.service;

import static com.assignment.util.EmployeeBuilder.anEmployeeWithDefaults;
import static com.assignment.util.ReservationUtils.beginningOfTheWorkDay;
import static com.assignment.util.ReservationUtils.endOfTheWorkDay;
import static com.assignment.util.ReservationUtils.generateReservations;
import static com.assignment.util.ReservationUtils.getRandomId;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import com.assignment.dao.ReservationRepository;
import com.assignment.dao.RoomRepository;
import com.assignment.domain.Reservation;
import com.assignment.domain.Room;
import com.assignment.dto.BookingCalendar;
import com.assignment.exception.RoomNotFoundException;
import com.assignment.service.BookingCalendarService;
import com.assignment.service.impl.BookingCalendarServiceImpl;
import com.assignment.util.RoomBuilder;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BookingCalendarServiceImplTest {

  @Mock
  private RoomRepository roomRepository;
  @Mock
  private ReservationRepository reservationRepository;

  private BookingCalendarService bookingCalendarService;

  @Before
  public void setUp() throws Exception {
    bookingCalendarService = new BookingCalendarServiceImpl(roomRepository, reservationRepository);
  }

  @Test(expected = RoomNotFoundException.class)
  public void buildBookingCalendar_roomNotExist_RoomNotFoundException() throws Exception {
    long notExistingRoomId = getRandomId();
    given(roomRepository.findOne(notExistingRoomId)).willReturn(null);
    whenBuildBookingCalendar(notExistingRoomId);
  }

  @Test
  public void buildBookingCalendar_roomExistsAndReservationsAreEmpty_emptyCalendar()
      throws Exception {
    // Given
    Room room = givenRoomExists();
    given(reservationRepository.findByRoomIdOrderByStartDateAsc(room.getId()))
        .willReturn(Collections.emptyList());
    // When
    BookingCalendar bookingCalendar = whenBuildBookingCalendar(room.getId());
    // Then
    thenBookingCalendarBuilded(room, bookingCalendar);
    assertThat(bookingCalendar.getBookings().size(), is(0));
  }

  @Test
  public void buildBookingCalendar_roomExistsAndReservationsNotEmpty_filledCalendar()
      throws RoomNotFoundException {
    // Given
    Room room = givenRoomExists();
    List<Reservation> reservations = givenReservationsAreNotEmpty(room);
    // When
    BookingCalendar bookingCalendar = whenBuildBookingCalendar(room.getId());
    // Then
    thenBookingCalendarBuilded(room, bookingCalendar);
    Map<LocalDate, List<Reservation>> bookings = bookingCalendar.getBookings();
    assertThat(bookings.size(), greaterThan(0));
    LocalDate today = beginningOfTheWorkDay().toLocalDate();
    assertThat(bookings, hasKey(today));
    List<Reservation> reservationsToday = bookings.get(today);
    assertThat(reservationsToday, not(empty()));
    assertThat(reservationsToday, is(reservations));
  }

  private Room givenRoomExists() {
    Room room = RoomBuilder.aRoomWithDefaults().withId(getRandomId()).build();
    given(roomRepository.findOne(room.getId())).willReturn(room);
    return room;
  }

  private List<Reservation> givenReservationsAreNotEmpty(Room room) {
    List<Reservation> reservations = generateReservations(
        anEmployeeWithDefaults().withId(getRandomId()).build(), room, beginningOfTheWorkDay(),
        endOfTheWorkDay());
    given(reservationRepository.findByRoomIdOrderByStartDateAsc(room.getId()))
        .willReturn(reservations);
    return reservations;
  }

  private BookingCalendar whenBuildBookingCalendar(Long roomId) throws RoomNotFoundException {
    return bookingCalendarService.buildBookingCalendar(roomId);
  }

  private void thenBookingCalendarBuilded(Room room, BookingCalendar bookingCalendar) {
    assertThat(bookingCalendar, notNullValue());
    assertThat(bookingCalendar.getRoomId(), equalTo(room.getId()));
  }

}
