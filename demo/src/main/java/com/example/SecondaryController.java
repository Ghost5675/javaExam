package com.example;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SecondaryController {

    @FXML
    private Label display;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void signUP() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void log() throws Exception{
        String username = DatabaseConnection.checkLogIn(emailField.getText(), passwordField.getText());
        if (username != null) {
            display.setText("Welcome, " + username + "!");
            DatabaseConnection.currentUser = username;
            DatabaseConnection.currentEmail = emailField.getText();
            App.setRoot("game");
        } else {
            display.setText("Invalid email or password");
        }
    }

    @FXML
    private void forgot() throws IOException{
        App.setRoot("third");
    }
}