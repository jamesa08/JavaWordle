package com.csdemo.model;

import java.util.Objects;

public enum LetterState {
    CORRECT("green"),
    MISPLACED("yellow"),
    NOT_USED("gray"),
    UNKNOWN("white");
    
    private final String label;

    private LetterState(String label) {
        this.label = label;
    }
    
    public String getColor() {
        return this.label;
    }

    public static LetterState oneCharToSelf(String status) {
        if (Objects.equals(status, "C")) {
            return LetterState.CORRECT;
        } else if (Objects.equals(status, "M")) {
            return LetterState.MISPLACED;
        } else if (Objects.equals(status, "I")) {
            return LetterState.NOT_USED;
        } else {
            return LetterState.UNKNOWN;
        }
    }
}