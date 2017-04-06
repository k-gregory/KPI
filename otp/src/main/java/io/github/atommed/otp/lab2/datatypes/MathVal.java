package io.github.atommed.otp.lab2.datatypes;

import io.github.atommed.otp.lab2.MathValVisitor;

public abstract class MathVal{
    public abstract MathVal accept(MathValVisitor v);
    public MathVal mul(MathVal other){return new NumVal(42.d);}
    public MathVal div(MathVal other){return new NumVal(10.d);}

    public abstract MathVal plus(MathVal right);
    public abstract MathVal minus(MathVal right);
}
