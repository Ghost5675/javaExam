package com.example.pacman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;

import com.example.tetris.FormFigure;

public class PacManGame extends Application {
    public static final int MOVE = 16;
    public static final int SIZE = 16;

    public static final int XMAX = SIZE * 28;
    public static final int YMAX = SIZE * 31;

    public static final int initalXPacMan = 14;
    public static final int initalYPacMan = 23;

    public static final int initalXBlinky = 15;
    public static final int initalYBlinky = 11;

    public static final int initalXPinky = 11;
    public static final int initalYPinky = 14;

    public static final int initalXInky = 13;
    public static final int initalYInky = 14;

    public static final int initalXClyde = 15;
    public static final int initalYClyde = 14;

    public static PacMan pacMan = new PacMan(initalXPacMan, initalYPacMan, "/com/example/forPacMan/pacman.png", SIZE);
    public static Ghost blinky = new Ghost(initalXBlinky, initalYBlinky, "/com/example/forPacMan/blinky.png", SIZE,
            "Red");
    public static Ghost pinky = new Ghost(initalXPinky, initalYPinky, "/com/example/forPacMan/pinky.png", SIZE, "Pink");
    public static Ghost inky = new Ghost(initalXInky, initalYInky, "/com/example/forPacMan/inky.png", SIZE, "Cyan");
    public static Ghost clyde = new Ghost(initalXClyde, initalYClyde, "/com/example/forPacMan/clyde.png", SIZE,
            "Oreange");

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
        String filePath = getClass().getResource("/com/example/forPacMan/PacManMAP.txt").getPath();
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
                        Circle point = new Circle(y * SIZE + SIZE / 2.0, x * SIZE + SIZE / 2.0, SIZE * 0.15,
                                Color.YELLOW);
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

        gameBoard.getChildren().add(pacMan.getSprite());
        gameBoard.getChildren().add(blinky.getSprite());
        gameBoard.getChildren().add(pinky.getSprite());
        gameBoard.getChildren().add(inky.getSprite());
        gameBoard.getChildren().add(clyde.getSprite());

        moveOnKeyPress();

        stage.setResizable(false);
        stage.setTitle("PACMAN");
        stage.setScene(scene);
        stage.show();
    }

    private Timer currentTimer = null; // Общий таймер для движения
    private TimerTask currentTask = null; // Текущая задача движения

    private void moveOnKeyPress() {
        scene.setOnKeyPressed(event -> {
            // Остановить текущий таймер, если он существует
            if (currentTimer != null) {
                currentTimer.cancel();
                currentTimer = null;
                currentTask = null;
            }

            // Создать новый таймер и задачу для движения
            currentTimer = new Timer();
            switch (event.getCode()) {
                case W:
                    currentTask = createMovementTask(0, -1); // Движение вверх
                    break;
                case S:
                    currentTask = createMovementTask(0, 1); // Движение вниз
                    break;
                case A:
                    currentTask = createMovementTask(-1, 0); // Движение влево
                    break;
                case D:
                    currentTask = createMovementTask(1, 0); // Движение вправо
                    break;
                default:
                    return; // Ничего не делать для других клавиш
            }

            // Запуск новой задачи с интервалом 100 мс
            currentTimer.schedule(currentTask, 0, 150);
        });
    }

    // Метод для создания задачи движения
    private TimerTask createMovementTask(int dx, int dy) {
        return new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    PacManController.movePacMan(dx, dy);
                });
            }
        };
    }
}
