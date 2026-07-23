package com.movieticket.model;

/** Simulated UPI payment. */
public class UpiPayment extends Payment {

    private static final long serialVersionUID = 1L;
    private final String upiId;

    public UpiPayment(String paymentId, double amount, String upiId) {
        super(paymentId, amount);
        this.upiId = upiId;
    }

    @Override
    public boolean process() {
        boolean valid = upiId != null && upiId.contains("@");
        status = valid ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
        return valid;
    }

    @Override
    public PaymentMethod getMethod() {
        return PaymentMethod.UPI;
    }
}
