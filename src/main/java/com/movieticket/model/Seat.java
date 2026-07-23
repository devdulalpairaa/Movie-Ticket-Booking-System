package com.movieticket.model;

import java.io.Serializable;

/**
 * A single seat inside a Screen. Seats are created and destroyed together with
 * their Screen -- this is COMPOSITION (Screen "has-a" Seat[][] and owns their lifecycle).
 */
public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int row;
    private final int col;
    private final String seatNumber;
    private final SeatType type;
    private SeatStatus status;

    public Seat(int row, int col, String seatNumber, SeatType type) {
        this.row = row;
        this.col = col;
        this.seatNumber = seatNumber;
        this.type = type;
        this.status = SeatStatus.AVAILABLE;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public SeatType getType() {
        return type;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public double priceFor(double baseShowPrice) {
        return baseShowPrice * type.getPriceMultiplier();
    }

    @Override
    public String toString() {
        return seatNumber;
    }
}
