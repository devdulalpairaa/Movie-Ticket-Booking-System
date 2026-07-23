package com.movieticket.gui;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/** A soft rounded-rectangle border, used for text fields so they read as clearly-defined inputs. */
public class RoundedBorder extends AbstractBorder {

    private final Color color;
    private final int thickness;
    private final int arc;
    private final Insets insets;

    public RoundedBorder(Color color, int thickness, int arc) {
        this.color = color;
        this.thickness = thickness;
        this.arc = arc;
        int pad = 8 + thickness;
        this.insets = new Insets(pad, 10 + thickness, pad, 10 + thickness);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));
        g2.drawRoundRect(x + thickness / 2, y + thickness / 2, width - thickness - 1, height - thickness - 1, arc, arc);
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.set(this.insets.top, this.insets.left, this.insets.bottom, this.insets.right);
        return insets;
    }
}
