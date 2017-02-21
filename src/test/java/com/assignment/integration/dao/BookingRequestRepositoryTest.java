package com.assignment.integration.dao;

import static com.assignment.util.BookingRequestUtils.beginningOfTheWorkDay;
import static com.assignment.util.BookingRequestUtils.endOfTheWorkDay;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.dao.BookingRequestRepository;
import com.assignment.dao.EmployeeRepository;
import com.assignment.dao.RoomRepository;
import com.assignment.domain.BookingRequest;
import com.assignment.domain.Employee;
import com.assignment.domain.Room;
import com.assignment.util.BookingRequestBuilder;

public class BookingRequestRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private BookingRequestRepository bookingRequestRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public JpaRepository getRepository() {
        return bookingRequestRepository;
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();
        prepareTestData();
    }

    @Test
    public void findByRoomIdAndStartDateBetween_successfully() throws Exception {
        LocalDateTime start = beginningOfTheWorkDay().plusHours(1);
        LocalDateTime end = start.plusHours(4);
        List<BookingRequest> bookingRequests = bookingRequestRepository.findByRoomIdAndStartDateBetween(1L, start, end);
        bookingRequests.forEach(br -> {
            assertThat(br.getStartDate(), lessThan(end));
            assertThat(br.getStartDate(), greaterThanOrEqualTo(start));
        });
    }

    private void prepareTestData() {
        LocalDateTime start = beginningOfTheWorkDay();
        LocalDateTime end = endOfTheWorkDay();
        Employee employee = employeeRepository.findOne(1L);
        Room room = roomRepository.findOne(1L);

        while (start.isBefore(end)) {
            BookingRequest bookingRequest = BookingRequestBuilder.aBookingRequestWithDefaults()
                    .withEmployee(employee)
                    .withStartDate(start)
                    .withRoom(room)
                    .build();
            bookingRequestRepository.save(bookingRequest);
            start = start.plusHours(1)
                    .plusMinutes(25);
        }
    }
}
