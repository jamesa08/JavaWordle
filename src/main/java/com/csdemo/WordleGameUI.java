package com.csdemo;

import com.csdemo.model.WordleGame;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.util.HashSet;

public class WordleGameUI extends WordleGame {
    @FXML
    private VBox root;
    @FXML
    private VBox allGuessContainer;

    @FXML
    private VBox keyboardContainer;

    @FXML
    private TextField guessInput;

    @FXML
    public Label messageBox;


    private KeyboardUI keyboard;

    private static final int MAX_GUESSES = 6;

    private int currentGuess = 0;
    // empty init, javaFX initializes class
    public WordleGameUI() { ; }

    // keyword constructor for other instances
    public WordleGameUI(String answer, HashSet<String> validGuesses) {
        this.setAttributes(answer, validGuesses);
    }


    // FXML class
    public void initialize() {
        root.styleProperty().bind(Bindings.createStringBinding(
                () -> {
                    double xSize = root.getWidth() / 550.0; // Adjust the reference width as needed
                    double ySize = root.getHeight() / 800.0; // Adjust the reference height as needed

                    // Maintain the aspect ratio by using the smaller scaling factor
                    double scale = Math.min(xSize, ySize);

                    return "-fx-font-size: 24px; -fx-scale-x: " + scale + "; -fx-scale-y: " + scale + ";";
                },
                root.widthProperty(), root.heightProperty()
        ));
    }


    // set attributes on class (ran after constructor, or must be called after javaFX initializes class)
    public void setAttributes(String answer, HashSet<String> validGuesses) {
        super.setAttributes(answer, validGuesses);
        keyboard = new KeyboardUI(keyboardContainer);
        keyboard.drawKeyboard();
    }

    // add a guess to the game
    public GuessUI addGuess(String guessStr) {
        GuessUI guessObj = new GuessUI();
        guessObj.setAttributes(guessStr, this.answer, allGuessContainer);
        this.guesses.add(guessObj);

        this.correctLetters.addAll(guessObj.correctLetters);
        this.misplacedLetters.addAll(guessObj.misplacedLetters);
        this.unusedLetters.addAll(guessObj.unusedLetters);

        this.misplacedLetters.removeAll(this.correctLetters);

        keyboard.update(this.correctLetters, this.misplacedLetters, this.unusedLetters);

        return guessObj;
    }

    // FXML binding to button call, handles game logic
    @FXML
    public void onAddGuess(ActionEvent event) {
        if (!guessInput.isEditable()) { return; }  // game over
        messageBox.setText("");  // clear error

        String guessStr = guessInput.getText().strip().toLowerCase();

        if (guessStr.length() != 5) {
            messageBox.setText("Guess must be 5 letters");
            return;
        }

        if (currentGuess >= MAX_GUESSES) {
            messageBox.setText(String.format("Game over. Answer is %s.", answer));
            guessInput.setEditable(false);
            return;
        }

        if (!validGuesses.contains(guessStr)) {
            messageBox.setText("Invalid guess");
            return;
        }

        GuessUI guessObj = this.addGuess(guessStr);
        guessObj.addGuessTiles();  // draw the tiles
        this.currentGuess += 1;

        // check if won
        if (guessObj.isCorrect()) {
            messageBox.setText(String.format("You won in %s guesses", currentGuess));
            messageBox.setTextFill(Paint.valueOf("green"));
            guessInput.setEditable(false);
        }
        guessInput.clear();
    }

}