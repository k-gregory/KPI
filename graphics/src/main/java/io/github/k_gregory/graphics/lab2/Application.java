package io.github.k_gregory.graphics.lab2;

import javax.swing.*;
import java.awt.*;

abstract class DrawPanel extends JPanel {
    private long usec;

    private long t, dt;

    public DrawPanel() {
        setPreferredSize(new Dimension(640, 480));

        usec = System.nanoTime();

        new Timer(10, (a) -> {
            long newUsec = System.nanoTime();
            long dtime = newUsec - usec;
            usec = newUsec;

            t = usec;
            dt = dtime;

            repaint();
        }).start();
    }

    private double usecToSec(long usec) {
        return ((double) usec) / 1e9;
    }

    public double getT() {
        return usecToSec(t);
    }

    public double getDT() {
        return usecToSec(dt);
    }

    abstract public void draw(Graphics2D g2d);

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setColor(Color.BLACK);
        g2d.clearRect(0, 0, getWidth(), getHeight());
        draw(g2d);
    }
}

public class Application {

    public static void main(String... args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Lab2");
            DrawPanel panel = new SunPanel();
            f.add(panel);
            f.pack();
            f.setVisible(true);
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        });
        System.out.println("Graphics lab 1 SWING!");
    }
}
