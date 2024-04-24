package com.csdemo;
import java.util.*;

public class Tile {
    // 1 letter
    private char _letter;
    // status of the tile, default is UNKNOWN
    private String _status;

    // all the valid statuses of a guess
    public static final Set<String> VALID_STATUSES = new HashSet<>();
    static {
        VALID_STATUSES.add(Guess.UNKNOWN);
        VALID_STATUSES.add(Guess.CORRECT);
        VALID_STATUSES.add(Guess.MISPLACED);
        VALID_STATUSES.add(Guess.NOT_USED);
    }

    public Tile(char letter, String status) {
        // char is always one character
        _letter = letter;
        assert VALID_STATUSES.contains(status);
        _status = status;
    }
    // default value for status is UNKNOWN
    public Tile(char letter) {
        this(letter, Guess.UNKNOWN);
    }

    public void setStatus(String status) {
        assert VALID_STATUSES.contains(status);
        _status = status;
    }

    public String getStatus() {
        return _status;
    }
}
