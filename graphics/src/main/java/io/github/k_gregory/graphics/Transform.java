package io.github.k_gregory.graphics;

import org.ejml.simple.SimpleMatrix;

public class Transform {
    public static SimpleMatrix rotate2d(double angle) {
        return new SimpleMatrix(new double[][]{
                {Math.cos(angle), Math.sin(angle), 0.0},
                {-Math.sin(angle), Math.cos(angle), 0.0},
                {0.0, 0.0, 1.0}
        });
    }

    public static SimpleMatrix translate2d(double x, double y) {
        return new SimpleMatrix(new double[][]{
                {1, 0, 0},
                {0, 1, 0},
                {x, y, 1}
        });
    }

    public static SimpleMatrix aspect(double realWidth, double realHeight,
                                      double wantedWidth, double wantedHeight) {
        double realAspect = realWidth / realHeight;
        double wantedAspect = wantedWidth / wantedHeight;

        double drawnWidth, drawnHeight;
        double offsetX, offsetY;

        if (realAspect > wantedAspect) {
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
                {multiplyX, 0, 0},
                {0, multiplyY, 0},
                {offsetX, offsetY, 1}
        });
    }
}
