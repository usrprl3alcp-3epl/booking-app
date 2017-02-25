package com.assignment.unit.dto;

import static com.assignment.util.EmployeeBuilder.anEmployeeWithDefaults;
import static com.assignment.util.ReservationUtils.beginningOfTheWorkDay;
import static com.assignment.util.ReservationUtils.endOfTheWorkDay;
import static com.assignment.util.ReservationUtils.generateReservations;
import static com.assignment.util.RoomBuilder.aRoomWithDefaults;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import com.assignment.domain.Employee;
import com.assignment.domain.Reservation;
import com.assignment.domain.Room;
import com.assignment.dto.BookingCalendar;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.RandomUtils;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Test;

public class BookingCalendarTest {

  @Test
  public void putReservation_checkGroupedByDayAsc_success() throws Exception {
    // Given
    BookingCalendar bookingCalendar = new BookingCalendar();
    Employee employee = givenEmployee();
    Room room = givenRoom();

    LocalDateTime todayStart = beginningOfTheWorkDay();
    LocalDateTime todayEnd = endOfTheWorkDay();
    LocalDateTime yesterdayStart = beginningOfTheWorkDay().minusDays(1);
    LocalDateTime yesterdayEnd = endOfTheWorkDay().minusDays(1);
    LocalDateTime tomorrowStart = beginningOfTheWorkDay().plusDays(1);
    LocalDateTime tomorrowEnd = endOfTheWorkDay().plusDays(1);

    List<Reservation> reservationsToday = generateReservations(employee, room, todayStart,
        todayEnd);
    List<Reservation> reservationsYesterday = generateReservations(employee, room, yesterdayStart,
        yesterdayEnd);
    List<Reservation> reservationsTomorrow = generateReservations(employee, room, tomorrowStart,
        tomorrowEnd);

    // When
    whenPopulateBookingCalendar(bookingCalendar, reservationsToday, reservationsYesterday,
        reservationsTomorrow);

    // Then
    thenBookingCalendarGroupedByDayAsc(bookingCalendar.getBookings(), yesterdayStart.toLocalDate(),
        todayStart.toLocalDate(), tomorrowStart.toLocalDate());
  }

  private void thenBookingCalendarGroupedByDayAsc(Map<LocalDate, List<Reservation>> bookings,
      LocalDate... dates) {
    assertThat(bookings.size(), equalTo(dates.length));
    assertThat(bookings.keySet(), IsIterableContainingInOrder
        .contains(dates));
  }

  @SafeVarargs
  private final void whenPopulateBookingCalendar(BookingCalendar bookingCalendar,
      List<Reservation>... reservationsLists) {
    for (List<Reservation> reservationList : reservationsLists) {
      reservationList.forEach(bookingCalendar::putReservation);
    }
  }

  private Room givenRoom() {
    return aRoomWithDefaults().withId(RandomUtils.nextLong()).build();
  }

  private Employee givenEmployee() {
    return anEmployeeWithDefaults().withId(RandomUtils.nextLong()).build();
  }
}
