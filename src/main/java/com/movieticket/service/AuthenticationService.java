package com.movieticket.service;

import com.movieticket.exception.InvalidLoginException;
import com.movieticket.exception.UserAlreadyExistsException;
import com.movieticket.model.*;
import com.movieticket.util.FileManager;
import com.movieticket.util.Logger;
import com.movieticket.util.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles registration and login for both Customers and Admins.
 * Loads/saves the user list from/to users.txt via FileManager.
 */
public class AuthenticationService {

    private final List<Person> people;
    private int nextUserId;

    public AuthenticationService() {
        this.people = new ArrayList<>(FileManager.loadUsers());
        this.nextUserId = computeNextId();
        ensureDefaultAdmin();
    }

    private int computeNextId() {
        int max = 0;
        for (Person p : people) {
            try {
                int idNum = Integer.parseInt(p.getUserId().replaceAll("\\D", ""));
                max = Math.max(max, idNum);
            } catch (NumberFormatException ignored) {
                // ignore malformed ids
            }
        }
        return max + 1;
    }

    private void ensureDefaultAdmin() {
        boolean hasAdmin = people.stream().anyMatch(p -> p instanceof Admin);
        if (!hasAdmin) {
            Admin admin = new Admin("U" + (nextUserId++), "System Admin", "admin", "admin123",
                    "admin@movietickets.com", "9999999999");
            people.add(admin);
            FileManager.appendUser(admin);
            Logger.info("Default admin account created (username: admin / password: admin123)");
        }
    }

    public Customer register(String name, String username, String password, String email, String phone)
            throws UserAlreadyExistsException {
        if (!Validator.isNotEmpty(username) || !Validator.isValidPassword(password)) {
            throw new IllegalArgumentException("Username and a password of at least 4 characters are required.");
        }
        for (Person p : people) {
            if (p.getUsername().equalsIgnoreCase(username)) {
                throw new UserAlreadyExistsException("Username \"" + username + "\" is already taken.");
            }
        }
        Customer customer = new Customer("U" + (nextUserId++), name, username, password, email, phone);
        people.add(customer);
        FileManager.appendUser(customer);
        Logger.action("User Registered: " + username);
        return customer;
    }

    public Person login(String username, String password) throws InvalidLoginException {
        for (Person p : people) {
            if (p.getUsername().equalsIgnoreCase(username)) {
                if (p.checkPassword(password)) {
                    Logger.action("User Logged In: " + username + " (" + p.getRole() + ")");
                    return p;
                } else {
                    throw new InvalidLoginException("Incorrect password for user \"" + username + "\".");
                }
            }
        }
        throw new InvalidLoginException("No account found with username \"" + username + "\".");
    }

    public List<Person> getAllUsers() {
        return people;
    }

    public long countCustomers() {
        return people.stream().filter(p -> p instanceof Customer).count();
    }
}
