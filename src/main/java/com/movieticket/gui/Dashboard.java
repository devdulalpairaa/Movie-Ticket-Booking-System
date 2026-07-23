package com.movieticket.gui;

import com.movieticket.model.Customer;
import com.movieticket.util.Logger;

import javax.swing.*;
import java.awt.*;

/** Landing screen after a Customer logs in: pick a city to start booking. */
public class Dashboard extends JPanel {

    private final MainFrame mainFrame;
    private final AppContext ctx;
    private final JLabel welcomeLabel = new JLabel();
    private final JComboBox<String> cityCombo = new JComboBox<>();

    public Dashboard(MainFrame mainFrame, AppContext ctx) {
        this.mainFrame = mainFrame;
        this.ctx = ctx;
        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND);
        add(TopBar.build(mainFrame, ctx, "Home"), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
    }

    private JPanel buildCenter() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(UIConstants.BACKGROUND);

        JPanel card = Components.card();
        card.setPreferredSize(new Dimension(460, 300));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        welcomeLabel.setFont(UIConstants.FONT_TITLE);
        welcomeLabel.setForeground(UIConstants.PRIMARY);
        card.add(welcomeLabel);
        card.add(Box.createVerticalStrut(6));

        JLabel sub = new JLabel("Let's book your next movie experience.");
        sub.setFont(UIConstants.FONT_BODY);
        sub.setForeground(UIConstants.TEXT_MUTED);
        card.add(sub);
        card.add(Box.createVerticalStrut(24));

        JLabel cityLabel = new JLabel("Choose your city");
        cityLabel.setFont(UIConstants.FONT_SUBHEADING);
        card.add(cityLabel);
        card.add(Box.createVerticalStrut(6));
        cityCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        card.add(cityCombo);
        card.add(Box.createVerticalStrut(24));

        JButton continueBtn = Components.primaryButton("Browse Theatres  \u2192");
        continueBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueBtn.addActionListener(e -> {
            String city = (String) cityCombo.getSelectedItem();
            if (city == null) {
                JOptionPane.showMessageDialog(this, "No cities available.");
                return;
            }
            ctx.selectedCity = city;
            Logger.action("City Selected: " + city);
            mainFrame.getTheatreSelectionPanel().refresh();
            mainFrame.showCard(MainFrame.THEATRE_SELECTION);
        });
        card.add(continueBtn);

        wrapper.add(card);
        return wrapper;
    }

    public void refresh() {
        Customer customer = ctx.currentCustomer();
        welcomeLabel.setText("Welcome, " + customer.getName() + "!");
        cityCombo.removeAllItems();
        for (String city : ctx.movieService.getAllCities()) {
            cityCombo.addItem(city);
        }
    }
}
