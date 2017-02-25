package com.assignment.rest.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.assignment.dto.BookingCalendar;
import com.assignment.exception.RoomNotFoundException;
import com.assignment.rest.assembler.BookingCalendarResourceAssembler;
import com.assignment.service.BookingCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/calendar", produces = APPLICATION_JSON_VALUE)
public class BookingCalendarResource {

  private final BookingCalendarService calendarService;
  private final BookingCalendarResourceAssembler resourceAssembler;

  @Autowired
  public BookingCalendarResource(BookingCalendarService bookingCalendarService,
      BookingCalendarResourceAssembler resourceAssembler) {
    this.calendarService = bookingCalendarService;
    this.resourceAssembler = resourceAssembler;
  }

  @RequestMapping(value = "/{roomId}", method = GET)
  public ResponseEntity<Resource> getCalendar(@PathVariable("roomId") Long roomId) {
    try {
      BookingCalendar bookingCalendar = calendarService.buildBookingCalendar(roomId);
      return ok(resourceAssembler.toResource(bookingCalendar));
    } catch (RoomNotFoundException e) {
      return ResponseEntity.notFound()
          .build();
    }
  }

}
