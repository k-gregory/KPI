package io.bitbucket.gregoryk1.despat.lab5.task1;

interface FillingHandler {
    boolean fill(String what);

    void setNext(FillingHandler next);
}

class Cistern implements FillingHandler {
    private final String name;
    private int capacity;
    private FillingHandler next;

    Cistern(int capacity, String name) {
        if (capacity < 0) throw new IllegalArgumentException("capacity");
        this.capacity = capacity;
        this.name = name;
    }

    @Override
    public boolean fill(String what) {
        if (capacity == 0) {
            System.out.println(name + " is empty");
            return next.fill(what);
        } else {
            System.out.println(name + " fills " + what);
            capacity--;
            return true;
        }
    }

    @Override
    public void setNext(FillingHandler next) {
        this.next = next;
    }
}

class FillErrorMessage implements FillingHandler {
    @Override
    public boolean fill(String what) {
        System.out.println("Failed to fill " + what);
        return false;
    }

    @Override
    public void setNext(FillingHandler next) {
    }
}

class CoffeMachine {
    private FillingHandler fillingHandler;

    public CoffeMachine() {
        FillingHandler[] handlers = new FillingHandler[4];
        for (int i = 0; i < 3; i++)
            handlers[i] = new Cistern(2, "Water cistern #" + (i + 1));

        handlers[3] = new FillErrorMessage();

        for (int i = 0; i < 3; i++)
            handlers[i].setNext(handlers[i + 1]);

        fillingHandler = handlers[0];
    }

    public void makeCoffe() {
        System.out.println("Starting making coffe. Putting coffe and dust in the can");
        boolean filled = fillingHandler.fill("the can");
        if (filled) System.out.println("Coffe ready, enjoy the dust. Next!");
        else System.out.println("Unlucky, no coffe for you. Go away");
    }
}

public class Application {
    public static void main(String... args) {
        CoffeMachine coffeMachine = new CoffeMachine();
        for (int i = 0; i < 7; i++) {
            coffeMachine.makeCoffe();
            System.out.println();
        }
    }
}
