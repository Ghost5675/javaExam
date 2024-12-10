package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FifthController {
    @FXML
    private Label display;

    @FXML
    private TextField passwordField;
    
    @FXML
    private TextField confirmField;
    
    @FXML
    public void confirm() throws IOException{
        if (passwordField.getText().equals(confirmField.getText())) {
            DatabaseConnection.updatePassword(ThirdController.saveEmail, passwordField.getText());
            App.setRoot("primary");
        } else {
            display.setText("error");
        }
    }

}
