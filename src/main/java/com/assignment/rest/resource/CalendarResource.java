package com.assignment.rest.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.dto.BookingCalendar;
import com.assignment.exception.RoomNotFoundException;
import com.assignment.service.CalendarService;

@RestController
public class CalendarResource {

    private final CalendarService calendarService;

    @Autowired
    public CalendarResource(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @RequestMapping(value = "/calendar/{roomId}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BookingCalendar> getCalendar(@PathVariable("roomId") Long roomId) {
        try {
            BookingCalendar bookingCalendar = calendarService.buildBookingCalendar(roomId);
            return ResponseEntity.ok(bookingCalendar);
        } catch (RoomNotFoundException e) {
            return ResponseEntity.notFound()
                    .build();
        }
    }

}
