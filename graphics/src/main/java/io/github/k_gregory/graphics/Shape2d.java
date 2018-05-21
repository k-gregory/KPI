package io.github.k_gregory.graphics;

import org.ejml.simple.SimpleMatrix;

import java.awt.geom.GeneralPath;

public class Shape2d {
    private final Point2d points[];

    public Shape2d(Point2d[] p) {
        this.points = p;
    }

    public Shape2d(double[] xs, double ys[]) {
        assert xs.length == ys.length;
        int l = xs.length;

        points = new Point2d[l];
        for (int i = 0; i < l; i++) {
            points[i] = new Point2d(xs[i], ys[i]);
        }
    }

    public GeneralPath toPath() {
        GeneralPath res = new GeneralPath();

        res.moveTo(points[0].x, points[0].y);
        for (Point2d point : points) {
            res.lineTo(point.x, point.y);
        }
        res.closePath();

        return res;
    }

    public Shape2d transform(SimpleMatrix m) {
        Point2d[] r = new Point2d[points.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = points[i].transform(m);
        }
        return new Shape2d(r);
    }
}
