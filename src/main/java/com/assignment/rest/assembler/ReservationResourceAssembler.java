package com.assignment.rest.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.assignment.domain.Employee;
import com.assignment.domain.Reservation;
import com.assignment.domain.Room;
import com.assignment.rest.resource.ReservationResource;

@Component
public class ReservationResourceAssembler extends ResourceAssemblerSupport<Reservation, Resource> {

    private final EntityLinks entityLinks;

    @Autowired
    public ReservationResourceAssembler(EntityLinks entityLinks) {
        super(ReservationResource.class, Resource.class);
        this.entityLinks = entityLinks;
    }

    @Override
    public Resource<Reservation> toResource(Reservation reservation) {
        Link self = entityLinks.linkToSingleResource(Reservation.class, reservation.getId())
                .withSelfRel();
        Link room = entityLinks.linkToSingleResource(Room.class, reservation.getRoom()
                .getId());
        Link employee = entityLinks.linkToSingleResource(Employee.class, reservation.getEmployee()
                .getId());
        return new Resource<>(reservation, self, room, employee);
    }
}