package com.csdemo;

import com.csdemo.model.LetterState;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.nio.file.Paths;

public class TileUI {
    public String character;
    public LetterState status;

    private Node root;

    public TileUI(String character, LetterState status) {
        this.character = character;
        this.status = status;

        try {
            URL tileURL = Paths.get("src/main/resources/fx_files/Tile.fxml").toUri().toURL();
            FXMLLoader tileLoader = new FXMLLoader(tileURL);
            this.root = tileLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Node buildTile() {
        Label tileText = (Label) root.lookup("#tileText");
        Rectangle tileBack = (Rectangle) root.lookup("#tileBack");

        tileText.setText(this.character);

        String color = status.getColor();
        tileBack.setFill(Paint.valueOf(color));

        return root;
    }

    public static void updateStatus(Node node, LetterState status) {


        Rectangle tileBack = (Rectangle) node.lookup("#tileBack");
        String color = status.getColor();
        tileBack.setFill(Paint.valueOf(color));
    }
}