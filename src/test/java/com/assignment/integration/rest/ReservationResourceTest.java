package com.assignment.integration.rest;

import static com.assignment.util.ReservationBuilder.aReservationWithDefaults;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.assignment.domain.Reservation;
import com.assignment.exception.BookingException;
import com.assignment.service.ReservationService;
import com.assignment.util.EmployeeBuilder;
import com.assignment.util.RoomBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReservationResourceTest {

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createReservation_fitRestriction_201Created() throws Exception {
        Reservation reservation = givenReservationWithEmployeeAndRoom();
        givenReservationServiceSavedSuccessfully(reservation);
        ResponseEntity<Reservation> response = whenCreateReservation(reservation);
        thenReservationCreatedSuccessfully(response, reservation);
    }

    private void givenReservationServiceSavedSuccessfully(Reservation reservation) throws BookingException {
        reservation.setId(235L);
        given(reservationService.save(any(Reservation.class))).willReturn(reservation);
    }

    private Reservation givenReservationWithEmployeeAndRoom() {
        return aReservationWithDefaults().withEmployee(EmployeeBuilder.anEmployeeWithDefaults()
                .build())
                .withRoom(RoomBuilder.aRoomWithDefaults()
                        .build())
                .build();
    }

    private ResponseEntity<Reservation> whenCreateReservation(Reservation reservation) {
        return restTemplate.postForEntity("/reservations", reservation, Reservation.class);
    }

    private void thenReservationCreatedSuccessfully(ResponseEntity<Reservation> response,
            Reservation expectedReservation) {
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        Reservation reservation = response.getBody();
        assertThat(reservation, equalTo(expectedReservation));
    }

}