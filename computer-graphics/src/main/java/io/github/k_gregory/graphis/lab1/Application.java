package io.github.k_gregory.graphis.lab1;

import io.github.k_gregory.graphis.Transforms;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    private Canvas canvas;

    private final double[] sonneXs = {
            1.6, 0.6, 1.3, 4.2, 6.5, 6.9, 5.1
    };

    private final double[] sonneYs = {
            5.1, 3.1, 0.3, 0, 1.3, 3.9, 5.8
    };

    private static final double[] mouthXs = {
            2.8, 4.2, 3.8
    };

    private static final double[] mouthYs = {
            3.8, 3.8, 4.4
    };

    private static final double rayWidth = 0.2;
    private static final int rayCount = 12;

    public Application(){
        super();

        assert mouthXs.length == mouthYs.length;
        assert sonneXs.length == sonneYs.length;

        for (int i = 0; i < sonneXs.length; i++) {
            sonneYs[i] /= 2;
            sonneYs[i] += 2;

            sonneXs[i] /= 2;
            sonneXs[i] += 2;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        canvas = new Canvas();

        root.getChildren().add(canvas);
        draw();

        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());

        ChangeListener<Number> sizeListener = (property, oldValue, newValue) -> {
            draw();
        };
        canvas.widthProperty().addListener(sizeListener);
        canvas.heightProperty().addListener(sizeListener);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Drawing Operations Test");
        primaryStage.show();
    }

    private Affine idenity = Affine.affine(1,0,0,1,0,0);

    private void draw(){
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        GraphicsContext gc = canvas.getGraphicsContext2D();

        //Draw background
        gc.setTransform(idenity);
        gc.setFill(Color.CORAL);
        gc.fillRect(0, 0, width, height);

        Affine aspect = Transforms.aspectAffine(width, height, 7, 7);
        gc.setTransform(aspect);
        gc.setLineWidth(0.005);

        //Draw grid
        //gc.strokeLine(7, 0, 0, 7);
        //gc.strokeLine(0, 0, 7, 7);
        //gc.strokeRect(0, 0, 7, 7);

        //Draw sonne circle
        gc.setFill(Color.YELLOW);
        gc.fillPolygon(sonneXs, sonneYs, sonneXs.length);


        //Draw rays
        Affine raysTransform = aspect.clone();
        Rotate rotation = new Rotate(360.0 / rayCount, 3.5, 3.5);
        for (int i = 0; i < rayCount; i++) {
            raysTransform.append(rotation);
            gc.setTransform(raysTransform);

            gc.fillRect(3.5, 3.5 - rayWidth / 2, 3.4, rayWidth);
        }
        gc.setTransform(aspect);

        //Draw eyes
        gc.setFill(Color.GREEN);
        gc.fillRect(3.2, 3.1, 0.2, 0.2);
        gc.fillRect(4.3, 3.1, 0.2, 0.2);

        //Draw mouth
        gc.setFill(Color.RED);
        gc.fillPolygon(mouthXs, mouthYs, mouthXs.length);
    }
}
