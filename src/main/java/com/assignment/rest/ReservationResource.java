package com.assignment.rest;

import static com.assignment.rest.ErrorResponse.anErrorResponse;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.assignment.domain.Reservation;
import com.assignment.exception.BookingException;
import com.assignment.service.ReservationService;

@RepositoryRestController
public class ReservationResource {

    private final ReservationService reservationService;

    @Autowired
    public ReservationResource(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @RequestMapping(method = POST, value = "/reservations")
    public @ResponseBody ResponseEntity<?> createReservation(@RequestBody final Resource<Reservation> newReservation) {
        try {
            Reservation reservation = reservationService.save(newReservation.getContent());
            return ok(reservation);
        } catch (BookingException e) {
            return badRequest().body(anErrorResponse(e));
        }
    }
}
