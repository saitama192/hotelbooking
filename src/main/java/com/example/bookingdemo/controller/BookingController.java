package com.example.bookingdemo.controller;

import com.example.bookingdemo.advice.ResourceNotAvailableException;
import com.example.bookingdemo.dto.BookingDTO;
import com.example.bookingdemo.model.Booking;
import com.example.bookingdemo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/bookingapp")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Creates a new booking.
     *
     * @param hotelId the ID of the hotel
     * @param customerId the ID of the customer
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return the created booking
     * @throws IllegalArgumentException if the dates are invalid
     */
    @PostMapping("/booking")
    public ResponseEntity<BookingDTO> createBooking(@RequestParam("hotel-id") Long hotelId, @RequestParam("customer-id") Long customerId, @RequestParam("check-in") LocalDate checkInDate, @RequestParam("check-out") LocalDate checkOutDate) {
        //TODO: Implement this method
        //check if the date is in past
        if (checkInDate.isBefore(LocalDate.now()) || checkOutDate.isBefore(LocalDate.now()) || checkOutDate.isBefore(checkInDate)) {
            throw new IllegalArgumentException("invalid dates entered");
        }
        return ResponseEntity.ok().body(bookingService.createBooking(hotelId, customerId, checkInDate, checkOutDate));
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param bookingId the ID of the booking
     * @return the booking
     * @throws ResourceNotAvailableException if the booking is not found
     */
    @GetMapping("/booking")
    public ResponseEntity<BookingDTO> getBooking(@RequestParam("id") Long bookingId) {

        return ResponseEntity.ok().body(bookingService.getBookingById(bookingId));
    }

}
