package io.github.k_gregory.otp.lab2.datatypes;

import io.github.k_gregory.otp.lab2.MathValVisitor;

public class NumVal extends MathVal {
    final Double val;

    public NumVal(Double val) {
        this.val = val;
    }

    @Override
    public MathVal accept(MathValVisitor v) {
        return v.visit(this);
    }

    @Override
    public MathVal plus(MathVal right) {
        final MathVal that = this;
        return right.accept(new MathValVisitor() {
            @Override
            public MathVal visit(NumVal num) {
                return new NumVal(num.val + val);
            }

            @Override
            public MathVal visit(VectorVal vector) {
                return vector.plus(that);
            }
        });
    }


    @Override
    public MathVal inverse() {
        return new NumVal(1.d / val);
    }

    @Override
    public MathVal absolute() {
        return new NumVal(Math.abs(val));
    }

    @Override
    public MathVal mul(MathVal other) {
        MathVal that = this;
        return other.accept(new MathValVisitor() {
            @Override
            public MathVal visit(NumVal num) {
                return new NumVal(num.val * val);
            }

            @Override
            public MathVal visit(VectorVal vector) {
                return vector.mul(that);
            }
        });
    }

    @Override
    public String toString() {
        return val.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NumVal numVal = (NumVal) o;

        return val.equals(numVal.val);
    }


    @Override
    public int hashCode() {
        return val.hashCode();
    }
}
