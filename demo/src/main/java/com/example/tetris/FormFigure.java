package com.example.tetris;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FormFigure {
    Rectangle a;
    Rectangle b;
    Rectangle c;
    Rectangle d;

    Color color;

    private String name;
    public int form = 1;

    public FormFigure(Rectangle a, Rectangle b, Rectangle c, Rectangle d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public FormFigure(Rectangle a, Rectangle b, Rectangle c, Rectangle d, String name) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.name = name;
        switch (name) {
            case "j":
                color = Color.BLUE;
                break;
            case "l":
                color = Color.ORANGE;
                break;
            case "o":
                color = Color.YELLOW;
                break;
            case "s":
                color = Color.GREEN;
                break;
            case "t":
                color = Color.MAGENTA;
                break;
            case "z":
                color = Color.RED;
                break;
            case "i":
                color = Color.CYAN;
                break;

        }
        this.a.setFill(color);
        this.b.setFill(color);
        this.c.setFill(color);
        this.d.setFill(color);
    }

    public String getName() {
        return name;
    }

    public void changeForm() {
        if (form != 4) {
            form++;
        } else {
            form = 1;
        }
    }

}
