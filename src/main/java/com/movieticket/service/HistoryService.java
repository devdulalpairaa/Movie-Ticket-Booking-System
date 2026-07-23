package com.movieticket.service;

import com.movieticket.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides booking-history and simple analytics used by the Admin Dashboard
 * and Booking History screen.
 */
public class HistoryService {

    private final BookingService bookingService;
    private final MovieService movieService;

    public HistoryService(BookingService bookingService, MovieService movieService) {
        this.bookingService = bookingService;
        this.movieService = movieService;
    }

    public List<Booking> historyFor(String customerId) {
        return bookingService.getBookingsForCustomer(customerId);
    }

    public double totalRevenue() {
        double total = 0;
        for (Booking b : bookingService.getAllBookings()) {
            if ("CONFIRMED".equals(b.getStatus())) {
                total += b.getTotalAmount();
            }
        }
        return total;
    }

    public int totalConfirmedBookings() {
        int count = 0;
        for (Booking b : bookingService.getAllBookings()) {
            if ("CONFIRMED".equals(b.getStatus())) {
                count++;
            }
        }
        return count;
    }

    /** Movie title -> number of confirmed bookings, used for "Movie Statistics". */
    public List<String> movieStatistics() {
        List<String> stats = new ArrayList<>();
        for (Movie movie : movieService.getAllMovies()) {
            int bookedSeats = 0;
            for (Show show : movieService.getAllShows()) {
                if (!show.getMovie().equals(movie)) continue;
                for (Booking b : bookingService.getAllBookings()) {
                    if (b.getShowId().equals(show.getShowId()) && "CONFIRMED".equals(b.getStatus())) {
                        bookedSeats += b.getSeatNumbers().size();
                    }
                }
            }
            stats.add(movie.getTitle() + " -> " + bookedSeats + " seat(s) booked");
        }
        return stats;
    }
}
