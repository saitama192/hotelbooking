package com.example.bookingdemo.service;

import com.example.bookingdemo.advice.ResourceNotAvailableException;
import com.example.bookingdemo.model.Room;
import com.example.bookingdemo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    /**
     * Retrieves a room by its ID.
     *
     * @param roomId the ID of the room
     * @return the room
     * @throws ResourceNotAvailableException if the room is not found
     */
    public Room getRoomById(Long roomId) {
    Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isEmpty()) {
        throw new ResourceNotAvailableException("Room not found.");
    }
        return roomOptional.get();
    }
}
