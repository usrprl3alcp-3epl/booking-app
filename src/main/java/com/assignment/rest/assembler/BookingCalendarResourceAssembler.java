package com.assignment.rest.assembler;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.assignment.domain.Reservation;
import com.assignment.domain.Room;
import com.assignment.dto.BookingCalendar;
import com.assignment.rest.resource.BookingCalendarResource;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class BookingCalendarResourceAssembler extends
    ResourceAssemblerSupport<BookingCalendar, Resource> {

  private final EntityLinks entityLinks;
  private final ReservationResourceAssembler reservationResourceAssembler;

  @Autowired
  public BookingCalendarResourceAssembler(EntityLinks entityLinks,
      ReservationResourceAssembler reservationResourceAssembler) {
    super(BookingCalendarResource.class, Resource.class);
    this.entityLinks = entityLinks;
    this.reservationResourceAssembler = reservationResourceAssembler;
  }

  @Override
  public Resource toResource(BookingCalendar bookingCalendar) {
    Resource resource = createResourceWithId(bookingCalendar.getRoomId(), bookingCalendar);
    resource.add(entityLinks.linkToSingleResource(Room.class, bookingCalendar.getRoomId()));
    return resource;
  }

  @Override
  protected Resource instantiateResource(BookingCalendar bookingCalendar) {
    Map<LocalDate, List<Resource<Reservation>>> preparedBookingCalendar = bookingCalendar
        .getBookings().entrySet()
        .stream()
        .collect(toMap(Entry::getKey,
            calendarDateEntry -> calendarDateEntry.getValue()
                .stream()
                .map(reservationResourceAssembler::toResource)
                .collect(toList())));

    return new Resource<>(preparedBookingCalendar);
  }


}
