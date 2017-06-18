package io.github.k_gregory.otp.lab1.impl;

import io.github.k_gregory.otp.lab1.Event;
import io.github.k_gregory.otp.lab1.FSM;
import io.github.k_gregory.otp.lab1.MachineState;

import static io.github.k_gregory.otp.lab1.Event.*;

public class StateFSM extends FSM {
    private PatternState s = PatternState.START;

    @Override
    public void reset() {
        s = PatternState.START;
    }

    @Override
    public MachineState nextState(Event e) {
        s = s.nextState(e);
        return s.getMachineState();
    }

    private enum PatternState {
        ERROR(MachineState.ERROR) {
            @Override
            public PatternState nextState(Event e) {
                if (e == PLUS) return Q1;
                else return ERROR;
            }
        },
        START(MachineState.START) {
            @Override
            public PatternState nextState(Event e) {
                if (e == PLUS) return Q1;
                else return ERROR;
            }
        },
        Q1(MachineState.Q1) {
            @Override
            public PatternState nextState(Event e) {
                if (e == DIGIT) return Q2;
                else return ERROR;
            }
        },
        Q2(MachineState.Q2) {
            @Override
            public PatternState nextState(Event e) {
                if (e == DIGIT) return Q2;
                else if (e == LATIN_CAPITAL) return Q3;
                else if (e == EOF) return SUCCESS;
                else return ERROR;
            }
        },
        Q3(MachineState.Q3) {
            @Override
            public PatternState nextState(Event e) {
                if (e == LATIN_CAPITAL) return Q3;
                else if (e == EOF) return SUCCESS;
                else return ERROR;
            }
        },
        SUCCESS(MachineState.SUCCESS) {
            @Override
            public PatternState nextState(Event e) {
                return ERROR;
            }
        };


        private MachineState machineState;

        PatternState(MachineState machineState) {
            this.machineState = machineState;
        }

        public abstract PatternState nextState(Event e);

        public MachineState getMachineState() {
            return machineState;
        }
    }
}
