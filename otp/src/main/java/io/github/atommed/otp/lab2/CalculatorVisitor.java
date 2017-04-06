package io.github.atommed.otp.lab2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.Collectors;

interface MathValVisitor{
    void visit(NumVal num);
    void visit(VectorVal vector);
}

abstract class MathVal{
    abstract void accept(MathValVisitor v);
}

class NumVal extends MathVal{ 
    private final Double val;
    public NumVal(Double val){
	this.val = val;
    }

    @Override
    public void accept(MathValVisitor v){
	v.visit(this);
    }

    @Override
    public String toString(){return val.toString();}
}

class VectorVal extends MathVal{ 
    private final MathVal[] elements;
    public VectorVal(MathVal... elements){
	this.elements = Arrays.copyOf(elements, elements.length);
    }
    @Override
    public void accept(MathValVisitor v){
	v.visit(this);
    }

    @Override
    public String toString(){
	return "["+Arrays
	    .stream(elements)
	    .map(MathVal::toString)
	    .collect(Collectors.joining(","))
	    +"]";
    }	
}

/**
 * Created by grego on 31.03.2017.
 */
class CalculatorVisitor extends MatrixBaseVisitor<Double> {
    private Map<String, Double> memory = new HashMap<>();

    @Override
    public Double visitDefProg(MatrixParser.DefProgContext ctx) {
        List<MatrixParser.StatementContext> statements = ctx.statement();
        for (int i = 0; i < statements.size() - 1; i++)
            visit(statements.get(i));
        return visit(statements.get(statements.size() - 1));
    }

    @Override
    public Double visitStatement(MatrixParser.StatementContext ctx) {
        return super.visitStatement(ctx);
    }

    @Override
    public Double visitGroup(MatrixParser.GroupContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Double visitVariable(MatrixParser.VariableContext ctx) {
        return memory.get(ctx.ID().getText());
    }

    @Override
    public Double visitMulDiv(MatrixParser.MulDivContext ctx) {
        Double left = visit(ctx.expr(0));
        Double right = visit(ctx.expr(1));
        if (ctx.op.getType() == MatrixParser.MULTIPLY)
            return left * right;
        else
            return left / right;
    }

    @Override
    public Double visitAddSub(MatrixParser.AddSubContext ctx) {
        Double left = visit(ctx.expr(0));
        Double right = visit(ctx.expr(1));
        if (ctx.op.getType() == MatrixParser.PLUS)
            return left + right;
        else
            return left - right;
    }

    @Override
    public Double visitFuncall(MatrixParser.FuncallContext ctx) {
        return super.visitFuncall(ctx);
    }

    @Override
    public Double visitLiteralValue(MatrixParser.LiteralValueContext ctx) {
        return Double.valueOf(ctx.literal().NUMBER().getText());
    }

    @Override
    public Double visitInverse(MatrixParser.InverseContext ctx) {
        return super.visitInverse(ctx);
    }

    @Override
    public Double visitNegate(MatrixParser.NegateContext ctx) {
        return super.visitNegate(ctx);
    }

    @Override
    public Double visitModule(MatrixParser.ModuleContext ctx) {
        return super.visitModule(ctx);
    }

    @Override
    public Double visitTranspose(MatrixParser.TransposeContext ctx) {
        return super.visitTranspose(ctx);
    }

    @Override
    public Double visitLiteral(MatrixParser.LiteralContext ctx) {
        return Double.valueOf(ctx.NUMBER().getText());
    }

    @Override
    public Double visitAssign(MatrixParser.AssignContext ctx) {
        Double newValue = visit(ctx.expr());
        memory.put(ctx.ID().getText(), newValue);
        return newValue;
    }

    @Override
    public Double visitVector(MatrixParser.VectorContext ctx) {
        return super.visitVector(ctx);
    }
}
