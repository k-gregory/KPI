package io.bitbucket.gregoryk1.despat.lab5.task2;

abstract class ObservatoryColleague {
    public final String name;
    protected ObservatoryMediator mediator;
    protected String starName;
    public ObservatoryColleague(String name, ObservatoryMediator mediator) {
        this.name = name;
        this.mediator = mediator;
    }

    public void trackStar(String name) {
        this.starName = name;
    }

    public void sendStarData() {
        mediator.receiveStarData(starName, this);
    }
}

class ConcreteObservatory extends ObservatoryColleague {
    public ConcreteObservatory(ObservatoryMediator mediator, String name) {
        super(name, mediator);
    }

    @Override
    public void trackStar(String name) {
        System.out.println(this.name + " starts tracking " + name);
        super.trackStar(name);
    }

}

abstract class ObservatoryMediator {
    abstract public void receiveStarData(String name, ObservatoryColleague colleague);
}

class ConcreteObservatoryMediator extends ObservatoryMediator {
    private ObservatoryColleague observatory1 = new ConcreteObservatory(this, "Eastern Observatory");
    private ObservatoryColleague observatory2 = new ConcreteObservatory(this, "Western Observatory");

    @Override
    public void receiveStarData(String name, ObservatoryColleague colleague) {
        System.out.println("Received info about " + name + " from " + colleague.name);
    }


    public void watch(String star) {
        System.out.println("###########  We need to track " + star + " today ########");

        System.out.println("Can watch " + star + " from 00:00 to 12:00 from " + observatory1.name);
        observatory1.trackStar(star);
        observatory1.sendStarData();
        observatory1.sendStarData();
        System.out.println();

        System.out.println("Can watch " + star + " from 12:00 to 23:59 from " + observatory2.name);
        observatory2.trackStar(star);
        observatory2.sendStarData();
        observatory2.sendStarData();
        System.out.println();
    }
}

public class Application {
    public static void main(String... args) {
        ConcreteObservatoryMediator mediator = new ConcreteObservatoryMediator();
        mediator.watch("Sirius");
        mediator.watch("Aldebaran");
    }
}
