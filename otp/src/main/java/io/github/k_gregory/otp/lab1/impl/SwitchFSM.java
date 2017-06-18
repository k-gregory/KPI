package io.github.k_gregory.otp.lab1.impl;

import io.github.k_gregory.otp.lab1.Event;
import io.github.k_gregory.otp.lab1.FSM;
import io.github.k_gregory.otp.lab1.MachineState;

import static io.github.k_gregory.otp.lab1.Event.*;
import static io.github.k_gregory.otp.lab1.MachineState.*;

public class SwitchFSM extends FSM {
    @Override
    public MachineState nextState(Event e) {
        MachineState next = ERROR;
        switch (getCurrentState()) {
            case START:
                if (e == PLUS) next = Q1;
                break;
            case Q1:
                if (e == DIGIT) next = Q2;
                break;
            case Q2:
                if (e == DIGIT) next = Q2;
                else if (e == LATIN_CAPITAL) next = Q3;
                else if (e == EOF) next = SUCCESS;
                break;
            case Q3:
                if (e == LATIN_CAPITAL) next = Q3;
                else if (e == EOF) next = SUCCESS;
                break;
        }
        return next;
    }
}
