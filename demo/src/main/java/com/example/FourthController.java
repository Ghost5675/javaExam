package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FourthController {
    @FXML
    private Label display;

    @FXML
    private TextField codeField;

    @FXML
    public void confirm() throws IOException {
        if (codeField.getText().equals(ThirdController.code)) {
            App.setRoot("fifth");
        } else {
            display.setText("wrong code");
        }

    }

    @FXML
    public void logIn() throws IOException {
        App.setRoot("secondary");
    }
}
