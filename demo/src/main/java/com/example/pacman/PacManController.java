package com.example.pacman;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class PacManController {
    public static void move(Rectangle object, int dx, int dy) {
        int[][] MESH = PacManGame.MESH;
        int newX = (int) object.getX() + dx * PacManGame.SIZE;
        int newY = (int) object.getY() + dy * PacManGame.SIZE;

        if (newX >= 0 && newX < PacManGame.XMAX && newY >= 0 && newY < PacManGame.YMAX && MESH[newX / PacManGame.SIZE][newY / PacManGame.SIZE] != 3) {
            object.setX(newX);
            object.setY(newY);
        }
    }

    // Метод для Pac-Man
    public static void movePacMan(int dx, int dy) {
        move(PacManGame.pacMan.getSprite(), dx, dy);
    }

    
}
