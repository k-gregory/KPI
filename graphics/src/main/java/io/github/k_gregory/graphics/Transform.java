package io.github.k_gregory.graphics;

import org.ejml.simple.SimpleMatrix;

public class Transform{
  public static SimpleMatrix aspect(double realWidth, double realHeight,
      double wantedWidth, double wantedHeight){
    double realAspect = realWidth / realHeight;
    double wantedAspect = wantedWidth / wantedHeight;

    double drawnWidth, drawnHeight;
    double offsetX, offsetY;

    if(realAspect > wantedAspect){
      drawnHeight = realHeight;
      drawnWidth = wantedAspect * realHeight;

      offsetX = (realWidth - drawnWidth) / 2;
      offsetY = 0;
    } else {
      drawnHeight = (1.0 / wantedAspect) * realWidth;
      drawnWidth = realWidth;

      offsetX = 0;
      offsetY = (realHeight - drawnHeight) / 2;
    }

    double multiplyX = drawnWidth / wantedWidth;
    double multiplyY = drawnHeight / wantedHeight;

    return new SimpleMatrix(new double[][]{
      {multiplyX, 0},
      {0, multiplyY},
      {offsetX, offsetY}
    });
  }
}
