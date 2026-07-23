package com.movieticket.model;

/** Simulated debit card payment. */
public class DebitCardPayment extends Payment {

    private static final long serialVersionUID = 1L;
    private final String cardNumber;

    public DebitCardPayment(String paymentId, double amount, String cardNumber) {
        super(paymentId, amount);
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean process() {
        boolean valid = cardNumber != null && cardNumber.replaceAll("\\s", "").length() == 16;
        status = valid ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
        return valid;
    }

    @Override
    public PaymentMethod getMethod() {
        return PaymentMethod.DEBIT_CARD;
    }
}
