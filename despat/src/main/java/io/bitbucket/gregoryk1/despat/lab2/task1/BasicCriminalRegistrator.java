package io.bitbucket.gregoryk1.despat.lab2.task1;

/**
 * Created by grego on 01.04.2017.
 */
public class BasicCriminalRegistrator implements CriminalRegistrator {
    @Override
    public void register(CriminalForm form){
        System.out.println("Registering " + form.fullName + " for " + form.captureReason + " to " + form.capturePeriod);
    }
}
