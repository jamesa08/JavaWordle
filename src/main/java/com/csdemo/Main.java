package com.csdemo;
import javafx.application.Application;
import javafx.stage.Stage;

import java.nio.file.Paths;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Create an instance of GameUI

            GameUI gameUI = new GameUI();

            // Load word files and answer files
            String wordFiles = Paths.get("src/main/resources/validGuesses.txt").toUri().toURL().getPath();
            String answerFiles = Paths.get("src/main/resources/answers.txt").toUri().toURL().getPath();

            gameUI.wordFiles = wordFiles;
            gameUI.answerFiles = answerFiles;

            gameUI.start(primaryStage);
            primaryStage.toFront();
        } catch (Exception e ){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
