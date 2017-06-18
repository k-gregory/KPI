package io.github.k_gregory.otp.lab2;

import io.github.k_gregory.otp.lab2.datatypes.MathVal;
import io.github.k_gregory.otp.lab2.datatypes.MathValOperation;
import io.github.k_gregory.otp.lab2.datatypes.NumVal;
import io.github.k_gregory.otp.lab2.datatypes.VectorVal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CalculatorVisitor extends MatrixBaseVisitor<MathVal> {
    private static final Map<String, MathValOperation> operators;

    static {
        operators = new HashMap<>();
        operators.put("det", mv -> new NumVal(((VectorVal) mv).toMatrix().det()));
        operators.put("rank", mv -> new NumVal((double) ((VectorVal) mv).toMatrix().rank()));
    }

    private final Map<String, MathVal> memory;

    public CalculatorVisitor(Map<String, MathVal> memory) {
        this.memory = memory;
    }

    public CalculatorVisitor() {
        this(new HashMap<>());
    }

    @Override
    public MathVal visitInverse(MatrixParser.InverseContext ctx) {
        return visit(ctx.expr()).inverse();
    }

    @Override
    public MathVal visitModule(MatrixParser.ModuleContext ctx) {
        return visit(ctx.expr()).absolute();
    }

    @Override
    public MathVal visitTranspose(MatrixParser.TransposeContext ctx) {
        return visit(ctx.expr()).transpose();
    }

    @Override
    public MathVal visitDefProg(MatrixParser.DefProgContext ctx) {
        List<MatrixParser.StatementContext> statements = ctx.statement();
        for (int i = 0; i < statements.size() - 1; i++)
            visit(statements.get(i));
        return visit(statements.get(statements.size() - 1));
    }

    @Override
    public MathVal visitMulDiv(MatrixParser.MulDivContext ctx) {
        MathVal left = visit(ctx.expr(0));
        MathVal right = visit(ctx.expr(1));
        if (ctx.op.getType() == MatrixParser.MULTIPLY)
            return left.mul(right);
        else if (ctx.op.getType() == MatrixParser.DIVIDE)
            return left.div(right);
        else if (ctx.op.getType() == MatrixParser.VECMULT)
            return left.vMul(right);
        else if (ctx.op.getType() == MatrixParser.MATMULT)
            return left.matMul(right);
        else throw new UnsupportedOperationException();
    }

    @Override
    public MathVal visitAddSub(MatrixParser.AddSubContext ctx) {
        MathVal left = visit(ctx.expr(0));
        MathVal right = visit(ctx.expr(1));
        if (ctx.op.getType() == MatrixParser.PLUS) {
            return left.plus(right);
        } else {
            return left.minus(right);
        }
    }

    @Override
    public MathVal visitAssign(MatrixParser.AssignContext ctx) {
        MathVal newValue = visit(ctx.expr());
        memory.put(ctx.ID().getText(), newValue);
        return newValue;
    }

    @Override
    public MathVal visitNegate(MatrixParser.NegateContext ctx) {
        return visit(ctx.expr()).negate();
    }

    @Override
    public MathVal visitNumLiteral(MatrixParser.NumLiteralContext ctx) {
        return new NumVal(Double.valueOf(ctx.NUMBER().getText()));
    }

    @Override
    public MathVal visitFuncall(MatrixParser.FuncallContext ctx) {
        String funcName = ctx.ID().getText();
        MathValOperation mathValOperation = operators.get(funcName);
        if (mathValOperation == null) throw new UnsupportedOperationException("Not found op " + funcName);
        return mathValOperation.perform(visit(ctx.expr()));
    }

    @Override
    public MathVal visitVariable(MatrixParser.VariableContext ctx) {
        return memory.get(ctx.ID().getText());
    }

    @Override
    public MathVal visitGroup(MatrixParser.GroupContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public MathVal visitVectorLiteral(MatrixParser.VectorLiteralContext ctx) {
        return new VectorVal(ctx.vector().expr().stream().map(this::visit).toArray(MathVal[]::new));
    }
}
