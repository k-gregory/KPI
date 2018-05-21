package io.github.k_gregory.graphics;

import org.ejml.simple.SimpleMatrix;

public class Point2d {
    public final double x, y;

    public Point2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2d(SimpleMatrix m) {
        this.x = m.get(0, 0);
        this.y = m.get(0, 1);
    }

    public Point2d transform(SimpleMatrix transform) {
        if (transform.numRows() != 3 || transform.numCols() != 3)
            throw new RuntimeException("Bad transform matrix");

        return new Point2d(m().mult(transform));
    }

    public SimpleMatrix m() {
        return new SimpleMatrix(new double[][]{{x, y, 1}});
    }
}
