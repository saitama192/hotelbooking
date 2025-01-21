# Booking Demo Application

This is a demo project for a booking application built with Spring Boot.

## Table of Contents

- [Requirements](#requirements)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Database](#database)

## Requirements

- Java 17
- Maven
- H2 Database

## Installation

1. Clone the repository:

    ```sh
    git clone https://github.com/saitama192/bookingdemo.git
    cd bookingdemo
    ```

2. Build the project:

    ```sh
    mvn clean install
    ```

## Running the Application

1. Start the application:

    ```sh
    mvn spring-boot:run
    ```

2. The application will be available at `http://localhost:8080`.

## API Endpoints

### Booking

- **Create a new booking**

    ```http
    POST /api/v1/bookingapp/booking
    ```

    **Parameters:**
    - `hotel-id` (Long): The ID of the hotel
    - `customer-id` (Long): The ID of the customer
    - `check-in` (LocalDate): The check-in date
    - `check-out` (LocalDate): The check-out date

- **Retrieve a booking by its ID**

    ```http
    GET /api/v1/bookingapp/booking
    ```

    **Parameters:**
    - `id` (Long): The ID of the booking

## Database

The application uses an H2 in-memory database. A sample SQL file with data is included in the project.
