package com.csdemo;
import java.util.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;



public class Guess {
    // initial state of letter until we determine its correct position
    public static String  UNKNOWN = "U";
    // letter is in correct position
    public static String CORRECT = "C";
    // letter is used at a location not already marked CORRECT
    public static String MISPLACED = "M";
    // letter is not used at all
    public static String NOT_USED = "I";


    Hashtable<String, String> colorDict= new Hashtable<>();

    // instance variables
    // the guess
    String _guess;
    // list corresponding to status of each of the five letters in the guess
    List<String> _result = new ArrayList<>();
    // whether the guess is completely correct (true if it is)
    boolean _isCorrect;
    // set containing the correct letters for the guess
    public Set<Character> correctLetters;
    // set containing the misplaced letters for the guess
    public Set<Character> misplacedLetters;
    // set containing unusedLetters
    public Set<Character> unusedLetters;


    public Guess(String guess, String answer) {
        colorDict.put("COLOR", "green");
        colorDict.put("MISPLACED", "yellow");
        colorDict.put("NOT_USED", "grey");

        assert guess.length() == 5 && answer.length() == 5;
        // save the guess for later processing and returning
        _guess = guess;
        // initial state of each letter
        for (int i = 0; i < 5; i++) {
            _result.add(UNKNOWN);
        }
        correctLetters = new LinkedHashSet<>();
        misplacedLetters = new LinkedHashSet<>();
        unusedLetters = new LinkedHashSet<>();
        _isCorrect = _evaluate(answer);
    }

    public String guess() { return _guess;}

    public List<String> result() { return _result;}

    // helper method to evaluate guess
    private boolean _evaluate(String answer) {

        char[] arrayGuess = _guess.toCharArray();
        char[] answerList = answer.toCharArray();
        boolean letterUsed = false;

        // iterate in reverse order since deleting from answer
        for (int i = 5; i > 0; i--) {
            // for else-if statement
            for (int j = 0; j< answerList.length; j++) {
                if (arrayGuess[i] == answerList[j]) {
                    letterUsed = true;
                    break; }
            }
            // if letter is in correct spot
            if (arrayGuess[i] == answerList[i]) {
                // mark as correct
                _result.set(i,CORRECT);
                // put it in correct set
                correctLetters.add(arrayGuess[i]);
                // remove so it isn't used later to mark same letter MISPLACED
                if (answerList.length != 0) {
                    // creates temporary array with all but the last element
                    char[] tempArray = Arrays.copyOfRange(answerList, 0, answerList.length - 1);
                    answerList = tempArray;
                }
            // letter is not used
            } else if (!letterUsed) {
                unusedLetters.add(arrayGuess[i]);
                _result.set(i,NOT_USED);
            }
        }
        // make answerArray with only letters from answer that are not correct
        List<String> answerArray = new ArrayList<>();
        for (int i = 0; i < _result.size(); i++) {
            if (!_result.get(i).equals(CORRECT)) {
                answerArray.add(_result.get(i));
            }
        }
        // any letters still UNKNOWN are either MISPLACED or NOT_USED
        for (int i = 0; i < _result.size(); i++) {
            if (_result.get(i).equals(UNKNOWN)) {
                String ch = _result.get(i);
                if (answerArray.contains(ch)) {
                    _result.set(i,MISPLACED);
                    answerArray.remove(ch);
                    // if it's not a correct letter, add to misplaced
                    if (!correctLetters.contains(ch.charAt(0))) {
                        misplacedLetters.add(ch.charAt(0));
                    }
                } else {
                    _result.set(i,NOT_USED);
                }
            }
        }
        // determine if guess is correct
        for (int i = 0; i < _result.size(); i++) {
            if (!_result.get(i).equals(CORRECT)) {
                return false;
            }
        }
        return true;
    }

    public boolean isCorrect() { return _isCorrect;}

    public static void main(String[] args) {
        System.out.println("testing");
        JavaFXTest.main(args);
    }
}

class JavaFXTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Hello, JavaFX!");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Test");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}