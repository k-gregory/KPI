package io.github.atommed.otp.lab2;

import Jama.Matrix;
import io.github.atommed.otp.lab2.datatypes.NumVal;
import io.github.atommed.otp.lab2.datatypes.VectorVal;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataTypeTests {
    @Test
    public void matrixDismensions(){
        Matrix matrix = new VectorVal(
                new VectorVal(new NumVal(1.d), new NumVal(2.d)),
                new VectorVal(new NumVal(3.d), new NumVal(4.d)),
                new VectorVal(new NumVal(5.d), new NumVal(6.d))
        ).toMatrix();
        assertEquals(3,matrix.getRowDimension());
        assertEquals(2, matrix.getColumnDimension());
    }

    @Test
    public void singleRowMatrix(){
        Matrix matrix = new VectorVal(new VectorVal(new NumVal(1.d), new NumVal(1.d))).toMatrix();
        assertEquals(1, matrix.getRowDimension());
        assertEquals(2, matrix.getColumnDimension());
    }

    @Test
    public void fromMatrix(){
        double[][] doubles = {
                {1, 2, 3},
                {4, 5, 6}
        };
        VectorVal vectorVal = VectorVal.fromMatrix(new Matrix(doubles));

        Matrix expected = new Matrix(doubles);
        Matrix actual = vectorVal.toMatrix();
        assertTrue(Arrays.deepEquals(expected.getArray(), actual.getArray()));
    }
}
