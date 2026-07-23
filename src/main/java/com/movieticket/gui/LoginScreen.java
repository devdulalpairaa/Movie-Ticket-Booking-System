package com.movieticket.gui;

import com.movieticket.exception.InvalidLoginException;
import com.movieticket.model.Admin;
import com.movieticket.model.Person;
import com.movieticket.util.Logger;

import javax.swing.*;
import java.awt.*;

/** First screen shown: lets a Customer or Admin log in, or navigate to Register. */
public class LoginScreen extends JPanel {

    private final MainFrame mainFrame;
    private final AppContext ctx;
    private final JTextField usernameField = Components.textField();
    private final JPasswordField passwordField = Components.passwordField();

    public LoginScreen(MainFrame mainFrame, AppContext ctx) {
        this.mainFrame = mainFrame;
        this.ctx = ctx;
        setLayout(new GridBagLayout());
        setBackground(UIConstants.BACKGROUND);
        add(buildCard());
    }

    private JPanel buildCard() {
        JPanel card = Components.card();
        card.setPreferredSize(new Dimension(400, 420));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = Components.title("\uD83C\uDFAC Movie Ticket Booking");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subtitle = new JLabel("Sign in to continue");
        subtitle.setFont(UIConstants.FONT_BODY);
        subtitle.setForeground(UIConstants.TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(24));

        card.add(labeled("Username"));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(14));

        card.add(labeled("Password"));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(22));

        JButton loginBtn = Components.primaryButton("Login");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> doLogin());
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(10));

        JButton registerBtn = Components.ghostButton("Create a new account");
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBtn.addActionListener(e -> mainFrame.showCard(MainFrame.REGISTER));
        card.add(registerBtn);

        card.add(Box.createVerticalStrut(16));
        JLabel hint = new JLabel("Admin demo login -> admin / admin123");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(UIConstants.TEXT_MUTED);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(hint);

        passwordField.addActionListener(e -> doLogin());
        return card;
    }

    private JLabel labeled(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_SUBHEADING);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        try {
            Person person = ctx.authService.login(username, password);
            ctx.currentUser = person;
            usernameField.setText("");
            passwordField.setText("");
            if (person instanceof Admin) {
                mainFrame.showCard(MainFrame.ADMIN_DASHBOARD);
            } else {
                mainFrame.showCard(MainFrame.DASHBOARD);
            }
        } catch (InvalidLoginException ex) {
            Logger.error("Login failed: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}
