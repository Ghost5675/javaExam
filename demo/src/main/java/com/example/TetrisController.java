package com.example;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class TetrisController {
    @FXML
    private Canvas gameCanvas;

    public void initialize() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        drawGrid(gc);
    }

    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(javafx.scene.paint.Color.GRAY);
        for (int x = 0; x <= 300; x += 30) {
            for (int y = 0; y <= 600; y += 30) {
                gc.strokeRect(x, y, 30, 30);
            }
        }
    }
}
