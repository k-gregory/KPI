package io.github.k_gregory.otp.lab2.datatypes;

import Jama.Matrix;
import io.github.k_gregory.otp.lab2.MathValVisitor;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class VectorVal extends MathVal {
    private MathVal[] elements;

    public VectorVal(MathVal... elements) {
        this.elements = elements;
    }

    public static VectorVal fromMatrix(Matrix m) {
        double[][] array = m.getArray();
        VectorVal[] rows = new VectorVal[array.length];
        for (int i = 0; i < array.length; i++) {
            NumVal[] cols = new NumVal[array[i].length];
            for (int j = 0; j < array[i].length; j++)
                cols[j] = new NumVal(array[i][j]);
            rows[i] = new VectorVal(cols);
        }
        return new VectorVal(rows);
    }

    @Override
    public MathVal accept(MathValVisitor v) {
        return v.visit(this);
    }

    private void assureSameLength(VectorVal other) {
        if (elements.length != other.elements.length)
            throw new IllegalArgumentException("Different number of elements in vector");
    }

    public Matrix toMatrix() {
        int m = elements.length;
        int n = ((VectorVal) elements[0]).elements.length;
        Matrix matrix = new Matrix(m, n);
        for (int i = 0; i < m; i++) {
            MathVal[] rows = ((VectorVal) elements[i]).elements;
            for (int j = 0; j < n; j++) {
                matrix.set(i, j, ((NumVal) rows[j]).val);
            }
        }
        return matrix;
    }

    @Override
    public MathVal mul(MathVal right) {
        return right.accept(new MathValVisitor() {
            @Override
            public MathVal visit(NumVal num) {
                return new VectorVal(stream(elements).map(el -> el.mul(num)).toArray(MathVal[]::new));
            }

            @Override
            public MathVal visit(VectorVal vector) {
                assureSameLength(vector);
                MathVal sum = elements[0].mul(vector.elements[0]);
                for (int i = 1; i < elements.length; i++) {
                    sum = sum.plus(elements[i].mul(vector.elements[i]));
                }
                return sum;
            }
        });
    }

    @Override
    public MathVal plus(MathVal right) {
        return right.accept(new MathValVisitor() {
            @Override
            public MathVal visit(NumVal num) {
                return new VectorVal(stream(elements).map(el -> el.plus(num)).toArray(MathVal[]::new));
            }

            @Override
            public MathVal visit(VectorVal vector) {
                assureSameLength(vector);
                MathVal[] newVector = new MathVal[elements.length];
                for (int i = 0; i < elements.length; i++)
                    newVector[i] = elements[i].plus(vector.elements[i]);
                return new VectorVal(newVector);
            }
        });
    }

    @Override
    public MathVal vMul(MathVal right) {
        return right.accept(new MathValVisitor() {
            @Override
            public MathVal visit(VectorVal vector) {
                if (elements.length != 3 || vector.elements.length != 3)
                    throw new IllegalArgumentException("Can't mult vectors if not 3 dismension");
                MathVal[] a = elements;
                MathVal[] b = vector.elements;

                MathVal iPart = a[1].mul(b[2]).minus(a[2].mul(b[1]));
                MathVal jPart = a[2].mul(b[0]).minus(a[0].mul(b[2]));
                MathVal kPart = a[0].mul(b[1]).minus(a[1].mul(b[0]));

                return new VectorVal(iPart, jPart, kPart);
            }
        });
    }

    @Override
    public MathVal matMul(MathVal other) {
        return other.accept(new MathValVisitor() {
            @Override
            public MathVal visit(VectorVal vector) {
                return fromMatrix(toMatrix().times(vector.toMatrix()));
            }
        });
    }

    @Override
    public MathVal inverse() {
        return fromMatrix(toMatrix().inverse());
    }

    @Override
    public MathVal transpose() {
        return fromMatrix(toMatrix().transpose());
    }

    @Override
    public String toString() {
        return "[" + stream(elements).map(MathVal::toString).collect(Collectors.joining(",")) + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VectorVal vectorVal = (VectorVal) o;

        return Arrays.deepEquals(vectorVal.elements, elements);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(elements);
    }
}
