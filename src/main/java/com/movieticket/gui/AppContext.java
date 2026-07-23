package com.movieticket.gui;

import com.movieticket.model.*;
import com.movieticket.service.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds shared services and the current session/selection state as the user
 * moves through the booking flow (city -> theatre -> movie -> show -> seats).
 * Passed into every GUI panel so business logic stays out of the GUI classes.
 */
public class AppContext {

    public final AuthenticationService authService = new AuthenticationService();
    public final MovieService movieService = new MovieService();
    public final BookingService bookingService = new BookingService(movieService);
    public final PaymentService paymentService = new PaymentService();
    public final TicketService ticketService = new TicketService();
    public final HistoryService historyService = new HistoryService(bookingService, movieService);

    // ----- session state -----
    public Person currentUser;
    public String selectedCity;
    public Theatre selectedTheatre;
    public Movie selectedMovie;
    public Show selectedShow;
    public final List<String> selectedSeats = new ArrayList<>();
    public Booking currentBooking;
    public Ticket lastTicket;

    public Customer currentCustomer() {
        return (Customer) currentUser;
    }

    public void resetBookingSelection() {
        selectedCity = null;
        selectedTheatre = null;
        selectedMovie = null;
        selectedShow = null;
        selectedSeats.clear();
        currentBooking = null;
        lastTicket = null;
    }
}
