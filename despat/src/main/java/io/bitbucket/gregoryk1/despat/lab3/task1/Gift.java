package io.bitbucket.gregoryk1.despat.lab3.task1;

/**
 * Created by grego on 07.04.2017.
 */
public interface Gift extends Cloneable {
    Gift clone() throws CloneNotSupportedException;

    void accept(GiftVisitor v);
}
