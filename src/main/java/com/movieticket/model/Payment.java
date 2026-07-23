package com.movieticket.model;

import java.io.Serializable;

/**
 * Abstract base for all payment methods. Demonstrates ABSTRACTION and is the
 * root of a POLYMORPHIC hierarchy: PaymentService works only against this type,
 * while the actual processing logic differs per subclass (see process()).
 */
public abstract class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String paymentId;
    protected double amount;
    protected PaymentStatus status;

    protected Payment(String paymentId, double amount) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }

    /** Each concrete payment type simulates its own processing/validation rules. */
    public abstract boolean process();

    public abstract PaymentMethod getMethod();

    public String getPaymentId() {
        return paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
