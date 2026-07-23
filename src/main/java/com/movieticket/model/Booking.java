package com.movieticket.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents one booking transaction made by a Customer for a Show.
 * COMPOSITION with Ticket: the Ticket is created for, and destroyed with,
 * this specific Booking. ASSOCIATION with Customer and Show: this Booking
 * references them but does not own their lifecycle.
 */
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String bookingId;
    private final String customerId;
    private final String showId;
    private final List<String> seatNumbers;
    private double totalAmount;
    private String status; // CONFIRMED, CANCELLED, WAITLISTED
    private final String bookingDate;
    private Ticket ticket; // Composition

    public Booking(String bookingId, String customerId, String showId,
                   List<String> seatNumbers, double totalAmount, String bookingDate) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.showId = showId;
        this.seatNumbers = new ArrayList<>(seatNumbers);
        this.totalAmount = totalAmount;
        this.status = "PENDING";
        this.bookingDate = bookingDate;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getShowId() {
        return showId;
    }

    public List<String> getSeatNumbers() {
        return seatNumbers;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "Booking{" + bookingId + ", show=" + showId + ", seats=" + seatNumbers +
                ", status=" + status + "}";
    }
}
