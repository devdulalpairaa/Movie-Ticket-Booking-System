package com.movieticket.gui;

import javax.swing.*;
import java.awt.*;

/** The single top-level JFrame. Uses CardLayout to switch between all screens. */
public class MainFrame extends JFrame {

    public static final String LOGIN = "LOGIN";
    public static final String REGISTER = "REGISTER";
    public static final String DASHBOARD = "DASHBOARD";
    public static final String THEATRE_SELECTION = "THEATRE_SELECTION";
    public static final String MOVIE_LIST = "MOVIE_LIST";
    public static final String MOVIE_DETAILS = "MOVIE_DETAILS";
    public static final String SEAT_LAYOUT = "SEAT_LAYOUT";
    public static final String PAYMENT = "PAYMENT";
    public static final String TICKET = "TICKET";
    public static final String HISTORY = "HISTORY";
    public static final String ADMIN_DASHBOARD = "ADMIN_DASHBOARD";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardContainer = new JPanel(cardLayout);
    private final AppContext ctx = new AppContext();

    private final LoginScreen loginScreen;
    private final RegisterScreen registerScreen;
    private final Dashboard dashboard;
    private final TheatreSelectionPanel theatreSelectionPanel;
    private final MovieListPanel movieListPanel;
    private final MovieDetailsPanel movieDetailsPanel;
    private final SeatLayoutPanel seatLayoutPanel;
    private final PaymentPanel paymentPanel;
    private final TicketPanel ticketPanel;
    private final BookingHistoryPanel bookingHistoryPanel;
    private final AdminDashboard adminDashboard;

    public MainFrame() {
        super("Movie Ticket Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        loginScreen = new LoginScreen(this, ctx);
        registerScreen = new RegisterScreen(this, ctx);
        dashboard = new Dashboard(this, ctx);
        theatreSelectionPanel = new TheatreSelectionPanel(this, ctx);
        movieListPanel = new MovieListPanel(this, ctx);
        movieDetailsPanel = new MovieDetailsPanel(this, ctx);
        seatLayoutPanel = new SeatLayoutPanel(this, ctx);
        paymentPanel = new PaymentPanel(this, ctx);
        ticketPanel = new TicketPanel(this, ctx);
        bookingHistoryPanel = new BookingHistoryPanel(this, ctx);
        adminDashboard = new AdminDashboard(this, ctx);

        cardContainer.add(loginScreen, LOGIN);
        cardContainer.add(registerScreen, REGISTER);
        cardContainer.add(dashboard, DASHBOARD);
        cardContainer.add(theatreSelectionPanel, THEATRE_SELECTION);
        cardContainer.add(movieListPanel, MOVIE_LIST);
        cardContainer.add(movieDetailsPanel, MOVIE_DETAILS);
        cardContainer.add(seatLayoutPanel, SEAT_LAYOUT);
        cardContainer.add(paymentPanel, PAYMENT);
        cardContainer.add(ticketPanel, TICKET);
        cardContainer.add(bookingHistoryPanel, HISTORY);
        cardContainer.add(adminDashboard, ADMIN_DASHBOARD);

        add(cardContainer);
        showCard(LOGIN);
    }

    public void showCard(String name) {
        if (DASHBOARD.equals(name)) {
            dashboard.refresh();
        } else if (ADMIN_DASHBOARD.equals(name)) {
            adminDashboard.refresh();
        } else if (HISTORY.equals(name)) {
            bookingHistoryPanel.refresh();
        } else if (LOGIN.equals(name)) {
            loginScreen.clearFields();
        }
        cardLayout.show(cardContainer, name);
    }

    public TheatreSelectionPanel getTheatreSelectionPanel() {
        return theatreSelectionPanel;
    }

    public MovieListPanel getMovieListPanel() {
        return movieListPanel;
    }

    public MovieDetailsPanel getMovieDetailsPanel() {
        return movieDetailsPanel;
    }

    public SeatLayoutPanel getSeatLayoutPanel() {
        return seatLayoutPanel;
    }

    public PaymentPanel getPaymentPanel() {
        return paymentPanel;
    }

    public TicketPanel getTicketPanel() {
        return ticketPanel;
    }

    public BookingHistoryPanel getBookingHistoryPanel() {
        return bookingHistoryPanel;
    }
}
