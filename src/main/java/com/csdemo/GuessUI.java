package com.csdemo;

import com.csdemo.model.Guess;
import com.csdemo.model.LetterState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.nio.file.Paths;

public class GuessUI extends Guess {
    // from WordleGameUI
    private VBox allGuessContainer;

    // field from GuessContainer.fxml
    @FXML
    private HBox guessContainer;

    // field from GuessContainer.fxml
    @FXML
    public Label tileText;

    public GuessUI(String guessStr, String answer, VBox allGuessContainer) {
        setAttributes(guessStr, answer, allGuessContainer);
    }

    public GuessUI() {}

    public void setAttributes(String guessStr, String answer, VBox allGuessContainer) {
        super.setAttributes(guessStr, answer);
        this.allGuessContainer = allGuessContainer;
    }

    public void addGuessTiles() {
        try {
            URL guessContainerURL = Paths.get("src/main/resources/fx_files/GuessContainer.fxml").toUri().toURL();
            FXMLLoader guessContainerLoader = new FXMLLoader(guessContainerURL);
            HBox guessContainer = guessContainerLoader.load();

            for (int i = 0; i < this.getGuessStr().length(); i++) {
                // get current guess chr
                String ch = String.valueOf(this.getGuessStr().charAt(i));

                // get status
                LetterState status = LetterState.oneCharToSelf(this.result().get(i));

                // create new tile
                TileUI tile = new TileUI(ch, status);

                // build the tile and add it to guess container
                Node tileBuilt = tile.buildTile();
                guessContainer.getChildren().add(tileBuilt);
            }
            allGuessContainer.getChildren().add(guessContainer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}