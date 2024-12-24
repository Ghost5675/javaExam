package com.example.pacman;

import javafx.scene.shape.Rectangle;

public class PacManController {
    public static void move(Rectangle object, int dx, int dy) {
        int[][] MESH = PacManGame.MESH;
        int newX = (int) object.getX() + dx * PacManGame.SIZE;
        int newY = (int) object.getY() + dy * PacManGame.SIZE;
        PacManGame.pacMan.setX(newX / PacManGame.SIZE);
        PacManGame.pacMan.setY(newY / PacManGame.SIZE);
        if (newX >= 0 && newX < PacManGame.XMAX && newY >= 0 && newY < PacManGame.YMAX
                && MESH[newX / PacManGame.SIZE][newY / PacManGame.SIZE] != 3 && MESH[newX / PacManGame.SIZE][newY / PacManGame.SIZE] != 4) {
            object.setX(newX);
            object.setY(newY);
        }
    }

    public static void movePacMan(int dx, int dy) {
        move(PacManGame.pacMan.getSprite(), dx, dy);
    }

}
