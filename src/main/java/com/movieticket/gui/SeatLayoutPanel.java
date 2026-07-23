package com.movieticket.gui;

import com.movieticket.exception.SeatAlreadyBookedException;
import com.movieticket.model.Seat;
import com.movieticket.model.SeatStatus;
import com.movieticket.model.Show;
import com.movieticket.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/** Renders the Show's 2D seat matrix as clickable buttons and handles multi-seat selection. */
public class SeatLayoutPanel extends JPanel {

    private final MainFrame mainFrame;
    private final AppContext ctx;
    private final JPanel gridPanel = new JPanel();
    private final JLabel infoLabel = new JLabel();
    private final JLabel totalLabel = new JLabel();

    public SeatLayoutPanel(MainFrame mainFrame, AppContext ctx) {
        this.mainFrame = mainFrame;
        this.ctx = ctx;
        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND);
        add(TopBar.build(mainFrame, ctx, "Select Seats"), BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(UIConstants.BACKGROUND);
        center.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton backBtn = Components.ghostButton("\u2190 Back");
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.addActionListener(e -> mainFrame.showCard(MainFrame.MOVIE_DETAILS));

        infoLabel.setFont(UIConstants.FONT_HEADING);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel screenLabel = new JLabel("S C R E E N   T H I S   W A Y");
        screenLabel.setFont(UIConstants.FONT_SUBHEADING);
        screenLabel.setForeground(UIConstants.TEXT_MUTED);
        screenLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        screenLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        gridPanel.setBackground(UIConstants.BACKGROUND);
        gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel legend = buildLegend();
        legend.setAlignmentX(Component.CENTER_ALIGNMENT);

        totalLabel.setFont(UIConstants.FONT_SUBHEADING);
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton proceedBtn = Components.primaryButton("Proceed to Payment  \u2192");
        proceedBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        proceedBtn.addActionListener(e -> proceed());

        JButton waitlistBtn = Components.ghostButton("Join Waiting List (Show Full)");
        waitlistBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        waitlistBtn.addActionListener(e -> {
            ctx.bookingService.joinWaitingList(ctx.selectedShow, ctx.currentUser.getUserId());
            JOptionPane.showMessageDialog(this, "You have been added to the waiting list.");
        });

        center.add(backBtn);
        center.add(Box.createVerticalStrut(10));
        center.add(infoLabel);
        center.add(screenLabel);
        center.add(gridPanel);
        center.add(Box.createVerticalStrut(16));
        center.add(legend);
        center.add(Box.createVerticalStrut(16));
        center.add(totalLabel);
        center.add(Box.createVerticalStrut(14));
        center.add(proceedBtn);
        center.add(Box.createVerticalStrut(8));
        center.add(waitlistBtn);

        JScrollPane scroll = new JScrollPane(center);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildLegend() {
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        legend.setOpaque(false);
        legend.add(legendItem(UIConstants.SEAT_AVAILABLE, "Available"));
        legend.add(legendItem(UIConstants.SEAT_SELECTED, "Selected"));
        legend.add(legendItem(UIConstants.SEAT_BOOKED, "Booked"));
        return legend;
    }

    private JPanel legendItem(Color color, String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        panel.setOpaque(false);
        JLabel swatch = new JLabel("  ");
        swatch.setOpaque(true);
        swatch.setBackground(color);
        swatch.setPreferredSize(new Dimension(18, 18));
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_BODY);
        panel.add(swatch);
        panel.add(label);
        return panel;
    }

    public void refresh() {
        Show show = ctx.selectedShow;
        infoLabel.setText(show.getMovie().getTitle() + " \u2014 " + show.getShowTime() + " \u2014 " +
                show.getScreen().getScreenName());

        Seat[][] matrix = show.getScreen().getSeatMatrix();
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(matrix.length, matrix[0].length, 6, 6));

        for (Seat[] row : matrix) {
            for (Seat seat : row) {
                if (seat.getStatus() != SeatStatus.BOOKED) {
                    seat.setStatus(SeatStatus.AVAILABLE);
                }
                gridPanel.add(buildSeatButton(seat, show));
            }
        }
        updateTotal();
        revalidate();
        repaint();
    }

    private JButton buildSeatButton(Seat seat, Show show) {
        if (seat.getStatus() == SeatStatus.BOOKED) {
            RoundedButton button = new RoundedButton(seat.getSeatNumber(), UIConstants.SEAT_BOOKED,
                    UIConstants.SEAT_BOOKED, UIConstants.SEAT_BOOKED, Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.BOLD, 11));
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setPreferredSize(new Dimension(44, 32));
            button.setEnabled(false);
            return button;
        }

        boolean selected = ctx.selectedSeats.contains(seat.getSeatNumber());
        RoundedButton button = new RoundedButton(seat.getSeatNumber(),
                selected ? UIConstants.SEAT_SELECTED : UIConstants.SEAT_AVAILABLE,
                selected ? UIConstants.SEAT_SELECTED : UIConstants.SEAT_AVAILABLE_HOVER,
                UIConstants.SEAT_SELECTED, selected ? Color.WHITE : UIConstants.TEXT_DARK);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setPreferredSize(new Dimension(44, 32));
        button.addActionListener(e -> {
            if (ctx.selectedSeats.contains(seat.getSeatNumber())) {
                ctx.selectedSeats.remove(seat.getSeatNumber());
                button.setColors(UIConstants.SEAT_AVAILABLE, UIConstants.SEAT_AVAILABLE_HOVER, UIConstants.SEAT_SELECTED);
                button.setForeground(UIConstants.TEXT_DARK);
            } else {
                ctx.selectedSeats.add(seat.getSeatNumber());
                button.setColors(UIConstants.SEAT_SELECTED, UIConstants.SEAT_SELECTED, UIConstants.SEAT_SELECTED);
                button.setForeground(Color.WHITE);
                Logger.action("Seat Selected: " + seat.getSeatNumber());
            }
            updateTotal();
        });
        return button;
    }

    private void updateTotal() {
        double total = 0;
        Seat[][] matrix = ctx.selectedShow.getScreen().getSeatMatrix();
        for (String seatNo : ctx.selectedSeats) {
            for (Seat[] row : matrix) {
                for (Seat seat : row) {
                    if (seat.getSeatNumber().equals(seatNo)) {
                        total += seat.priceFor(ctx.selectedShow.getBasePrice());
                    }
                }
            }
        }
        totalLabel.setText(ctx.selectedSeats.size() + " seat(s) selected \u2014 Total: \u20B9" + String.format("%.2f", total));
    }

    private void proceed() {
        if (ctx.selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one seat.");
            return;
        }
        try {
            ctx.currentBooking = ctx.bookingService.createBooking(
                    ctx.currentCustomer(), ctx.selectedShow, new ArrayList<>(ctx.selectedSeats));
            mainFrame.getPaymentPanel().refresh();
            mainFrame.showCard(MainFrame.PAYMENT);
        } catch (SeatAlreadyBookedException ex) {
            Logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Seat Unavailable", JOptionPane.WARNING_MESSAGE);
            refresh();
        }
    }
}
