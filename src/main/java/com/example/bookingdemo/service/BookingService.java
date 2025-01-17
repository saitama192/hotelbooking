package com.example.bookingdemo.service;

import com.example.bookingdemo.model.Booking;
import com.example.bookingdemo.model.Room;
import com.example.bookingdemo.model.User;
import com.example.bookingdemo.repository.BookingRepository;
import com.example.bookingdemo.repository.HotelRepository;
import com.example.bookingdemo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    public Booking createBooking(Long roomId, User user, LocalDate checkInDate, LocalDate checkOutDate) {
        // Validate date range
        if (checkInDate.isAfter(checkOutDate) || checkInDate.isEqual(checkOutDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }

        // Fetch the room
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isEmpty()) {
            throw new IllegalArgumentException("Room not found.");
        }

        Room room = roomOptional.get();

        // Check if the room is available for the given date range
        boolean isAvailable = isRoomAvailable(roomId, checkInDate, checkOutDate);
        if (!isAvailable) {
            throw new IllegalStateException("Room is not available for the selected dates.");
        }

        // Create the booking
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setUser(user); // Assuming userId is stored in the booking
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);

        return bookingRepository.save(booking);
    }

    // Check if a room is available for the given date range
    private boolean isRoomAvailable(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        // Fetch bookings for the room where there is an overlap in the date range
        return bookingRepository.findByRoomIdAndCheckOutDateGreaterThanEqualAndCheckInDateLessThanEqual(
                roomId, checkInDate, checkOutDate).isEmpty();
    }
}
