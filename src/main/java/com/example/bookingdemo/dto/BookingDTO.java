package com.example.bookingdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDTO {
    private long id;
    private String userName;
    private String hotelName;
    private LocalDate checkIn;

    private LocalDate checkOut;

    private String roomType;
    private Boolean isCancelled;
}
