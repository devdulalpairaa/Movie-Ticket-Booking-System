package com.movieticket.dsa;

import com.movieticket.model.Movie;
import java.util.List;

/**
 * Hand-written sorting algorithms (no Collections.sort used).
 */
public final class Sorting {

    private Sorting() {
    }

    /** Bubble sort movies by rating, descending. */
    public static void bubbleSortByRating(List<Movie> movies) {
        int n = movies.size();
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (movies.get(j).getRating() < movies.get(j + 1).getRating()) {
                    swap(movies, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
    }

    /** Quick sort movies alphabetically by title, ascending. */
    public static void quickSortByTitle(List<Movie> movies) {
        quickSort(movies, 0, movies.size() - 1);
    }

    private static void quickSort(List<Movie> movies, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(movies, low, high);
            quickSort(movies, low, pivotIndex - 1);
            quickSort(movies, pivotIndex + 1, high);
        }
    }

    private static int partition(List<Movie> movies, int low, int high) {
        String pivot = movies.get(high).getTitle();
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (movies.get(j).getTitle().compareToIgnoreCase(pivot) <= 0) {
                i++;
                swap(movies, i, j);
            }
        }
        swap(movies, i + 1, high);
        return i + 1;
    }

    private static void swap(List<Movie> movies, int i, int j) {
        Movie temp = movies.get(i);
        movies.set(i, movies.get(j));
        movies.set(j, temp);
    }
}
