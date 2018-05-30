package io.github.k_gregory.graphis;

import javafx.scene.canvas.GraphicsContext;

import java.util.Arrays;

public class ScalingContext {
    private final GraphicsContext gc;
    private final double mX, mY, oX, oY;

    public ScalingContext(GraphicsContext gc, double realWidth, double realHeight, double wantedWidth, double wantedHeight) {
        this.gc = gc;

        double realAspect = realWidth / realHeight;
        double wantedAspect = wantedWidth / wantedHeight;

        double drawnWidth, drawnHeight;

        if(realAspect > wantedAspect){
            drawnHeight = realHeight;
            drawnWidth = wantedAspect * realHeight;

            oX = (realWidth - drawnWidth) / 2;
            oY = 0;
        } else {
            drawnHeight = (1.0 / wantedAspect) * realWidth;
            drawnWidth = realWidth;

            oX = 0;
            oY = (realHeight - drawnHeight) / 2;
        }

         mX = drawnWidth / wantedWidth;
         mY = drawnHeight / wantedHeight;
    }

    public void strokeRect(double x, double y, double w, double h){
        double x1 = translateX(x);
        double y1 = translateY(y);
        double x2 = translateX(x + w);
        double y2 = translateY(y + h);

        gc.strokeRect(
                x1, y1,
                x2 - x1, y2 - y1
        );
    }

    public void fillRect(double x, double y, double w, double h){
        double x1 = translateX(x);
        double y1 = translateY(y);
        double x2 = translateX(x + w);
        double y2 = translateY(y + h);

        gc.fillRect(
                x1, y1,
                x2 - x1, y2 - y1
        );
    }

    public void strokeLine(double x1, double y1, double x2, double y2){
        gc.strokeLine(
                translateX(x1), translateY(y1),
                translateX(x2), translateY(y2)
        );
    }

    public void fillPolygon(double[] xPoints, double[] yPoints, int n){
        if(xPoints == null || yPoints == null) return;

        xPoints = Arrays.copyOf(xPoints, xPoints.length);
        yPoints = Arrays.copyOf(yPoints, yPoints.length);

        for (int i = 0; i < xPoints.length; i++) {
            xPoints[i] = translateX(xPoints[i]);
        }

        for(int i = 0; i < yPoints.length; i++){
            yPoints[i] = translateY(yPoints[i]);
        }

        gc.fillPolygon(xPoints, yPoints, n);
    }

    private double translateX(double x){
        return x * mX + oX;
    }

    private double translateY(double y){
        return y * mY + oY;
    }
}
