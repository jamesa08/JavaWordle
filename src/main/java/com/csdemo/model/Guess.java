package com.csdemo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class Guess {
    // const
    public static final String UNKNOWN = "U";
    public static final String CORRECT = "C";
    public static final String MISPLACED = "M";
    public static final String NOT_USED = "I";

    private String guessStr;

    private ArrayList<String> result;
    private boolean isCorrect;

    public HashSet<String> correctLetters;
    public HashSet<String> misplacedLetters;
    public HashSet<String> unusedLetters;

    public Guess(String guessStr, String answer) {
        assert guessStr.length() == 5 && answer.length() == 5;

        this.guessStr = guessStr;
        this.result = new ArrayList<String>(Collections.nCopies(5, Guess.UNKNOWN));
        this.correctLetters = new HashSet<String>();
        this.misplacedLetters = new HashSet<String>();
        this.unusedLetters = new HashSet<String>();
        this.isCorrect = this.evaluate(answer);
    }

    public Guess() {}

    public void setAttributes(String guessStr, String answer) {
        assert guessStr.length() == 5 && answer.length() == 5;

        this.guessStr = guessStr;
        this.result = new ArrayList<String>(Collections.nCopies(5, Guess.UNKNOWN));
        this.correctLetters = new HashSet<String>();
        this.misplacedLetters = new HashSet<String>();
        this.unusedLetters = new HashSet<String>();
        this.isCorrect = this.evaluate(answer);
    }

    public String getGuessStr() {
        return this.guessStr;
    }

    public ArrayList<String> result() {
        return this.result;
    }

    private boolean evaluate(String answer) {
        ArrayList<Character> answerList = answer.chars().mapToObj( c -> (char) c).collect(Collectors.toCollection(ArrayList::new));

        for (int i = 4; i >= 0; i--) {
            if (this.guessStr.charAt(i) == answerList.get(i)) {
                this.result.set(i, Guess.CORRECT);
                this.correctLetters.add(String.valueOf(this.guessStr.charAt(i)));
                answerList.remove(i);
            } else if (!answerList.contains(this.guessStr.charAt(i))) {
                this.unusedLetters.add(String.valueOf(this.guessStr.charAt(i)));
                this.result.set(i, Guess.NOT_USED);
            }
        }

        int i = 0;
        for (String status : result) {
            if (!Objects.equals(status, Guess.CORRECT)) {
                answerList.add(answer.charAt(i));
            }
            i++;
        }

        i = 0;
        for (String status : result) {
            if (Objects.equals(status, Guess.UNKNOWN)) {
                char ch = this.guessStr.charAt(i);

                if (answerList.contains(ch)) {
                    this.result.set(i, Guess.MISPLACED);
                    answerList.remove(Character.valueOf(ch));

                    if (!this.correctLetters.contains(String.valueOf(ch))) {
                        this.misplacedLetters.add(String.valueOf(ch));
                    }
                } else {
                    this.result.set(i, Guess.NOT_USED);
                }
            }
            i++;
        }

        for (String status : result) {
            if (!Objects.equals(status, Guess.CORRECT)) {
                return false;
            }
        }

        return true;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
