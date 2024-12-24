package com.example;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.fxml.FXML;

import com.example.tetris.TetrisGame;

import java.io.IOException;
import javafx.scene.control.Label;

import com.example.pacman.PacManGame; 

public class gamen {
    private boolean isTetris = false;
    private boolean isPacMan = false;
    
    @FXML
    private Label Text;

    @FXML
    public void initialize() {
        if (DatabaseConnection.currentUser != null) {
            Text.setText(DatabaseConnection.currentUser.toString());
        } else {
            Text.setText("Welcome, Guest!"); 
        }
    }

    @FXML
    void inPacMan(ActionEvent event) throws Exception {
        if (!isTetris && !isPacMan) {
            Stage stage = new Stage();
            PacManGame pacManGame = new PacManGame();
            pacManGame.start(stage); 
            isPacMan = true;
            if (TetrisGame.fall != null) {
                TetrisGame.fall.cancel();
                TetrisGame.fall.purge(); // Полностью очищает задачи таймера
            }
            stage.setOnCloseRequest(e -> isPacMan = false);
        } else {
            System.out.println("Tetris or Pac-Man is already running");
        }
    }

    @FXML
    void inTetris(ActionEvent event) throws Exception {
        if (!isTetris && !isPacMan) {
            Stage stage = new Stage();
            TetrisGame tetrisGame = new TetrisGame();
            tetrisGame.start(stage); 
            isTetris = true;

            stage.setOnCloseRequest(e -> isTetris = false);
        } else {
            System.out.println("Tetris or Pac-Man is already running");
        }
    }

    @FXML
    void outlog(ActionEvent event) throws IOException {
        App.setRoot("primary");
        DatabaseConnection.currentUser = "";
        System.out.println("Exiting application...");
    }
}
