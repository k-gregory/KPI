package io.github.k_gregory.otp.lab1;

public abstract class FSM {
    private MachineState currentState;

    public void reset() {
        currentState = MachineState.START;
    }

    public MachineState getCurrentState() {
        return currentState;
    }

    public boolean match(String s) {
        reset();
        for (char c : s.toCharArray()) {
            currentState = nextState(Event.fromChar(c));
            if (currentState == MachineState.ERROR) break;
        }
        currentState = nextState(Event.EOF);
        return currentState == MachineState.SUCCESS;
    }

    public abstract MachineState nextState(Event e);
}
