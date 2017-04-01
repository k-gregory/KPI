package io.bitbucket.gregoryk1.despat.lab2.task1;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

/**
 * Created by grego on 01.04.2017.
 */
public class CriminalForm {
    public final String fullName;
    public final String captureReason;
    public final LocalDate capturedDate;
    public final Period capturePeriod;

    public CriminalForm(String fullName, String captureReason, LocalDate capturedDate, Period capturePeriod) {
        this.fullName = fullName;
        this.captureReason = captureReason;
        this.capturedDate = capturedDate;
        this.capturePeriod = capturePeriod;
    }
}
