package com.movieticket.service;

import com.movieticket.model.*;
import com.movieticket.util.FileManager;
import com.movieticket.util.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Generates and persists Tickets once a Booking has been paid for.
 * Booking "has-a" Ticket -- COMPOSITION, created here.
 */
public class TicketService {

    public Ticket generateTicket(Booking booking, Show show, Customer customer, Payment payment) {
        String ticketId = "TCK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));

        Ticket ticket = new Ticket(
                ticketId,
                booking.getBookingId(),
                customer.getName(),
                show.getMovie().getTitle(),
                show.getTheatre().getName(),
                show.getScreen().getScreenName(),
                show.getShowTime(),
                booking.getSeatNumbers(),
                payment.getAmount(),
                payment.getMethod().name(),
                date
        );

        booking.setTicket(ticket);
        FileManager.appendTicket(ticket);
        Logger.action("Ticket Generated: " + ticketId);
        System.out.println(ticket.prettyPrint());
        return ticket;
    }
}
