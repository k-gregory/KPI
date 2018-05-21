package io.github.k_gregory.graphics.lab2;

import io.github.k_gregory.graphics.Point2d;
import io.github.k_gregory.graphics.Shape2d;
import io.github.k_gregory.graphics.Transform;
import org.ejml.simple.SimpleMatrix;

import java.awt.*;


public class SunPanel extends DrawPanel {
    private static final double[] mouthXs = {2.8, 4.2, 3.8};
    private static final double[] mouthYs = {3.8, 3.8, 4.4};
    private static final double rayWidth = 0.2;
    private static final double raylength = 3;
    private static final int rayCount = 12;
    private final SimpleMatrix sunPosT = Transform.translate2d(3.5, 3.5);
    private final double[] sonneXs = {1.6, 0.6, 1.3, 4.2, 6.5, 6.9, 5.1};
    private final double[] sonneYs = {5.1, 3.1, 0.3, 0, 1.3, 3.9, 5.8};
    private final double[] rectXs = {0.1, 0.1, 6.9, 6.9};
    private final double[] rectYs = {0.1, 6.9, 6.9, 0.1};
    private final double[] rayXs = {-rayWidth / 2, -rayWidth / 2, rayWidth / 2, rayWidth / 2};
    private final double[] rayYs = {0.1, raylength + 0.1, raylength + 0.1, 0.1};
    private final Point2d leye = new Point2d(3.2, 3.1);
    private final Point2d reye = new Point2d(4.3, 3.1);
    private final Shape2d sonne;
    private final Shape2d mouth;
    private final Shape2d rect = new Shape2d(rectXs, rectYs);
    private final Shape2d ray = new Shape2d(rayXs, rayYs);
    private final BasicStroke rectStroke = new BasicStroke(3, BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_MITER);
    private final GradientPaint sonnePaint = new GradientPaint(5, 25,
            Color.ORANGE, 20, 2, Color.YELLOW, true);

    public SunPanel() {
        for (int i = 0; i < sonneXs.length; i++) {
            sonneYs[i] /= 2;
            sonneYs[i] += 2;

            sonneXs[i] /= 2;
            sonneXs[i] += 2;
        }

        sonne = new Shape2d(sonneXs, sonneYs);
        mouth = new Shape2d(mouthXs, mouthYs);
    }

    @Override
    public void draw(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();

        SimpleMatrix aspect = Transform.aspect(width, height, 7, 7);

        //Draw rays
        g2d.setColor(Color.YELLOW);
        g2d.setStroke(rectStroke);
        for (int i = 0; i <= rayCount; i++) {
            double angle = -getT() * 4 + (Math.PI * 2 / rayCount * i);
            SimpleMatrix m = Transform.rotate2d(angle).mult(sunPosT);
            //SimpleMatrix m = Transform.rotate2d(-getT() * 5).mult(Transform.translate2d(3.5, 3.5));
            SimpleMatrix mvp = m.mult(aspect);
            g2d.fill(ray.transform(mvp).toPath());
        }

        //Rraw rect
        g2d.setStroke(rectStroke);
        g2d.setColor(Color.BLACK);
        g2d.draw(rect.transform(aspect).toPath());

        //Draw body
        g2d.setPaint(sonnePaint);
        g2d.fill(sonne.transform(aspect).toPath());

        //Draw mouth
        g2d.setPaint(new Color(1.0f, 0.0f, 0.0f, ((float) Math.sin(getT() * 3) + 1) / 2));
        g2d.fill(mouth.transform(aspect).toPath());

        //Draw eyes
        g2d.setColor(Color.GREEN);
        Point2d leT = leye.transform(aspect);
        Point2d reT = reye.transform(aspect);
        g2d.fillOval((int) leT.x, (int) leT.y, 50, 50);
        g2d.fillOval((int) reT.x, (int) reT.y, 50, 50);
    }
}
