package io.bitbucket.gregoryk1.despat.lab2.task2;

import java.time.LocalDate;

public class Application {
    private static void lineSeparation(){
        for(int i = 0; i < 15; i++) System.out.print(' ');
        System.out.println();
    }
    public static void main(String... args){
        Terminal terminal = new Terminal();
        SubwayClient john = new SubwayClient("John");

        terminal.pass(john, john.tokenPayment());
        lineSeparation();

        john.buyTokens(1);
        terminal.pass(john, john.tokenPayment());
        terminal.pass(john, john.tokenPayment());
        lineSeparation();

        john.buyMonthSubscription(LocalDate.now());
        terminal.pass(john, john.monthSubscriptionPayment());
        terminal.pass(john, john.tokenPayment());
        lineSeparation();

        john.buyPassesSubscription(3);
        for(int i=0; i < 4; i++) terminal.pass(john, john.passSubscriptionPayment());
        lineSeparation();

        john.buyMonthSubscription(LocalDate.now().minusMonths(1).minusDays(1));
        terminal.pass(john, john.monthSubscriptionPayment());
    }
}
