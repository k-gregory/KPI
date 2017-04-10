package io.github.k_gregory.otp.lab1;

public enum Event {
    UNKNOWN, PLUS, DIGIT, LATIN_CAPITAL, EOF;

    public static Event fromChar(char c) {
        if (c >= 'A' && c <= 'Z') return LATIN_CAPITAL;
        else if (c == '+') return PLUS;
        else if (c >= '0' && c <= '9') return DIGIT;
        else return UNKNOWN;
    }
}
