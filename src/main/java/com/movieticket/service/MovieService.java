package com.movieticket.service;

import com.movieticket.dsa.Searching;
import com.movieticket.dsa.Sorting;
import com.movieticket.exception.MovieNotFoundException;
import com.movieticket.model.*;
import com.movieticket.util.FileManager;
import com.movieticket.util.Logger;

import java.util.*;

/**
 * Central service for Movies, Theatres, Screens and Shows.
 * Movies persist to movies.txt. Theatres/Screens/Shows are seeded in-memory
 * on startup (there is no theatres.txt per the project specification) and
 * linked to movies via AGGREGATION (Movie.addTheatre).
 */
public class MovieService {

    private final List<Movie> movies = new ArrayList<>();
    private final List<Theatre> theatres = new ArrayList<>();
    private final List<Show> shows = new ArrayList<>();
    private int nextMovieId = 1;
    private int nextShowId = 1;

    public MovieService() {
        List<Movie> loaded = FileManager.loadMovies();
        if (loaded.isEmpty()) {
            seedDefaultData();
        } else {
            movies.addAll(loaded);
            seedTheatresAndShows();
        }
        recomputeNextMovieId();
    }

    private void recomputeNextMovieId() {
        int max = 0;
        for (Movie m : movies) {
            try {
                max = Math.max(max, Integer.parseInt(m.getMovieId().replaceAll("\\D", "")));
            } catch (NumberFormatException ignored) {
            }
        }
        nextMovieId = max + 1;
    }

    private void seedDefaultData() {
        movies.add(new Movie("M1", "Interstellar", "Sci-Fi", "English", 169, 4.8,
                "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival."));
        movies.add(new Movie("M2", "The Dark Knight", "Action", "English", 152, 4.9,
                "Batman faces the Joker, a criminal mastermind who plunges Gotham into anarchy."));
        movies.add(new Movie("M3", "3 Idiots", "Comedy-Drama", "Hindi", 170, 4.7,
                "Two friends search for their long-lost companion, revisiting college memories along the way."));
        movies.add(new Movie("M4", "Inception", "Sci-Fi", "English", 148, 4.7,
                "A thief who steals corporate secrets through dream-sharing technology is given an inverse task."));
        movies.add(new Movie("M5", "RRR", "Action-Drama", "Telugu", 187, 4.6,
                "A fictional story about two legendary revolutionaries and their journey away from home."));
        FileManager.saveMovies(movies);
        seedTheatresAndShows();
    }

    private void seedTheatresAndShows() {
        Theatre pvrDelhi = new Theatre("T1", "PVR Vegas Mall", "Delhi");
        pvrDelhi.addScreen(newScreen("SC1", "Screen 1", 6, 8));
        pvrDelhi.addScreen(newScreen("SC2", "Screen 2", 6, 8));

        Theatre inoxDelhi = new Theatre("T2", "INOX Nehru Place", "Delhi");
        inoxDelhi.addScreen(newScreen("SC3", "Screen 1", 5, 8));

        Theatre pvrMumbai = new Theatre("T3", "PVR Phoenix Mall", "Mumbai");
        pvrMumbai.addScreen(newScreen("SC4", "Screen 1", 6, 8));
        pvrMumbai.addScreen(newScreen("SC5", "Screen 2", 6, 8));

        Theatre cinepolisBangalore = new Theatre("T4", "Cinepolis Forum Mall", "Bangalore");
        cinepolisBangalore.addScreen(newScreen("SC6", "Screen 1", 5, 8));

        theatres.addAll(List.of(pvrDelhi, inoxDelhi, pvrMumbai, cinepolisBangalore));

        String[] times = {"10:00 AM", "1:30 PM", "4:45 PM", "7:30 PM", "10:15 PM"};
        Random rnd = new Random(42);
        for (Movie movie : movies) {
            // Every movie plays at 2-3 random theatres (Aggregation link)
            List<Theatre> shuffled = new ArrayList<>(theatres);
            Collections.shuffle(shuffled, rnd);
            int count = 2 + rnd.nextInt(2);
            for (int i = 0; i < Math.min(count, shuffled.size()); i++) {
                Theatre theatre = shuffled.get(i);
                movie.addTheatre(theatre);
                Screen screen = theatre.getScreens().get(rnd.nextInt(theatre.getScreens().size()));
                String time = times[rnd.nextInt(times.length)];
                double basePrice = 150 + rnd.nextInt(6) * 20;
                shows.add(new Show("SH" + (nextShowId++), movie, theatre, screen, time, basePrice));
            }
        }
        Logger.info("Loaded " + movies.size() + " movies, " + theatres.size() + " theatres, " + shows.size() + " shows.");
    }

    private Screen newScreen(String id, String name, int rows, int cols) {
        return new Screen(id, name, rows, cols);
    }

    // -------------------------------------------------------------- queries
    public List<Movie> getAllMovies() {
        return movies;
    }

    public List<String> getAllCities() {
        Set<String> cities = new TreeSet<>();
        for (Theatre t : theatres) {
            cities.add(t.getCity());
        }
        return new ArrayList<>(cities);
    }

    public List<Theatre> getTheatresInCity(String city) {
        List<Theatre> result = new ArrayList<>();
        for (Theatre t : theatres) {
            if (t.getCity().equalsIgnoreCase(city)) {
                result.add(t);
            }
        }
        return result;
    }

    public List<Movie> getMoviesForTheatre(Theatre theatre) {
        List<Movie> result = new ArrayList<>();
        for (Movie m : movies) {
            if (m.getTheatres().contains(theatre)) {
                result.add(m);
            }
        }
        return result;
    }

    public List<Show> getShows(Movie movie, Theatre theatre) {
        List<Show> result = new ArrayList<>();
        for (Show s : shows) {
            if (s.getMovie().equals(movie) && s.getTheatre().equals(theatre)) {
                result.add(s);
            }
        }
        return result;
    }

    public Show getShowById(String showId) {
        for (Show s : shows) {
            if (s.getShowId().equals(showId)) {
                return s;
            }
        }
        return null;
    }

    public Movie findMovieById(String movieId) throws MovieNotFoundException {
        for (Movie m : movies) {
            if (m.getMovieId().equals(movieId)) {
                return m;
            }
        }
        throw new MovieNotFoundException("Movie with id " + movieId + " not found.");
    }

    /** Uses the custom linear search DSA implementation. */
    public Movie searchMovieByTitle(String title) {
        return Searching.linearSearchByTitle(movies, title);
    }

    /** Returns movies sorted by rating (bubble sort, descending -- highest rated first). */
    public List<Movie> moviesSortedByRating() {
        List<Movie> copy = new ArrayList<>(movies);
        Sorting.bubbleSortByRating(copy);
        return copy;
    }

    /** Returns movies sorted alphabetically by title (quick sort). */
    public List<Movie> moviesSortedByTitle() {
        List<Movie> copy = new ArrayList<>(movies);
        Sorting.quickSortByTitle(copy);
        return copy;
    }

    // -------------------------------------------------------------- admin CRUD
    public Movie addMovie(String title, String genre, String language, int duration,
                           double rating, String description) {
        Movie movie = new Movie("M" + (nextMovieId++), title, genre, language, duration, rating, description);
        movies.add(movie);
        FileManager.saveMovies(movies);
        Logger.action("Admin Added Movie: " + title);
        return movie;
    }

    public void updateMovie(Movie movie, String title, String genre, String language,
                             int duration, double rating, String description) {
        movie.setTitle(title);
        movie.setGenre(genre);
        movie.setLanguage(language);
        movie.setDurationMinutes(duration);
        movie.setRating(rating);
        movie.setDescription(description);
        FileManager.saveMovies(movies);
        Logger.action("Admin Updated Movie: " + title);
    }

    public void deleteMovie(Movie movie) throws MovieNotFoundException {
        if (!movies.remove(movie)) {
            throw new MovieNotFoundException("Cannot delete: movie not found.");
        }
        shows.removeIf(s -> s.getMovie().equals(movie));
        FileManager.saveMovies(movies);
        Logger.action("Admin Deleted Movie: " + movie.getTitle());
    }

    public List<Show> getAllShows() {
        return shows;
    }
}
