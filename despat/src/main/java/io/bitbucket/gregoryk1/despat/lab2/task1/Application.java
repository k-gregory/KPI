package io.bitbucket.gregoryk1.despat.lab2.task1;

import java.time.LocalDate;
import java.time.Period;

/**
 * Created by grego on 01.04.2017.
 */
public class Application {
    public static void main(String... args) {
        ProfilableRegistratorProxy registrator = new ProfilableRegistratorProxy();
        registrator.register(new CriminalForm("Vasya", "alcohol driving", LocalDate.now(), Period.ofDays(5)));
        registrator.register(new CriminalForm("Vova", "tunijadstvo", LocalDate.now(), Period.ofYears(5)));
    }
}
