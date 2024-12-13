package com.example.tetris;


import java.util.Random;

public class Tetromino {
    private static final int[][][] SHAPES = {
        {{0, 0}, {1, 0}, {0, 1}, {1, 1}}, // O
        {{0, 0}, {1, 0}, {2, 0}, {3, 0}}, // I
        {{0, 0}, {0, 1}, {1, 1}, {2, 1}}, // J
        {{2, 0}, {0, 1}, {1, 1}, {2, 1}}, // L
        {{0, 0}, {1, 0}, {1, 1}, {2, 1}}, // Z
        {{1, 0}, {2, 0}, {0, 1}, {1, 1}}, // S
        {{0, 0}, {1, 0}, {2, 0}, {1, 1}}  // T
    };

    private int[][] blocks;
    private int x, y; // Position on the board

    public Tetromino(int[][] shape) {
        this.blocks = shape;
        this.x = 4; // Default starting x position
        this.y = 0; // Default starting y position
    }

    public static Tetromino randomTetromino() {
        Random random = new Random();
        int index = random.nextInt(SHAPES.length);
        return new Tetromino(SHAPES[index]);
    }

    public int[][] getBlocks() {
        return blocks;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveDown() {
        y++;
    }

    public void moveLeft() {
        x--;
    }

    public void moveRight() {
        x++;
    }

    public void rotate() {
        for (int i = 0; i < blocks.length; i++) {
            int temp = blocks[i][0];
            blocks[i][0] = -blocks[i][1];
            blocks[i][1] = temp;
        }
    }

    public void undoRotate() {
        for (int i = 0; i < blocks.length; i++) {
            int temp = blocks[i][1];
            blocks[i][1] = -blocks[i][0];
            blocks[i][0] = temp;
        }
    }
}
