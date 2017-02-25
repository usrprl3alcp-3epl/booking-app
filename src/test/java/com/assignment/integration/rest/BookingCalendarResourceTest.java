package com.assignment.integration.rest;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.assignment.dto.BookingCalendar;
import com.assignment.exception.RoomNotFoundException;
import com.assignment.integration.BookingApplicationTest;
import com.assignment.service.BookingCalendarService;
import com.assignment.util.ReservationUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public class BookingCalendarResourceTest extends BookingApplicationTest {

  @MockBean
  BookingCalendarService bookingCalendarService;
  @Autowired
  private TestRestTemplate restTemplate;

  private static final String CALENDAR_RESOURCE_URL = "/calendar";

  @Test
  public void getCalendar_roomNotFound_404NotFound() throws Exception {
    Long roomId = givenRoomNotFound();
    ResponseEntity<Resource<BookingCalendar>> response = whenGetCalendar(roomId);
    assertThat(response.getStatusCode(), is(NOT_FOUND));
  }

  @Test
  public void getCalendar_emptyCalendar_200ok() throws Exception {
    // Given
    Long roomId = givenEmptyBookingCalendar();
    // When
    ResponseEntity<Resource<BookingCalendar>> response = whenGetCalendar(roomId);
    // Then
    assertThat(response.getStatusCode(), is(OK));
    assertThat(response.getBody(), notNullValue());
  }

  private long givenEmptyBookingCalendar() throws RoomNotFoundException {
    Long roomId = ReservationUtils.getRandomId();
    BookingCalendar bookingCalendar = new BookingCalendar();
    bookingCalendar.setRoomId(roomId);
    given(bookingCalendarService.buildBookingCalendar(roomId)).willReturn(bookingCalendar);
    return roomId;
  }

  private ResponseEntity<Resource<BookingCalendar>> whenGetCalendar(Long roomId) {
    String url = String.format("%s/%d", CALENDAR_RESOURCE_URL, roomId);
    return restTemplate.exchange(url, GET, HttpEntity.EMPTY,
        new ParameterizedTypeReference<Resource<BookingCalendar>>() {
        });
  }

  private Long givenRoomNotFound() throws RoomNotFoundException {
    long roomId = ReservationUtils.getRandomId();
    given(bookingCalendarService.buildBookingCalendar(roomId))
        .willThrow(new RoomNotFoundException());
    return roomId;
  }

}
