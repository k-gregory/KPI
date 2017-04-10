package io.github.k_gregory.otp.lab1;

import io.github.k_gregory.otp.lab1.impl.StateFSM;
import io.github.k_gregory.otp.lab1.impl.SwitchFSM;
import io.github.k_gregory.otp.lab1.impl.TableFSM;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class Task2 {
    @Parameter
    public FSM fsm;

    @Parameters
    public static Object[] implementations() {
        return new Object[]{new StateFSM(), new SwitchFSM(), new TableFSM()};
    }

    private void assertMatch(String s) {
        assertTrue(fsm.match(s));
    }

    private void assertNoMatch(String s) {
        assertFalse(fsm.match(s));
    }

    @Test
    public void matchFull() {
        assertMatch("+555THIS");
        assertMatch("+9I");
        assertMatch("+88K");
    }

    @Test
    public void matchMinimal() {
        assertMatch("+1");
        assertMatch("+5");
        assertMatch("+4M");
        assertMatch("+8A");

    }

    @Test
    public void noMatchExtra() {
        assertNoMatch("+55THIS42");
        assertNoMatch("+9THIS42THAT");
        assertNoMatch("+1T1T");
    }

    @Test
    public void matchNoLatin() {
        assertMatch("+5511");
        assertMatch("+5511103044");
        assertMatch("+5");
    }

    @Test
    public void noMatchWithoutPlus() {
        assertNoMatch("555LOL");
        assertNoMatch("-555LOL");
        assertNoMatch("5X");
    }

    @Test
    public void matchesSingle() {
        assertMatch("+3XX");
        assertMatch("+44MA");
        assertMatch("+4M");
    }
}
