package sample;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    public static final int LEN = 10;

    private String keyPress = "";

    private final Marker start = new Marker(Color.rgb(96, 165, 97));
    private final Marker end = new Marker(Color.rgb(227, 74, 111));

    private final DijkstraPathfinder dijkstraPathfinder = new DijkstraPathfinder();
    List<Point> path = new ArrayList<>();

    private boolean pathFound = false;

    private GraphicsContext graphicsContext2D;

    @FXML
    private Canvas mainCanvas;

    @FXML
    public static CheckBox diagonalMoves;

    @FXML
    public void initialize() {
        mainCanvas.addEventFilter(MouseEvent.ANY, (e) -> mainCanvas.requestFocus());
        graphicsContext2D = mainCanvas.getGraphicsContext2D();
        diagonalMoves = new CheckBox();
        resetCanvas();
    }

    /**
     * Restores every state to default.
     */
    private void resetCanvas() {
        clearCanvas();
        keyPress = "";
        start.disable();
        end.disable();
        resetDijkstra();
    }

    /**
     * Resets every state of Dijkstra.
     */
    private void resetDijkstra(){
        dijkstraPathfinder.clearGrid();
        path.clear();
        pathFound = false;
    }

    /**
     * Sets the color of canvas to white.
     */
    private void clearCanvas() {
        graphicsContext2D.setFill(Color.WHITE);
        graphicsContext2D.fillRect(0, 0, mainCanvas.getHeight(), mainCanvas.getWidth());
    }

    /**
     * Draws horizontal and vertical lines on the canvas.
     */
    private void drawLines() {

        graphicsContext2D.setFill(Color.GRAY);
        graphicsContext2D.setLineWidth(0.1);

        for (int i = 0; i < mainCanvas.getHeight(); i += LEN) {
            graphicsContext2D.strokeLine(0, i, mainCanvas.getWidth(), i);
            graphicsContext2D.strokeLine(i, 0, i, mainCanvas.getHeight());
        }

    }

    /**
     * Returns the position of mouse on canvas.
     *
     * @param mouseEvent Mouseevent
     * @return Returns the position of the mouse on canvas.
     */
    private Point mousePosOnCanvas(MouseEvent mouseEvent) {

        // UI window is 600*500. Canvas is 500*500. -100 and -0 maps mouse's window co-ordinates on canvas'
        Point mousePos = new Point((int) (mouseEvent.getSceneX() - 150), (int) (mouseEvent.getSceneY() - 0));

        // finds the nearest factor of 10 for x and y
        mousePos.x = ((mousePos.x + (LEN - (mousePos.x % LEN))) - LEN) / LEN;
        mousePos.y = ((mousePos.y + (LEN - (mousePos.y % LEN))) - LEN) / LEN;

        return mousePos;

    }

    @FXML
    private void addWall(MouseEvent mouseEvent) {

        if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            Point mousePos = mousePosOnCanvas(mouseEvent);
            // makes sure mouse is inside the canvas
            if ((mousePos.x >= 0 && mousePos.x < mainCanvas.getHeight() / LEN) && (mousePos.y >= 0 && mousePos.y < mainCanvas.getHeight() / LEN)) {
                addWall(mousePos);
            }
        }

    }

    /**
     * Adds a wall to the grid.
     *
     * @param pos Position where the wall needs to be added.
     */
    private void addWall(Point pos) {

        // draws wall on canvas
        graphicsContext2D.setFill(Color.BLACK);
        graphicsContext2D.fillRect(pos.x * LEN, pos.y * LEN, LEN, LEN);

        // adds wall to grid
        DijkstraPathfinder.Vertex updatedVertex = dijkstraPathfinder.getVertex(pos.x, pos.y);
        updatedVertex.setType(DijkstraPathfinder.VERTEX_TYPE.WALL);
        dijkstraPathfinder.setVertex(pos.x, pos.y, updatedVertex);

        // draws wall over start and removes start
        if (start.getPosition().x == pos.x && start.getPosition().y == pos.y) {
            start.disable();
        }

        // draws wall over end and removes end
        if (end.getPosition().x == pos.x && end.getPosition().y == pos.y) {
            end.disable();
        }

    }

    /**
     * Adds marker to the grid.
     *
     * @param marker Type of the marker that needs to be placed.
     * @param pos    Position where the marker needs to be placed.
     */
    private void setMarker(Marker marker, Point pos) {

        graphicsContext2D.setEffect(new Glow(0.9));

        if (marker.getEnabled()) {
            graphicsContext2D.setFill(Color.WHITE);
            graphicsContext2D.fillRect(marker.getPosition().x * LEN, marker.getPosition().y * LEN, LEN, LEN);
            path.forEach(p -> graphicsContext2D.fillRect(p.x * LEN, p.y * LEN, LEN, LEN));
            marker.disable();
        }

        if (!marker.getEnabled()) {
            marker.setPosition(pos);
            graphicsContext2D.setFill(marker.getColor());
            graphicsContext2D.fillRect(marker.getPosition().x * LEN, marker.getPosition().y * LEN, LEN, LEN);
        }

    }

    /**
     * Adds starting and finishing marker on the grid.
     * @param mouseEvent Mouseevent.
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
     * Starts the pathfinding animation.
     * @param actionEvent Actionevent.
     */
    @FXML
    private void startButton(ActionEvent actionEvent) {

        Alert missingMarkerAlert = new Alert(Alert.AlertType.ERROR);
        missingMarkerAlert.setHeaderText(null);
        missingMarkerAlert.setGraphic(null);

        if (!start.getEnabled()) {
            missingMarkerAlert.setContentText("Starting point not set.");
            missingMarkerAlert.showAndWait();
            return;
        }

        if (!end.getEnabled()) {
            missingMarkerAlert.setContentText("Ending point not set.");
            missingMarkerAlert.showAndWait();
            return;
        }

        resetDijkstra();

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
     * Covers a percentage of the grid with walls.
     *
     * @param wallDensity The percentage of the gird that needs to covered with wall.
     */
    private void genRandomWalls(double wallDensity) {

        for (int i = 0; i < mainCanvas.getHeight() / LEN; i++) {
            for (int j = 0; j < mainCanvas.getWidth() / LEN; j++) {
                if (Math.random() < wallDensity) {
                    addWall(new Point((int) (Math.random() * mainCanvas.getHeight() / LEN), (int) (Math.random() * mainCanvas.getHeight() / LEN)));
                }
            }
        }

    }

    /**
     * Adds starting and ending point at random positions.
     */
    private void genRandomStartEnd() {

        setMarker(start, new Point((int) (Math.random() * mainCanvas.getHeight() / LEN), (int) (Math.random() * mainCanvas.getHeight() / LEN)));

        do {
            setMarker(end, new Point((int) (Math.random() * mainCanvas.getHeight() / LEN), (int) (Math.random() * mainCanvas.getHeight() / LEN)));
        } while (start.getPosition().equals(end.getPosition()));

    }

    @FXML
    public void genRandom(ActionEvent actionEvent) {
        resetCanvas();
        genRandomWalls(0.25);
        genRandomStartEnd();
    }

    @FXML
    private void option(KeyEvent keyEvent) {
        keyPress = keyEvent.getText().toLowerCase();
    }

    @FXML
    private void clearButton() {
        resetCanvas();
    }

    @FXML
    private void quitButton() {
        System.exit(0);
    }

    @FXML
    public void flip(ActionEvent actionEvent) {
        diagonalMoves.fire();
    }
}