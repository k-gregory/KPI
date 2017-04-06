package io.github.atommed.otp.lab2.datatypes;

import io.github.atommed.otp.lab2.MathValVisitor;

public class NumVal extends MathVal {
    private final Double val;
    public NumVal(Double val){
	this.val = val;
    }

    @Override
    public MathVal accept(MathValVisitor v){
        return v.visit(this);
    }

    @Override
    public MathVal plus(MathVal right) {
        return right.accept(new MathValVisitor() {
            @Override
            public MathVal visit(NumVal num) {
                return new NumVal(num.val+val);
            }
        });
    }

    @Override
    public MathVal minus(MathVal right) {
        return right.accept(new MathValVisitor() {
            @Override
            public MathVal visit(NumVal num) {
                return new NumVal(val - num.val);
            }
        });
    }

    @Override
    public MathVal mul(MathVal other) {
        return other.accept(new MathValVisitor() {
            @Override
            public MathVal visit(NumVal num) {
                return new NumVal(num.val * val);
            }
        });
    }

    @Override
    public MathVal div(MathVal other) {
        return other.accept(new MathValVisitor() {
            @Override
            public MathVal visit(NumVal num) {
                return new NumVal(val / num.val);
            }
        });
    }

    @Override
    public String toString(){return val.toString();}

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
