package com.example.bookingdemo.controller;

import com.example.bookingdemo.dto.BookingDTO;
import com.example.bookingdemo.model.Booking;
import com.example.bookingdemo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookingapp")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/hi")
    public String sayHi() {
        return "Hello!";
    }

//    @PostMapping("/booking")
//    public ResponseEntity<Booking> createBooking(@RequestParam("room-id") Long roomId, @RequestParam("customer-id") Long customerId, @RequestParam("check-in") LocalDate checkInDate, @RequestParam("check-out") LocalDate checkOutDate) {
//        //TODO: Implement this method
//        return ResponseEntity.ok().body(bookingService.getBookingById(12344L));
//    }
    @GetMapping("/booking")
    public ResponseEntity<BookingDTO> getBooking(@RequestParam("id") Long bookingId) {

        return ResponseEntity.ok().body(bookingService.getBookingById(bookingId));
    }

}
