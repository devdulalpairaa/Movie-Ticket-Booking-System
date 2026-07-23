package com.movieticket.model;

/** Simulated credit card payment. */
public class CreditCardPayment extends Payment {

    private static final long serialVersionUID = 1L;
    private final String cardNumber;

    public CreditCardPayment(String paymentId, double amount, String cardNumber) {
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
        return PaymentMethod.CREDIT_CARD;
    }
}
