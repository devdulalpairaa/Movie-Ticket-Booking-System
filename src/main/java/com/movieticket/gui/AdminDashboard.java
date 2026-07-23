package com.movieticket.gui;

import com.movieticket.exception.MovieNotFoundException;
import com.movieticket.model.Booking;
import com.movieticket.model.Movie;
import com.movieticket.model.Show;
import com.movieticket.util.Logger;
import com.movieticket.util.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/** Admin-only screen: manage movies (CRUD) and view system-wide statistics. */
public class AdminDashboard extends JPanel {

    private final MainFrame mainFrame;
    private final AppContext ctx;

    private final JLabel revenueLabel = new JLabel();
    private final JLabel usersLabel = new JLabel();
    private final JLabel bookingsLabel = new JLabel();

    private final DefaultTableModel movieModel = new DefaultTableModel(
            new Object[]{"ID", "Title", "Genre", "Language", "Duration", "Rating"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable movieTable = new JTable(movieModel);

    private final DefaultTableModel bookingModel = new DefaultTableModel(
            new Object[]{"Booking ID", "Customer", "Movie", "Seats", "Amount", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public AdminDashboard(MainFrame mainFrame, AppContext ctx) {
        this.mainFrame = mainFrame;
        this.ctx = ctx;
        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(UIConstants.PRIMARY);
        top.setPreferredSize(new Dimension(100, 56));
        top.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        JLabel title = new JLabel("\uD83C\uDFAC MovieTix \u2014 Admin");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        top.add(title, BorderLayout.WEST);
        JButton logoutBtn = new RoundedButton("Logout", null, new Color(51, 65, 85), new Color(15, 23, 42), Color.WHITE);
        logoutBtn.addActionListener(e -> {
            Logger.action("Admin Logged Out");
            ctx.currentUser = null;
            mainFrame.showCard(MainFrame.LOGIN);
        });
        top.add(logoutBtn, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Overview", buildOverviewTab());
        tabs.addTab("Manage Movies", buildMoviesTab());
        tabs.addTab("All Bookings", buildBookingsTab());
        add(tabs, BorderLayout.CENTER);
    }

    // ---------------------------------------------------------------- overview
    private JPanel buildOverviewTab() {
        JPanel panel = new JPanel();
        panel.setBackground(UIConstants.BACKGROUND);
        panel.setLayout(new GridLayout(1, 3, 16, 16));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        panel.add(statCard("Total Revenue", revenueLabel));
        panel.add(statCard("Total Users", usersLabel));
        panel.add(statCard("Confirmed Bookings", bookingsLabel));
        return panel;
    }

    private JPanel statCard(String label, JLabel valueLabel) {
        JPanel card = Components.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel(label);
        titleLabel.setFont(UIConstants.FONT_SUBHEADING);
        titleLabel.setForeground(UIConstants.TEXT_MUTED);
        valueLabel.setFont(UIConstants.FONT_TITLE);
        valueLabel.setForeground(UIConstants.PRIMARY);
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(valueLabel);
        return card;
    }

    // ---------------------------------------------------------------- movies
    private JPanel buildMoviesTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        movieTable.setRowHeight(26);
        panel.add(new JScrollPane(movieTable), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttons.setOpaque(false);
        JButton addBtn = Components.primaryButton("Add Movie");
        addBtn.addActionListener(e -> showMovieDialog(null));
        JButton editBtn = Components.secondaryButton("Update Selected");
        editBtn.addActionListener(e -> {
            Movie movie = selectedMovie();
            if (movie != null) showMovieDialog(movie);
        });
        JButton deleteBtn = Components.ghostButton("Delete Selected");
        deleteBtn.addActionListener(e -> deleteSelectedMovie());
        buttons.add(addBtn);
        buttons.add(editBtn);
        buttons.add(deleteBtn);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private Movie selectedMovie() {
        int row = movieTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a movie first.");
            return null;
        }
        String id = (String) movieModel.getValueAt(row, 0);
        try {
            return ctx.movieService.findMovieById(id);
        } catch (MovieNotFoundException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            return null;
        }
    }

    private void deleteSelectedMovie() {
        Movie movie = selectedMovie();
        if (movie == null) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Delete \"" + movie.getTitle() + "\"?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                ctx.movieService.deleteMovie(movie);
                refreshMovies();
            } catch (MovieNotFoundException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    private void showMovieDialog(Movie existing) {
        JTextField titleField = Components.textField();
        JTextField genreField = Components.textField();
        JTextField langField = Components.textField();
        JTextField durationField = Components.textField();
        JTextField ratingField = Components.textField();
        JTextField descField = Components.textField();

        if (existing != null) {
            titleField.setText(existing.getTitle());
            genreField.setText(existing.getGenre());
            langField.setText(existing.getLanguage());
            durationField.setText(String.valueOf(existing.getDurationMinutes()));
            ratingField.setText(String.valueOf(existing.getRating()));
            descField.setText(existing.getDescription());
        }

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Title:")); form.add(titleField);
        form.add(new JLabel("Genre:")); form.add(genreField);
        form.add(new JLabel("Language:")); form.add(langField);
        form.add(new JLabel("Duration (min):")); form.add(durationField);
        form.add(new JLabel("Rating (0-5):")); form.add(ratingField);
        form.add(new JLabel("Description:")); form.add(descField);

        int result = JOptionPane.showConfirmDialog(this, form,
                existing == null ? "Add Movie" : "Update Movie", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        if (!Validator.isNotEmpty(titleField.getText())) {
            JOptionPane.showMessageDialog(this, "Title is required.");
            return;
        }
        try {
            int duration = Integer.parseInt(durationField.getText().trim());
            double rating = Double.parseDouble(ratingField.getText().trim());
            if (existing == null) {
                ctx.movieService.addMovie(titleField.getText().trim(), genreField.getText().trim(),
                        langField.getText().trim(), duration, rating, descField.getText().trim());
            } else {
                ctx.movieService.updateMovie(existing, titleField.getText().trim(), genreField.getText().trim(),
                        langField.getText().trim(), duration, rating, descField.getText().trim());
            }
            refreshMovies();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Duration and Rating must be numeric.");
        }
    }

    // ---------------------------------------------------------------- bookings
    private JPanel buildBookingsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JTable bookingTable = new JTable(bookingModel);
        bookingTable.setRowHeight(26);
        panel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);
        return panel;
    }

    // ---------------------------------------------------------------- refresh
    public void refresh() {
        revenueLabel.setText("\u20B9" + String.format("%.2f", ctx.historyService.totalRevenue()));
        usersLabel.setText(String.valueOf(ctx.authService.countCustomers()));
        bookingsLabel.setText(String.valueOf(ctx.historyService.totalConfirmedBookings()));
        refreshMovies();
        refreshBookings();
    }

    private void refreshMovies() {
        movieModel.setRowCount(0);
        for (Movie m : ctx.movieService.getAllMovies()) {
            movieModel.addRow(new Object[]{m.getMovieId(), m.getTitle(), m.getGenre(), m.getLanguage(),
                    m.getDurationMinutes() + " min", m.getRating()});
        }
    }

    private void refreshBookings() {
        bookingModel.setRowCount(0);
        for (Booking b : ctx.bookingService.getAllBookings()) {
            Show show = ctx.movieService.getShowById(b.getShowId());
            String movieTitle = show != null ? show.getMovie().getTitle() : "(deleted)";
            bookingModel.addRow(new Object[]{b.getBookingId(), b.getCustomerId(), movieTitle,
                    String.join(", ", b.getSeatNumbers()), "\u20B9" + String.format("%.2f", b.getTotalAmount()),
                    b.getStatus()});
        }
    }
}
