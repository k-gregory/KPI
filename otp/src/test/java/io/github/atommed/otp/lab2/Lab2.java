package io.github.atommed.otp.lab2;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Lab2 {

    private Double calculate(String expression){
        ANTLRInputStream inputStream = new ANTLRInputStream(expression);
        MatrixLexer lexer = new MatrixLexer(inputStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        MatrixParser parser = new MatrixParser(tokenStream);
        CalculatorVisitor visitor = new CalculatorVisitor();
        Double result = visitor.visit(parser.defProg());
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        return result;
    }

    private void assertCalculate(Double expected, String expression){
        assertEquals(expected, calculate(expression));
    }


    @Test
    public void simpleAlgebra(){
        assertCalculate(4.d,"2+2;");
        assertCalculate(3.d, "6-(2+2)/2-1;");
    }
    @Test
    public void assignment(){
        assertCalculate(4.d,"a=4");
        assertCalculate(5.d,"a=5;a;");
        assertCalculate(10.d, "a=3;b=7;c=a+b");
        assertCalculate(32.d, "a=3;b=7;c=a+b;c/5+c*3");
    }
}
