package com.movieticket.service;

import com.movieticket.exception.InvalidPaymentException;
import com.movieticket.model.*;
import com.movieticket.util.Logger;

import java.util.UUID;

/**
 * Simulates payment processing (no real payment gateway). Demonstrates
 * POLYMORPHISM: the same processPayment() method works for any Payment
 * subtype because it programs against the abstract Payment reference.
 */
public class PaymentService {

    public Payment processPayment(PaymentMethod method, double amount, String detail)
            throws InvalidPaymentException {
        String paymentId = "PAY" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Payment payment = switch (method) {
            case UPI -> new UpiPayment(paymentId, amount, detail);
            case CREDIT_CARD -> new CreditCardPayment(paymentId, amount, detail);
            case DEBIT_CARD -> new DebitCardPayment(paymentId, amount, detail);
            case CASH -> new CashPayment(paymentId, amount);
        };

        boolean success = payment.process(); // polymorphic dispatch
        if (!success) {
            Logger.error("Payment Failed: " + method + " for Rs. " + amount);
            throw new InvalidPaymentException("Payment via " + method + " failed. Please check the details entered.");
        }
        Logger.action("Payment Successful: Rs. " + amount + " via " + method);
        return payment;
    }
}
