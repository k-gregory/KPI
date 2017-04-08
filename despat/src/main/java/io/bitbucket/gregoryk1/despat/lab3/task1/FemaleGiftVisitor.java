package io.bitbucket.gregoryk1.despat.lab3.task1;

/**
 * Created by grego on 08.04.2017.
 */
public class FemaleGiftVisitor extends BaseGiftVisitor{
    public FemaleGiftVisitor(String name, int calmLevel) {
        super(name, calmLevel);
    }

    @Override
    public void visit(CarModel c) {
        say("At least, "+c+" is not a sweetroll");
    }

    @Override
    public void visit(Doll d) {
        String[] dollNames = {"iDol", "Doll.js", "libDoll", "Ksyuha","Mashka", "Natashka"};
        String[] dresses = {"no dress", "pants and shirt", "skirt and pullower"};
        String newName = dollNames[rng.nextInt(dollNames.length)];
        String newDress = dresses[rng.nextInt(dresses.length)];
        d.setName(newName);
        d.setDress(newDress);
        System.out.println(name + " now has a " + d);
    }
}
