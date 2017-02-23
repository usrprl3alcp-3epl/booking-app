package com.assignment.integration.rest;

import com.assignment.domain.Reservation;
import com.assignment.exception.BookingException;
import com.assignment.rest.ErrorResponse;
import com.assignment.service.ReservationService;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static com.assignment.util.EmployeeBuilder.anEmployeeWithDefaults;
import static com.assignment.util.ReservationBuilder.aReservationWithDefaults;
import static com.assignment.util.RoomBuilder.aRoomWithDefaults;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReservationResourceTest {

    @MockBean
    private ReservationService reservationService;
    @Autowired
    private TestRestTemplate restTemplate;

    private static final String RESERVATIONS_RESOURCE_URL = "/reservations";

    @Test
    public void createReservation_fitsRestrictions_201Created() throws Exception {
        Reservation reservation = givenReservationWithEmployeeAndRoom();
        givenReservationServiceWillSaveSuccessfully(reservation);
        ResponseEntity<Reservation> response = whenCreateReservation(reservation);
        thenReservationCreatedSuccessfully(response, reservation);
    }

    @Test
    public void createReservation_failsRestrictions_409Conflict() throws Exception {
        Reservation reservation = givenReservationWithEmployeeAndRoom();
        givenReservationServiceThrows(BookingException.class);
        ResponseEntity<ErrorResponse> response = whenCreateInvalidReservation(reservation);
        thenFailed(response, CONFLICT);
    }

    @Test
    public void createReservation_withoutEmployee_400BadRequest() throws Exception {
        Reservation reservation = givenReservationWithoutEmployee();
        ResponseEntity<ErrorResponse> response = whenCreateInvalidReservation(reservation);
        thenFailed(response, BAD_REQUEST);
    }

    @Test
    public void createReservation_withoutRoom_400BadRequest() throws Exception {
        Reservation reservation = givenReservationWithoutRoom();
        ResponseEntity<ErrorResponse> response = whenCreateInvalidReservation(reservation);
        thenFailed(response, BAD_REQUEST);
    }

    @Test
    public void createReservation_serverFails_500InternalServerError() throws Exception {
        Reservation reservation = givenReservationWithEmployeeAndRoom();
        givenReservationServiceThrows(RuntimeException.class);
        ResponseEntity<Reservation> response = whenCreateReservation(reservation);
        thenFailed(response, INTERNAL_SERVER_ERROR);
    }

    @Test
    public void updateReservation_fitsRestrictions_200ok() throws Exception {
        Reservation reservation = givenReservationWithEmployeeAndRoom();
        givenReservationExist(reservation);
        Reservation changedReservation = givenReservationChangedForUpdate(reservation);
        givenReservationServiceWillSaveSuccessfully(changedReservation);
        ResponseEntity<Reservation> response = whenUpdateReservation(changedReservation);
        thenReservationUpdatedSuccessfully(response, reservation);
    }

    @Test
    public void updateReservation_doNotExist_404NotFound() throws Exception {
        Reservation reservation = givenReservationWithEmployeeAndRoom();
        givenReservationNotExist(reservation);
        ResponseEntity<Reservation> response = whenUpdateReservation(reservation);
        thenFailed(response, NOT_FOUND);
    }

    @Test
    public void updateReservation_failsRestrictions_409Conflict() throws Exception {
        Reservation reservation = givenReservationWithEmployeeAndRoom();
        givenReservationExist(reservation);
        Reservation changedReservation = givenReservationChangedForUpdate(reservation);
        givenReservationServiceThrows(BookingException.class);
        ResponseEntity<ErrorResponse> response = whenCreateInvalidReservation(changedReservation);
        thenFailed(response, CONFLICT);
    }

    @Test
    public void getReservations_200ok() throws Exception {
        ResponseEntity<Object> response = restTemplate.getForEntity(RESERVATIONS_RESOURCE_URL, Object.class);
        assertThat(response.getStatusCode(), is(OK));
    }

    @Test
    public void getReservation_isNotExist_404NotFound() throws Exception {
        ResponseEntity<Reservation> response =
                restTemplate.getForEntity(RESERVATIONS_RESOURCE_URL + RandomUtils.nextLong(), Reservation.class);
        thenFailed(response, NOT_FOUND);
    }

    private Reservation givenReservationWithoutEmployee() {
        return aReservationWithDefaults().withRoom(aRoomWithDefaults().build())
                .build();
    }

    private void givenReservationExist(Reservation reservation) throws BookingException {
        setReservationIdIfHasNot(reservation);
        given(reservationService.get(reservation.getId())).willReturn(reservation);
    }

    private void givenReservationNotExist(Reservation reservation) {
        setReservationIdIfHasNot(reservation);
        given(reservationService.get(reservation.getId())).willReturn(null);
    }

    private Reservation givenReservationChangedForUpdate(Reservation reservation) {
        return aReservationWithDefaults().withId(reservation.getId())
                .withEmployee(anEmployeeWithDefaults().build())
                .withRoom(aRoomWithDefaults().build())
                .withStartDate(reservation.getStartDate().plusMinutes(1))
                .build();
    }

    private void givenReservationServiceThrows(Class exceptionClass) throws BookingException {
        given(reservationService.save(any(Reservation.class))).willThrow(exceptionClass);
    }

    private Reservation givenReservationWithoutRoom() {
        return aReservationWithDefaults().withEmployee(anEmployeeWithDefaults().build())
                .build();
    }

    private void givenReservationServiceWillSaveSuccessfully(Reservation reservation) throws BookingException {
        setReservationIdIfHasNot(reservation);
        given(reservationService.save(any(Reservation.class))).willReturn(reservation);
    }

    private void setReservationIdIfHasNot(Reservation reservation) {
        if (reservation.getId() == null) {
            reservation.setId(RandomUtils.nextLong());
        }
    }

    private Reservation givenReservationWithEmployeeAndRoom() {
        return aReservationWithDefaults().withEmployee(anEmployeeWithDefaults().build())
                .withRoom(aRoomWithDefaults().build())
                .build();
    }

    private ResponseEntity<Reservation> whenCreateReservation(Reservation reservation) {
        return restTemplate.postForEntity(RESERVATIONS_RESOURCE_URL, reservation, Reservation.class);
    }

    private ResponseEntity<ErrorResponse> whenCreateInvalidReservation(Reservation reservation) {
        return restTemplate.postForEntity(RESERVATIONS_RESOURCE_URL, reservation, ErrorResponse.class);
    }

    private ResponseEntity<Reservation> whenUpdateReservation(Reservation reservation) {
        String updateUrl = String.format("%s/%d", RESERVATIONS_RESOURCE_URL, reservation.getId());
        HttpEntity<Reservation> requestEntity = new HttpEntity<>(reservation);
        return restTemplate.exchange(updateUrl, PUT, requestEntity, Reservation.class);
    }

    private void thenReservationCreatedSuccessfully(ResponseEntity<Reservation> response,
            Reservation expectedReservation) {
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        Reservation reservation = response.getBody();
        assertThat(reservation, notNullValue());
        assertThat(reservation, equalTo(expectedReservation));
    }

    private void thenReservationUpdatedSuccessfully(ResponseEntity<Reservation> response,
                                                    Reservation expectedReservation) {
        assertThat(response.getStatusCode(), is(OK));
        Reservation reservation = response.getBody();
        assertThat(reservation, notNullValue());
        assertThat(reservation, not(equalTo(expectedReservation)));
    }

    private void thenFailed(ResponseEntity<?> response, HttpStatus expectedStatus) {
        assertThat(response.getStatusCode(), is(expectedStatus));
    }

}