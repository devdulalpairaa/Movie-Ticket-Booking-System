package com.movieticket.exception;

/** Thrown when a login attempt fails due to wrong username/password. */
public class InvalidLoginException extends Exception {
    public InvalidLoginException(String message) {
        super(message);
    }
}
