package io.github.k_gregory.graphics.lab1;

import java.awt.*;

import javax.swing.*;
import javax.swing.SwingUtilities;

import org.ejml.simple.SimpleMatrix;

import io.github.k_gregory.graphics.*;

abstract class DrawPanel extends JPanel{
  long usec;

  private long t, dt;

  private double usecToSec(long usec){
    return ((double) usec) / 1e9;
  }

  public double getT(){
    return usecToSec(t);
  }

  public double getDT(){
    return usecToSec(dt);
  }

  public DrawPanel(){
    usec = System.nanoTime();

    new Timer(10, (a)->{
      long newUsec = System.nanoTime();
      long dtime = newUsec - usec;
      usec = newUsec;

      t = usec;
      dt = dtime;

      repaint();
    }).start();
  }

  abstract public void draw(Graphics2D g2d);

  @Override
  public void paint(Graphics g){
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.BLACK);
    g2d.clearRect(0, 0, getWidth(), getHeight());
    draw(g2d);
  }
}

public class Application {

  public static void main(String... args){
    SwingUtilities.invokeLater(()->{
      JFrame f = new JFrame("LOL");
      DrawPanel panel = new SunPanel();
      f.add(panel);
      f.pack();
      f.setVisible(true);
      f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    });
    System.out.println("Graphics lab 1 SWING!");
  }
}
