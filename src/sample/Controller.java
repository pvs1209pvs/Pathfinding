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

    /**
     * Resets the whole canvas.
     */
    private void resetCanvas() {

        clearCanvas();
        drawLines();

        keyPress = "";

        start = new Marker(Color.rgb(96, 165, 97));
        end = new Marker(Color.rgb(227, 74, 111));

        dijkstraPathfinder = new DijkstraPathfinder(gridSize);
        path = new ArrayList<>();
        pathFound = false;

    }

    /**
     * Covers canvas with white color.
     */
    private void clearCanvas() {
        graphicsContext2D.setFill(Color.WHITE);
        graphicsContext2D.fillRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
    }

    /**
     * Draws horizontal and vertical lines on the canvas.
     */
    private void drawLines(){
        
        graphicsContext2D.setFill(Color.BLACK);
        graphicsContext2D.setLineWidth(0.15);

        for (int i = 0; i < 500; i+=len) {
            graphicsContext2D.strokeLine(0, i, 500, i);
            graphicsContext2D.strokeLine(i, 0, i, 500);
        }

    }


    /**
     * Mouse position on the canvas rounded off to the nearest len value.
     * @param mouseEvent Mouse event.
     * @return Position of the mouse on canvas.
     */
    private Point mousePosOnCanvas(MouseEvent mouseEvent) {

        Point mousePos = new Point((int) (mouseEvent.getSceneX() - 100), (int) (mouseEvent.getSceneY() - 0));
        mousePos.x = ((mousePos.x + (len - (mousePos.x % len))) - len) / len;
        mousePos.y = ((mousePos.y + (len - (mousePos.y % len))) - len) / len;
        return mousePos;

    }

    /**
     * Adds wall to the grid from user mouse event.
     * @param mouseEvent Mouse event.
     */
    @FXML
    private void addWall(MouseEvent mouseEvent) {

        Point mousePos = mousePosOnCanvas(mouseEvent);

        // handles mouse going outside the canvas
        if(mousePos.x < gridSize && mousePos.x >= 0 && mousePos.y < gridSize && mousePos.y >= 0){
            addWall(mousePos);
        }

    }

    /**
     * Adds wall to the grid.
     * Called via user mouse event and random generator.
     * @param mousePos Position of the mouse where the wall needs to be added.
     */
    private void addWall(Point mousePos) {

        graphicsContext2D.setFill(Color.BLACK);
        graphicsContext2D.fillRect(mousePos.x * len, mousePos.y * len, len, len);

        DijkstraPathfinder.Vertex updatedVertex = dijkstraPathfinder.getVertex(mousePos.x, mousePos.y);
        updatedVertex.setType(DijkstraPathfinder.VERTEX_TYPE.WALL);
        dijkstraPathfinder.setVertex( updatedVertex);

        // replaces start with wall
        if (start.getPosition().x == mousePos.x && start.getPosition().y == mousePos.y) {
            start.unSet();
        }

        // replaces end with wall
        if (end.getPosition().x == mousePos.x && end.getPosition().y == mousePos.y) {
            end.unSet();
        }

    }

    /**
     * Sets the marker on the grid.
     * @param marker Type of the marker that needs to be placed.
     * @param pos Position where the marker needs to be placed.
     */
    private void setMarker(Marker marker, Point pos) {

        graphicsContext2D.setEffect(new Glow(0.9));

        if (!marker.isSet()) {
            marker.setPosition(pos);
            graphicsContext2D.setFill(marker.getColor());
            graphicsContext2D.fillRect(marker.getPosition().x * len, marker.getPosition().y * len, len, len);
        }

    }

    /**
     * Adds start or/and finish marker.
     * @param mouseEvent Mouse event.
     */
    @FXML
    private void addStartFinish(MouseEvent mouseEvent) {

        switch (keyPress) {
            case "s" -> setMarker(start, mousePosOnCanvas(mouseEvent));
            case "e" -> setMarker(end, mousePosOnCanvas(mouseEvent));
        }

        keyPress = "";

    }

    /**
     * Returns the marker that isn't set.
     * @return Empty string if both the markers are set. "s" if start is missing. "e" if end is missing.
     */
    private String isStartFinishSet() {

        if (!start.isSet()) {
            return "s";
        }

        if (!end.isSet()) {
            return "e";
        }

        return "";

    }


    /**
     * Stats the pathfinding animation.
     * @param actionEvent Action event.
     */
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

    /**
     * Adds random walls on the grid.
     */
    private void genRandomWalls() {

        for (int i = 0; i < mainCanvas.getHeight() / len; i++) {
            for (int j = 0; j < mainCanvas.getWidth() / len; j++) {
                if (Math.random() < 0.6) {
                    addWall(new Point((int) (Math.random() * dijkstraPathfinder.getSize()), (int) (Math.random() * dijkstraPathfinder.getSize())));
                }
            }
        }


    }

    /**
     * Places start and finish marker randomly on the grid.
     */
    private void genRandomStartEnd() {

        setMarker(start, new Point((int) (Math.random() * dijkstraPathfinder.getSize()), (int) (Math.random() * dijkstraPathfinder.getSize())));

        do {
            setMarker(end, new Point((int) (Math.random() * dijkstraPathfinder.getSize()), (int) (Math.random() * dijkstraPathfinder.getSize())));
        } while (start.getPosition().equals(end.getPosition()));

    }

    /**
     * Generate random map.
     * @param actionEvent Action event.
     */
    @FXML
    private void genRandom(ActionEvent actionEvent) {
        resetCanvas();
        genRandomWalls();
        genRandomStartEnd();
    }

    /**
     * Used to place start and finish marker on the canvas.
     * Hold 's' to place start and 'e' to place end.
     * @param keyEvent Key press.
     */
    @FXML
    private void option(KeyEvent keyEvent) {
        keyPress = keyEvent.getText().toLowerCase();
    }

    /**
     * Clear the whole canvas
     */
    @FXML
    private void clearButton() {
        resetCanvas();
    }

    /**
     * Closes the program.
     */
    @FXML
    private void quitButton() {
        System.exit(0);
    }


    /**
     * Decreases the size of the grid by 5 units.
     * @param actionEvent Action event.
     */
    @FXML
    public void decrGridSize(ActionEvent actionEvent) {
        if (gridSize > 5) {
            gridSize -= 5;
            len = 500 / gridSize;
            resetCanvas();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setContentText("Grid size cannot recede 5 units ");
            alert.showAndWait();

        }
    }

    /**
     * Increases the size of the grid by 5 units.
     * @param actionEvent Action event.
     */
    public void incrGridSize(ActionEvent actionEvent) {
        if (gridSize < 50) {
            gridSize += 5;
            len = 500 / gridSize;
            resetCanvas();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setContentText("Grid size cannot exceed 5 units ");
            alert.showAndWait();
        }
    }

}
