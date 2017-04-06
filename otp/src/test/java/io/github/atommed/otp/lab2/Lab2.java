package io.github.atommed.otp.lab2;

import io.github.atommed.otp.lab2.datatypes.MathVal;
import io.github.atommed.otp.lab2.datatypes.NumVal;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Lab2 {

    private MathVal calculate(String expression){
        ANTLRInputStream inputStream = new ANTLRInputStream(expression);
        MatrixLexer lexer = new MatrixLexer(inputStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        MatrixParser parser = new MatrixParser(tokenStream);
        CalculatorVisitor visitor = new CalculatorVisitor();
        MathVal result = visitor.visit(parser.defProg());
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        return result;
    }

    private void assertCalculate(MathVal expected, String expression){
        assertEquals(expected, calculate(expression));
    }
    private void assertNumVal(Double v, String expression){assertCalculate(new NumVal(v), expression);}


    @Test
    public void simpleAlgebra(){
        assertNumVal(4.d,"2+2;");
        assertNumVal(3.d, "6-(2+2)/2-1;");
    }
    @Test
    public void assignment(){
        assertNumVal(4.d,"a=4");
        assertNumVal(5.d,"a=5;a;");
        assertNumVal(10.d, "a=3;b=7;c=a+b");
        assertNumVal(32.d, "a=3;b=7;c=a+b;c/5+c*3");
    }
}
