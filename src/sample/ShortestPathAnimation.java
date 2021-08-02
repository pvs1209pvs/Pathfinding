package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

import static sample.Controller.len;

public class ShortestPathAnimation extends AnimationTimer {

    private final GraphicsContext graphicsContext;
    private final Iterator<Point> shortestPathIterator;
    private long prevTime = 0;

    ShortestPathAnimation(List<Point> shortestPath, GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
        this.shortestPathIterator = shortestPath.iterator();
    }

    @Override
    public void handle(long l) {

        if (l - prevTime > 15e6) {
            prevTime = l;

            if(shortestPathIterator.hasNext()){
                Point v = shortestPathIterator.next();
                graphicsContext.setFill(Color.rgb(78, 165, 210));
                graphicsContext.setStroke(Color.GRAY);
                graphicsContext.setLineWidth(0.1);
                graphicsContext.fillRect(v.x * len, v.y * len, len, len);
            }

        }

    }
}
