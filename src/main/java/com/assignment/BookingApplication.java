package com.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.http.MediaType;

import com.assignment.domain.Employee;
import com.assignment.domain.Reservation;
import com.assignment.domain.Room;

@SpringBootApplication
public class BookingApplication extends RepositoryRestConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.setDefaultMediaType(MediaType.APPLICATION_JSON);
        config.exposeIdsFor(Reservation.class, Employee.class, Room.class);
    }
}
