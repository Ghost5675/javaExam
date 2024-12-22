package com.example.pacman;

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

    public int getY() {
        return y;
    }

    public void move(int dx, int dy, int size) {
        x += dx;
        y += dy;
        sprite.setX(x * size);
        sprite.setY(y * size);
    }
}

class PacMan extends PacMAnCharacter {
    public static boolean isPill = true;

    public PacMan(int x, int y, String imagePath, int size) {
        super(x, y, imagePath, size);
    }

    // Специфическое поведение Pac-Man (если нужно)
    public void eat() {
        System.out.println("Pac-Man съел точку!");
    }
}

class Ghost extends PacMAnCharacter {
    private String color;

    public Ghost(int x, int y, String imagePath, int size, String color) {
        super(x, y, imagePath, size);
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    // Специфическое поведение призраков
    public void chase() {
        System.out.println(color + " призрак преследует Pac-Man!");
    }
    
    public void Scatter() {
        System.out.println(color + " призрак разбегается");
    }

    public void Frightened() {
        if (PacMan.isPill) {
            getSprite().setFill(new ImagePattern(new Image(getClass().getResource("/com/example/forPacMan/frightness.png").toString())));;
        }
        System.out.println(color + " призрак боится");
    }

    
}