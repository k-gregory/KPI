package io.github.k_gregory.graphics.lab1;

import java.awt.*;

import javax.swing.*;
import javax.swing.SwingUtilities;

import org.ejml.simple.SimpleMatrix;

import io.github.k_gregory.graphics.*;


public class SunPanel extends DrawPanel {
  @Override
  public void draw(Graphics2D g2d){
    int width = getWidth();
    int height = getHeight();

    SimpleMatrix aspect = Transform.aspect(width, height, 2, 1);

    Point2d centre = new Point2d(1, 0.5);
    Point2d asc = new Point2d(centre.m().mult(aspect));

    Point2d lp = new Point2d(0.05, 0.05);
    Point2d rp = new Point2d(1.95, 0.95);
    
    Point2d l = lp.transform(aspect);
    Point2d r = rp.transform(aspect);

    int lx = (int)l.x;
    int ly = (int)l.y;
    int w = (int)(r.x - l.x);
    int h = (int)(r.y - l.y);

    g2d.drawString(String.format("(%f %f) (%f %f)", l.x, l.y, r.x, r.y), 100, 50);

    g2d.drawRect(lx, ly, w, h);

    g2d.drawString(String.format("%dx%d %f", width, height, getT()), (int)asc.x, (int)asc.y);
  }
}
