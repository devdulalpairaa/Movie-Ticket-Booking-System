package com.movieticket.gui;

import java.awt.*;

/** Shared colours and fonts so every screen has a consistent, modern look. */
public final class UIConstants {
    public static final Color PRIMARY = new Color(30, 41, 59);      // slate-800
    public static final Color PRIMARY_HOVER = new Color(51, 65, 85);   // slate-700
    public static final Color PRIMARY_PRESS = new Color(15, 23, 42);   // slate-900
    public static final Color ACCENT = new Color(220, 38, 38);      // red-600 (cinema red)
    public static final Color ACCENT_HOVER = new Color(239, 68, 68);   // red-500
    public static final Color ACCENT_DARK = new Color(185, 28, 28);    // red-700 (press)
    public static final Color BACKGROUND = new Color(248, 250, 252);// slate-50
    public static final Color CARD_BG = Color.WHITE;
    public static final Color BORDER = new Color(203, 213, 225);    // slate-300
    public static final Color TEXT_DARK = new Color(15, 23, 42);
    public static final Color TEXT_MUTED = new Color(71, 85, 105);     // slate-600 (darkened for readability)
    public static final Color SUCCESS = new Color(22, 163, 74);
    public static final Color SEAT_AVAILABLE = new Color(226, 232, 240);
    public static final Color SEAT_AVAILABLE_HOVER = new Color(203, 213, 225);
    public static final Color SEAT_SELECTED = new Color(34, 197, 94);
    public static final Color SEAT_BOOKED = new Color(148, 163, 184);

    public static final int RADIUS = 12;

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 25);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 19);
    public static final Font FONT_SUBHEADING = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);

    private UIConstants() {
    }
}
