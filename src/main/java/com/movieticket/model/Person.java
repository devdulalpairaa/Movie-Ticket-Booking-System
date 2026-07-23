package com.movieticket.model;

import java.io.Serializable;

/**
 * Abstract base class representing any person who can log into the system.
 * Demonstrates ABSTRACTION (abstract class + abstract method) and
 * ENCAPSULATION (private fields with controlled access via getters/setters).
 */
public abstract class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;
    private String name;
    private String username;
    private String password;
    private String email;
    private String phone;

    protected Person(String userId, String name, String username, String password,
                      String email, String phone) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    /** Every concrete Person type must declare its role. Forces subclasses to specialize. */
    public abstract String getRole();

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean checkPassword(String candidate) {
        return this.password != null && this.password.equals(candidate);
    }

    @Override
    public String toString() {
        return getRole() + "{userId=" + userId + ", username=" + username + ", name=" + name + "}";
    }
}
