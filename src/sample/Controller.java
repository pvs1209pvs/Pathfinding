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

    private static int gridSize = 50;
    public static int len = 500 / gridSize;

    private String keyPress;

    private Marker start;
    private Marker end;

    private DijkstraPathfinder dijkstraPathfinder;
    private List<Point> path;

    private boolean pathFound;

    private GraphicsContext graphicsContext2D;

    @FXML
    private Canvas mainCanvas;

    @FXML
    public void initialize() {
        mainCanvas.addEventFilter(MouseEvent.ANY, e -> mainCanvas.requestFocus());
        graphicsContext2D = mainCanvas.getGraphicsContext2D();

        resetCanvas();
    }

    private void resetCanvas() {

        clearCanvas();
        drawLines();

        keyPress = "";

        start = new Marker(Color.rgb(96, 165, 97));
        end = new Marker(Color.rgb(227, 74, 111));

        dijkstraPathfinder = new DijkstraPathfinder(gridSize);
        pathFound = false;
        path = new ArrayList<>();

    }

    private void clearCanvas() {
        graphicsContext2D.setFill(Color.WHITE);
        graphicsContext2D.fillRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
    }

    private void drawLines(){
        
        graphicsContext2D.setFill(Color.BLACK);
        graphicsContext2D.setLineWidth(0.15);

        for (int i = 0; i < 500; i+=len) {
            graphicsContext2D.strokeLine(0, i, 500, i);
            graphicsContext2D.strokeLine(i, 0, i, 500);
        }

    }


    private Point mousePosOnCanvas(MouseEvent mouseEvent) {

        Point mousePos = new Point((int) (mouseEvent.getSceneX() - 100), (int) (mouseEvent.getSceneY() - 0));
        mousePos.x = ((mousePos.x + (len - (mousePos.x % len))) - len) / len;
        mousePos.y = ((mousePos.y + (len - (mousePos.y % len))) - len) / len;
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
        graphicsContext2D.fillRect(mousePos.x * len, mousePos.y * len, len, len);

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
            graphicsContext2D.fillRect(marker.getPosition().x * len, marker.getPosition().y * len, len, len);
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

        for (int i = 0; i < mainCanvas.getHeight() / len; i++) {
            for (int j = 0; j < mainCanvas.getWidth() / len; j++) {
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
    private void genRandom(ActionEvent actionEvent) {
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
        if (gridSize > 5) {
            gridSize -= 5;
            len = 500 / gridSize;
            resetCanvas();
        }
    }

    public void incrGridSize(ActionEvent actionEvent) {
        if (gridSize < 50) {
            gridSize += 5;
            len = 500 / gridSize;
            resetCanvas();
        }
    }
}

/*
  TODO Can you place wall on start/end?
 */
