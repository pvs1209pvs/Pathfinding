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

    public static final int LEN = 10;

    private String keyPress = "";

    private final Marker start = new Marker(Color.rgb(96, 165, 97));
    private final Marker end = new Marker(Color.rgb(227, 74, 111));

    private DijkstraPathfinder dijkstraPathfinder = new DijkstraPathfinder();

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
        start.setSet(false);
        end.setSet(false);
        dijkstraPathfinder = new DijkstraPathfinder();
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

    private Point mousePosOnCanvas(MouseEvent mouseEvent) {

        Point mousePos = new Point((int) (mouseEvent.getSceneX() - 100), (int) (mouseEvent.getSceneY() - 0));
        mousePos.x = (mousePos.x + (LEN - (mousePos.x % LEN))) - LEN;
        mousePos.y = (mousePos.y + (LEN - (mousePos.y % LEN))) - LEN;
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

        GraphicsContext g = mainCanvas.getGraphicsContext2D();
        g.setFill(Color.BLACK);
        g.fillRect(mousePos.x, mousePos.y, LEN, LEN);

        DijkstraPathfinder.Vertex updatedVertex = dijkstraPathfinder.getVertex(mousePos.x / LEN, mousePos.y / LEN);
        updatedVertex.setType(DijkstraPathfinder.VERTEX_TYPE.WALL);
        dijkstraPathfinder.setVertex(mousePos.x / LEN, mousePos.y / LEN, updatedVertex);

        if (start.getPosition().x == mousePos.x && start.getPosition().y == mousePos.y) {
            start.unSet();
        }

        if (end.getPosition().x == mousePos.x && end.getPosition().y == mousePos.y) {
            end.unSet();
        }
    }

    private void genRandomWalls(double wallDensity) {

        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                if (Math.random() < wallDensity) {
                    Point mousePos = new Point((int) (Math.random() * 500), (int) (Math.random() * 500));
                    mousePos.x = (mousePos.x + (LEN - (mousePos.x % LEN))) - LEN;
                    mousePos.y = (mousePos.y + (LEN - (mousePos.y % LEN))) - LEN;
                    addWall(mousePos);
                }
            }
        }


    }

    private void setMarker(Marker marker, Point pos) {

        GraphicsContext graphicsContext2D = mainCanvas.getGraphicsContext2D();
        graphicsContext2D.setEffect(new Glow(0.9));

        if (!marker.isSet()) {
            graphicsContext2D.setFill(marker.getColor());
            marker.setPosition(pos);


            graphicsContext2D.fillRect(marker.getPosition().x, marker.getPosition().y, LEN, LEN);
        }

    }

    private void genRandomStartEnd() {

        Point mousePos = new Point((int) (Math.random() * 500), (int) (Math.random() * 500));
        mousePos.x = (mousePos.x + (LEN - (mousePos.x % LEN))) - LEN;
        mousePos.y = (mousePos.y + (LEN - (mousePos.y % LEN))) - LEN;

        setMarker(start, mousePos);

        do {
            mousePos = new Point((int) (Math.random() * 500), (int) (Math.random() * 500));
            mousePos.x = (mousePos.x + (LEN - (mousePos.x % LEN))) - LEN;
            mousePos.y = (mousePos.y + (LEN - (mousePos.y % LEN))) - LEN;

            setMarker(end, mousePos);
        } while (start.getPosition().equals(end.getPosition()));

    }

    @FXML
    private void addStartFinish(MouseEvent mouseEvent) {

        switch (keyPress) {
            case "s" -> setMarker(start, mousePosOnCanvas(mouseEvent));
            case "e" -> setMarker(end, mousePosOnCanvas(mouseEvent));
        }

        keyPress = "";

    }

    @FXML
    private void startButton(ActionEvent actionEvent) {

        if (!start.isSet()) {
            System.out.println("start not set");
            return;
        }
        if (!end.isSet()) {
            System.out.println("end not set");
            return;
        }

        start.getPosition().x /= LEN;
        start.getPosition().y /= LEN;
        end.getPosition().x /= LEN;
        end.getPosition().y /= LEN;

        List<DijkstraPathfinder.Vertex> path = dijkstraPathfinder.shortestPath(end.getPosition(), start.getPosition());

        // remove start and end point from the shortest path
//        System.out.println(start.getPosition() + " " + end.getPosition());
//        System.out.println(path.get(0).getC() + "," + path.get(0).getR());
//        System.out.println(path.get(path.size() - 1).getC() + "," + path.get(path.size() - 1).getR());
//        System.out.println(path);
//        path.remove(0);
//        path.remove(path.size() - 1);

        System.out.println(start.getPosition() + " " + end.getPosition());
        System.out.println(path);
        path.removeIf(v ->
                (v.getR() == start.getPosition().x && v.getC() == start.getPosition().y) ||
                        (v.getR() == end.getPosition().x && v.getC() == end.getPosition().y));
        System.out.println(path);
        System.out.println();

        new ShortestPathAnimation(path, mainCanvas.getGraphicsContext2D()).start();

    }

    @FXML
    public void genRandom(ActionEvent actionEvent) {
        resetCanvas();
        genRandomWalls(Math.random());
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


}

/*
  TODO Can you place wall on start/end?
 */
