package com.movieticket.gui;

import com.movieticket.exception.UserAlreadyExistsException;
import com.movieticket.model.Customer;
import com.movieticket.util.Logger;
import com.movieticket.util.Validator;

import javax.swing.*;
import java.awt.*;

/** Registration screen for new Customers. */
public class RegisterScreen extends JPanel {

    private final MainFrame mainFrame;
    private final AppContext ctx;

    private final JTextField nameField = Components.textField();
    private final JTextField usernameField = Components.textField();
    private final JPasswordField passwordField = Components.passwordField();
    private final JTextField emailField = Components.textField();
    private final JTextField phoneField = Components.textField();

    public RegisterScreen(MainFrame mainFrame, AppContext ctx) {
        this.mainFrame = mainFrame;
        this.ctx = ctx;
        setLayout(new GridBagLayout());
        setBackground(UIConstants.BACKGROUND);
        add(buildCard());
    }

    private JPanel buildCard() {
        JPanel card = Components.card();
        card.setPreferredSize(new Dimension(420, 520));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = Components.title("Create Account");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(20));

        card.add(field("Full Name", nameField));
        card.add(field("Username", usernameField));
        card.add(field("Password", passwordField));
        card.add(field("Email", emailField));
        card.add(field("Phone (10 digits)", phoneField));

        card.add(Box.createVerticalStrut(10));
        JButton registerBtn = Components.primaryButton("Register");
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBtn.addActionListener(e -> doRegister());
        card.add(registerBtn);
        card.add(Box.createVerticalStrut(10));

        JButton backBtn = Components.ghostButton("Already have an account? Login");
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.addActionListener(e -> mainFrame.showCard(MainFrame.LOGIN));
        card.add(backBtn);

        return card;
    }

    private JPanel field(String labelText, JComponent input) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel label = new JLabel(labelText);
        label.setFont(UIConstants.FONT_SUBHEADING);
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        input.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(input);
        panel.add(Box.createVerticalStrut(12));
        return panel;
    }

    private void doRegister() {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (!Validator.isNotEmpty(name) || !Validator.isNotEmpty(username)) {
            showError("Name and username are required.");
            return;
        }
        if (!Validator.isValidPassword(password)) {
            showError("Password must be at least 4 characters.");
            return;
        }
        if (!Validator.isValidEmail(email)) {
            showError("Please enter a valid email address.");
            return;
        }
        if (!Validator.isValidPhone(phone)) {
            showError("Phone number must be exactly 10 digits.");
            return;
        }

        try {
            Customer customer = ctx.authService.register(name, username, password, email, phone);
            JOptionPane.showMessageDialog(this, "Registration successful! You can now log in as \"" +
                    customer.getUsername() + "\".", "Welcome", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            mainFrame.showCard(MainFrame.LOGIN);
        } catch (UserAlreadyExistsException ex) {
            Logger.error("Registration failed: " + ex.getMessage());
            showError(ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Registration Error", JOptionPane.ERROR_MESSAGE);
    }

    private void clearFields() {
        nameField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        emailField.setText("");
        phoneField.setText("");
    }
}
