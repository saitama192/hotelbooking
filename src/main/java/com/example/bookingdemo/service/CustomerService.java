package com.example.bookingdemo.service;

import com.example.bookingdemo.advice.ResourceNotAvailableException;
import com.example.bookingdemo.model.Customer;
import com.example.bookingdemo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    /**
     * Retrieves a customer by their ID.
     *
     * @param userId the ID of the customer
     * @return the customer
     * @throws ResourceNotAvailableException if the customer is not found
     */
    public Customer getCustomerById(Long userId){
        Optional<Customer> userOptional = customerRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new ResourceNotAvailableException("User not found.");
        }
        return userOptional.get();
    }
}
