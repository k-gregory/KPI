package io.bitbucket.gregoryk1.despat.lab3.task1;

/**
 * Created by grego on 08.04.2017.
 */
public class MaleGiftVisitor extends BaseGiftVisitor {
    public MaleGiftVisitor(String name, int calmLevel) {
        super(name, calmLevel);
    }

    @Override
    public void visit(CarModel c) {
        if (!isCalm()) {
            System.out.println(name + " doesn't like " + c + ", he crushes it");
            c.crush();
        }
        c.move();
    }

    @Override
    public void visit(Doll d) {
        say(d + " is still better than a sweetroll");
    }
}
