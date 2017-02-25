package com.assignment.dao;

import com.assignment.domain.Reservation;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data-REST will create implementation for Repository and REST resource by itself.
 * We should just configure and customize it.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  List<Reservation> findByRoomId(@Param("roomId") Long roomId);

  List<Reservation> findByRoomIdOrderByStartDateAsc(@Param("roomId") Long roomId);

  List<Reservation> findByRoomIdAndStartDateBetween(@Param("roomId") Long roomId,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate);

  List<Reservation> findOverlapped(@Param("roomId") Long roomId,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate);

}