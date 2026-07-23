package com.movieticket.exception;

/** Thrown during registration when the username is already taken. */
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
