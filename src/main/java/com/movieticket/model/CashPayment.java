package com.movieticket.model;

/** Simulated cash payment made at the counter. Always succeeds if amount is positive. */
public class CashPayment extends Payment {

    private static final long serialVersionUID = 1L;

    public CashPayment(String paymentId, double amount) {
        super(paymentId, amount);
    }

    @Override
    public boolean process() {
        boolean valid = amount > 0;
        status = valid ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
        return valid;
    }

    @Override
    public PaymentMethod getMethod() {
        return PaymentMethod.CASH;
    }
}
