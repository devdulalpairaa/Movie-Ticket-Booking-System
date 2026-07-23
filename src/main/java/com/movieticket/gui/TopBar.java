package com.movieticket.gui;

import com.movieticket.util.Logger;

import javax.swing.*;
import java.awt.*;

/** Reusable top navigation bar shown on every customer-facing screen. */
public final class TopBar {

    private TopBar() {
    }

    public static JPanel build(MainFrame mainFrame, AppContext ctx, String currentStepLabel) {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(UIConstants.PRIMARY);
        bar.setPreferredSize(new Dimension(100, 56));
        bar.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel appName = new JLabel("\uD83C\uDFAC MovieTix");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        appName.setForeground(Color.WHITE);

        JLabel step = new JLabel(currentStepLabel);
        step.setFont(UIConstants.FONT_BODY);
        step.setForeground(new Color(203, 213, 225));
        step.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(appName);
        left.add(step);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        JButton homeBtn = flatButton("Home");
        homeBtn.addActionListener(e -> mainFrame.showCard(MainFrame.DASHBOARD));

        JButton historyBtn = flatButton("My Bookings");
        historyBtn.addActionListener(e -> {
            mainFrame.getBookingHistoryPanel().refresh();
            mainFrame.showCard(MainFrame.HISTORY);
        });

        JButton logoutBtn = flatButton("Logout");
        logoutBtn.addActionListener(e -> {
            Logger.action("User Logged Out: " + ctx.currentUser.getUsername());
            ctx.currentUser = null;
            ctx.resetBookingSelection();
            mainFrame.showCard(MainFrame.LOGIN);
        });

        right.add(homeBtn);
        right.add(historyBtn);
        right.add(logoutBtn);

        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private static JButton flatButton(String text) {
        return new RoundedButton(text, null, new Color(51, 65, 85), new Color(15, 23, 42), Color.WHITE);
    }
}
