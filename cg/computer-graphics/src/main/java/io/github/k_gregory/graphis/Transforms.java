package io.github.k_gregory.graphis;

import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;

public class Transforms {
    public static Affine aspectAffine(double realWidth, double realHeight, double wantedWidth, double wantedHeight){
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

        return Transform.affine(multiplyX, 0, 0, multiplyY, offsetX, offsetY);
    }
}
