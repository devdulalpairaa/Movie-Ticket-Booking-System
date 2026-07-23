package com.movieticket.model;

import java.io.Serializable;

/**
 * A Show is one screening slot of a Movie on a particular Screen inside a Theatre
 * at a particular time. Demonstrates ASSOCIATION: a Show references a Movie,
 * a Theatre and a Screen, none of which it owns.
 */
public class Show implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String showId;
    private final Movie movie;
    private final Theatre theatre;
    private final Screen screen;
    private final String showTime; // e.g. "7:30 PM"
    private final double basePrice;

    public Show(String showId, Movie movie, Theatre theatre, Screen screen,
                String showTime, double basePrice) {
        this.showId = showId;
        this.movie = movie;
        this.theatre = theatre;
        this.screen = screen;
        this.showTime = showTime;
        this.basePrice = basePrice;
    }

    public String getShowId() {
        return showId;
    }

    public Movie getMovie() {
        return movie;
    }

    public Theatre getTheatre() {
        return theatre;
    }

    public Screen getScreen() {
        return screen;
    }

    public String getShowTime() {
        return showTime;
    }

    public double getBasePrice() {
        return basePrice;
    }

    @Override
    public String toString() {
        return movie.getTitle() + " @ " + theatre.getName() + " - " + showTime;
    }
}
