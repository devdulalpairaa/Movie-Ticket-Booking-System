package com.movieticket.model;

/** Type of a seat, each with its own price multiplier over the base show price. */
public enum SeatType {
    REGULAR(1.0),
    PREMIUM(1.5),
    VIP(2.0);

    private final double priceMultiplier;

    SeatType(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }
}
