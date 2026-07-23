package com.movieticket.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A JButton painted with soft rounded corners and a colour shift on hover/press.
 * Native Look-and-Feel chrome tends to paint flat, low-contrast buttons on top
 * of whatever background colour is set (especially on Windows), so this bypasses
 * that and paints the fill itself - giving every button a consistent, clearly
 * visible, and interactive look on any platform.
 */
public class RoundedButton extends JButton {

    private static final int ARC = 12;

    private Color baseColor;
    private Color hoverColor;
    private Color pressColor;
    private Color disabledColor = new Color(203, 213, 225);
    private Color outlineColor;
    private boolean hovering = false;
    private boolean pressing = false;

    public RoundedButton(String text, Color baseColor, Color hoverColor, Color pressColor, Color fg) {
        super(text);
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
        this.pressColor = pressColor;
        setForeground(fg);
        setFont(UIConstants.FONT_BUTTON);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovering = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovering = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressing = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressing = false;
                repaint();
            }
        });
    }

    /** Optional outline colour, drawn even when the fill is transparent (used for the ghost style). */
    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
        repaint();
    }

    /** Updates the base/hover/press fill colours after construction (e.g. a seat button changing state). */
    public void setColors(Color baseColor, Color hoverColor, Color pressColor) {
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
        this.pressColor = pressColor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color fill = !isEnabled() ? disabledColor : pressing ? pressColor : hovering ? hoverColor : baseColor;
        if (fill != null) {
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC, ARC);
        }
        if (outlineColor != null) {
            g2.setColor(outlineColor);
            g2.setStroke(new BasicStroke(1.4f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC, ARC);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
