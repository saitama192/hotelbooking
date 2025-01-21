package com.example.bookingdemo.service;

import com.example.bookingdemo.dto.BookingDTO;
import com.example.bookingdemo.model.Booking;
import com.example.bookingdemo.model.Customer;
import com.example.bookingdemo.model.Hotel;
import com.example.bookingdemo.model.Room;
import com.example.bookingdemo.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BookingServiceTest {

    @Mock
    private HotelService hotelService;

    @Mock
    private RoomService roomService;

    @Mock
    private CustomerService customerService;
    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void getBookingById() {
        Long bookingId = 1L;
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setCheckInDate(LocalDate.of(2025, 12, 1));
        booking.setCheckOutDate(LocalDate.of(2025, 12, 10));

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("hellouser@email.com");
        booking.setCustomer(customer);

        Hotel hotel = new Hotel();
        hotel.setName("Hotel California");


        Room room = new Room();
        room.setId(1L);
        room.setRoomType("Deluxe");
        room.setHotel(hotel);
        booking.setRoom(room);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        // Act
        BookingDTO result = bookingService.getBookingById(bookingId);

        // Assert
        assertEquals(bookingId, result.getId());
        assertEquals("John Doe", result.getUserName());
        assertEquals("Deluxe", result.getRoomType());
        assertEquals(LocalDate.of(2025, 12, 1), result.getCheckIn());
        assertEquals(LocalDate.of(2025, 12, 10), result.getCheckOut());
    }

    @Test
    void createBookingSuccess() {
        // Arrange
        // Arrange
        Long hotelId = 1L;
        Long userId = 1L;
        LocalDate checkInDate = LocalDate.of(2025, 12, 1);
        LocalDate checkOutDate = LocalDate.of(2025, 12, 10);

        Customer customer = new Customer();
        customer.setId(userId);
        customer.setName("John Doe");

        Hotel hotel = new Hotel();
        hotel.setId(hotelId);
        hotel.setName("Hotel California");

        Room room = new Room();
        room.setId(1L);
        room.setRoomType("Deluxe");
        room.setHotel(hotel);

        List<Room> availableRooms = Collections.singletonList(room);

        when(customerService.getCustomerById(userId)).thenReturn(customer);
        when(hotelService.findAvailableRooms(hotelId, checkInDate, checkOutDate)).thenReturn(availableRooms);
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setCustomer(customer);
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);

        when(bookingRepository.save(booking)).thenAnswer(invocation -> {
            Booking savedBooking = invocation.getArgument(0);
            savedBooking.setId(1L);
            return savedBooking;
        });


        // Act
        BookingDTO result = bookingService.createBooking(hotelId, userId, checkInDate, checkOutDate);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getUserName());
        assertEquals("Hotel California", result.getHotelName());
        assertEquals("Deluxe", result.getRoomType());
        assertEquals(checkInDate, result.getCheckIn());
        assertEquals(checkOutDate, result.getCheckOut());
    }

    @Test
    void createBookingNoAvailableRooms() {
        // Arrange
        Long hotelId = 1L;
        Long userId = 1L;
        LocalDate checkInDate = LocalDate.of(2025, 12, 1);
        LocalDate checkOutDate = LocalDate.of(2025, 12, 10);

        Customer customer = new Customer();
        customer.setId(userId);
        customer.setName("John Doe");

        when(customerService.getCustomerById(userId)).thenReturn(customer);
        when(hotelService.findAvailableRooms(hotelId, checkInDate, checkOutDate)).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> bookingService.createBooking(hotelId, userId, checkInDate, checkOutDate));
    }

    @Test
    void createBookingWrongDates() {
        // Arrange
        Long hotelId = 1L;
        Long userId = 1L;
        LocalDate checkInDate = LocalDate.of(2024, 12, 1);
        LocalDate checkOutDate = LocalDate.of(2024, 12, 1);

        Customer customer = new Customer();
        customer.setId(userId);
        customer.setName("John Doe");

        when(customerService.getCustomerById(userId)).thenReturn(customer);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(hotelId, userId, checkInDate, checkOutDate));
    }
}