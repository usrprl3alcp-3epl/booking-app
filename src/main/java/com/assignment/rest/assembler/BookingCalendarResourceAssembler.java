package com.assignment.rest.assembler;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.assignment.domain.Reservation;
import com.assignment.domain.Room;
import com.assignment.dto.BookingCalendar;
import com.assignment.rest.resource.BookingCalendarResource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.ControllerEntityLinks;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class BookingCalendarResourceAssembler extends
    ResourceAssemblerSupport<BookingCalendar, Resource> {

  private final EntityLinks entityLinks;
  private final ReservationResourceAssembler reservationResourceAssembler;
  private final ControllerEntityLinks controllerEntityLinks;

  @Autowired
  public BookingCalendarResourceAssembler(EntityLinks entityLinks,
      ReservationResourceAssembler reservationResourceAssembler,
      ControllerEntityLinks controllerEntityLinks) {
    super(BookingCalendarResource.class, Resource.class);
    this.entityLinks = entityLinks;
    this.reservationResourceAssembler = reservationResourceAssembler;
    this.controllerEntityLinks = controllerEntityLinks;
  }

  @Override
  public Resource toResource(BookingCalendar bookingCalendar) {
    Resource resource = createResourceWithId(bookingCalendar.getRoomId(), bookingCalendar);
    resource.add(entityLinks.linkToSingleResource(Room.class, bookingCalendar.getRoomId()));
    return resource;
  }

  public Resources<Resource> toResources(Collection<BookingCalendar> bookingCalendars) {
    final ArrayList<Resource> resources = new ArrayList<>();
    bookingCalendars.forEach(bc -> resources.add(toResource(bc)));
    Link selfRel = controllerEntityLinks.linkToCollectionResource(BookingCalendar.class)
        .withSelfRel();
    return new Resources<>(resources, selfRel);
  }

  @Override
  protected Resource instantiateResource(BookingCalendar bookingCalendar) {
    Map<LocalDate, List<Resource<Reservation>>> bc = bookingCalendar.getBookings().entrySet()
        .stream()
        .collect(toMap(Entry::getKey, listReservationMapper(), throwingMerger(), TreeMap::new));
    return new Resource<>(bc);
  }

  private Function<Entry<LocalDate, List<Reservation>>, List<Resource<Reservation>>> listReservationMapper() {
    return calendarEntry -> calendarEntry.getValue()
        .stream()
        .map(reservationResourceAssembler::toResource)
        .collect(toList());
  }

  private static <T> BinaryOperator<T> throwingMerger() {
    return (u, v) -> {
      throw new IllegalStateException(String.format("Duplicate key %s", u));
    };
  }


}
