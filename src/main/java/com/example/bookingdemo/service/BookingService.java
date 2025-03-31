package com.example.bookingdemo.service;

import com.example.bookingdemo.advice.ResourceNotAvailableException;
import com.example.bookingdemo.dto.BookingDTO;
import com.example.bookingdemo.model.Booking;
import com.example.bookingdemo.model.Customer;
import com.example.bookingdemo.model.Room;
import com.example.bookingdemo.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private CustomerService customerService;

    /**
     * Retrieves a booking by its ID.
     *
     * @param bookingID the ID of the booking
     * @return the booking DTO
     * @throws ResourceNotAvailableException if the booking is not found
     */
    public BookingDTO getBookingById(Long bookingID){
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

    /**
     * Creates a new booking.
     *
     * @param hotelId the ID of the hotel
     * @param userId the ID of the user
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return the created booking DTO
     * @throws IllegalArgumentException if the dates are invalid
     * @throws RuntimeException if no rooms are available for the selected dates
     */
    public BookingDTO createBooking(Long hotelId, Long userId, LocalDate checkInDate, LocalDate checkOutDate) {
        // Validate date range
        Customer customer = customerService.getCustomerById(userId);

        if (checkInDate.isBefore(LocalDate.now()) || checkOutDate.isBefore(LocalDate.now()) || checkOutDate.isBefore(checkInDate)) {
            throw new IllegalArgumentException("invalid check in and check out dates entered");
        }

        //fetch the rooms for Hotel
        List<Room> rooms = hotelService.findAvailableRooms(hotelId, checkInDate, checkOutDate);
        if(rooms.isEmpty()){
            throw new RuntimeException("No rooms available for the selected dates.");
        }

        Room room  = rooms.get(0); //get the first available room


        // Create the booking
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setCustomer(customer); // Assuming userId is stored in the booking
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);

        return getBookingDto(bookingRepository.save(booking));
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
            Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
            if (bookingOptional.isEmpty()) {
                throw new ResourceNotAvailableException("booking not found.");
            }
            Booking booking = bookingOptional.get();
            booking.setIsCancelled(true);
            bookingRepository.save(booking);
    }

    public List<BookingDTO> getAllBookings(Long customerId) {
        return new ArrayList<>();

    }
}
