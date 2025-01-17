package com.example.bookingdemo.service;

import com.example.bookingdemo.model.Room;
import com.example.bookingdemo.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public List<Room> findAvailableRooms(Long hotelId, LocalDate checkInDate, LocalDate checkOutDate) {
        return hotelRepository.findAvailableRoomsForHotelInDateRange(hotelId, checkInDate, checkOutDate);
    }
}
