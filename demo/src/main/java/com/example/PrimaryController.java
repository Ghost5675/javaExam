package com.example;

import com.example.tetris.TetrisGame;

import javafx.scene.control.Label;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PrimaryController {
    @FXML
    private Label display;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void sign() {
        if (!DatabaseConnection.checkUser(emailField.getText())) {
            if (!emailField.getText().contains("@")) {
                display.setText("wrong email");
                return;
            }
            DatabaseConnection.add(nameField.getText(), emailField.getText(), passwordField.getText());
            display.setText(nameField.getText());
        } else {
            display.setText("This user already exist");
        }
    }

    @FXML
    public void logIn() throws Exception {
        Stage newStage = new Stage();
        TetrisGame game = new TetrisGame();
        game.start(newStage);
        
    }
}
