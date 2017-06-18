package io.bitbucket.gregoryk1.despat.lab3.task1;

/**
 * Created by grego on 07.04.2017.
 */
public interface GiftVisitor {
    void visit(CarModel c);

    void visit(Doll d);

    void visit(SweetRoll s);

    default void visit(GiftPack pack) {
        for (Gift g : pack.getGifts())
            g.accept(this);
    }
}
