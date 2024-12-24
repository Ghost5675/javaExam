package com.example.pacman;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class PacMAnCharacter {
    private Rectangle sprite;
    private int x;
    private int y;

    public PacMAnCharacter(int x, int y, String imagePath, int size) {
        this.x = x;
        this.y = y;
        this.sprite = new Rectangle(x * size, y * size, size, size);

        Image image = new Image(getClass().getResource(imagePath).toString());
        this.sprite.setFill(new ImagePattern(image));
    }

    public Rectangle getSprite() {
        return sprite;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void move(int dx, int dy, int size) {
        x += dx;
        y += dy;
        sprite.setX(x * size);
        sprite.setY(y * size);
    }
}

class PacMan extends PacMAnCharacter {

    public PacMan(int x, int y, String imagePath, int size) {
        super(x, y, imagePath, size);
    }
}

class Ghost extends PacMAnCharacter {
    public enum GhostMode {
        CHASE, SCATTER, FRIGHTENED
    }

    private GhostMode currentMode = GhostMode.SCATTER;
    public boolean isEaten = false;
    private String name;
    private int initialX;
    private int initialY;
    private int dx;
    private int dy;
    private int[][] MESH = PacManGame.MESH;

    public Ghost(int x, int y, String imagePath, int size, String name) {
        super(x, y, imagePath, size);
        this.name = name;
        this.initialX = x;
        this.initialY = y;

        int[] initialDir = getRandomDirectionWithoutWall(x, y, MESH);
        this.dx = initialDir[0];
        this.dy = initialDir[1];

    }

    private static int[] getRandomDirectionWithoutWall(int startX, int startY, int[][] MESH) {

        List<int[]> directions = Arrays.asList(
                new int[] { 0, -1 }, 
                new int[] { 0, 1 }, 
                new int[] { -1, 0 }, 
                new int[] { 1, 0 } 
        );

        Collections.shuffle(directions);

        for (int[] d : directions) {
            int nx = startX + d[0];
            int ny = startY + d[1];

            if (nx >= 0 && nx < (PacManGame.XMAX / PacManGame.SIZE)
                    && ny >= 0 && ny < (PacManGame.YMAX / PacManGame.SIZE)) {

                if (MESH[nx][ny] != 3) {
                    return d;
                }
            }
        }

        return new int[] { 0, 0 };
    }

    public int getInitialX() {
        return initialX;
    }

    public int getInitialY() {
        return initialY;
    }

    public String getname() {
        return name;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public void chase() {
        if (this.name == "blinky") {
            System.out.println("Blinky преследует Pac-man");
        }
        if (this.name == "blinky") {
            System.out.println("Blinky преследует Pac-man");
        }
        if (this.name == "blinky") {
            System.out.println("Blinky преследует Pac-man");
        }
        if (this.name == "blinky") {
            System.out.println("Blinky преследует Pac-man");
        }
        System.out.println(name + " призрак преследует Pac-Man!");
    }

    public void scatter() {
        if (this.name == "blinky") {
            System.out.println(getX() + " " + getY());
            System.out.println("Blinky преследует Pac-man");
        }
        if (this.name == "pinky") {
            System.out.println("pinky преследует Pac-man");
        }
        if (this.name == "inky") {
            System.out.println("inky преследует Pac-man");
        }
        if (this.name == "clyde") {
            System.out.println("clyde преследует Pac-man");
        }
        System.out.println(name + " призрак разбегается");
    }

    public void Frightened() {
        this.isEaten = true;

        getSprite().setFill(new ImagePattern(
                new Image(getClass().getResource("/com/example/forPacMan/frightness.png").toString())));

        System.out.println(name + " призрак боится");
    }

    public void NormalMode() {
        this.isEaten = false;

        getSprite().setFill(new ImagePattern(
                new Image(getClass().getResource("/com/example/forPacMan/" + name.toLowerCase() + ".png").toString())));
        System.out.println(name + " призрак вернулся в нормальное состояние");
    }

    public void setMode(GhostMode mode) {
        this.currentMode = mode;
        switch (mode) {
            case CHASE:
                System.out.println(name + " перешёл в режим преследования.");
                chase();
                break;
            case SCATTER:
                System.out.println(name + " разбегается по углам.");
                scatter();
                break;
            case FRIGHTENED:
                Frightened();
                break;
        }
    }

    public GhostMode getMode() {
        return currentMode;
    }

}