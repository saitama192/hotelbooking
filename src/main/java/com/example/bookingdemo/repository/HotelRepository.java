package com.example.bookingdemo.repository;

import com.example.bookingdemo.model.Hotel;
import com.example.bookingdemo.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId AND NOT EXISTS (" +
            "SELECT b FROM Booking b WHERE b.room.id = r.id " +
            "AND ((b.checkInDate < :checkOutDate AND b.checkOutDate > :checkInDate))" +
            ")")
    List<Room> findAvailableRoomsForHotelInDateRange(@Param("hotelId") Long hotelId,
                                                     @Param("checkInDate") LocalDate checkInDate,
                                                     @Param("checkOutDate") LocalDate checkOutDate);

}
