package io.github.k_gregory.otp.lab2.datatypes;

import io.github.k_gregory.otp.lab2.MathValVisitor;

public abstract class MathVal {
    public abstract MathVal accept(MathValVisitor v);

    public MathVal mul(MathVal right) {
        throw new UnsupportedOperationException();
    }

    public MathVal plus(MathVal right) {
        throw new UnsupportedOperationException();
    }

    public MathVal vMul(MathVal right) {
        throw new UnsupportedOperationException();
    }

    public MathVal transpose() {
        throw new UnsupportedOperationException();
    }

    public MathVal inverse() {
        throw new UnsupportedOperationException();
    }

    public MathVal absolute() {
        throw new UnsupportedOperationException();
    }

    public MathVal matMul(MathVal other) {
        throw new UnsupportedOperationException();
    }

    public MathVal negate() {
        return mul(new NumVal(-1.d));
    }

    public MathVal div(MathVal right) {
        return mul(right.inverse());
    }

    public MathVal minus(MathVal right) {
        return plus(right.negate());
    }
}
