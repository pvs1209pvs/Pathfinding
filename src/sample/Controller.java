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


    private Marker start = new Marker(Color.rgb(96, 165, 97));
    private Marker end = new Marker(Color.rgb(227, 74, 111));

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

    @FXML
    private void addWall(MouseEvent mouseEvent) {

        Point mousePos = new Point((int) (mouseEvent.getSceneX() - 100), (int) (mouseEvent.getSceneY() - 0));

        mousePos.x = (mousePos.x + (LEN - (mousePos.x % LEN))) - LEN;
        mousePos.y = (mousePos.y + (LEN - (mousePos.y % LEN))) - LEN;

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

    private void setMarker(Marker marker, Point pos) {

        GraphicsContext graphicsContext2D = mainCanvas.getGraphicsContext2D();

        if (!marker.isSet()) {
            graphicsContext2D.setFill(marker.getColor());
            marker.setPosition(pos);
            graphicsContext2D.fillRect(marker.getPosition().x, marker.getPosition().y, LEN, LEN);
        }

    }

    @FXML
    private void addStartFinish(MouseEvent mouseEvent) {

        Point mousePos = new Point((int) (mouseEvent.getSceneX() - 100), (int) (mouseEvent.getSceneY() - 0));
        mousePos.x = (mousePos.x + (LEN - (mousePos.x % LEN))) - LEN;
        mousePos.y = (mousePos.y + (LEN - (mousePos.y % LEN))) - LEN;

        GraphicsContext graphicsContext2D = mainCanvas.getGraphicsContext2D();
        graphicsContext2D.setEffect(new Glow(0.9));

        switch (keyPress) {
            case "s" -> setMarker(start, mousePos);
            case "e" -> setMarker(end, mousePos);
        }
        
        keyPress = "";

    }

    @FXML
    private void option(KeyEvent keyEvent) {
        keyPress = keyEvent.getText().toLowerCase();
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
        path.remove(0);
        path.remove(path.size() - 1);

        new ShortestPathAnimation(path, mainCanvas.getGraphicsContext2D()).start();

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
