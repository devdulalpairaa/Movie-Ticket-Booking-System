package com.movieticket.exception;

/** Thrown when a requested movie cannot be located in the system. */
public class MovieNotFoundException extends Exception {
    public MovieNotFoundException(String message) {
        super(message);
    }
}
