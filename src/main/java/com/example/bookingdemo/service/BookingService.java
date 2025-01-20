package com.example.bookingdemo.service;

import com.example.bookingdemo.advice.ResourceNotAvailableException;
import com.example.bookingdemo.dto.BookingDTO;
import com.example.bookingdemo.model.Booking;
import com.example.bookingdemo.model.Customer;
import com.example.bookingdemo.model.Room;
import com.example.bookingdemo.repository.BookingRepository;
import com.example.bookingdemo.repository.HotelRepository;
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
    private RoomService roomService;

    @Autowired
    private CustomerService customerService;

    public BookingDTO getBookingById(Long bookingID){
        //TODO: add correct id
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingID);
        if (bookingOptional.isEmpty()) {
            throw new ResourceNotAvailableException("booking not found.");
        }

        return getBookingDto(bookingOptional.get());
    }

    private BookingDTO getBookingDto(Booking booking) {
        return BookingDTO.builder()
                .id(booking.getId())
                .userName(booking.getCustomer().getName())
                .hotelName(booking.getRoom().getHotel().getName())
                .roomType(booking.getRoom().getRoomType())
                .checkIn(booking.getCheckInDate())
                .checkOut(booking.getCheckOutDate())
                .isCancelled(booking.getIsCancelled())
                .build();
    }

    public Booking createBooking(Long roomId, Long userId, LocalDate checkInDate, LocalDate checkOutDate) {
        // Validate date range
        Customer customer = customerService.getCustomerById(userId);

        if (checkInDate.isAfter(checkOutDate) || checkInDate.isEqual(checkOutDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }

        // Fetch the room
        Room room = roomService.getRoomById(roomId);

        // Check if the room is available for the given date range
        boolean isAvailable = isRoomAvailable(roomId, checkInDate, checkOutDate);
        if (!isAvailable) {
            throw new IllegalStateException("Room is not available for the selected dates.");
        }

        // Create the booking
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setCustomer(customer); // Assuming userId is stored in the booking
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
