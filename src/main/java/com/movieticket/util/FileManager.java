package com.movieticket.util;

import com.movieticket.model.*;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all File I/O for the application. Data is stored as simple
 * pipe-delimited (|) text files inside a "data" directory next to the
 * application (created automatically on first run). This satisfies the
 * "no database" requirement while giving persistence across sessions.
 *
 * File formats:
 *   users.txt    : userId|role|name|username|password|email|phone
 *   movies.txt   : movieId|title|genre|language|durationMinutes|rating|description
 *   bookings.txt : bookingId|customerId|showId|seat1,seat2,...|totalAmount|status|bookingDate
 *   tickets.txt  : ticketId|bookingId|customerName|movieTitle|theatreName|screenName|showTime|seat1,seat2,...|amount|paymentMethod|bookingDate
 */
public final class FileManager {

    private static final String DATA_DIR = "data";
    public static final String USERS_FILE = DATA_DIR + File.separator + "users.txt";
    public static final String MOVIES_FILE = DATA_DIR + File.separator + "movies.txt";
    public static final String BOOKINGS_FILE = DATA_DIR + File.separator + "bookings.txt";
    public static final String TICKETS_FILE = DATA_DIR + File.separator + "tickets.txt";

    private FileManager() {
    }

    /** Ensures the data directory and all data files exist before the app starts. */
    public static void initDataFiles() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            createIfMissing(USERS_FILE);
            createIfMissing(MOVIES_FILE);
            createIfMissing(BOOKINGS_FILE);
            createIfMissing(TICKETS_FILE);
            Logger.info("Data files verified/created under ./" + DATA_DIR + "/");
        } catch (IOException e) {
            Logger.error("Failed to initialise data directory: " + e.getMessage());
        }
    }

    private static void createIfMissing(String path) throws IOException {
        Path p = Paths.get(path);
        if (!Files.exists(p)) {
            Files.createFile(p);
        }
    }

    // ---------------------------------------------------------------- generic
    private static List<String> readLines(String path) {
        List<String> lines = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) {
            return lines;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            Logger.error("Error reading " + path + ": " + e.getMessage());
        }
        return lines;
    }

    private static void writeLines(String path, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            Logger.error("Error writing " + path + ": " + e.getMessage());
        }
    }

    private static void appendLine(String path, String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            Logger.error("Error appending to " + path + ": " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------- users
    public static List<Person> loadUsers() {
        List<Person> people = new ArrayList<>();
        for (String line : readLines(USERS_FILE)) {
            String[] p = line.split("\\|", -1);
            if (p.length < 7) continue;
            String userId = p[0], role = p[1], name = p[2], username = p[3],
                    password = p[4], email = p[5], phone = p[6];
            if ("ADMIN".equals(role)) {
                people.add(new Admin(userId, name, username, password, email, phone));
            } else {
                people.add(new Customer(userId, name, username, password, email, phone));
            }
        }
        return people;
    }

    public static void saveUsers(List<Person> people) {
        List<String> lines = new ArrayList<>();
        for (Person p : people) {
            lines.add(String.join("|", p.getUserId(), p.getRole(), p.getName(),
                    p.getUsername(), p.getPassword(), p.getEmail(), p.getPhone()));
        }
        writeLines(USERS_FILE, lines);
    }

    public static void appendUser(Person p) {
        appendLine(USERS_FILE, String.join("|", p.getUserId(), p.getRole(), p.getName(),
                p.getUsername(), p.getPassword(), p.getEmail(), p.getPhone()));
    }

    // ---------------------------------------------------------------- movies
    public static List<Movie> loadMovies() {
        List<Movie> movies = new ArrayList<>();
        for (String line : readLines(MOVIES_FILE)) {
            String[] p = line.split("\\|", -1);
            if (p.length < 7) continue;
            movies.add(new Movie(p[0], p[1], p[2], p[3],
                    Integer.parseInt(p[4]), Double.parseDouble(p[5]), p[6]));
        }
        return movies;
    }

    public static void saveMovies(List<Movie> movies) {
        List<String> lines = new ArrayList<>();
        for (Movie m : movies) {
            lines.add(String.join("|", m.getMovieId(), m.getTitle(), m.getGenre(), m.getLanguage(),
                    String.valueOf(m.getDurationMinutes()), String.valueOf(m.getRating()),
                    m.getDescription()));
        }
        writeLines(MOVIES_FILE, lines);
    }

    // ---------------------------------------------------------------- bookings
    public static List<Booking> loadBookings() {
        List<Booking> bookings = new ArrayList<>();
        for (String line : readLines(BOOKINGS_FILE)) {
            String[] p = line.split("\\|", -1);
            if (p.length < 6) continue;
            List<String> seats = new ArrayList<>();
            for (String s : p[3].split(",")) {
                if (!s.isBlank()) seats.add(s);
            }
            Booking b = new Booking(p[0], p[1], p[2], seats, Double.parseDouble(p[4]), p[6]);
            b.setStatus(p[5]);
            bookings.add(b);
        }
        return bookings;
    }

    public static void saveBookings(List<Booking> bookings) {
        List<String> lines = new ArrayList<>();
        for (Booking b : bookings) {
            lines.add(String.join("|", b.getBookingId(), b.getCustomerId(), b.getShowId(),
                    String.join(",", b.getSeatNumbers()), String.valueOf(b.getTotalAmount()),
                    b.getStatus(), b.getBookingDate()));
        }
        writeLines(BOOKINGS_FILE, lines);
    }

    public static void appendBooking(Booking b) {
        appendLine(BOOKINGS_FILE, String.join("|", b.getBookingId(), b.getCustomerId(), b.getShowId(),
                String.join(",", b.getSeatNumbers()), String.valueOf(b.getTotalAmount()),
                b.getStatus(), b.getBookingDate()));
    }

    // ---------------------------------------------------------------- tickets
    public static List<Ticket> loadTickets() {
        List<Ticket> tickets = new ArrayList<>();
        for (String line : readLines(TICKETS_FILE)) {
            String[] p = line.split("\\|", -1);
            if (p.length < 11) continue;
            List<String> seats = new ArrayList<>();
            for (String s : p[7].split(",")) {
                if (!s.isBlank()) seats.add(s);
            }
            tickets.add(new Ticket(p[0], p[1], p[2], p[3], p[4], p[5], p[6], seats,
                    Double.parseDouble(p[8]), p[9], p[10]));
        }
        return tickets;
    }

    public static void appendTicket(Ticket t) {
        appendLine(TICKETS_FILE, String.join("|", t.getTicketId(), t.getBookingId(),
                t.getCustomerName(), t.getMovieTitle(), t.getTheatreName(), t.getScreenName(),
                t.getShowTime(), String.join(",", t.getSeatNumbers()), String.valueOf(t.getAmount()),
                t.getPaymentMethod(), t.getBookingDate()));
    }

    public static void saveTickets(List<Ticket> tickets) {
        List<String> lines = new ArrayList<>();
        for (Ticket t : tickets) {
            lines.add(String.join("|", t.getTicketId(), t.getBookingId(),
                    t.getCustomerName(), t.getMovieTitle(), t.getTheatreName(), t.getScreenName(),
                    t.getShowTime(), String.join(",", t.getSeatNumbers()), String.valueOf(t.getAmount()),
                    t.getPaymentMethod(), t.getBookingDate()));
        }
        writeLines(TICKETS_FILE, lines);
    }
}
