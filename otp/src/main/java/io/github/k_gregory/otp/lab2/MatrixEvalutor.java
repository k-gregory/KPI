package io.github.k_gregory.otp.lab2;

import io.github.k_gregory.otp.lab2.datatypes.MathVal;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MatrixEvalutor {
    private final Map<String, MathVal> memory;

    public MatrixEvalutor(Map<String, MathVal> memory) {
        this.memory = memory;
    }

    public MatrixEvalutor() {
        this(new HashMap<>());
    }

    public static void main(String... args) {
        MatrixEvalutor evalutor = new MatrixEvalutor();
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\\R+");
        while (scanner.hasNext()) {
            try {
                System.out.println(evalutor.evalute(scanner.next()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void clearMemory() {
        memory.clear();
    }

    public MathVal evalute(String expression) {
        ANTLRInputStream inputStream = new ANTLRInputStream(expression);
        MatrixLexer lexer = new MatrixLexer(inputStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        MatrixParser parser = new MatrixParser(tokenStream);
        CalculatorVisitor visitor = new CalculatorVisitor(memory);
        MathVal result = visitor.visit(parser.defProg());
        if (parser.getNumberOfSyntaxErrors() != 0) throw new RuntimeException("Bad source code!");
        return result;
    }
}
