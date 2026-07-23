package com.movieticket.service;

import com.movieticket.dsa.CustomQueue;
import com.movieticket.dsa.CustomStack;
import com.movieticket.exception.NoBookingFoundException;
import com.movieticket.exception.SeatAlreadyBookedException;
import com.movieticket.model.*;
import com.movieticket.util.FileManager;
import com.movieticket.util.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Core booking engine: seat locking, booking creation/cancellation, undo
 * (CustomStack) and waiting list handling when a show is full (CustomQueue).
 */
public class BookingService {

    private final List<Booking> bookings;
    private final MovieService movieService;
    private int nextBookingId;

    /** Per-customer undo stack of their most recent bookings (DSA requirement). */
    private final Map<String, CustomStack<Booking>> undoStacks = new HashMap<>();

    /** Per-show waiting list, used when all seats are taken (DSA requirement). */
    private final Map<String, CustomQueue<String>> waitingLists = new HashMap<>();

    public BookingService(MovieService movieService) {
        this.movieService = movieService;
        this.bookings = new ArrayList<>(FileManager.loadBookings());
        this.nextBookingId = computeNextId();
        replaySeatStates();
    }

    private int computeNextId() {
        int max = 0;
        for (Booking b : bookings) {
            try {
                max = Math.max(max, Integer.parseInt(b.getBookingId().replaceAll("\\D", "")));
            } catch (NumberFormatException ignored) {
            }
        }
        return max + 1;
    }

    /** Marks seats as BOOKED for any bookings loaded from file at startup. */
    private void replaySeatStates() {
        for (Booking b : bookings) {
            if (!"CONFIRMED".equals(b.getStatus())) continue;
            Show show = movieService.getShowById(b.getShowId());
            if (show == null) continue;
            for (String seatNo : b.getSeatNumbers()) {
                Seat seat = findSeat(show.getScreen(), seatNo);
                if (seat != null) {
                    seat.setStatus(SeatStatus.BOOKED);
                }
            }
            undoStacks.computeIfAbsent(b.getCustomerId(), k -> new CustomStack<>()).push(b);
        }
    }

    private Seat findSeat(Screen screen, String seatNumber) {
        for (Seat[] row : screen.getSeatMatrix()) {
            for (Seat seat : row) {
                if (seat.getSeatNumber().equals(seatNumber)) {
                    return seat;
                }
            }
        }
        return null;
    }

    public boolean isShowFull(Show show) {
        for (Seat[] row : show.getScreen().getSeatMatrix()) {
            for (Seat seat : row) {
                if (seat.getStatus() == SeatStatus.AVAILABLE) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Adds a customer to the waiting list queue for a fully booked show. */
    public void joinWaitingList(Show show, String customerId) {
        waitingLists.computeIfAbsent(show.getShowId(), k -> new CustomQueue<>()).enqueue(customerId);
        Logger.action("Customer " + customerId + " added to waiting list for " + show);
    }

    public int getWaitingListSize(Show show) {
        CustomQueue<String> q = waitingLists.get(show.getShowId());
        return q == null ? 0 : q.size();
    }

    /**
     * Attempts to book the given seats for the given customer & show.
     * Throws SeatAlreadyBookedException if any requested seat is not available.
     */
    public Booking createBooking(Customer customer, Show show, List<String> seatNumbers)
            throws SeatAlreadyBookedException {
        List<Seat> targetSeats = new ArrayList<>();
        for (String seatNo : seatNumbers) {
            Seat seat = findSeat(show.getScreen(), seatNo);
            if (seat == null || seat.getStatus() == SeatStatus.BOOKED) {
                throw new SeatAlreadyBookedException("Seat " + seatNo + " is already booked. Please choose another seat.");
            }
            targetSeats.add(seat);
        }

        double total = 0;
        for (Seat seat : targetSeats) {
            total += seat.priceFor(show.getBasePrice());
        }

        String bookingDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
        Booking booking = new Booking("B" + (nextBookingId++), customer.getUserId(), show.getShowId(),
                seatNumbers, total, bookingDate);
        booking.setStatus("PENDING"); // becomes CONFIRMED only after successful payment
        bookings.add(booking);

        for (Seat seat : targetSeats) {
            seat.setStatus(SeatStatus.BOOKED);
        }

        Logger.action("Seats Selected: " + seatNumbers + " for show " + show);
        return booking;
    }

    /** Called after successful payment to persist the booking and push it to the undo stack. */
    public void confirmBooking(Booking booking) {
        booking.setStatus("CONFIRMED");
        FileManager.saveBookings(bookings);
        undoStacks.computeIfAbsent(booking.getCustomerId(), k -> new CustomStack<>()).push(booking);
        Logger.action("Booking Confirmed: " + booking.getBookingId());
    }

    /** Releases seats back to AVAILABLE if payment failed or booking abandoned. */
    public void releaseSeats(Booking booking, Show show) {
        for (String seatNo : booking.getSeatNumbers()) {
            Seat seat = findSeat(show.getScreen(), seatNo);
            if (seat != null) {
                seat.setStatus(SeatStatus.AVAILABLE);
            }
        }
        bookings.remove(booking);
        Logger.warn("Seats released back for booking " + booking.getBookingId());
    }

    /** Undo/cancel the customer's most recent confirmed booking (Stack-based). */
    public Booking undoLastBooking(String customerId) throws NoBookingFoundException {
        CustomStack<Booking> stack = undoStacks.get(customerId);
        if (stack == null || stack.isEmpty()) {
            throw new NoBookingFoundException("No recent booking available to cancel.");
        }
        Booking booking = stack.pop();
        booking.setStatus("CANCELLED");
        Show show = movieService.getShowById(booking.getShowId());
        if (show != null) {
            for (String seatNo : booking.getSeatNumbers()) {
                Seat seat = findSeat(show.getScreen(), seatNo);
                if (seat != null) {
                    seat.setStatus(SeatStatus.AVAILABLE);
                }
            }
        }
        FileManager.saveBookings(bookings);
        Logger.action("Last Booking Cancelled (Undo): " + booking.getBookingId());
        return booking;
    }

    public List<Booking> getBookingsForCustomer(String customerId) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getCustomerId().equals(customerId)) {
                result.add(b);
            }
        }
        return result;
    }

    public List<Booking> getAllBookings() {
        return bookings;
    }

    public void appendToFile(Booking booking) {
        FileManager.appendBooking(booking);
    }

    public void saveAll() {
        FileManager.saveBookings(bookings);
    }
}
