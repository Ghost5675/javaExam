package com.example.tetris;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.DatabaseConnection;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TetrisGame extends Application {

	public static final int MOVE = 25;
	public static final int SIZE = 25;

	public static int XMAX = SIZE * 12;
	public static int YMAX = SIZE * 24;

	public static int[][] MESH = new int[XMAX / SIZE][YMAX / SIZE];

	private static Pane group = new Pane();
	private static FormFigure object;

	private static Scene scene = new Scene(group, XMAX + 200, YMAX);

	public static int score = 0;
	private static int top = 0;
	private static int linesNo = 0;
	private static int highScore = DatabaseConnection.getHighScore("tetris", DatabaseConnection.currentEmail);

	private static boolean game = true;

	private static FormFigure nextObj = Controller.makeRect();

	Button reset = new Button("Reset");
	Text scoretext = new Text("Score: " + score);
	Text level = new Text("Lines: " + linesNo);
	Text Highscoretext = new Text(
			"High Score: " + highScore);
	public static Timer fall = new Timer();

	@Override
	public void start(Stage stage) throws Exception {
		reset.setFocusTraversable(false);

		if (fall != null) {
			fall.cancel();
			fall.purge(); 
		}
		for (int[] a : MESH) {
			Arrays.fill(a, 0);
		}

		for (int i = 0; i < XMAX; i += MOVE) {
			Line line = new Line(i, 0, i, YMAX);
			line.setStroke(Color.web("#33e1ed"));
			group.getChildren().addAll(line);
		}

		for (int i = 0; i <= YMAX; i += MOVE) {
			Line line = new Line(0, i, XMAX, i);
			line.setStroke(Color.web("#33e1ed"));
			group.getChildren().addAll(line);
		}

		Line line = new Line(XMAX, 0, XMAX, YMAX);
		group.setStyle("-fx-background-color: #2d2d44;");
		scoretext.setStyle("-fx-font: 20 arial; -fx-fill: white;");
		scoretext.setY(50);
		scoretext.setX(XMAX + 5);

		reset.setStyle("-fx-font: 20 arial; -fx-fill: white; -fx-background-color: #33e1ed;");
		reset.setLayoutX(XMAX + 5);
		reset.setLayoutY(YMAX - 50);

		Highscoretext.setStyle("-fx-font: 20 arial; -fx-fill: white;");
		Highscoretext.setY(100);
		Highscoretext.setX(XMAX + 5);

		level.setStyle("-fx-font: 20 arial; -fx-fill: white;");
		level.setY(150);
		level.setX(XMAX + 5);
		level.setFill(Color.GREEN);

		group.getChildren().addAll(scoretext, line, level, Highscoretext, reset);

		FormFigure a = nextObj;
		group.getChildren().addAll(a.a, a.b, a.c, a.d);
		moveOnKeyPress(a);
		object = a;
		nextObj = Controller.makeRect();

		group.getChildren().clear();

		resetGame();

		stage.setScene(scene);
		stage.setTitle("T E T R I S");
		stage.show();
		fall = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						if (object.a.getY() == 0 || object.b.getY() == 0 || object.c.getY() == 0
								|| object.d.getY() == 0)
							top++;
						else
							top = 0;
						if (top == 2) {
							Text over = new Text("GAME OVER");
							over.setFill(Color.RED);
							over.setStyle("-fx-font: 70 arial;");
							over.setX(10);
							over.setY(10);
							group.getChildren().add(over);
							game = false;
						}

						if (top == 15) {
							System.exit(0);
						}
						if (game) {
							MoveDown(object);
							scoretext.setText("Score: " + score);
							level.setText("Lines: " + linesNo);
							if (score > highScore) {
								highScore = score;
								Highscoretext.setText("High Score: " + highScore);
							}
						} else {
							resetGame();
							if (highScore > DatabaseConnection.getHighScore("tetris",
									DatabaseConnection.currentEmail)) {
								DatabaseConnection.changeScore(DatabaseConnection.currentEmail, highScore, "tetris");
							}

						}
					}

				});
			}

		};
		fall.schedule(task, 0, 300);
		stage.setOnCloseRequest(event -> {
			if (fall != null) {
				fall.cancel();
				fall.purge(); 
			}
			Platform.exit();

		});
		reset.setOnAction(event -> {
			resetGame();
		});
	}

	private long lastManualDropTime = 0;
	private static final long MANUAL_DROP_DELAY = 50;

	private void moveOnKeyPress(FormFigure form) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
					case UP:
						MoveTurn(form);
						break;
					case LEFT:
						Controller.MoveLeft(form);
						break;
					case RIGHT:
						Controller.MoveRight(form);
						break;
					case DOWN:
						long currentTime = System.currentTimeMillis();
						if (currentTime - lastManualDropTime > MANUAL_DROP_DELAY) {
							lastManualDropTime = currentTime;
							MoveDown(form);
							score++;
							scoretext.setText("Score: " + score);
						}
						break;

					default:
						break;
				}
			}

		});

	}

	public static void MoveTurn(FormFigure form) {
		int f = form.form;
		Rectangle a = form.a;
		Rectangle b = form.b;
		Rectangle c = form.c;
		Rectangle d = form.d;

		switch (form.getName()) {
			case "j":
				if (f == 1 && cB(a, 2, -1) && cB(b, 1, -2) && cB(c, 0, -1) && cB(d, -1, 0)) {
					MoveRight(a);
					MoveRight(a);
					MoveUp(a);

					MoveRight(b);
					MoveUp(b);
					MoveUp(b);
					MoveUp(c);
					MoveLeft(d);
					form.changeForm();
					break;
				}
				if (f == 2 && cB(a, 0, 2) && cB(b, 1, 1) && cB(c, 0, 0) && cB(d, -1, -1)) {
					MoveDown(a);
					MoveDown(a);

					MoveRight(b);
					MoveDown(b);

					MoveUp(d);
					MoveLeft(d);
					form.changeForm();
					break;
				}
				if (f == 3 && cB(a, -2, 0) && cB(b, -1, 1) && cB(c, 0, 0) && cB(d, 1, -1)) {
					MoveLeft(a);
					MoveLeft(a);

					MoveDown(b);
					MoveLeft(b);

					MoveRight(d);
					MoveUp(d);
					form.changeForm();
					break;
				}
				if (f == 4 && cB(a, 0, -1) && cB(b, -1, 0) && cB(c, 0, 1) && cB(d, 1, 2)) {
					MoveUp(a);

					MoveLeft(b);

					MoveDown(c);

					MoveDown(d);
					MoveDown(d);
					MoveRight(d);
					form.changeForm();
					break;
				}
				break;

			case "l":
				if (f == 1 && cB(a, -1, 1) && cB(b, 0, -2) && cB(c, -1, -1) && cB(d, -2, 0)) {
					MoveLeft(a);
					MoveDown(a);

					MoveUp(b);
					MoveUp(b);

					MoveUp(c);
					MoveLeft(c);

					MoveLeft(d);
					MoveLeft(d);
					form.changeForm();
					break;
				}
				if (f == 2 && cB(a, -2, 0) && cB(b, 1, 1) && cB(c, 0, 0) && cB(d, -1, -1)) {
					MoveLeft(a);
					MoveLeft(a);

					MoveRight(b);
					MoveDown(b);

					MoveUp(d);
					MoveLeft(d);
					form.changeForm();
					break;
				}
				if (f == 3 && cB(a, 1, -2) && cB(b, 0, 1) && cB(c, 1, 0) && cB(d, 2, -1)) {
					MoveRight(a);
					MoveUp(a);
					MoveUp(a);

					MoveDown(b);

					MoveRight(c);

					MoveRight(d);
					MoveRight(d);
					MoveUp(d);
					form.changeForm();
					break;
				}
				if (f == 4 && cB(a, 2, 1) && cB(b, -1, 0) && cB(c, 0, 1) && cB(d, 1, 2)) {
					MoveDown(a);
					MoveRight(a);
					MoveRight(a);

					MoveLeft(b);

					MoveDown(c);

					MoveDown(d);
					MoveDown(d);
					MoveRight(d);
					form.changeForm();
					break;
				}
				break;
			case "i":
				if (f == 1 && cB(a, 1, -2) && cB(b, 0, -1) && cB(c, 0, -1) && cB(d, -2, 1)) {
					MoveRight(a);
					MoveUp(a);
					MoveUp(a);

					MoveUp(b);

					MoveLeft(c);

					MoveLeft(d);
					MoveLeft(d);
					MoveDown(d);

					form.changeForm();
					break;
				}
				if (f == 2 && cB(a, 2, 1) && cB(b, 0, 0) && cB(c, 0, 1) && cB(d, -1, -2)) {
					MoveRight(a);
					MoveRight(a);
					MoveDown(a);

					MoveRight(b);

					MoveUp(c);

					MoveUp(d);
					MoveUp(d);
					MoveLeft(d);

					form.changeForm();
					break;
				}
				if (f == 3 && cB(a, -1, 2) && cB(b, 0, 1) && cB(c, 1, 0) && cB(d, 2, -1)) {
					MoveLeft(a);
					MoveDown(a);
					MoveDown(a);

					MoveDown(b);

					MoveRight(c);

					MoveRight(d);
					MoveRight(d);
					MoveUp(d);

					form.changeForm();
					break;
				}
				if (f == 4 && cB(a, -2, -1) && cB(b, -1, 0) && cB(c, 0, 1) && cB(d, 1, 2)) {
					MoveLeft(a);
					MoveLeft(a);
					MoveUp(a);

					MoveLeft(b);

					MoveDown(c);

					MoveRight(d);
					MoveDown(d);
					MoveDown(d);

					form.changeForm();
					break;
				}

			case "o":
				// "O" shape does not rotate
				break;

			case "t":
				if (f == 1 && cB(a, 1, -1) && cB(b, 0, 0) && cB(c, -1, -1) && cB(d, -1, 1)) {
					MoveRight(a);
					MoveUp(a);

					MoveLeft(c);
					MoveUp(c);

					MoveLeft(d);
					MoveDown(d);

					form.changeForm();
					break;
				}
				if (f == 2 && cB(a, 1, 1) && cB(b, 0, 0) && cB(c, 1, -1) && cB(d, -1, -1)) {
					MoveRight(a);
					MoveDown(a);

					MoveRight(c);
					MoveUp(c);

					MoveLeft(d);
					MoveUp(d);
					form.changeForm();
					break;
				}
				if (f == 3 && cB(a, -1, 1) && cB(b, 0, 0) && cB(c, 1, 1) && cB(d, 1, -1)) {
					MoveLeft(a);
					MoveDown(a);

					MoveRight(c);
					MoveDown(c);

					MoveRight(d);
					MoveUp(d);
					form.changeForm();
					break;
				}
				if (f == 4 && cB(a, -1, -1) && cB(b, 0, 0) && cB(c, -1, 1) && cB(d, 1, 1)) {
					MoveLeft(a);
					MoveUp(a);

					MoveLeft(c);
					MoveDown(c);

					MoveRight(d);
					MoveDown(d);
					form.changeForm();
					break;
				}
				break;

			case "z":
				if (f == 1 && cB(a, -1, 0) && cB(b, 0, 1) && cB(c, 0, -1) && cB(d, -1, -2)) {
					MoveLeft(a);

					MoveDown(b);

					MoveUp(c);

					MoveLeft(d);
					MoveUp(d);
					MoveUp(d);

					form.changeForm();
					break;
				}
				if (f == 2 && cB(a, 2, -1) && cB(b, 1, -2) && cB(c, 1, 0) && cB(d, 2, 1)) {
					MoveRight(a);
					MoveRight(a);
					MoveUp(a);

					MoveRight(b);
					MoveUp(b);
					MoveUp(b);

					MoveRight(c);

					MoveRight(d);
					MoveRight(d);
					MoveDown(d);
					form.changeForm();
					break;
				}
				if (f == 3 && cB(a, -1, 2) && cB(b, 0, 3) && cB(c, 0, 1) && cB(d, -1, 0)) {
					MoveLeft(a);
					MoveDown(a);
					MoveDown(a);

					MoveDown(b);
					MoveDown(b);
					MoveDown(b);

					MoveDown(c);

					MoveLeft(d);
					form.changeForm();
					break;
				}
				if (f == 4 && cB(a, 0, -1) && cB(b, -1, -2) && cB(c, -1, 0) && cB(d, 0, 1)) {
					MoveUp(a);

					MoveLeft(b);
					MoveUp(b);
					MoveUp(b);

					MoveLeft(c);

					MoveDown(d);
					form.changeForm();
					break;
				}
				break;

			case "s":
				if (f == 1 && cB(a, -1, 1) && cB(b, 0, 0) && cB(c, -1, -1) && cB(d, 0, -2)) {
					MoveLeft(a);
					MoveDown(a);

					MoveUp(c);
					MoveLeft(c);

					MoveUp(d);
					MoveUp(d);

					form.changeForm();
					break;
				}
				if (f == 2 && cB(a, -1, -1) && cB(b, 0, 0) && cB(c, 1, -1) && cB(d, 2, 0)) {
					MoveUp(a);
					MoveLeft(a);

					MoveRight(c);
					MoveUp(c);

					MoveRight(d);
					MoveRight(d);
					form.changeForm();
					break;
				}
				if (f == 3 && cB(a, 1, -1) && cB(b, 0, 0) && cB(c, 1, 1) && cB(d, 0, 2)) {
					MoveRight(a);
					MoveUp(a);

					MoveRight(c);
					MoveDown(c);

					MoveDown(d);
					MoveDown(d);
					form.changeForm();
					break;
				}
				if (f == 4 && cB(a, 1, 1) && cB(b, 0, 0) && cB(c, -1, 1) && cB(d, -2, 0)) {
					MoveRight(a);
					MoveDown(a);

					MoveLeft(c);
					MoveDown(c);

					MoveLeft(d);
					MoveLeft(d);

					form.changeForm();
					break;
				}
				break;
		}
	}

	private static void RemoveRows(Pane pane) {
		ArrayList<Integer> lines = new ArrayList<>();
		ArrayList<Node> newrects = new ArrayList<>();
		int full = 0;

		for (int i = 0; i < MESH[0].length; i++) {
			for (int j = 0; j < MESH.length; j++) {
				if (MESH[j][i] == 1) {
					full++;
				}
			}
			if (full == MESH.length) {
				lines.add(i); 
			}
			full = 0;
		}

		if (!lines.isEmpty()) {
			for (int line : lines) {
				List<Node> toRemove = new ArrayList<>();
				for (Node node : pane.getChildren()) {
					if (node instanceof Rectangle) {
						Rectangle rect = (Rectangle) node;
						if (rect.getY() == line * SIZE) {
							MESH[(int) rect.getX() / SIZE][(int) rect.getY() / SIZE] = 0;
							toRemove.add(node);
						} else {
							newrects.add(node);
						}
					}
				}
				pane.getChildren().removeAll(toRemove);
			}

			for (Node node : newrects) {
				if (node instanceof Rectangle) {
					Rectangle rect = (Rectangle) node;
					int originalY = (int) rect.getY() / SIZE;

					long rowsBelow = lines.stream().filter(line -> originalY < line).count();

					if (rowsBelow > 0) {
						int oldX = (int) rect.getX() / SIZE;
						int oldY = (int) rect.getY() / SIZE;
						MESH[oldX][oldY] = 0; 
						rect.setY(rect.getY() + rowsBelow * SIZE);
					}
				}
			}

			for (Node node : pane.getChildren()) {
				if (node instanceof Rectangle) {
					Rectangle rect = (Rectangle) node;
					try {
						int x = (int) rect.getX() / SIZE;
						int y = (int) rect.getY() / SIZE;
						MESH[x][y] = 1;
					} catch (ArrayIndexOutOfBoundsException e) {
						System.out.println("ERROR: " + e);
					}
				}
			}
		}

		// Увеличиваем счет
		score += 50 * lines.size();
		linesNo += lines.size();
	}

	private static void MoveDown(Rectangle rect) {
		if (rect.getY() + MOVE < YMAX) {
			rect.setY(rect.getY() + MOVE);
		}
	}

	private static void MoveRight(Rectangle rect) {
		if (rect.getX() + MOVE < XMAX) {
			rect.setX(rect.getX() + MOVE);
		}
	}

	private static void MoveLeft(Rectangle rect) {
		if (rect.getX() - MOVE >= 0) {
			rect.setX(rect.getX() - MOVE);
		}
	}

	private static void MoveUp(Rectangle rect) {
		if (rect.getY() - MOVE > 0) {
			rect.setY(rect.getY() - MOVE);
		}
	}

	private void MoveDown(FormFigure form) {
		if (form.a.getY() == YMAX - SIZE || form.b.getY() == YMAX - SIZE || form.c.getY() == YMAX - SIZE
				|| form.d.getY() == YMAX - SIZE || moveA(form) || moveB(form) || moveC(form) || moveD(form)) {
			MESH[(int) form.a.getX() / SIZE][(int) form.a.getY() / SIZE] = 1;
			MESH[(int) form.b.getX() / SIZE][(int) form.b.getY() / SIZE] = 1;
			MESH[(int) form.c.getX() / SIZE][(int) form.c.getY() / SIZE] = 1;
			MESH[(int) form.d.getX() / SIZE][(int) form.d.getY() / SIZE] = 1;
			RemoveRows(group);

			FormFigure a = nextObj;
			nextObj = Controller.makeRect();
			object = a;
			group.getChildren().addAll(a.a, a.b, a.c, a.d);
			moveOnKeyPress(a);

		}

		if (form.a.getY() + MOVE < YMAX && form.b.getY() + MOVE < YMAX && form.c.getY() + MOVE < YMAX
				&& form.d.getY() + MOVE < YMAX) {
			int movea = MESH[(int) form.a.getX() / SIZE][((int) form.a.getY() / SIZE) + 1];
			int moveb = MESH[(int) form.b.getX() / SIZE][((int) form.b.getY() / SIZE) + 1];
			int movec = MESH[(int) form.c.getX() / SIZE][((int) form.c.getY() / SIZE) + 1];
			int moved = MESH[(int) form.d.getX() / SIZE][((int) form.d.getY() / SIZE) + 1];
			if (movea == 0 && movea == moveb && moveb == movec && movec == moved) {
				form.a.setY(form.a.getY() + MOVE);
				form.b.setY(form.b.getY() + MOVE);
				form.c.setY(form.c.getY() + MOVE);
				form.d.setY(form.d.getY() + MOVE);
			}
		}
	}

	private boolean moveA(FormFigure form) {
		return (MESH[(int) form.a.getX() / SIZE][((int) form.a.getY() / SIZE) + 1] == 1);
	}

	private boolean moveB(FormFigure form) {
		return (MESH[(int) form.b.getX() / SIZE][((int) form.b.getY() / SIZE) + 1] == 1);
	}

	private boolean moveC(FormFigure form) {
		return (MESH[(int) form.c.getX() / SIZE][((int) form.c.getY() / SIZE) + 1] == 1);
	}

	private boolean moveD(FormFigure form) {
		return (MESH[(int) form.d.getX() / SIZE][((int) form.d.getY() / SIZE) + 1] == 1);
	}

	private static boolean cB(Rectangle rect, int x, int y) {
		try {
			int newX = ((int) rect.getX() / SIZE) + x;
			int newY = ((int) rect.getY() / SIZE) + y;

			if (newX <= 0 || newX >= MESH.length || newY <= 0 || newY >= MESH[0].length) {
				return false;
			}

			return MESH[newX][newY] == 0;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	public void resetGame() {
		reset.setFocusTraversable(false);

		group.getChildren().clear();

		for (int[] row : MESH) {
			Arrays.fill(row, 0);
		}

		score = 0;
		linesNo = 0;
		top = 0;
		game = true;

		object = Controller.makeRect();
		nextObj = Controller.makeRect();

		for (int i = 0; i <= XMAX; i += MOVE) {
			Line line = new Line(i, 0, i, YMAX);
			line.setStroke(Color.web("#33e1ed"));
			group.getChildren().add(line);
		}
		for (int i = 0; i <= YMAX; i += MOVE) {
			Line line = new Line(0, i, XMAX, i);
			line.setStroke(Color.web("#33e1ed"));
			group.getChildren().add(line);
		}


		scoretext.setText("Score: 0");
		level.setText("Lines: 0");
		Highscoretext
				.setText("High Score: " + highScore);
		reset.setLayoutX(XMAX + 5);
		reset.setLayoutY(YMAX - 50);
		group.getChildren().addAll(scoretext, level, Highscoretext, reset);

		group.getChildren().addAll(object.a, object.b, object.c, object.d);

		moveOnKeyPress(object);

	}

	private void stopTimer() {
		if (fall != null) {
			fall.cancel();
			fall.purge();
			fall = null;
		}
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		stopTimer(); 
	}

}