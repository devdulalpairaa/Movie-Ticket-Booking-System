package com.movieticket.gui;

import com.movieticket.model.Theatre;
import com.movieticket.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/** Lists theatres available in the previously chosen city. */
public class TheatreSelectionPanel extends JPanel {

    private final MainFrame mainFrame;
    private final AppContext ctx;
    private final JPanel listPanel = new JPanel();

    public TheatreSelectionPanel(MainFrame mainFrame, AppContext ctx) {
        this.mainFrame = mainFrame;
        this.ctx = ctx;
        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND);
        add(TopBar.build(mainFrame, ctx, "Choose Theatre"), BorderLayout.NORTH);

        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(UIConstants.BACKGROUND);
        listPanel.setBorder(BorderFactory.createEmptyBorder(24, 60, 24, 60));

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    public void refresh() {
        listPanel.removeAll();

        JLabel heading = Components.heading("Theatres in " + ctx.selectedCity);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(heading);
        listPanel.add(Box.createVerticalStrut(16));

        List<Theatre> theatres = ctx.movieService.getTheatresInCity(ctx.selectedCity);
        for (Theatre theatre : theatres) {
            listPanel.add(buildTheatreCard(theatre));
            listPanel.add(Box.createVerticalStrut(12));
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel buildTheatreCard(Theatre theatre) {
        JPanel card = Components.card();
        card.setLayout(new BorderLayout());
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(theatre.getName());
        name.setFont(UIConstants.FONT_SUBHEADING);
        JLabel screens = new JLabel(theatre.getScreens().size() + " screen(s)");
        screens.setFont(UIConstants.FONT_BODY);
        screens.setForeground(UIConstants.TEXT_MUTED);
        textPanel.add(name);
        textPanel.add(screens);

        JButton selectBtn = Components.primaryButton("Select");
        selectBtn.addActionListener(e -> {
            ctx.selectedTheatre = theatre;
            Logger.action("Theatre Selected: " + theatre.getName());
            mainFrame.getMovieListPanel().refresh();
            mainFrame.showCard(MainFrame.MOVIE_LIST);
        });

        card.add(textPanel, BorderLayout.WEST);
        card.add(selectBtn, BorderLayout.EAST);
        return card;
    }
}
