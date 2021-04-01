package sample;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;

public class Controller {

    private final int LEN = 10;

    private String keyPress = "";

    private boolean startSet = false;
    private boolean endSet = false;

    private double[] startCord = new double[]{-1, -1};
    private double[] endCord = new double[]{-1, -1};

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

        if (startCord[0] == x && startCord[1] == y) {
            startCord = new double[]{-1, -1};
            startSet = false;
        }

        if (endCord[0] == x && endCord[1] == y) {
            endCord = new double[]{-1, -1};
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
            startCord = new double[]{x, y};
        }

        if (!endSet && keyPress.equals("e")) {
            g.setFill(Color.rgb(227, 74, 111));
            endSet = true;
            endCord = new double[]{x, y};
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

        System.out.println(Arrays.toString(startCord));

        List<Gridder.Vertex> path = gridder.sp((int) startCord[0] / LEN, (int) startCord[1] / LEN, (int) endCord[0] / LEN, (int) endCord[1] / LEN);

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

        if (!startSet) {
            System.out.println("start not set");
        }
        if (!endSet) {
            System.out.println("end not set");
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
