package io.github.atommed.otp.lab2.datatypes;

import io.github.atommed.otp.lab2.MathValVisitor;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class VectorVal extends MathVal {
    private final Stream<MathVal> elements;
    public VectorVal(Stream<MathVal> elements){
        this.elements = elements;
    }
    @Override
    public MathVal accept(MathValVisitor v){
return 	v.visit(this);
    }

    @Override
    public MathVal plus(MathVal right) {
        return right.accept(new MathValVisitor() {
            @Override
            public MathVal visit(VectorVal vector) {
               return super.visit(vector);
            }
        });
    }

    @Override
    public MathVal minus(MathVal right) {
        return null;
    }

    @Override
    public String toString(){
	return "["+elements.map(MathVal::toString).collect(Collectors.joining(",")) +"]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VectorVal vectorVal = (VectorVal) o;

        return elements.equals(vectorVal.elements);
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }
}
