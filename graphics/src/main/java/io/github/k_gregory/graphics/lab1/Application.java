package io.github.k_gregory.graphics.lab1;

import javax.swing.*;
import javax.swing.SwingUtilities;

public class Application {

  public static void main(String... args){
    SwingUtilities.invokeLater(()->{
      JFrame f = new JFrame("LOL");
      f.add(new JLabel("Hello"));
      f.pack();
      f.setVisible(true);
    });
    System.out.println("Graphics lab 1 SWING");
  }
}
