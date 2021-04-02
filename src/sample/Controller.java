package sample;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.List;

public class Controller {

    private final int LEN = 10;

    private String keyPress = "";

    private boolean startSet = false;
    private boolean endSet = false;

    private Point start = new Point(-1,-1);
    private Point end = new Point(-1,-1);

    private Gridder gridder = new Gridder();

    @FXML
    private Canvas mainCanvas;

    @FXML
    public void initialize() {
        mainCanvas.addEventFilter(MouseEvent.ANY, (e) -> mainCanvas.requestFocus());
        resetCanvas();
    }

    private void resetCanvas() {
        drawEmptySpots();
        drawLines();
        keyPress = "";
        startSet = false;
        endSet = false;
        gridder = new Gridder();
    }

    private void drawEmptySpots() {

        GraphicsContext g = mainCanvas.getGraphicsContext2D();

        g.setFill(Color.WHITE);

        for (int i = 0; i < mainCanvas.getHeight(); i += LEN) {
            for (int j = 0; j < mainCanvas.getWidth(); j += LEN) {
                g.fillRect(i, j, LEN, LEN);

            }
        }

        g.setFill(Color.BLACK);

    }

    private void drawLines() {

        GraphicsContext g = mainCanvas.getGraphicsContext2D();

        g.setLineWidth(0.1);
        g.setFill(Color.GRAY);

        for (int i = 0; i < mainCanvas.getHeight(); i += LEN) {
            g.strokeLine(0, i, mainCanvas.getWidth(), i);
            g.strokeLine(i, 0, i, mainCanvas.getHeight());
        }

        g.setFill(Color.BLACK);

    }

    @FXML
    public void addWall(MouseEvent mouseEvent) {

        double x = mouseEvent.getSceneX() - 100;
        double y = mouseEvent.getSceneY() - 0;

        x = (x + (LEN - (x % LEN))) - LEN;
        y = (y + (LEN - (y % LEN))) - LEN;

        GraphicsContext g = mainCanvas.getGraphicsContext2D();
        g.setFill(Color.BLACK);
        g.fillRect(x, y, LEN, LEN);

        gridder.grid[(int) (x / LEN)][(int) (y / LEN)].type = 'w';

        if (start.x == x && start.y == y) {
            start = new Point(-1,-1);
            startSet = false;
        }

        if (end.x == x && end.y == y) {
            end = new Point(-1, -1);
            endSet = false;
        }

    }

    @FXML
    public void addStartFinish(MouseEvent mouseEvent) {

        double x = mouseEvent.getSceneX() - 100;
        double y = mouseEvent.getSceneY() - 0;

        x = (x + (LEN - (x % LEN))) - LEN;
        y = (y + (LEN - (y % LEN))) - LEN;

        GraphicsContext g = mainCanvas.getGraphicsContext2D();

        Glow glow = new Glow();
        glow.setLevel(0.9);
        g.setEffect(glow);

        if (!startSet && keyPress.equals("s")) {
            g.setFill(Color.rgb(96, 165, 97));
            startSet = true;
            start = new Point((int)x, (int)y);
        }

        if (!endSet && keyPress.equals("e")) {
            g.setFill(Color.rgb(227, 74, 111));
            endSet = true;
            end = new Point((int)x, (int)y);
        }

        keyPress = "";

        g.fillRect(x, y, LEN, LEN);
        g.setFill(Color.rgb(3, 7, 30));

    }

    @FXML
    public void option(KeyEvent keyEvent) {
        keyPress = keyEvent.getText();
    }

    @FXML
    public void startButton(ActionEvent actionEvent) {

        if (!startSet) {
            System.out.println("start not set");
            return;
        }
        if (!endSet) {
            System.out.println("end not set");
            return;
        }

        start.x /= LEN;
        start.y /= LEN;
        end.x /= LEN;
        end.y /= LEN;
        List<Gridder.Vertex> path = gridder.sp(start, end);

        GraphicsContext g = mainCanvas.getGraphicsContext2D();

        path.remove(0);
        path.remove(path.size()-1);

        for (Gridder.Vertex v : path) {
            g.setFill(Color.rgb(78, 165, 210));
            g.setStroke(Color.GRAY);
            g.setLineWidth(0.1);
            g.strokeRect(v.c * LEN, v.r * LEN, LEN, LEN);
            g.fillRect(v.c * LEN, v.r * LEN, LEN, LEN);
        }

    }

    @FXML
    public void clearButton() {
        resetCanvas();
    }

    @FXML
    public void quitButton() {
        System.exit(0);
    }

}

/*
  TODO Can you place wall on start/end?
 */
