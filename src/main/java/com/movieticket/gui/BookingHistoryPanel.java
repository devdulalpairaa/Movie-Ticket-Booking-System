package com.movieticket.gui;

import com.movieticket.exception.NoBookingFoundException;
import com.movieticket.model.Booking;
import com.movieticket.model.Show;
import com.movieticket.util.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/** Shows a Customer's full booking history and lets them undo the most recent booking. */
public class BookingHistoryPanel extends JPanel {

    private final MainFrame mainFrame;
    private final AppContext ctx;
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Booking ID", "Movie", "Theatre", "Show Time", "Seats", "Amount", "Status", "Date"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    public BookingHistoryPanel(MainFrame mainFrame, AppContext ctx) {
        this.mainFrame = mainFrame;
        this.ctx = ctx;
        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND);
        add(TopBar.build(mainFrame, ctx, "Booking History"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(UIConstants.BACKGROUND);
        center.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel heading = Components.heading("My Bookings");
        center.add(heading, BorderLayout.NORTH);

        table.setRowHeight(26);
        table.setFont(UIConstants.FONT_BODY);
        JScrollPane scroll = new JScrollPane(table);
        center.add(scroll, BorderLayout.CENTER);

        JButton undoBtn = Components.primaryButton("Cancel Last Booking (Undo)");
        undoBtn.addActionListener(e -> undoLast());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT));
        south.setOpaque(false);
        south.add(undoBtn);
        center.add(south, BorderLayout.SOUTH);

        add(center, BorderLayout.CENTER);
    }

    public void refresh() {
        model.setRowCount(0);
        for (Booking b : ctx.historyService.historyFor(ctx.currentUser.getUserId())) {
            Show show = ctx.movieService.getShowById(b.getShowId());
            String movieTitle = show != null ? show.getMovie().getTitle() : "(deleted)";
            String theatreName = show != null ? show.getTheatre().getName() : "-";
            String showTime = show != null ? show.getShowTime() : "-";
            model.addRow(new Object[]{b.getBookingId(), movieTitle, theatreName, showTime,
                    String.join(", ", b.getSeatNumbers()), "\u20B9" + String.format("%.2f", b.getTotalAmount()),
                    b.getStatus(), b.getBookingDate()});
        }
    }

    private void undoLast() {
        try {
            Booking cancelled = ctx.bookingService.undoLastBooking(ctx.currentUser.getUserId());
            JOptionPane.showMessageDialog(this, "Booking " + cancelled.getBookingId() + " has been cancelled.");
            refresh();
        } catch (NoBookingFoundException ex) {
            Logger.warn(ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Nothing to Undo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
