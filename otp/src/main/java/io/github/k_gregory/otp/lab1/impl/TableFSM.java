package io.github.k_gregory.otp.lab1.impl;

import io.github.k_gregory.otp.lab1.Event;
import io.github.k_gregory.otp.lab1.FSM;
import io.github.k_gregory.otp.lab1.MachineState;

import java.util.HashMap;
import java.util.Map;

import static io.github.k_gregory.otp.lab1.Event.*;
import static io.github.k_gregory.otp.lab1.MachineState.*;


public class TableFSM extends FSM {
    private static Map<StateRule, MachineState> table = new HashMap<>();

    static {
        when(START, PLUS).go(Q1);

        when(Q1, DIGIT).go(Q2);

        when(Q2, DIGIT).go(Q2);
        when(Q2, LATIN_CAPITAL).go(Q3);
        when(Q2, EOF).go(SUCCESS);

        when(Q3, LATIN_CAPITAL).go(Q3);
        when(Q3, EOF).go(SUCCESS);
    }

    private static StateResolver when(MachineState current, Event event) {
        return (newState) -> table.put(new StateRule(current, event), newState);
    }

    @Override
    public MachineState nextState(Event e) {
        MachineState currentState = getCurrentState();
        MachineState newState = table.get(new StateRule(currentState, e));
        return newState != null ? newState : ERROR;
    }

    @FunctionalInterface
    private interface StateResolver {
        void go(MachineState s);
    }

    private static class StateRule {
        MachineState s;
        Event e;
        public StateRule(MachineState s, Event e) {
            this.s = s;
            this.e = e;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StateRule stateRule = (StateRule) o;

            return s == stateRule.s && e == stateRule.e;
        }

        @Override
        public int hashCode() {
            int result = s.hashCode();
            result = 31 * result + e.hashCode();
            return result;
        }
    }
}
