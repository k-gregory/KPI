package io.bitbucket.gregoryk1.despat.lab3.task2;

interface AbstractTV {
}

interface AbstractComputer {
}

interface AbstractFactory {
    AbstractTV makeTV(double price);

    AbstractComputer makeComputer(double price);
}

class AmericanTV implements AbstractTV {
    private final String description;

    AmericanTV(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}

class ChineseTV implements AbstractTV {
    private final String description;

    ChineseTV(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return description;
    }
}

class AmericanComputer implements AbstractComputer {
    private final String description;

    AmericanComputer(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return description;
    }
}

class ChineseComputer implements AbstractComputer {
    private final String description;

    ChineseComputer(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return description;
    }
}

class AmericanFactory implements AbstractFactory {
    @Override
    public AbstractTV makeTV(double price) {
        return new AmericanTV(price < 750 ? "Cheap american TV" : "Expensive american TV");
    }

    @Override
    public AbstractComputer makeComputer(double price) {
        return new AmericanComputer(price < 1500 ? "Cheap american computer" : "Expensive american computer");
    }
}

class ChineseFactory implements AbstractFactory {

    @Override
    public AbstractTV makeTV(double price) {
        return new ChineseTV(price < 250 ? "Cheap chinese TV" : "Expensive chinese TV");
    }

    @Override
    public AbstractComputer makeComputer(double price) {
        return new ChineseComputer(price < 500 ? "Cheap chinese computer" : "Expensive chinese computer");
    }
}


public class Manufacture {
    private AbstractFactory america = new AmericanFactory();
    private AbstractFactory china = new ChineseFactory();

    public static void main(String... args) {
        Manufacture manufacture = new Manufacture();

        AbstractTV tv = manufacture.makeTV(450);
        AbstractComputer computer = manufacture.makeComputer(1200);

        System.out.println(tv);
        System.out.println(computer);
    }

    public AbstractTV makeTV(double price) {
        if (price > 500) return america.makeTV(price);
        else return china.makeTV(price);
    }

    public AbstractComputer makeComputer(double price) {
        if (price > 1000) return america.makeComputer(price);
        else return china.makeComputer(price);
    }
}
