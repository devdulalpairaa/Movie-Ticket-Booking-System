package com.movieticket.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A theatre located in a particular city. Owns a list of Screens.
 * COMPOSITION with Screen (screens belong exclusively to one theatre).
 * A Theatre is independent of any single Movie -- many Movies can be linked to
 * it and it continues to exist even if a Movie stops showing -- this is why
 * Movie-to-Theatre is modelled as AGGREGATION (see Movie.getTheatres()).
 */
public class Theatre implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String theatreId;
    private final String name;
    private final String city;
    private final List<Screen> screens = new ArrayList<>();

    public Theatre(String theatreId, String name, String city) {
        this.theatreId = theatreId;
        this.name = name;
        this.city = city;
    }

    public void addScreen(Screen screen) {
        screens.add(screen);
    }

    public String getTheatreId() {
        return theatreId;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public List<Screen> getScreens() {
        return screens;
    }

    @Override
    public String toString() {
        return name + " (" + city + ")";
    }
}
