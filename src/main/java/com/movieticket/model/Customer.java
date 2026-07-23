package com.movieticket.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete Person representing an end user of the system.
 * Demonstrates INHERITANCE (extends Person) and ASSOCIATION
 * (a Customer is associated with many Booking objects, but a Booking
 * can exist as its own record independent of the in-memory Customer list).
 */
public class Customer extends Person {

    private static final long serialVersionUID = 1L;

    private final List<String> bookingIds = new ArrayList<>();

    public Customer(String userId, String name, String username, String password,
                     String email, String phone) {
        super(userId, name, username, password, email, phone);
    }

    @Override
    public String getRole() {
        return "CUSTOMER";
    }

    public List<String> getBookingIds() {
        return bookingIds;
    }

    public void addBookingId(String bookingId) {
        bookingIds.add(bookingId);
    }

    public void removeBookingId(String bookingId) {
        bookingIds.remove(bookingId);
    }
}
