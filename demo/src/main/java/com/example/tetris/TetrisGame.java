package com.example.tetris;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.css.Size;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class TetrisGame extends Application {

	public static final int MOVE = 25;
	public static final int SIZE = 25;

	public static int XMAX = SIZE * 12;
	public static int YMAX = SIZE * 24;

	public static int[][] MESH = new int[XMAX / SIZE][YMAX / SIZE];

	private static Pane group = new Pane();
	private static FormFigure object;

	private static Scene scene = new Scene(group, XMAX + 150, YMAX);

	public static int score = 0;
	private static int top = 0;

	private static boolean game = true;

	private static FormFigure nextObj = Controller.makeRect();

	private static int linesNo = 0;

	@Override
	public void start(Stage stage) throws Exception {
		for (int[] a : MESH) {
			Arrays.fill(a, 0);
		}

		Line line = new Line(XMAX, 0, XMAX, YMAX);

		Text scoretext = new Text("Score: ");

		scoretext.setStyle("-fx-font: 20 arial;");
		scoretext.setY(50);
		scoretext.setX(XMAX + 5);

		Text level = new Text("Lines: ");
		level.setStyle("-fx-font: 20 arial;");
		level.setY(100);
		level.setX(XMAX + 5);
		level.setFill(Color.GREEN);

		group.getChildren().addAll(scoretext, line, level);

		FormFigure a = nextObj;
		group.getChildren().addAll(a.a, a.b, a.c, a.d);
		moveOnKeyPress(a);
		object = a;
		nextObj = Controller.makeRect();

		stage.setScene(scene);
		stage.setTitle("T E T R I S");
		stage.show();

		Timer fall = new Timer();
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
							level.setText("lines: " + linesNo);
						}
					}

				});
			}

		};
		fall.schedule(task, 0, 300);
	}

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
						MoveDown(form);
						score++;
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
				if (f == 1 && cB(a, 1, -1) && cB(c, -1, -1) && cB(d, -2, -2)) {
					MoveRight(form.a);
					MoveDown(form.a);
					MoveDown(form.c);
					MoveLeft(form.c);
					MoveDown(form.d);
					MoveDown(form.d);
					MoveLeft(form.d);
					MoveLeft(form.d);
					form.changeForm();
					break;
				}
				if (f == 2 && cB(a, 1, -1) && cB(c, -1, -1) && cB(d, -2, -2)) {
					MoveRight(form.a);
					MoveDown(form.a);
					MoveDown(form.c);
					MoveLeft(form.c);
					MoveDown(form.d);
					MoveDown(form.d);
					MoveLeft(form.d);
					MoveLeft(form.d);
					form.changeForm();
					break;
				}
				if (f == 3 && cB(a, 1, -1) && cB(c, -1, -1) && cB(d, -2, -2)) {
					MoveRight(form.a);
					MoveDown(form.a);
					MoveDown(form.c);
					MoveLeft(form.c);
					MoveDown(form.d);
					MoveDown(form.d);
					MoveLeft(form.d);
					MoveLeft(form.d);
					form.changeForm();
					break;
				}
				if (f == 4 && cB(a, 1, -1) && cB(c, -1, -1) && cB(d, -2, -2)) {
					MoveRight(form.a);
					MoveDown(form.a);
					MoveDown(form.c);
					MoveLeft(form.c);
					MoveDown(form.d);
					MoveDown(form.d);
					MoveLeft(form.d);
					MoveLeft(form.d);
					form.changeForm();
					break;
				}
				break;
		}
	}

	private static void RemoveRows(Pane pane) {
		ArrayList<Node> rects = new ArrayList<Node>();
		ArrayList<Integer> lines = new ArrayList<Integer>();
		ArrayList<Node> newrects = new ArrayList<Node>();
		int full = 0;
		for (int i = 0; i < MESH[0].length; i++) {
			for (int j = 0; j < MESH.length; j++) {
				if (MESH[j][i] == 1) {
					full++;
				}
			}
			if (full == MESH.length) {
				lines.add(i + lines.size());
			}
			full = 0;
		}
		if (lines.size() > 0) {
			do {
				for (Node node : pane.getChildren()) {
					if (node instanceof Rectangle)
						rects.add(node);
				}
				score += 50;
				linesNo++;
				for (Node node : rects) {
					Rectangle a = (Rectangle) node;
					if (a.getY() == lines.get(0) * SIZE) {
						MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
						pane.getChildren().remove(node);
					} else {
						newrects.add(node);
					}
				}
				for (Node node : newrects) {
					Rectangle a = (Rectangle) node;
					if (a.getY() < lines.get(0) * SIZE) {
						MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
						a.setY(a.getY() + SIZE);
					}
					lines.remove(0);
					rects.clear();
					newrects.clear();
					for (Node node2 : pane.getChildren()) {
						if (node2 instanceof Rectangle) {
							rects.add(node);
						}
					}
					for (Node node2 : rects) {
						Rectangle a2 = (Rectangle) node2;
						try {
							MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 1;
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("ERROR: " + e);
						}
					}
				}
				rects.clear();

			} while (lines.size() > 0);
		}
	}

	private static void MoveDown(Rectangle rect) {
		if (rect.getY() + MOVE < YMAX) {
			rect.setY(rect.getY() + MOVE);
		}
	}

	private static void MoveRight(Rectangle rect) {
		if (rect.getX() + MOVE < XMAX - SIZE) {
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
		boolean yb = false;
		boolean xb = false;
		if (x >= 0)
			xb = rect.getX() + x * MOVE <= XMAX - SIZE;
		if (x < 0)
			xb = rect.getX() + x * MOVE >= 0;
		if (y >= 0)
			xb = rect.getY() + y * MOVE > 0;
		if (y < 0)
			xb = rect.getY() + y * MOVE < YMAX;
		return xb && yb && MESH[((int) rect.getX() / SIZE) + x][((int) rect.getY() / SIZE) + y] == 0;
	}

}