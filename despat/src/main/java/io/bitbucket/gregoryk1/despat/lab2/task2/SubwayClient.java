package io.bitbucket.gregoryk1.despat.lab2.task2;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

interface TokenPayer {
    boolean tryPayToken();
}

interface MonthSubscriptionPayer {
    boolean checkMonthSubscription();
}

interface PassSubscriptionPayer {
    boolean tryUsePassSubscription();
}

public class SubwayClient {
    private static PaymentMethod noPaymentResult = () -> {
        System.out.println("Tried to pass without payment");
        return false;
    };
    private int tokensLeft = 0;
    private PaymentMethod tokensChecker = new TokenPaymentAdapter(this);
    private PaymentMethod monthSubscription;
    private PaymentMethod passesSubscription;
    private String name;

    public SubwayClient(String name) {
        this.name = name;
    }

    public void buyTokens(int number) {
        if (number < 1) throw new IllegalArgumentException("Can't buy less than 1 token");
        System.out.println(name + " buys " + number + " tokens");
        tokensLeft += number;
    }

    public void buyMonthSubscription(LocalDate acquired) {
        System.out.println(name + " buys month subscription");
        this.monthSubscription = new MonthSubscriptionAdapter(acquired);
    }

    public void buyPassesSubscription(int count) {
        System.out.println(name + " buys subscription for " + count + " times");
        this.passesSubscription = new PassesSubscriptionAdapter(count);
    }

    public PaymentMethod tokenPayment() {
        return tokensChecker;
    }

    public PaymentMethod passSubscriptionPayment() {
        return passesSubscription != null ? passesSubscription : noPaymentResult;
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

    private static class TokenPayment implements TokenPayer {
        private SubwayClient client;

        private TokenPayment(SubwayClient client) {
            this.client = client;
        }

        @Override
        public boolean tryPayToken() {
            System.out.println("Payment by token requested");
            if (client.tokensLeft > 0) {
                client.tokensLeft--;
                return true;
            } else {
                System.out.println("No tokens left. Maybe u buy some?");
                return false;
            }
        }
    }

    private static class TokenPaymentAdapter extends TokenPayment implements PaymentMethod {
        public TokenPaymentAdapter(SubwayClient c) {
            super(c);
        }

        @Override
        public boolean tryPass() {
            return tryPayToken();
        }
    }

    private static class MonthSubscription implements MonthSubscriptionPayer {
        private LocalDate acquired;

        private MonthSubscription(LocalDate acquired) {
            this.acquired = acquired;
        }

        @Override
        public boolean checkMonthSubscription() {
            System.out.println("Trying to pay by month subscription");
            if (ChronoUnit.MONTHS.between(acquired, LocalDate.now()) < 1) {
                return true;
            } else {
                System.out.println("Your month subscription finished, by new one, NOW!");
                return false;
            }
        }
    }

    private static class MonthSubscriptionAdapter extends MonthSubscription implements PaymentMethod {
        public MonthSubscriptionAdapter(LocalDate ack) {
            super(ack);
        }

        @Override
        public boolean tryPass() {
            return checkMonthSubscription();
        }
    }

    private static class PassesSubscription implements PassSubscriptionPayer {
        private int passesLeft;

        public PassesSubscription(int passesLeft) {
            if (passesLeft < 1) throw new IllegalArgumentException("passesLeft must be positive");
            this.passesLeft = passesLeft;
        }

        @Override
        public boolean tryUsePassSubscription() {
            System.out.println("Payment by passes subscr. requested");
            if (passesLeft > 0) {
                passesLeft--;
                return true;
            } else {
                System.out.println("No passes left in subscription, RENEW IT NOWWW!");
                return false;
            }
        }
    }

    private static class PassesSubscriptionAdapter extends PassesSubscription implements PaymentMethod {
        public PassesSubscriptionAdapter(int c) {
            super(c);
        }

        @Override
        public boolean tryPass() {
            return tryUsePassSubscription();
        }
    }
}
