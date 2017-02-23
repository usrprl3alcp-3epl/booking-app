package com.assignment.rest;

import static com.assignment.rest.ErrorResponse.anErrorMessage;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.assignment.domain.Reservation;
import com.assignment.exception.BookingException;
import com.assignment.service.ReservationService;

/**
 * Custom implementation for POST and PUT methods instead of standard Spring Data-Rest.
 * GET and DELETE methods are not customized.
 * RestExceptionHandler handle some common errors like 500 or 404.
 */
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
        } catch (BookingException e) {
            return ResponseEntity.status(CONFLICT)
                    .body(anErrorMessage(e.getMessage()));
        }
    }

    @RequestMapping(value = "/reservations/{reservationId}", method = PUT)
    public @ResponseBody ResponseEntity<?> updateReservation(@RequestBody @Valid final Reservation bookingRequest,
            @PathVariable("reservationId") Long reservationId) {
        if (!isReservationExist(reservationId)) {
            return ResponseEntity.status(NOT_FOUND)
                    .build();
        }
        try {
            bookingRequest.setId(reservationId);
            Reservation reservation = reservationService.save(bookingRequest);
            return ResponseEntity.status(OK)
                    .body(reservation);
        } catch (BookingException e) {
            return ResponseEntity.status(CONFLICT)
                    .body(anErrorMessage(e.getMessage()));
        }
    }

    private boolean isReservationExist(Long reservationId) {
        return reservationService.get(reservationId) != null;
    }

}
