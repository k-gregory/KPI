package io.github.k_gregory.otp.lab2;

import Jama.Matrix;
import io.github.k_gregory.otp.lab1.ParseException;
import io.github.k_gregory.otp.lab2.datatypes.MathVal;
import io.github.k_gregory.otp.lab2.datatypes.NumVal;
import io.github.k_gregory.otp.lab2.datatypes.VectorVal;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class EvalutionsTests {

    private MathVal calculate(String expression) {
        return new MatrixEvalutor().evalute(expression);
    }

    private void assertCalculate(MathVal expected, String expression) {
        assertEquals(expected, calculate(expression));
    }

    private void assertNumVal(Double v, String expression) {
        assertCalculate(new NumVal(v), expression);
    }

    private void assertNumVecVal(String expression, Double... doubles) {
        assertCalculate(new VectorVal(Arrays.stream(doubles).map(NumVal::new).toArray(NumVal[]::new)), expression);
    }

    @Test(expected = ParseException.class)
    public void bracketsMatch() {
        calculate("1+[[13]");
    }

    @Test(expected = ParseException.class)
    public void noExtraParsed() {
        calculate("1+2 ololo some");
    }

    @Test
    public void memoryDurability() {
        MatrixEvalutor evalutor = new MatrixEvalutor();
        evalutor.evalute("c=42");
        assertEquals(new NumVal(2.d), evalutor.evalute("c-40"));
    }

    @Test
    public void spacesAreAllowed() {
        assertNumVal(2.d, "1 + 1");
        assertNumVal(5.d, " a = 1 ; 4 + a ;");
    }

    @Test
    public void simpleAlgebra() {
        assertNumVal(4.d, "2+2;");
        assertNumVal(1.d, "1+0");
        assertNumVal(1.d, "0+1");
        assertNumVal(5.d, "10/2");
        assertNumVal(3.d, "6-(2+2)/2-1;");

        assertNumVal(5.d, "|-5|");
    }

    @Test
    public void vectorAlgebra() {
        assertNumVecVal("[1]+[0]", 1.d);
        assertNumVecVal("[0]+[1]", 1.d);
        assertNumVecVal("[-5,2,15]x[6,1,-2]", -19.d, 80.d, -17.d);
    }

    @Test
    public void assignment() {
        assertNumVal(4.d, "a=4");
        assertNumVal(5.d, "a=5;a;");
        assertNumVal(32.d, "a=3;b=7;c=a+b;c/5+c*3");
        assertNumVecVal("[0]", 0.d);
        assertNumVecVal("a=[1,2];b=[0,-4];c=a+b", 1.d, -2.d);
    }

    @Test
    public void vectorAssignment() {
        assertNumVecVal("[1]", 1.d);
        assertNumVecVal("[1,2]", 1.d, 2.d);
        assertNumVecVal("[-1]", -1.d);
    }

    @Test
    public void vectorScalar() {
        assertNumVecVal("[1,2,3]*2", 2.d, 4.d, 6.d);
        assertNumVecVal("1/10*[10,20,30]", 1.d, 2.d, 3.d);
        assertNumVecVal("1/10*[10,-20,30]", 1.d, -2.d, 3.d);
    }

    @Test
    public void nestedVectors() {
        assertCalculate(new VectorVal(new VectorVal(new NumVal(1.d))), "[[1]]");
        assertNumVal(6.5, "[[0.5]]*[[13]]");
        assertCalculate(new VectorVal(new VectorVal(new NumVal(-4.d))), "[[1]]+[[-5]]");
        assertNumVal(40.d, "[([1,2]*[2,3]),4]*[3,4]");
        assertNumVecVal("[1,2,3]x[1,rank([[1,2],[3,4]]),1]", -4.d, 2.d, 0.d);
    }

    @Test
    public void matrixTranspose() {
        Matrix matrix = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        assertCalculate(VectorVal.fromMatrix(matrix), "[[1,4],[2,5],[3,6]]^t");
        assertCalculate(VectorVal.fromMatrix(matrix).transpose(), "[[1,4],[2,5],[3,6]]");
    }

    @Test
    public void matrixInverse() {
        assertCalculate(VectorVal.fromMatrix(new Matrix(new double[][]{
                {1, 2},
                {3, 4}
        })), "[[-2,1],[3/2,-1/2]]^1");
    }

    @Test
    public void matrixMult() {
        assertCalculate(VectorVal.fromMatrix(new Matrix(new double[][]{
                {58, 64},
                {139, 154}
        })), "[[1,2,3],[4,5,6]].[[7,8],[9,10],[11,12]]");
    }

    @Test
    public void matrixRank() {
        assertNumVal(2.d, "rank([[1,2,3],[4,5,6],[7,8,9]])");
    }

    @Test
    public void matrixDet() {
        assertNumVal(-48.d, "det([[1,2,3],[4,9,6],[7,8,9]])");
    }

    @Test
    public void variant6() {
        assertCalculate(new VectorVal(
                new VectorVal(new NumVal(0.5), new NumVal(1.d)),
                new VectorVal(new NumVal(1.5), new NumVal(2.d))
        ), "[[1,2],[3,4]]/rank([[1,2],[3,4]])");
    }
}
