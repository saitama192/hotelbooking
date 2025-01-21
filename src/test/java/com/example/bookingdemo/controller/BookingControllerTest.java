package com.example.bookingdemo.controller;

import com.example.bookingdemo.advice.ResourceNotAvailableException;
import com.example.bookingdemo.dto.BookingDTO;
import com.example.bookingdemo.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private BookingDTO bookingDTO;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        bookingDTO = new BookingDTO();
        bookingDTO.setId(1L);
        bookingDTO.setUserName("John Doe");
        bookingDTO.setHotelName("Hotel California");
        bookingDTO.setRoomType("Deluxe");
        bookingDTO.setCheckIn(LocalDate.of(2025, 12, 1));
        bookingDTO.setCheckOut(LocalDate.of(2025, 12, 10));
    }

    @Test
    void createBooking() throws Exception {
        when(bookingService.createBooking(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(bookingDTO);

        String bookingJson = objectMapper.writeValueAsString(bookingDTO);

        mockMvc.perform(post("/api/v1/bookingapp/booking")
                        .param("hotel-id", "1")
                        .param("customer-id", "1")
                        .param("check-in", "2025-12-01")
                        .param("check-out", "2025-12-10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson))
                .andExpect(status().isOk())
                .andExpect(content().json(bookingJson));
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.getBookingById(anyLong())).thenReturn(bookingDTO);

        mockMvc.perform(get("/api/v1/bookingapp/booking")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDTO)));
    }

}