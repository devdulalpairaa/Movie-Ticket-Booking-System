package com.movieticket.gui;

import com.movieticket.exception.InvalidPaymentException;
import com.movieticket.model.Payment;
import com.movieticket.model.PaymentMethod;
import com.movieticket.util.Logger;

import javax.swing.*;
import java.awt.*;

/** Simulated payment screen: choose a method, enter details, and "pay". */
public class PaymentPanel extends JPanel {

    private final MainFrame mainFrame;
    private final AppContext ctx;
    private final JComboBox<PaymentMethod> methodCombo = new JComboBox<>(PaymentMethod.values());
    private final JTextField detailField = Components.textField();
    private final JLabel amountLabel = new JLabel();
    private final JLabel detailLabel = new JLabel("UPI ID");

    public PaymentPanel(MainFrame mainFrame, AppContext ctx) {
        this.mainFrame = mainFrame;
        this.ctx = ctx;
        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND);
        add(TopBar.build(mainFrame, ctx, "Payment"), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);

        methodCombo.addActionListener(e -> updateDetailLabel());
    }

    private JPanel buildCenter() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(UIConstants.BACKGROUND);

        JPanel card = Components.card();
        card.setPreferredSize(new Dimension(420, 380));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = Components.heading("Complete Payment");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(8));

        amountLabel.setFont(UIConstants.FONT_TITLE);
        amountLabel.setForeground(UIConstants.ACCENT);
        amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(amountLabel);
        card.add(Box.createVerticalStrut(20));

        JLabel methodLabel = new JLabel("Payment Method");
        methodLabel.setFont(UIConstants.FONT_SUBHEADING);
        methodLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(methodLabel);
        methodCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        methodCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(methodCombo);
        card.add(Box.createVerticalStrut(14));

        detailLabel.setFont(UIConstants.FONT_SUBHEADING);
        detailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(detailLabel);
        detailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        detailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(detailField);
        card.add(Box.createVerticalStrut(24));

        JButton payBtn = Components.primaryButton("Pay Now");
        payBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        payBtn.addActionListener(e -> doPay());
        card.add(payBtn);
        card.add(Box.createVerticalStrut(8));

        JButton cancelBtn = Components.ghostButton("Cancel & Release Seats");
        cancelBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        cancelBtn.addActionListener(e -> {
            ctx.bookingService.releaseSeats(ctx.currentBooking, ctx.selectedShow);
            mainFrame.showCard(MainFrame.MOVIE_DETAILS);
        });
        card.add(cancelBtn);

        wrapper.add(card);
        return wrapper;
    }

    private void updateDetailLabel() {
        PaymentMethod method = (PaymentMethod) methodCombo.getSelectedItem();
        detailField.setText("");
        if (method == null) return;
        switch (method) {
            case UPI -> detailLabel.setText("UPI ID (e.g. name@bank)");
            case CREDIT_CARD, DEBIT_CARD -> detailLabel.setText("Card Number (16 digits)");
            case CASH -> detailLabel.setText("No details needed for cash");
        }
    }

    public void refresh() {
        amountLabel.setText("\u20B9" + String.format("%.2f", ctx.currentBooking.getTotalAmount()));
        methodCombo.setSelectedIndex(0);
        updateDetailLabel();
    }

    private void doPay() {
        PaymentMethod method = (PaymentMethod) methodCombo.getSelectedItem();
        String detail = detailField.getText().trim();
        try {
            Payment payment = ctx.paymentService.processPayment(method, ctx.currentBooking.getTotalAmount(), detail);
            ctx.bookingService.confirmBooking(ctx.currentBooking);
            ctx.currentCustomer().addBookingId(ctx.currentBooking.getBookingId());
            ctx.lastTicket = ctx.ticketService.generateTicket(ctx.currentBooking, ctx.selectedShow,
                    ctx.currentCustomer(), payment);
            mainFrame.getTicketPanel().refresh();
            mainFrame.showCard(MainFrame.TICKET);
        } catch (InvalidPaymentException ex) {
            Logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Payment Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
