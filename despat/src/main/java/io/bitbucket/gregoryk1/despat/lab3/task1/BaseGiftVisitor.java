package io.bitbucket.gregoryk1.despat.lab3.task1;

import java.util.Random;

public abstract class BaseGiftVisitor implements GiftVisitor {
    protected static final Random rng = new Random();
    protected final String name;
    private final int calmLevel;

    protected BaseGiftVisitor(String name, int calmLevel) {
        this.name = name;
        this.calmLevel = calmLevel;
    }

    public String getName() {
        return name;
    }

    protected void say(String what) {
        System.out.println(name + " says: " + what);
    }

    protected boolean isCalm() {
        return rng.nextInt(calmLevel) != 0;
    }

    public void logVisit(Gift g) {
        System.out.println(name + " gets a gift: " + g);
    }

    @Override
    public void visit(SweetRoll s) {
        say("Thay make me to eat " + s);
        if (isCalm()) {
            s.checkTaste();
        } else say("No, i'm not going to eat " + s);
    }
}
