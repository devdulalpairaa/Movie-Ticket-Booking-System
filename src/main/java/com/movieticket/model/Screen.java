package com.movieticket.model;

import java.io.Serializable;

/**
 * A screen (auditorium) inside a Theatre. Owns a 2D matrix of Seat objects.
 * COMPOSITION: Seats cannot exist without their Screen; when a Screen is
 * destroyed, its seat layout is destroyed with it.
 */
public class Screen implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String screenId;
    private final String screenName;
    private final int rows;
    private final int cols;
    private final Seat[][] seatMatrix; // DSA REQUIREMENT: 2D array seat layout

    public Screen(String screenId, String screenName, int rows, int cols) {
        this.screenId = screenId;
        this.screenName = screenName;
        this.rows = rows;
        this.cols = cols;
        this.seatMatrix = buildSeatMatrix(rows, cols);
    }

    private Seat[][] buildSeatMatrix(int rows, int cols) {
        Seat[][] matrix = new Seat[rows][cols];
        for (int r = 0; r < rows; r++) {
            char rowLabel = (char) ('A' + r);
            SeatType type;
            if (r < rows * 0.2) {
                type = SeatType.VIP;
            } else if (r < rows * 0.5) {
                type = SeatType.PREMIUM;
            } else {
                type = SeatType.REGULAR;
            }
            for (int c = 0; c < cols; c++) {
                String seatNumber = rowLabel + String.valueOf(c + 1);
                matrix[r][c] = new Seat(r, c, seatNumber, type);
            }
        }
        return matrix;
    }

    public String getScreenId() {
        return screenId;
    }

    public String getScreenName() {
        return screenName;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Seat[][] getSeatMatrix() {
        return seatMatrix;
    }

    public int getTotalSeats() {
        return rows * cols;
    }

    @Override
    public String toString() {
        return screenName;
    }
}
