package io.bitbucket.gregoryk1.despat.lab3.task1;

/**
 * Created by grego on 07.04.2017.
 */
public class Doll implements Gift {
    private String name = "unnamed";
    private String dressName = "no dress";

    public void setDress(String dress){
        this.dressName = dress;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "doll "+ name + " in " + dressName;
    }

    @Override
    public Gift clone() throws CloneNotSupportedException {
        return (Doll) super.clone();
    }

    @Override
    public void accept(GiftVisitor v) {
        v.visit(this);
    }
}
