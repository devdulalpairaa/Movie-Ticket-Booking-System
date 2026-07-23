package com.movieticket.dsa;

import com.movieticket.model.Movie;
import java.util.List;

/**
 * Hand-written searching algorithms (no library search methods used).
 */
public final class Searching {

    private Searching() {
    }

    /** Linear search for a movie by (case-insensitive) title substring. Used for the search feature. */
    public static Movie linearSearchByTitle(List<Movie> movies, String titleQuery) {
        String query = titleQuery.toLowerCase();
        for (Movie movie : movies) {
            if (movie.getTitle().toLowerCase().contains(query)) {
                return movie;
            }
        }
        return null;
    }

    /**
     * Binary search for a movie by exact title on a list that is ALREADY sorted
     * alphabetically by title (e.g. via Sorting.quickSortByTitle first).
     */
    public static int binarySearchByTitle(List<Movie> sortedMovies, String title) {
        int low = 0;
        int high = sortedMovies.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = sortedMovies.get(mid).getTitle().compareToIgnoreCase(title);
            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }
}
