package com.example.pacman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PacManGame extends Application {

    public static final int MOVE = 16;
    public static final int SIZE = 16;

    public static final int XMAX = SIZE * 28;
    public static final int YMAX = SIZE * 31;

    public static final int initalX = 14; 
    public static final int initalY = 23; 

    private static Pane gameBoard = new Pane();

    public static int[][] MESH = new int[XMAX / SIZE][YMAX / SIZE];

    private static Scene scene = new Scene(gameBoard, XMAX + 192, YMAX);

    private static boolean game = true;

    @Override
    public void start(Stage stage) throws Exception {
        for (int[] a : MESH) {
            Arrays.fill(a, 0);
        }

        // Загрузка файла карты
        String filePath = getClass().getResource("/com/example/PacManMAP.txt").getPath();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(" ");
                for (int col = 0; col < values.length; col++) {
                    MESH[col][row] = Integer.parseInt(values[col]);
                }
                row++;
            }
        }

        gameBoard.setStyle("-fx-background-color: black");

        // Отображение текста для счёта
        Text highScore = new Text("HIGH SCORE:");
        Text score = new Text("SCORE:");

        highScore.setStyle("-fx-fill: WHITE; -fx-font-size: 16px; -fx-font-weight: bold;");
        highScore.setX(XMAX + 16);
        highScore.setY(16);

        score.setStyle("-fx-fill: WHITE; -fx-font-size: 16px; -fx-font-weight: bold;");
        score.setX(XMAX + 16);
        score.setY(48);

        gameBoard.getChildren().addAll(highScore, score);

        // Отрисовка сетки
        for (int i = 0; i <= XMAX; i += MOVE) {
            Line line = new Line(i, 0, i, YMAX);
            gameBoard.getChildren().add(line);
        }
        for (int i = 0; i < YMAX; i += MOVE) {
            Line line = new Line(0, i, XMAX, i);
            gameBoard.getChildren().add(line);
        }

        // Отрисовка карты
        for (int y = 0; y < MESH.length; y++) {
            for (int x = 0; x < MESH[y].length; x++) {
                Rectangle rect = new Rectangle(y * SIZE, x * SIZE, SIZE, SIZE);
                rect.setFill(Color.BLACK); 
                gameBoard.getChildren().add(rect); 
        
                switch (MESH[y][x]) {
                    case 3: 
                        rect.setFill(Color.BLUE);
                        break;
                    case 1: 
                        Circle point = new Circle(y * SIZE + SIZE / 2.0, x * SIZE + SIZE / 2.0, SIZE * 0.15, Color.YELLOW);
                        gameBoard.getChildren().add(point);
                        break;
                    case 2: 
                        Circle pill = new Circle(y * SIZE + SIZE / 2.0, x * SIZE + SIZE / 2.0, SIZE * 0.3, Color.RED);
                        gameBoard.getChildren().add(pill);
                        break;
                    default: 
                        break;
                }
            }
        }

        for (int i = 0; i <= XMAX; i += MOVE) {
            Line line = new Line(i, 0, i, YMAX);
            gameBoard.getChildren().addAll(line);
        }

        for (int i = 0; i < YMAX; i += MOVE) {
            Line line = new Line(0, i, XMAX, i);
            gameBoard.getChildren().addAll(line);
        }

        // Добавление Pac-Man
        Rectangle spawnPacMan = new Rectangle(initalX * SIZE, initalY * SIZE, SIZE, SIZE);
        Image image = new Image(getClass().getResource("/com/example/pacman.png").toString());
        spawnPacMan.setFill(new ImagePattern(image));
        gameBoard.getChildren().add(spawnPacMan);

        stage.setResizable(false);
        stage.setTitle("PACMAN");
        stage.setScene(scene);
        stage.show();
    }
}
