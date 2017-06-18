package io.github.k_gregory.otp.lab1;

import io.github.k_gregory.otp.lab1.impl.StateFSM;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;


@RunWith(Theories.class)
public class Task2Theories {
    @DataPoint
    public static String matched = "+330FIK";
    @DataPoint
    public static String notMatched = "+330FIk";

    @Theory
    public void correctAccepted(String param) {
        assumeTrue(Pattern.matches("\\+\\d+[A-Z]*", param));
        assertTrue(new StateFSM().match(param));
    }

    @Theory
    public void lowerCaseNotAccepted(String param) {
        assumeFalse(Pattern.matches("\\+\\d+[A-Z]*", param));
        assertFalse(new StateFSM().match(param));
    }
}
