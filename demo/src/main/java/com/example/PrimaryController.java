package com.example;


import javafx.scene.control.Label;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
    public void sign() throws Exception {
        if (!DatabaseConnection.checkUser(emailField.getText())) {
            if (!emailField.getText().contains("@")) {
                display.setText("wrong email");
                return;
            }
            DatabaseConnection.add(nameField.getText(), emailField.getText(), passwordField.getText());
            DatabaseConnection.currentUser = nameField.getText();
            DatabaseConnection.currentEmail = emailField.getText();
            App.setRoot("game");
            display.setText(nameField.getText());
        } else {
            display.setText("This user already exist");
        }
    }

    @FXML
    public void logIn() throws Exception {
        App.setRoot("secondary");
    }
}
