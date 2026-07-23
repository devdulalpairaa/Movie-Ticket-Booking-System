package com.movieticket.gui;

import com.movieticket.model.Movie;
import com.movieticket.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/** Shows movies playing at the selected theatre, with search and sort (DSA-backed). */
public class MovieListPanel extends JPanel {

    private final MainFrame mainFrame;
    private final AppContext ctx;
    private final JPanel listPanel = new JPanel();
    private final JTextField searchField = Components.textField();

    public MovieListPanel(MainFrame mainFrame, AppContext ctx) {
        this.mainFrame = mainFrame;
        this.ctx = ctx;
        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND);
        JPanel north = new JPanel(new BorderLayout());
        north.add(TopBar.build(mainFrame, ctx, "Choose Movie"), BorderLayout.NORTH);
        north.add(buildToolbar(), BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.setBackground(UIConstants.BACKGROUND);
        toolbar.setBorder(BorderFactory.createEmptyBorder(20, 60, 10, 60));

        searchField.setToolTipText("Search movies by title (linear search)");
        toolbar.add(searchField, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        JButton searchBtn = Components.secondaryButton("Search");
        searchBtn.addActionListener(e -> doSearch());
        JButton sortRatingBtn = Components.ghostButton("Sort by Rating");
        sortRatingBtn.addActionListener(e -> renderMovies(ctx.movieService.moviesSortedByRating()));
        JButton sortTitleBtn = Components.ghostButton("Sort by Title (A-Z)");
        sortTitleBtn.addActionListener(e -> renderMovies(ctx.movieService.moviesSortedByTitle()));
        buttons.add(searchBtn);
        buttons.add(sortRatingBtn);
        buttons.add(sortTitleBtn);
        toolbar.add(buttons, BorderLayout.EAST);
        return toolbar;
    }

    private void doSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            refresh();
            return;
        }
        Movie found = ctx.movieService.searchMovieByTitle(query);
        if (found == null) {
            JOptionPane.showMessageDialog(this, "No movie found matching \"" + query + "\".");
            return;
        }
        renderMovies(List.of(found));
    }

    public void refresh() {
        searchField.setText("");
        renderMovies(ctx.movieService.getMoviesForTheatre(ctx.selectedTheatre));
    }

    private void renderMovies(List<Movie> movies) {
        listPanel.removeAll();
        listPanel.setLayout(new GridLayout(0, 2, 16, 16));
        listPanel.setBackground(UIConstants.BACKGROUND);
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 24, 60));

        for (Movie movie : movies) {
            if (ctx.selectedTheatre != null && !movie.getTheatres().contains(ctx.selectedTheatre)) {
                continue;
            }
            listPanel.add(buildMovieCard(movie));
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel buildMovieCard(Movie movie) {
        JPanel card = Components.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(movie.getTitle());
        title.setFont(UIConstants.FONT_SUBHEADING);
        JLabel meta = new JLabel(movie.getGenre() + " \u2022 " + movie.getLanguage() + " \u2022 " +
                movie.getDurationMinutes() + " min");
        meta.setFont(UIConstants.FONT_BODY);
        meta.setForeground(UIConstants.TEXT_MUTED);
        JLabel rating = new JLabel("\u2B50 " + movie.getRating() + " / 5");
        rating.setFont(UIConstants.FONT_BODY);

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(meta);
        card.add(Box.createVerticalStrut(4));
        card.add(rating);
        card.add(Box.createVerticalStrut(10));

        JButton viewBtn = Components.primaryButton("View Details");
        viewBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        viewBtn.addActionListener(e -> {
            ctx.selectedMovie = movie;
            Logger.action("Movie Selected: " + movie.getTitle());
            mainFrame.getMovieDetailsPanel().refresh();
            mainFrame.showCard(MainFrame.MOVIE_DETAILS);
        });
        card.add(viewBtn);
        return card;
    }
}
