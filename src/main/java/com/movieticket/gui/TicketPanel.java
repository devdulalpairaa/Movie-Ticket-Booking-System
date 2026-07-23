package com.movieticket.gui;

import javax.swing.*;
import java.awt.*;

/** Shows the generated Ticket after a successful payment. */
public class TicketPanel extends JPanel {

    private final MainFrame mainFrame;
    private final AppContext ctx;
    private final JTextArea ticketArea = new JTextArea();

    public TicketPanel(MainFrame mainFrame, AppContext ctx) {
        this.mainFrame = mainFrame;
        this.ctx = ctx;
        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND);
        add(TopBar.build(mainFrame, ctx, "Your Ticket"), BorderLayout.NORTH);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(UIConstants.BACKGROUND);

        JPanel card = Components.card();
        card.setPreferredSize(new Dimension(480, 420));
        card.setLayout(new BorderLayout());

        JLabel success = new JLabel("\u2705 Booking Confirmed!", SwingConstants.CENTER);
        success.setFont(UIConstants.FONT_TITLE);
        success.setForeground(UIConstants.SUCCESS);
        success.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        card.add(success, BorderLayout.NORTH);

        ticketArea.setEditable(false);
        ticketArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        ticketArea.setBackground(new Color(248, 250, 252));
        card.add(new JScrollPane(ticketArea), BorderLayout.CENTER);

        JButton doneBtn = Components.primaryButton("Back to Home");
        doneBtn.addActionListener(e -> {
            ctx.resetBookingSelection();
            mainFrame.showCard(MainFrame.DASHBOARD);
        });
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        south.setOpaque(false);
        south.add(doneBtn);
        card.add(south, BorderLayout.SOUTH);

        wrapper.add(card);
        add(wrapper, BorderLayout.CENTER);
    }

    public void refresh() {
        ticketArea.setText(ctx.lastTicket.prettyPrint());
    }
}
