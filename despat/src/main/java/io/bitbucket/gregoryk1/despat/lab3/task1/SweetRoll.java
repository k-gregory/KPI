package io.bitbucket.gregoryk1.despat.lab3.task1;

public class SweetRoll implements Gift {
    private String taste;

    public SweetRoll(String taste) {
        this.taste = taste;
    }

    @Override
    public String toString() {
        return "sweet roll with " + taste;
    }

    public void checkTaste() {
        System.out.println(toString() + " tastes delicious");
    }

    @Override
    public Gift clone() throws CloneNotSupportedException {
        return (SweetRoll) super.clone();
    }

    @Override
    public void accept(GiftVisitor v) {
        v.visit(this);
    }
}
