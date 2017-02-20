package com.assignment.integration.dao;

import com.assignment.dao.BookingRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public class BookingRequestRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private BookingRequestRepository bookingRequestRepository;

    @Override
    public JpaRepository getRepository() {
        return bookingRequestRepository;
    }
}
