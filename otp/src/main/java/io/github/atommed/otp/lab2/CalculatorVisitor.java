package io.github.atommed.otp.lab2;

import io.github.atommed.otp.lab2.datatypes.MathVal;
import io.github.atommed.otp.lab2.datatypes.NumVal;
import io.github.atommed.otp.lab2.datatypes.VectorVal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by grego on 31.03.2017.
 */
class CalculatorVisitor extends MatrixBaseVisitor<MathVal> {
    private Map<String, MathVal> memory = new HashMap<>();

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
        else
            return left.div(right);
    }

    @Override
    public MathVal visitAddSub(MatrixParser.AddSubContext ctx) {
        MathVal left = visit(ctx.expr(0));
        MathVal right = visit(ctx.expr(1));
        if (ctx.op.getType() == MatrixParser.PLUS)
            return left.plus(right);
        else
            return left.minus(right);
    }


    @Override
    public MathVal visitAssign(MatrixParser.AssignContext ctx) {
        MathVal newValue = visit(ctx.expr());
        memory.put(ctx.ID().getText(), newValue);
        return newValue;
    }

    @Override
    public MathVal visitNumLiteral(MatrixParser.NumLiteralContext ctx) {
        return new NumVal(Double.valueOf(ctx.NUMBER().getText()));
    }

    @Override
    public MathVal visitVariable(MatrixParser.VariableContext ctx) {
        return memory.get(ctx.ID().getText());
    }

    @Override
    public MathVal visitStatement(MatrixParser.StatementContext ctx) {
        return super.visitStatement(ctx);
    }

    @Override
    public MathVal visitGroup(MatrixParser.GroupContext ctx) {
        return visit(ctx.expr());
    }
}
