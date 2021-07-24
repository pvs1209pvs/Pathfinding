package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Iterator;
import java.util.List;

import static sample.Controller.LEN;

public class PathAnimation extends AnimationTimer {

    private final GraphicsContext graphicsContext;
    private final Iterator<DijkstraPathfinder.Vertex> shortestPathIterator;
    private long prevTime = 0;

    PathAnimation(List<DijkstraPathfinder.Vertex> shortestPath, GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
        this.shortestPathIterator = shortestPath.iterator();
    }

    @Override
    public void handle(long l) {

        if (l - prevTime > 15e6) {
            prevTime = l;

            if(shortestPathIterator.hasNext()){
                DijkstraPathfinder.Vertex v = shortestPathIterator.next();
                graphicsContext.setFill(Color.rgb(78, 165, 210));
                graphicsContext.setStroke(Color.GRAY);
                graphicsContext.setLineWidth(0.1);
                graphicsContext.fillRect(v.getC() * LEN, v.getR() * LEN, LEN, LEN);
            }

        }

    }
}
