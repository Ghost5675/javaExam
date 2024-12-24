package com.example.pacman;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.scene.shape.Rectangle;

public class PacManController {
    public static void move(Rectangle object, int dx, int dy) {
        int[][] MESH = PacManGame.MESH;
        int newX = (int) object.getX() + dx * PacManGame.SIZE;
        int newY = (int) object.getY() + dy * PacManGame.SIZE;
        PacManGame.pacMan.setX(newX / PacManGame.SIZE);
        PacManGame.pacMan.setY(newY / PacManGame.SIZE);
        if (newX >= 0 && newX < PacManGame.XMAX && newY >= 0 && newY < PacManGame.YMAX
                && MESH[newX / PacManGame.SIZE][newY / PacManGame.SIZE] != 3
                && MESH[newX / PacManGame.SIZE][newY / PacManGame.SIZE] != 4) {
            object.setX(newX);
            object.setY(newY);
        }
    }

    public static void movePacMan(int dx, int dy) {
        move(PacManGame.pacMan.getSprite(), dx, dy);
    }

    public static void moveGhost(Ghost ghost) {
        Rectangle object = ghost.getSprite();
        int[][] MESH = PacManGame.MESH;

        int currDx = ghost.getDx();
        int currDy = ghost.getDy();

        if (!canMove(ghost, currDx, currDy, MESH)) {
            int[] newDirection = findNewDirection(ghost, MESH);
            currDx = newDirection[0];
            currDy = newDirection[1];
            ghost.setDx(currDx);
            ghost.setDy(currDy);
        }

        int newX = (int) object.getX() + currDx * PacManGame.SIZE;
        int newY = (int) object.getY() + currDy * PacManGame.SIZE;

        ghost.setX(newX / PacManGame.SIZE);
        ghost.setY(newY / PacManGame.SIZE);

   
        if (newX >= 0 && newX < PacManGame.XMAX
                && newY >= 0 && newY < PacManGame.YMAX
                && MESH[newX / PacManGame.SIZE][newY / PacManGame.SIZE] != 3) {
            object.setX(newX);
            object.setY(newY);
        }
    }

    private static boolean canMove(Ghost ghost, int dx, int dy, int[][] MESH) {
        int nextCellX = ghost.getX() + dx; 
        int nextCellY = ghost.getY() + dy;

        if (nextCellX < 0 || nextCellX >= PacManGame.XMAX / PacManGame.SIZE
                || nextCellY < 0 || nextCellY >= PacManGame.YMAX / PacManGame.SIZE) {
            return false;
        }

        if (MESH[nextCellX][nextCellY] == 3) {
            return false;
        }
        return true;
    }

    public static int[] findNewDirection(Ghost ghost, int[][] MESH) {
        List<int[]> directions = Arrays.asList(
                new int[] { 0, -1 }, 
                new int[] { 0, 1 },
                new int[] { -1, 0 }, 
                new int[] { 1, 0 } 
        );

        Collections.shuffle(directions);

        for (int[] d : directions) {
            if (canMove(ghost, d[0], d[1], MESH)) {
                return d;
            }
        }

        return new int[] { 0, 0 };
    }

}
