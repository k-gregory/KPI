package io.github.k_gregory.otp.lab2;

import io.github.k_gregory.otp.lab2.datatypes.MathVal;
import io.github.k_gregory.otp.lab2.datatypes.NumVal;
import io.github.k_gregory.otp.lab2.datatypes.VectorVal;

public abstract class MathValVisitor {
    public MathVal visit(NumVal num) {
        throw new UnsupportedOperationException();
    }

    public MathVal visit(VectorVal vector) {
        throw new UnsupportedOperationException();
    }
}
