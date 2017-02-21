package com.assignment.dao;

import com.assignment.domain.BookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRequestRepository extends JpaRepository<BookingRequest, Long> {

    List<BookingRequest> findByRoomId(Long roomId);

    List<BookingRequest> findByRoomIdAndStartDateBetween(Long roomId, LocalDateTime startDate, LocalDateTime endDate);

}