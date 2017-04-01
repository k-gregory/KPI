package io.bitbucket.gregoryk1.despat.lab2.task2;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SubwayClient {
    private int tokensLeft = 0;
    private PaymentMethod tokensChecker = new TokenPayment(this);
    private PaymentMethod monthSubscription;
    private PaymentMethod passesSubscription;
    private String name;

    private static PaymentMethod noPaymentResult = () -> {
        System.out.println("Tried to pass without payment");
        return false;
    };

    public SubwayClient(String name) {
        this.name = name;
    }

    public void buyTokens(int number){
        if(number < 1) throw new IllegalArgumentException("Can't buy less than 1 token");
        System.out.println(name + " buys " + number + " tokens");
        tokensLeft += number;
    }

    public void buyMonthSubscription(LocalDate acquired){
        System.out.println(name + " buys month subscription");
        this.monthSubscription = new MonthSubscription(acquired);
    }

    public void buyPassesSubscription(int count){
        System.out.println(name + " buys subscription for " + count + " times");
        this.passesSubscription = new PassesSubscription(count);
    }

    public PaymentMethod tokenPayment() {
        return tokensChecker;
    }

    public PaymentMethod passSubscriptionPayment() {
        return passesSubscription != null? passesSubscription : noPaymentResult;
    }

    public PaymentMethod monthSubscriptionPayment() {
        return monthSubscription != null ? monthSubscription : noPaymentResult;
    }

    public void setMonthSubscription(PaymentMethod monthSubscription) {
        this.monthSubscription = monthSubscription;
    }

    public String getName() {
        return name;
    }

    private static class TokenPayment implements PaymentMethod {
        private SubwayClient client;

        private TokenPayment(SubwayClient client) {
            this.client = client;
        }

        @Override
        public boolean tryPass() {
            System.out.println("Payment by token requested");
            if(client.tokensLeft > 0){
                client.tokensLeft--;
                return true;
            } else {
                System.out.println("No tokens left. Maybe u buy some?");
                return false;
            }
        }
    }
    private static class MonthSubscription implements PaymentMethod {
        private LocalDate acquired;

        private MonthSubscription(LocalDate acquired) {
            this.acquired = acquired;
        }

        @Override
        public boolean tryPass() {
            System.out.println("Trying to pay by month subscription");
            if(ChronoUnit.MONTHS.between(acquired, LocalDate.now()) < 1){
                return true;
            } else {
                System.out.println("Your month subscription finished, by new one, NOW!");
                return false;
            }
        }
    }
    private static class PassesSubscription implements PaymentMethod {
        private int passesLeft;
        public PassesSubscription(int passesLeft) {
            if(passesLeft < 1) throw new IllegalArgumentException("passesLeft must be positive");
            this.passesLeft = passesLeft;
        }

        @Override
        public boolean tryPass() {
            System.out.println("Payment by passes subscr. requested");
            if(passesLeft > 0) {
                passesLeft--;
                return true;
            } else {
                System.out.println("No passes left in subscription, RENEW IT NOWWW!");
                return false;
            }
        }
    }
}
