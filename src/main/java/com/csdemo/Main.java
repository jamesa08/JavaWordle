package com.csdemo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent; 
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.net.URL;
import java.nio.file.Paths;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            URL url = Paths.get("./src/main/sample.fxml").toUri().toURL();
            Parent root = FXMLLoader.load(url);

            // Parent root = FXMLLoader.load(getClass().getResource("./src/main/sample.fxml"));
            primaryStage.setTitle("Wordle Game");
            primaryStage.setScene(new Scene(root, 300, 275));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}