package com.movieticket.exception;

/** Thrown when there is no booking available to cancel/undo. */
public class NoBookingFoundException extends Exception {
    public NoBookingFoundException(String message) {
        super(message);
    }
}
