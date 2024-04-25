package com.csdemo.model;

import java.util.ArrayList;
import java.util.HashSet;

public class WordleGame {

    public String answer;
    protected ArrayList<Guess> guesses;

    protected HashSet<String> validGuesses;
    public HashSet<String> correctLetters = new HashSet<String>();
    public HashSet<String> misplacedLetters = new HashSet<String>();
    public HashSet<String> unusedLetters = new HashSet<String>();

    public WordleGame() {}
    public WordleGame(String answer, HashSet<String> validGuesses) {
        setAttributes(answer, validGuesses);
    }

    public void setAttributes(String answer, HashSet<String> validGuesses) {
        this.answer = answer;
        this.guesses = new ArrayList<Guess>();
        this.validGuesses = validGuesses;
    }

    public Guess addGuess(String guessStr) {
        Guess guessObj = new Guess(guessStr, this.answer);
        this.guesses.add(guessObj);

        this.correctLetters.addAll(guessObj.correctLetters);
        this.misplacedLetters.addAll(guessObj.misplacedLetters);
        this.unusedLetters.addAll(guessObj.unusedLetters);

        this.misplacedLetters.removeAll(this.correctLetters);

        return guessObj;
    }
}