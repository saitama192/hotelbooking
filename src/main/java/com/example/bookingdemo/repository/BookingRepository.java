package com.example.bookingdemo.repository;

import com.example.bookingdemo.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Custom query to find bookings that overlap with the given date range
    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId " +
            "AND ((b.checkInDate BETWEEN :checkInDate AND :checkOutDate) " +
            "OR (b.checkOutDate BETWEEN :checkInDate AND :checkOutDate))")
    List<Booking> findByRoomIdAndCheckOutDateGreaterThanEqualAndCheckInDateLessThanEqual(
            @Param("roomId") Long roomId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate);


}
