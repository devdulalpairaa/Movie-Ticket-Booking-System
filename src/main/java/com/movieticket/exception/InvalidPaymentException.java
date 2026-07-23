package com.movieticket.exception;

/** Thrown when a simulated payment fails validation. */
public class InvalidPaymentException extends Exception {
    public InvalidPaymentException(String message) {
        super(message);
    }
}
