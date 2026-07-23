package com.movieticket.model;

import java.io.Serializable;
import java.util.List;

/**
 * The physical ticket produced after a successful payment.
 * Booking "has-a" Ticket via COMPOSITION: a Ticket has no meaning and cannot
 * exist without the Booking that generated it.
 */
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String ticketId;
    private final String bookingId;
    private final String customerName;
    private final String movieTitle;
    private final String theatreName;
    private final String screenName;
    private final String showTime;
    private final List<String> seatNumbers;
    private final double amount;
    private final String paymentMethod;
    private final String bookingDate;

    public Ticket(String ticketId, String bookingId, String customerName, String movieTitle,
                  String theatreName, String screenName, String showTime,
                  List<String> seatNumbers, double amount, String paymentMethod, String bookingDate) {
        this.ticketId = ticketId;
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.movieTitle = movieTitle;
        this.theatreName = theatreName;
        this.screenName = screenName;
        this.showTime = showTime;
        this.seatNumbers = seatNumbers;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.bookingDate = bookingDate;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getTheatreName() {
        return theatreName;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getShowTime() {
        return showTime;
    }

    public List<String> getSeatNumbers() {
        return seatNumbers;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String prettyPrint() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== MOVIE TICKET ==========\n");
        sb.append("Ticket ID     : ").append(ticketId).append("\n");
        sb.append("Booking ID    : ").append(bookingId).append("\n");
        sb.append("Customer Name : ").append(customerName).append("\n");
        sb.append("Movie         : ").append(movieTitle).append("\n");
        sb.append("Theatre       : ").append(theatreName).append("\n");
        sb.append("Screen        : ").append(screenName).append("\n");
        sb.append("Show Time     : ").append(showTime).append("\n");
        sb.append("Seats         : ").append(String.join(", ", seatNumbers)).append("\n");
        sb.append("Amount Paid   : Rs. ").append(String.format("%.2f", amount)).append("\n");
        sb.append("Payment Mode  : ").append(paymentMethod).append("\n");
        sb.append("Booking Date  : ").append(bookingDate).append("\n");
        sb.append("===================================\n");
        return sb.toString();
    }
}
