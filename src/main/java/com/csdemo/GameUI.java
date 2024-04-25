package com.csdemo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Random;
public class GameUI extends Application {

    private WordleGameUI wordleGameUI;
    public String wordFiles;
    public String answerFiles;
    private HashSet<String> validGuesses = new HashSet<String>();
    private String answer;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the Main View FXML
        URL url = Paths.get("src/main/resources/fx_files/MainView.fxml").toUri().toURL();

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();

        try (BufferedReader wordFile = new BufferedReader(new FileReader(wordFiles))) {
            String line;
            while ((line = wordFile.readLine()) != null) {
                validGuesses.add(line);
            }
        }

        // Pick answer from answers list (if provided)
        if (answerFiles != null) {
            try (BufferedReader answerFile = new BufferedReader(new FileReader(answerFiles))) {
                String line;
                int numLines = 0;
                Random random = new Random();
                while ((line = answerFile.readLine()) != null) {
                    if (random.nextInt(++numLines) == 0) {
                        answer = line;
                    }
                }
            }
        } else {
            // Pick answer from guess list if there is no answer file
            int randomIndex = new Random().nextInt(validGuesses.size());
            answer = validGuesses.toArray(new String[0])[randomIndex];
        }

        // Get the controller instance
        wordleGameUI = loader.getController();
        wordleGameUI.setAttributes(answer, validGuesses);
        System.out.println("Answer: " + answer);


        // Create the scene and set it on the primary stage
        Scene scene = new Scene(root, 550, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}