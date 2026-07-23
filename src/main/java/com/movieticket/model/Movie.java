package com.movieticket.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A movie available for booking. Holds a list of Theatres that are screening it.
 * AGGREGATION: Theatre objects exist independently of Movie (a theatre survives
 * even if it stops showing a given movie), so Movie only *references* Theatre,
 * it does not own its lifecycle.
 */
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    private String movieId;
    private String title;
    private String genre;
    private String language;
    private int durationMinutes;
    private double rating;
    private String description;
    private final List<Theatre> theatres = new ArrayList<>(); // Aggregation

    public Movie(String movieId, String title, String genre, String language,
                 int durationMinutes, double rating, String description) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.language = language;
        this.durationMinutes = durationMinutes;
        this.rating = rating;
        this.description = description;
    }

    public void addTheatre(Theatre theatre) {
        if (!theatres.contains(theatre)) {
            theatres.add(theatre);
        }
    }

    public List<Theatre> getTheatres() {
        return theatres;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return title + " (" + genre + ", " + language + ")";
    }
}
