package com.example;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
    
    @FXML
    public void switchToGame() throws IOException {
        App.setRoot("tetris");
    }
}
