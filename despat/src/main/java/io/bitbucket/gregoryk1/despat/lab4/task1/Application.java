package io.bitbucket.gregoryk1.despat.lab4.task1;

enum DayOfWeekState {
    MONDAY(1, 4),
    TUESDAY(2, 4),
    WEDNESDAY(3, 4),
    THURSDAY(1, 4),
    FRIDAY(2, 4),
    SATURDAY(3, 4),
    SUNDAY(-1, -1);

    public final int morningPill;
    public final int eveningPill;

    DayOfWeekState(int morningPill, int eveningPill) {
        this.morningPill = morningPill;
        this.eveningPill = eveningPill;
    }
}

enum TimeOfDayState {
    EVENING, MORNING
}

class PillHelperContext {
    private DayOfWeekState dayOfWeekState;
    private TimeOfDayState timeOfDayState;

    public void setDayOfWeek(DayOfWeekState dayOfWeekState) {
        this.dayOfWeekState = dayOfWeekState;
    }

    public void setTimeOfDay(TimeOfDayState timeOfDayState) {
        this.timeOfDayState = timeOfDayState;
    }

    public int getCurrentPill() {
        switch (timeOfDayState) {
            case EVENING:
                return dayOfWeekState.eveningPill;
            case MORNING:
                return dayOfWeekState.morningPill;
            default:
                throw new RuntimeException();
        }
    }

    public void printAdvice() {
        int result = getCurrentPill();
        String currentTime = dayOfWeekState.name().toLowerCase() + " " + timeOfDayState.name().toLowerCase();
        if (result == -1)
            System.out.println("No pills are needed at " + currentTime);
        else
            System.out.println("You shall take #" + result + " at " + currentTime);
    }
}

public class Application {
    public static void main(String... args) {
        PillHelperContext pillHelper = new PillHelperContext();
        for (DayOfWeekState s : DayOfWeekState.values()) {
            pillHelper.setDayOfWeek(s);
            pillHelper.setTimeOfDay(TimeOfDayState.MORNING);
            pillHelper.printAdvice();
            pillHelper.setTimeOfDay(TimeOfDayState.EVENING);
            pillHelper.printAdvice();
            System.out.println();
        }
    }
}
