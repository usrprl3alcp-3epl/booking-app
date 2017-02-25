package com.assignment.util;

import static com.assignment.util.ReservationBuilder.aReservationWithDefaults;

import com.assignment.domain.Employee;
import com.assignment.domain.Reservation;
import com.assignment.domain.Room;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.RandomUtils;

public final class ReservationUtils {

  private ReservationUtils() {
  }

  public static LocalDateTime beginningOfTheWorkDay() {
    return LocalDateTime.now()
        .withHour(8)
        .withMinute(0)
        .withSecond(0)
        .withNano(0);
  }

  public static LocalDateTime endOfTheWorkDay() {
    return LocalDateTime.now()
        .withHour(20)
        .withMinute(0)
        .withSecond(0)
        .withNano(0);
  }

  public static List<Reservation> generateReservations(Employee employee, Room room,
      LocalDateTime start, LocalDateTime end) {
    List<Reservation> reservations = new ArrayList<>();
    LocalDateTime time = LocalDateTime.from(start);
    while (time.isBefore(end)) {
      reservations.add(aReservationWithDefaults()
          .withEmployee(employee)
          .withStartDate(time.plusNanos(RandomUtils.nextLong(0, 10000)))
          .withRoom(room)
          .build());
      time = time.plusHours(1)
          .plusMinutes(25);
    }

    return reservations;
  }
}
