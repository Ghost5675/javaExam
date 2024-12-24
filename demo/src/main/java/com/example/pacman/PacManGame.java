package com.example.pacman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.DatabaseConnection;

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
            "blinky");
    public static Ghost pinky = new Ghost(initalXPinky, initalYPinky, "/com/example/forPacMan/pinky.png", SIZE, "pinky");
    public static Ghost inky = new Ghost(initalXInky, initalYInky, "/com/example/forPacMan/inky.png", SIZE, "inky");
    public static Ghost clyde = new Ghost(initalXClyde, initalYClyde, "/com/example/forPacMan/clyde.png", SIZE,
            "clyde");

    private static Pane gameBoard = new Pane();

    public static int[][] MESH = new int[XMAX / SIZE][YMAX / SIZE];

    private static Scene scene = new Scene(gameBoard, XMAX + 192, YMAX);

    private static boolean game = true;

    private static int live = 3;

    Text highScore = new Text("HIGH SCORE: " + DatabaseConnection.getHighScore("pacman", DatabaseConnection.currentEmail));
    Text score = new Text("SCORE: 0");
    Text Live = new Text("Live: " + live);
    Text user = new Text("User: " + DatabaseConnection.currentUser);

    int scoreUpdate = 0;
    int highScoreValue = 0;

    @Override
    public void start(Stage stage) throws Exception {
        for (int[] a : MESH) {
            Arrays.fill(a, 0);
        }

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

        highScore.setStyle("-fx-fill: WHITE; -fx-font-size: 16px; -fx-font-weight: bold;");
        highScore.setX(XMAX + 16);
        highScore.setY(16);

        score.setStyle("-fx-fill: WHITE; -fx-font-size: 16px; -fx-font-weight: bold;");
        score.setX(XMAX + 16);
        score.setY(48);

        Live.setStyle("-fx-fill: WHITE; -fx-font-size: 16px; -fx-font-weight: bold;");
        Live.setX(XMAX + 16);
        Live.setY(YMAX - 80);

        user.setStyle("-fx-fill: WHITE; -fx-font-size: 16px; -fx-font-weight: bold;");
        user.setX(XMAX + 16);
        user.setY(YMAX - 32);

        gameBoard.getChildren().addAll(highScore, score, Live, user);

        for (int i = 0; i <= XMAX; i += MOVE) {
            Line line = new Line(i, 0, i, YMAX);
            gameBoard.getChildren().add(line);
        }
        for (int i = 0; i < YMAX; i += MOVE) {
            Line line = new Line(0, i, XMAX, i);
            gameBoard.getChildren().add(line);
        }

        for (int y = 0; y < MESH.length; y++) {
            for (int x = 0; x < MESH[y].length; x++) {
                Rectangle rect = new Rectangle(y * SIZE, x * SIZE, SIZE, SIZE);
                rect.setFill(Color.BLACK);
                gameBoard.getChildren().add(rect);

                switch (MESH[y][x]) {
                    case 4:
                        rect.setFill(Color.GRAY);
                        break;

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

    private Timer currentTimer = null;
    private TimerTask currentTask = null;

    private void moveOnKeyPress() {
        scene.setOnKeyPressed(event -> {
            if (currentTimer != null) {
                currentTimer.cancel();
                currentTimer = null;
                currentTask = null;
            }

            currentTimer = new Timer();
            switch (event.getCode()) {
                case W:
                    currentTask = createMovementTask(0, -1);
                    break;
                case A:
                    currentTask = createMovementTask(-1, 0);
                    break;
                case S:
                    currentTask = createMovementTask(0, 1);
                    break;
                case D:
                    currentTask = createMovementTask(1, 0);
                    break;
                default:
                    return;
            }

            currentTimer.schedule(currentTask, 0, 150);
        });
    }

    private TimerTask createMovementTask(int dx, int dy) {
        return new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    checkAndEatPoint();
                    checkAndEatPill();
                    checkAndEatGhost();
                    PacManController.movePacMan(dx, dy);
                });
            }
        };
    }

    private void checkAndEatPoint() {
        // Текущие координаты Pac-Man в сетке
        int pacManX = (int) (pacMan.getX());
        int pacManY = (int) (pacMan.getY());

        if (MESH[pacManX][pacManY] == 1) {
            MESH[pacManX][pacManY] = 0;
            scoreUpdate += 10;
            score.setText("SCORE: " + scoreUpdate);

            updateHighScore();

            removePointFromGameBoard(pacManX, pacManY);
        }
    }

    private void checkAndEatPill() {
        int pacManX = (int) (pacMan.getX());
        int pacManY = (int) (pacMan.getY());

        if (MESH[pacManX][pacManY] == 2) {
            startFrightenedMode();

            scoreUpdate += 50;
            score.setText("SCORE: " + scoreUpdate);

            updateHighScore();

            MESH[pacManX][pacManY] = 0;
            removePointFromGameBoard(pacManX, pacManY);

        }

    }

    private void checkAndEatGhost() {
        int pacManX = (int) (pacMan.getX());
        int pacManY = (int) (pacMan.getY());

        Ghost[] ghosts = { blinky, pinky, inky, clyde };

        for (Ghost ghost : ghosts) {
            int ghostX = (int) (ghost.getX());
            int ghostY = (int) (ghost.getY());

            if (ghostX == pacManX && ghostY == pacManY) {
                System.out.println(ghostX + " " + ghost.getname() + " " + ghostY);
                if (ghost.isEaten) {
                    ghost.setX(11 * SIZE);
                    ghost.setY(14 * SIZE);

                    ghost.getSprite().setX(11 * SIZE);
                    ghost.getSprite().setY(14 * SIZE);

                    scoreUpdate += 200;
                    score.setText("SCORE: " + scoreUpdate);
                    updateHighScore();

                    System.out.println(ghost.getname() + " съеден!");
                } else {

                    live -= 1;
                    Live.setText("Live: " + live);

                    pacMan.setX(initalXPacMan * SIZE);
                    pacMan.setY(initalYPacMan * SIZE);

                    pacMan.getSprite().setX(initalXPacMan * SIZE);
                    pacMan.getSprite().setY(initalYPacMan * SIZE);

                    if (live <= 0) {
                        System.out.println("Игра окончена!");
                        gameOver();
                    }

                }
            }
        }
    }

    private void startFrightenedMode() {
        blinky.Frightened();
        pinky.Frightened();
        inky.Frightened();
        clyde.Frightened();

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        Runnable task = () -> {
            Platform.runLater(() -> {
                
                Ghost[] ghosts = { blinky, pinky, inky, clyde };
                for (Ghost ghost : ghosts) {
                    ghost.NormalMode();
                    
                }
            });
            scheduler.shutdown();
        };

        System.out.println("Режим страха активен на 5 секунд...");
        scheduler.schedule(task, 5, TimeUnit.SECONDS);
    }

    private void removePointFromGameBoard(int x, int y) {
        gameBoard.getChildren().removeIf(node -> {
            if (node instanceof Circle) {
                Circle point = (Circle) node;
                return (int) point.getCenterX() / SIZE == x && (int) point.getCenterY() / SIZE == y;
            }
            return false;
        });
    }

    private void updateHighScore() {
        if (scoreUpdate > highScoreValue) {
            highScoreValue = scoreUpdate;
            highScore.setText("HIGH SCORE: " + highScoreValue);
        }
    }

    

    private void gameOver(){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText("You lost all lives!");
            alert.setContentText("Final Score: " + scoreUpdate);
            alert.showAndWait();
            DatabaseConnection.changeScore(DatabaseConnection.currentEmail, highScoreValue, "pacman");
            
        });
    }
}
