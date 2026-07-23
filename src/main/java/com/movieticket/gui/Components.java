package com.movieticket.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/** Small factory helpers for consistent-looking Swing widgets across all screens. */
public final class Components {

    private Components() {
    }

    public static JButton primaryButton(String text) {
        RoundedButton button = new RoundedButton(text, UIConstants.ACCENT, UIConstants.ACCENT_HOVER,
                UIConstants.ACCENT_DARK, Color.WHITE);
        return button;
    }

    public static JButton secondaryButton(String text) {
        RoundedButton button = new RoundedButton(text, UIConstants.PRIMARY, UIConstants.PRIMARY_HOVER,
                UIConstants.PRIMARY_PRESS, Color.WHITE);
        return button;
    }

    public static JButton ghostButton(String text) {
        RoundedButton button = new RoundedButton(text, null, new Color(226, 232, 240),
                new Color(203, 213, 225), UIConstants.PRIMARY);
        button.setOutlineColor(UIConstants.BORDER);
        return button;
    }

    public static JLabel heading(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_HEADING);
        label.setForeground(UIConstants.TEXT_DARK);
        return label;
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_TITLE);
        label.setForeground(UIConstants.PRIMARY);
        return label;
    }

    public static JPanel card() {
        JPanel panel = new JPanel();
        panel.setBackground(UIConstants.CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(new Color(226, 232, 240), 1, UIConstants.RADIUS),
                new EmptyBorder(16, 18, 16, 18)));
        return panel;
    }

    public static JTextField textField() {
        JTextField field = new JTextField();
        field.setFont(UIConstants.FONT_BODY);
        field.setForeground(UIConstants.TEXT_DARK);
        applyFieldStyle(field);
        return field;
    }

    public static JPasswordField passwordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(UIConstants.FONT_BODY);
        field.setForeground(UIConstants.TEXT_DARK);
        applyFieldStyle(field);
        return field;
    }

    private static void applyFieldStyle(JTextField field) {
        RoundedBorder normal = new RoundedBorder(UIConstants.BORDER, 1, 8);
        RoundedBorder focused = new RoundedBorder(UIConstants.ACCENT, 2, 8);
        field.setBorder(normal);
        field.setBackground(Color.WHITE);
        field.setCaretColor(UIConstants.TEXT_DARK);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(focused);
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(normal);
            }
        });
    }
}
