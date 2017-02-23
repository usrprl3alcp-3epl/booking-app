package com.assignment.rest;

import static com.assignment.rest.ErrorResponse.anErrorMessage;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.assignment.domain.Reservation;
import com.assignment.exception.BookingException;
import com.assignment.service.ReservationService;

@RepositoryRestController
@Validated
public class ReservationResource {

    private final ReservationService reservationService;

    @Autowired
    public ReservationResource(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @RequestMapping(value = "/reservations", method = POST)
    public @ResponseBody ResponseEntity<?> createReservation(@RequestBody @Valid final Reservation bookingRequest) {
        try {
            Reservation reservation = reservationService.save(bookingRequest);
            return ResponseEntity.status(CREATED)
                    .body(reservation);
        } catch (BookingException ex) {
            return ResponseEntity.status(CONFLICT)
                    .body(anErrorMessage(ex.getMessage()));
        }
    }

}
