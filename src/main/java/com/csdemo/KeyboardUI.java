package com.csdemo;

import com.csdemo.model.LetterState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class KeyboardUI {
    private static final String TOP_ROW = "qwertyuiop";
    private static final String MIDDLE_ROW = "asdfghjkl";
    private static final String BOTTOM_ROW = "zxcvbnm";

    public HBox keyboardTopRow;

    public HBox keyboardMiddleRow;

    public HBox keyboardBottomRow;

    @FXML
    private VBox allKeyboardContainer;

    private HashMap<String, Node> keyMap = new HashMap<String, Node>();

    public KeyboardUI(VBox allKeyboardContainer) {
        setAttributes(allKeyboardContainer);
    }

    public KeyboardUI() {}

    public void setAttributes(VBox keyboardContainer) {
        this.allKeyboardContainer = keyboardContainer;
    }
    public void initialize() {
        ;
    }

    public void drawKeyboard() {
        try {
            URL keyboardContainerURL = Paths.get("src/main/resources/fx_files/KeyboardContainer.fxml").toUri().toURL();
            FXMLLoader keyboardContainerLoader = new FXMLLoader(keyboardContainerURL);
            VBox keyboardContainer = keyboardContainerLoader.load();
            this.keyboardTopRow = (HBox) keyboardContainer.lookup("#keyboardTopRow");
            this.keyboardMiddleRow = (HBox) keyboardContainer.lookup("#keyboardMiddleRow");
            this.keyboardBottomRow = (HBox) keyboardContainer.lookup("#keyboardBottomRow");

            for (String key : TOP_ROW.split("")) {
                TileUI tile = new TileUI(key, LetterState.UNKNOWN);
                Node tileBuilt = tile.buildTile();
                keyboardTopRow.getChildren().add(tileBuilt);
                keyMap.put(key, tileBuilt);
            }

            for (String key : MIDDLE_ROW.split("")) {
                TileUI tile = new TileUI(key, LetterState.UNKNOWN);
                Node tileBuilt = tile.buildTile();
                keyboardMiddleRow.getChildren().add(tileBuilt);
                keyMap.put(key, tileBuilt);
            }

            for (String key : BOTTOM_ROW.split("")) {
                TileUI tile = new TileUI(key, LetterState.UNKNOWN);
                Node tileBuilt = tile.buildTile();
                keyboardBottomRow.getChildren().add(tileBuilt);
                keyMap.put(key, tileBuilt);
            }

            allKeyboardContainer.getChildren().add(keyboardContainer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(HashSet<String> correctLetters, HashSet<String> misplacedLetters, HashSet<String> unusedLetters) {
        LetterState[] statuses = {LetterState.NOT_USED, LetterState.MISPLACED, LetterState.CORRECT};
        List<HashSet<String>> letterGroups = Arrays.asList(
                unusedLetters,
                misplacedLetters,
                correctLetters
        );

        for (int i = 0; i < letterGroups.size(); i++) {
            HashSet<String> letters = letterGroups.get(i);
            LetterState status = statuses[i];

            for (String letter : letters) {
                Node tile = keyMap.get(letter);
                TileUI.updateStatus(tile, status);
            }
        }
    }
}