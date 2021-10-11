package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

public class Controller {


    public Button details;
    public TextArea summary;
    public Text wallDensity;
    public Text gridLen;

    private GraphicsContext graphicsContext2D;
    private String keyPress;

    static final int CANVAS_SIZE = 800;
    private static int gridSize = 100;
    public static int len = CANVAS_SIZE / gridSize;

    private int density = 5;

    private final Marker start = new Marker(Color.rgb(96, 165, 97));
    private final Marker end = new Marker(Color.rgb(227, 74, 111));

    private final List<Point> walls = new LinkedList<>();
    private boolean pathFound;
    private Algorithm lastAlgoRan;

    @FXML
    private Canvas mainCanvas;

    @FXML
    public void initialize() {
        mainCanvas.setHeight(CANVAS_SIZE);
        mainCanvas.setWidth(CANVAS_SIZE);
        mainCanvas.addEventFilter(MouseEvent.ANY, e -> mainCanvas.requestFocus());
        graphicsContext2D = mainCanvas.getGraphicsContext2D();
        wallDensity.setText("Wall Density " + density/10.0);
        gridLen.setText("Grid Size " + gridSize);
        resetCanvas();

    }

    /**
     * Resets the whole canvas.
     */
    private void resetCanvas() {

        keyPress = "";

        redrawCanvas();
        resetMarkers();

        pathFound = false;

        walls.clear();

    }

    private void redrawCanvas() {
        clearCanvas();
        drawLines();
    }

    private void resetMarkers() {
        start.unSet();
        end.unSet();
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
    private void drawLines() {

        graphicsContext2D.setFill(Color.BLACK);
        graphicsContext2D.setLineWidth(0.15);

        for (int i = 0; i < CANVAS_SIZE; i += len) {
            graphicsContext2D.strokeLine(0, i, CANVAS_SIZE, i);
            graphicsContext2D.strokeLine(i, 0, i, CANVAS_SIZE);
        }

    }


    /**
     * Mouse position on the canvas rounded off to the nearest len value.
     *
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
     * Draw wall from user mouse event.
     *
     * @param mouseEvent Mouse event.
     */
    @FXML
    private void drawWall(MouseEvent mouseEvent) {

        Point mousePos = mousePosOnCanvas(mouseEvent);

        // handles mouse going outside the canvas
        if (mousePos.x < gridSize && mousePos.x >= 0 && mousePos.y < gridSize && mousePos.y >= 0) {
            drawWall(mousePos);
        }

    }

    /**
     * Draws wall.
     * Called via user mouse event and random generator.
     *
     * @param pos Position of the mouse where the wall needs to be added.
     */
    private void drawWall(Point pos) {

        graphicsContext2D.setFill(Color.BLACK);
        graphicsContext2D.fillRect(pos.x * len, pos.y * len, len, len);

        if (!walls.contains(new Point(pos.x, pos.y)))
            walls.add(new Point(pos.x, pos.y));

        // replaces start with wall
        if (start.getPosition() == pos) {
            start.unSet();
        }

        // replaces end with wall
        if (end.getPosition() == pos) {
            end.unSet();
        }

    }

    /**
     * Sets the marker on the grid.
     *
     * @param marker Type of the marker that needs to be placed.
     * @param pos    Position where the marker needs to be placed.
     */
    private void drawMarker(Marker marker, Point pos) {

        graphicsContext2D.setEffect(new Glow(0.9));

        if (!marker.isSet()) {
            marker.setPosition(pos);
            graphicsContext2D.setFill(marker.getColor());
            graphicsContext2D.fillRect(marker.getPosition().x * len, marker.getPosition().y * len, len, len);
        }

    }

    /**
     * Adds start or/and finish marker.
     *
     * @param mouseEvent Mouse event.
     */
    @FXML
    private void addStartFinish(MouseEvent mouseEvent) {

        switch (keyPress) {
            case "s" -> drawMarker(start, mousePosOnCanvas(mouseEvent));
            case "e" -> drawMarker(end, mousePosOnCanvas(mouseEvent));
        }

        keyPress = "";

    }

    private void writeSummary(int shortestPathLength) {
        summary.setText(lastAlgoRan + " = " + shortestPathLength);
    }

    private boolean hasStartFinish() {

        Alert missingStartFinishAlert = new Alert(Alert.AlertType.ERROR);
        missingStartFinishAlert.setHeaderText(null);
        missingStartFinishAlert.setGraphic(null);

        if (!start.isSet()) {
            missingStartFinishAlert.setContentText("Starting point not set.");
            missingStartFinishAlert.showAndWait();
            return false;
        }
        if (!end.isSet()) {
            missingStartFinishAlert.setContentText("Ending point not set.");
            missingStartFinishAlert.showAndWait();
            return false;
        }


        return true;
    }

    private void resetSameGrid(Algorithm lastAlgoRan){
        if (pathFound) {
            if (this.lastAlgoRan.equals(lastAlgoRan)) {
                redrawCanvas();
                walls.forEach(this::drawWall);
                start.setSet(false);
                end.setSet(false);
                drawMarker(start, start.getPosition());
                drawMarker(end, end.getPosition());
                pathFound = false;
            } else {
                showPathInvalidMAlert();
            }
        }
    }

    @FXML
    public void startAStar(ActionEvent actionEvent) {

        if (!hasStartFinish()) {
            return;
        }

        resetSameGrid(Algorithm.DIJKSTRA);


        AStar.Vertex[][] grid = new AStar.Vertex[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = new AStar.Vertex(new Point(i, j));
            }
        }

        addWallsToGrid(grid);

        List<Point> path = AStar.shortestPath(grid, start.getPosition(), end.getPosition());

        lastAlgoRan = Algorithm.ASTAR;
        playPathAnim(grid, path);


    }


    /**
     * Stats the pathfinding animation.
     *
     * @param actionEvent Action event.
     */
    @FXML
    private void startDij(ActionEvent actionEvent) {

        if (!hasStartFinish()) {
            return;
        }

        resetSameGrid(Algorithm.ASTAR);

        Dijkstra.Vertex[][] grid = new Dijkstra.Vertex[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = new Dijkstra.Vertex(new Point(i, j));
            }
        }

        addWallsToGrid(grid);

        List<Point> path = Dijkstra.shortestPath(grid, start.getPosition(), end.getPosition());


        lastAlgoRan = Algorithm.DIJKSTRA;
        playPathAnim(grid, path);


    }

    private void addWallsToGrid(GridVertex[][] grid) {
        walls.forEach(w -> grid[w.x][w.y].setType(VertexType.WALL));
        grid[start.getPosition().x][start.getPosition().y].setType(VertexType.PATH);
        grid[end.getPosition().x][end.getPosition().y].setType(VertexType.PATH);

    }

    private void writeAStarScore(GridVertex[][] grid) {

        if (gridSize > 25) {
            System.out.println("Grid size cannot exceed 25 units for show details");
            return;
        }

        graphicsContext2D.setFont(new Font("Arial", 10));
        graphicsContext2D.setFill(Color.BLACK);
        DecimalFormat decimalFormat = new DecimalFormat("##.#");

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                double score = grid[i][j].getScore();
                String scoreText = score == Integer.MAX_VALUE ? "Inf" : decimalFormat.format(score);
                graphicsContext2D.fillText(scoreText, i * (len), (j + 1) * (len));

            }
        }
    }

    private void showPathInvalidMAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(pathFound ? "Path already found." : "No path exists.");
        alert.showAndWait();
    }

    private void playPathAnim(GridVertex[][] grid, List<Point> path) {

        if (path.isEmpty()) {
            showPathInvalidMAlert();
        } else {
            if (!pathFound) {
                new ShortestPathAnimation(path, mainCanvas.getGraphicsContext2D()).start();
                pathFound = true;
                writeSummary(path.size());
            }
        }

        final boolean[] clicked = {false};
        details.setOnMouseClicked(eve -> {
            if (!clicked[0]) {
                writeAStarScore(grid);
                clicked[0] = true;
            }
        });

    }

    /**
     * Adds random walls on the grid.
     */
    private void genRandomWalls() {
        MazeGenerator.genRandomWalls(gridSize, density/10.0
        ).forEach(this::drawWall);
    }

    /**
     * Places start and finish marker randomly on the grid.
     */
    private void genRandomStartEnd() {

        Point[] markers = MazeGenerator.genMarkers(gridSize);

        drawMarker(start, markers[0]);
        drawMarker(end, markers[1]);

    }

    /**
     * Generate random map.
     *
     * @param actionEvent Action event.
     */
    @FXML
    private void genRandom(ActionEvent actionEvent) {
        resetCanvas();
        genRandomWalls();
        genRandomStartEnd();
    }

    /**
     * Decreases the size of the grid by 5 units.
     *
     * @param actionEvent Action event.
     */
    @FXML
    public void decrGridSize(ActionEvent actionEvent) {
        if (gridSize > 5) {
            gridSize -= 5;
            len = CANVAS_SIZE / gridSize;
            gridLen.setText("Grid Size " + gridSize);

            resetCanvas();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setContentText("Grid size cannot recede 5 units ");
            alert.showAndWait();

        }
    }

    /**
     * Increases the size of the grid by 5 units.
     *
     * @param actionEvent Action event.
     */
    public void incrGridSize(ActionEvent actionEvent) {
        if (gridSize < 200) {
            gridSize += 5;
            len = CANVAS_SIZE / gridSize;
            gridLen.setText("Grid Size " + gridSize);

            resetCanvas();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setContentText("Grid size cannot exceed " + gridSize + "  units ");
            alert.showAndWait();
        }
    }

    /**
     * Used to place start and finish marker on the canvas.
     * Hold 's' to place start and 'e' to place end.
     *
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

    public void incrDensity(MouseEvent mouseEvent) {
        if(density < 10){
            density += 1;
            wallDensity.setText("Wall Density " + density/10.0);
        }
    }

    public void decrDensity(MouseEvent mouseEvent) {
        if(density > 0){
            density -= 1;
            wallDensity.setText("Wall Density " + density/10.0);

        }
    }
}
