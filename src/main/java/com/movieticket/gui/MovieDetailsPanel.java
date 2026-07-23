package com.movieticket.gui;

import com.movieticket.model.Movie;
import com.movieticket.model.Show;
import com.movieticket.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/** Displays full movie info plus the list of available Shows to pick from. */
public class MovieDetailsPanel extends JPanel {

    private final MainFrame mainFrame;
    private final AppContext ctx;
    private final JPanel infoPanel = new JPanel();
    private final JPanel showsPanel = new JPanel();

    public MovieDetailsPanel(MainFrame mainFrame, AppContext ctx) {
        this.mainFrame = mainFrame;
        this.ctx = ctx;
        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND);
        add(TopBar.build(mainFrame, ctx, "Movie Details"), BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(UIConstants.BACKGROUND);
        center.setBorder(BorderFactory.createEmptyBorder(24, 60, 24, 60));

        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(UIConstants.CARD_BG);
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        showsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 12));
        showsPanel.setBackground(UIConstants.BACKGROUND);
        showsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton backBtn = Components.ghostButton("\u2190 Back to Movies");
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> mainFrame.showCard(MainFrame.MOVIE_LIST));

        center.add(backBtn);
        center.add(Box.createVerticalStrut(16));
        center.add(wrapCard(infoPanel));
        center.add(Box.createVerticalStrut(20));
        JLabel showsHeading = Components.heading("Available Shows at " + "");
        showsHeading.setName("showsHeading");
        center.add(showsHeading);
        center.add(showsPanel);

        JScrollPane scroll = new JScrollPane(center);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel wrapCard(JPanel inner) {
        JPanel card = Components.card();
        card.setLayout(new BorderLayout());
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(inner, BorderLayout.CENTER);
        return card;
    }

    public void refresh() {
        Movie movie = ctx.selectedMovie;
        infoPanel.removeAll();

        JLabel title = new JLabel(movie.getTitle());
        title.setFont(UIConstants.FONT_TITLE);
        JLabel meta = new JLabel(movie.getGenre() + " \u2022 " + movie.getLanguage() + " \u2022 " +
                movie.getDurationMinutes() + " min \u2022 \u2B50 " + movie.getRating());
        meta.setFont(UIConstants.FONT_BODY);
        meta.setForeground(UIConstants.TEXT_MUTED);
        JTextArea desc = new JTextArea(movie.getDescription());
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setEditable(false);
        desc.setOpaque(false);
        desc.setFont(UIConstants.FONT_BODY);
        desc.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        infoPanel.add(title);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(meta);
        infoPanel.add(desc);

        showsPanel.removeAll();
        List<Show> shows = ctx.movieService.getShows(movie, ctx.selectedTheatre);
        for (Show show : shows) {
            showsPanel.add(buildShowButton(show));
        }
        revalidate();
        repaint();
    }

    private JButton buildShowButton(Show show) {
        JButton btn = Components.secondaryButton(show.getShowTime() + "  (" + show.getScreen().getScreenName() +
                ")  \u20B9" + (int) show.getBasePrice());
        btn.addActionListener(e -> {
            ctx.selectedShow = show;
            ctx.selectedSeats.clear();
            Logger.action("Show Selected: " + show.getShowTime());
            mainFrame.getSeatLayoutPanel().refresh();
            mainFrame.showCard(MainFrame.SEAT_LAYOUT);
        });
        return btn;
    }
}
