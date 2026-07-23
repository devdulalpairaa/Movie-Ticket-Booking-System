package com.movieticket.model;

/**
 * Concrete Person representing a system administrator.
 * Demonstrates INHERITANCE and POLYMORPHISM: getRole() is overridden
 * differently from Customer, and both are treated as Person references
 * throughout AuthenticationService.
 */
public class Admin extends Person {

    private static final long serialVersionUID = 1L;

    public Admin(String userId, String name, String username, String password,
                 String email, String phone) {
        super(userId, name, username, password, email, phone);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }
}
