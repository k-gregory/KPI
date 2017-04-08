package io.bitbucket.gregoryk1.despat.lab1.task2;

import io.codearte.jfairy.Fairy;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

interface MoneyCollector{
    double collectMoney();
    String getName();
    void display(int depth);

    default void printCollects(int depth){
        String money = NumberFormat.getCurrencyInstance(Locale.US).format(collectMoney());
        Util.print(depth, getName() + " collects " + money);
    }
}

class Util {
    public static void print(int prefixLength, String content){
        char[] chars = new char[prefixLength];
        Arrays.fill(chars, '.');
        System.out.println(new String(chars)+content);
    }

    private static double randomInRange(Random r, double min, double max){
        return min + r.nextDouble() * (max - min);
    }

    public static MoneyCollector createComposite(String role, Fairy fairy, MoneyCollector... slaves){
        return new CompositeMoneyCollector(
                role + " " + fairy.person().getFullName(),slaves
        );
    }

    public static String fullName(Fairy fairy){return fairy.person().getFullName();}

    public static MoneyCollector[] createParent(double minProfit, double maxProfit, int length, Fairy fairy){
        MoneyCollector[] collectors = new MoneyCollector[length];
        Random rng = new Random();
        for (int i = 0; i < length; i++) {
            double profit = randomInRange(rng, minProfit, maxProfit);
            collectors[i] = new LeafMoneyCollector("Parent" + " " + fairy.person().getFullName(), profit);
        }
        return collectors;
    }
}

class LeafMoneyCollector implements MoneyCollector{
    private final String name;
    private final double givesMoney;

    public LeafMoneyCollector(String name, double givesMoney) {
        this.name = name;
        this.givesMoney = givesMoney;
    }

    @Override
    public double collectMoney() {
        return givesMoney;
    }

    @Override
    public void display(int depth) {
        printCollects(depth);
    }

    @Override
    public String getName() {
        return name;
    }
}

class CompositeMoneyCollector implements MoneyCollector{
    private final MoneyCollector[] moneyCollectors;
    private final String name;
    public CompositeMoneyCollector(String name, MoneyCollector... moneyCollectors){
        this.name = name;
        this.moneyCollectors = moneyCollectors;
    }

    @Override
    public double collectMoney() {
        return  Arrays.stream(moneyCollectors)
                .map(MoneyCollector::collectMoney).reduce(0.d, Double::sum);
    }

    @Override
    public void display(int depth) {
        printCollects(depth);
        for(MoneyCollector mc: moneyCollectors)
            mc.display(depth + 6);
    }

    @Override
    public String getName() {
        return name;
    }
}



public class Task2 {
    public static void main(String... args){
        Fairy fairy = Fairy.create();
        MoneyCollector[] parents2b = Util.createParent(30, 60, 5, fairy);
        MoneyCollector[] parents3a = Util.createParent(60, 80, 3, fairy);
        MoneyCollector[] parents4a = Util.createParent(45, 60, 3, fairy);
        MoneyCollector[] parents6a = Util.createParent(20, 80, 8, fairy);
        MoneyCollector[] parents6b = Util.createParent(50, 60, 4, fairy);
        MoneyCollector[] parents9a = Util.createParent(10, 120, 8, fairy);

        MoneyCollector master2b = Util.createComposite("2B master", fairy, parents2b);
        MoneyCollector master3a = Util.createComposite("3A master", fairy, parents3a);
        MoneyCollector master4a = Util.createComposite("4A master", fairy, parents4a);
        MoneyCollector master6a = Util.createComposite("6A master", fairy, parents6a);
        MoneyCollector master6b = Util.createComposite("6B master", fairy, parents6b);
        MoneyCollector master9a = Util.createComposite("9A master", fairy, parents9a);

        MoneyCollector youngMaster = Util.createComposite("Youngest master", fairy, master2b, master3a, master4a);
        MoneyCollector middleMaster = Util.createComposite("Middle master", fairy, master6a, master6b);
        MoneyCollector oldMaster = Util.createComposite("Old master", fairy, master9a);

        MoneyCollector headmasta = Util.createComposite("HEADMASTA", fairy, youngMaster, middleMaster, oldMaster);
        headmasta.display(0);
    }
}
