package com.assignment.rest.config;

import com.assignment.rest.resource.BookingCalendarResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.core.ControllerEntityLinks;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class BookingResourceProcessor implements
    ResourceProcessor<RepositoryLinksResource> {

  private final ControllerEntityLinks controllerEntityLinks;

  @Autowired
  public BookingResourceProcessor(ControllerEntityLinks controllerEntityLinks) {
    this.controllerEntityLinks = controllerEntityLinks;
  }

  @Override
  public RepositoryLinksResource process(RepositoryLinksResource resource) {
    resource.add(ControllerLinkBuilder.linkTo(BookingCalendarResource.class).withRel("calendars"));
    return resource;
  }
}
