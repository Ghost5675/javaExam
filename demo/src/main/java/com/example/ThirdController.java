package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ThirdController {
    static String saveEmail;
    static String code;

    @FXML
    private Label display;
    
    @FXML
    private TextField emailField;

    @FXML
    public void confirm() throws IOException{
        if (DatabaseConnection.checkUser(emailField.getText())) {
            ThirdController.code = EmailSender.getCode();
            EmailSender.sender(DatabaseConnection.getUser(emailField.getText()), emailField.getText(), code);
            ThirdController.saveEmail = emailField.getText();
            App.setRoot("fourth");
        } else {
            display.setText("wrong email");
        }

    }

    @FXML
    public void logIn() throws IOException {
        App.setRoot("secondary");
    }
}
