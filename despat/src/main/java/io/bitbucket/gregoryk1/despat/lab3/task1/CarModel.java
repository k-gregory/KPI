package io.bitbucket.gregoryk1.despat.lab3.task1;

/**
 * Created by grego on 07.04.2017.
 */
public class CarModel implements Gift {
    private boolean isBroken;
    private final String name;

    public CarModel(String name) {
        this.name = name;
    }

    public void crush(){isBroken = true;}
    public void move(){
        System.out.println(isBroken ? "Broken car can't move =\\" : "Car " + name +" model moves!");
    }

    @Override
    public String toString() {
        return name + " car model";
    }

    @Override
    public Gift clone() throws CloneNotSupportedException {
        return (CarModel) super.clone();
    }

    @Override
    public void accept(GiftVisitor v) {
        v.visit(this);
    }
}
