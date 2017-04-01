package io.bitbucket.gregoryk1.despat.lab2.task1;

import java.time.temporal.ChronoUnit;

/**
 * Created by grego on 01.04.2017.
 */
public class ProfilableRegistratorProxy implements CriminalRegistrator {
    private BasicCriminalRegistrator registrator = new BasicCriminalRegistrator();

    @Override
    public void register(CriminalForm form) {
        long days = ChronoUnit.DAYS.between(form.capturedDate, form.capturedDate.plus(form.capturePeriod));
        if(days > 15)
            System.out.println("Creating a profile for " + form.fullName);
        registrator.register(form);
    }
}
