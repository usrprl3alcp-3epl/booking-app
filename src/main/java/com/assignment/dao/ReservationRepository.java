package com.assignment.dao;

import com.assignment.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByRoomId(Long roomId);

    List<Reservation> findByRoomIdAndStartDateBetween(Long roomId, LocalDateTime startDate, LocalDateTime endDate);

}