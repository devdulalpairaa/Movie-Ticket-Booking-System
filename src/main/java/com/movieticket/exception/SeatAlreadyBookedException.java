package com.movieticket.exception;

/** Thrown when a user tries to book a seat that is already booked. */
public class SeatAlreadyBookedException extends Exception {
    public SeatAlreadyBookedException(String message) {
        super(message);
    }
}
