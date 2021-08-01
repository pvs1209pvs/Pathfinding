package sample;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {


    private String keyPress = "";

    private final Marker start = new Marker(Color.rgb(96, 165, 97));
    private final Marker end = new Marker(Color.rgb(227, 74, 111));

    private static int gridSize = 5;
    private DijkstraPathfinder dijkstraPathfinder = new DijkstraPathfinder(gridSize);
    private List<Point> path = new ArrayList<>();

    private boolean pathFound = false;

    private GraphicsContext graphicsContext2D;

    @FXML
    private Canvas mainCanvas;

    public static int LEN = 500/gridSize;


    @FXML
    public void initialize() {
        mainCanvas.addEventFilter(MouseEvent.ANY, e -> mainCanvas.requestFocus());
        graphicsContext2D = mainCanvas.getGraphicsContext2D();

        resetCanvas();
    }

    private void resetCanvas() {

        drawEmptySpots();
        drawLines();

        keyPress = "";

        start.unSet();
        end.unSet();

        dijkstraPathfinder = new DijkstraPathfinder(gridSize);
        pathFound = false;
        path.clear();

    }

    private void drawEmptySpots() {
        graphicsContext2D.setFill(Color.WHITE);
        graphicsContext2D.fillRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
    }

    private void drawLines() {

        graphicsContext2D.setLineWidth(0.1);
        graphicsContext2D.setFill(Color.GRAY);

        for (int i = 0; i < mainCanvas.getHeight(); i += LEN) {
            graphicsContext2D.strokeLine(0, i, mainCanvas.getWidth(), i);
            graphicsContext2D.strokeLine(i, 0, i, mainCanvas.getHeight());
        }

        graphicsContext2D.setFill(Color.BLACK);

    }

    private Point mousePosOnCanvas(MouseEvent mouseEvent) {

        Point mousePos = new Point((int) (mouseEvent.getSceneX() - 100), (int) (mouseEvent.getSceneY() - 0));
        mousePos.x = ((mousePos.x + (LEN - (mousePos.x % LEN))) - LEN) / LEN;
        mousePos.y = ((mousePos.y + (LEN - (mousePos.y % LEN))) - LEN) / LEN;
        System.out.println(mousePos);
        return mousePos;

    }

    @FXML
    private void addWall(MouseEvent mouseEvent) {
        addWall(mousePosOnCanvas(mouseEvent));
    }

    private void addWall(Point mousePos) {

        // handles mouse going outside the canvas
        if (mousePos.x < 0.0 || mousePos.x >= mainCanvas.getHeight() || mousePos.y < 0 || mousePos.y >= mainCanvas.getWidth()) {
            return;
        }

        graphicsContext2D.setFill(Color.BLACK);
        graphicsContext2D.fillRect(mousePos.x * LEN, mousePos.y * LEN, LEN, LEN);

        DijkstraPathfinder.Vertex updatedVertex = dijkstraPathfinder.getVertex(mousePos.x, mousePos.y);
        updatedVertex.setType(DijkstraPathfinder.VERTEX_TYPE.WALL);
        dijkstraPathfinder.setVertex(mousePos.x, mousePos.y, updatedVertex);

        if (start.getPosition().x == mousePos.x && start.getPosition().y == mousePos.y) {
            start.unSet();
        }

        if (end.getPosition().x == mousePos.x && end.getPosition().y == mousePos.y) {
            end.unSet();
        }
    }

    private void setMarker(Marker marker, Point pos) {

        graphicsContext2D.setEffect(new Glow(0.9));

        if (!marker.isSet()) {
            marker.setPosition(pos);
            graphicsContext2D.setFill(marker.getColor());
            graphicsContext2D.fillRect(marker.getPosition().x * LEN, marker.getPosition().y * LEN, LEN, LEN);
        }

    }

    @FXML
    private void addStartFinish(MouseEvent mouseEvent) {

        switch (keyPress) {
            case "s" -> setMarker(start, mousePosOnCanvas(mouseEvent));
            case "e" -> setMarker(end, mousePosOnCanvas(mouseEvent));
        }

        keyPress = "";

    }

    private String isStartFinishSet() {

        if (!start.isSet()) {
            return "s";
        }

        if (!end.isSet()) {
            return "e";
        }

        return "";

    }


    @FXML
    private void startButton(ActionEvent actionEvent) {

        Alert missingStartFinishAlert = new Alert(Alert.AlertType.ERROR);
        missingStartFinishAlert.setHeaderText(null);
        missingStartFinishAlert.setGraphic(null);

        switch (isStartFinishSet()) {
            case "s" -> {
                missingStartFinishAlert.setContentText("Starting point not set.");
                missingStartFinishAlert.showAndWait();
                return;
            }

            case "e" -> {
                missingStartFinishAlert.setContentText("Ending point not set.");
                missingStartFinishAlert.showAndWait();
                return;
            }
        }

        path = dijkstraPathfinder.shortestPath(start.getPosition(), end.getPosition());
        path.remove(end.getPosition());
        path.remove(start.getPosition());

        if (path.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setContentText(pathFound ? "Path already found." : "No path exists.");
            alert.showAndWait();
        } else {
            if (!pathFound) {
                new ShortestPathAnimation(path, mainCanvas.getGraphicsContext2D()).start();
                pathFound = true;
            }
        }

    }

    private void genRandomWalls(double wallDensity) {

        for (int i = 0; i < mainCanvas.getHeight() / LEN; i++) {
            for (int j = 0; j < mainCanvas.getWidth() / LEN; j++) {
                if (Math.random() < wallDensity) {
                    addWall(new Point((int) (Math.random() * dijkstraPathfinder.getSize()), (int) (Math.random() * dijkstraPathfinder.getSize())));
                }
            }
        }


    }

    private void genRandomStartEnd() {

        setMarker(start, new Point((int) (Math.random() * dijkstraPathfinder.getSize()), (int) (Math.random() * dijkstraPathfinder.getSize())));

        do {
            setMarker(end, new Point((int) (Math.random() * dijkstraPathfinder.getSize()), (int) (Math.random() * dijkstraPathfinder.getSize())));
        } while (start.getPosition().equals(end.getPosition()));

    }

    @FXML
    public void genRandom(ActionEvent actionEvent) {
        resetCanvas();
        genRandomWalls(0.6);
        genRandomStartEnd();
    }

    @FXML
    private void option(KeyEvent keyEvent) {
        keyPress = keyEvent.getText().toLowerCase();
    }

    @FXML
    private void clearButton() {
        initialize();
    }

    @FXML
    private void quitButton() {
        System.exit(0);
    }


    @FXML
    public void decrGridSize(ActionEvent actionEvent) {
        if(gridSize > 5){
            gridSize -= 5;
            resetCanvas();
        }
    }

    public void incrGridSize(ActionEvent actionEvent) {
        if(gridSize < 50){
            gridSize += 5;
            resetCanvas();
        }
    }
}

/*
  TODO Can you place wall on start/end?
 */
