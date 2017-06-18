package io.bitbucket.gregoryk1.despat.lab4.task2;

interface VisaAcquireVisitor {
    void visit(Sluzbovez sluzbovez);

    void visit(PrivatnyPidpriemez pidpriemez);

    void visit(Child child);
}

interface VisaAcquirable {
    void accept(VisaAcquireVisitor visaAcquireVisitor);
}

class VisaCenter implements VisaAcquireVisitor {
    @Override
    public void visit(Sluzbovez sluzbovez) {
        sluzbovez.showPassport();
        sluzbovez.showCertificate();
        System.out.println("Sluzbovec got his visa");
    }

    @Override
    public void visit(PrivatnyPidpriemez pidpriemez) {
        pidpriemez.showPassport();
        pidpriemez.reportTaxation();

        System.out.println("Pidpriemez got his visa");
    }

    @Override
    public void visit(Child child) {
        child.showBirthCertificate();

        System.out.println("Child got his visa");
    }
}

class Sluzbovez implements VisaAcquirable {
    public void showCertificate() {
        System.out.println("Sluzbovec shows their certificate");
    }

    public void showPassport() {
        System.out.println("Sluzbovec shows their passport");
    }

    @Override
    public void accept(VisaAcquireVisitor visaAcquireVisitor) {
        visaAcquireVisitor.visit(this);
    }
}

class PrivatnyPidpriemez implements VisaAcquirable {
    public void showPassport() {
        System.out.println("Pidpriemez shows their passport");
    }

    public void reportTaxation() {
        System.out.println("Pidpriemez reports about paying taxation");
    }

    @Override
    public void accept(VisaAcquireVisitor visaAcquireVisitor) {
        visaAcquireVisitor.visit(this);
    }
}

class Child implements VisaAcquirable {
    public void showBirthCertificate() {
        System.out.println("Child shows their birth certificate");
    }

    @Override
    public void accept(VisaAcquireVisitor visaAcquireVisitor) {
        visaAcquireVisitor.visit(this);
    }
}


public class Application {
    public static void main(String... args) {
        VisaAcquireVisitor visaCenter = new VisaCenter();

        VisaAcquirable sluzbovez = new Sluzbovez();
        VisaAcquirable pidpriemez = new PrivatnyPidpriemez();
        VisaAcquirable child = new Child();

        sluzbovez.accept(visaCenter);
        System.out.println();
        pidpriemez.accept(visaCenter);
        System.out.println();
        child.accept(visaCenter);
    }
}
