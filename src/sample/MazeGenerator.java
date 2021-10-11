package sample;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MazeGenerator {

    static List<Point> genRandomWalls(int gridSize, double density) {

        return IntStream.range(0, (int) (gridSize * gridSize * density))
                .mapToObj(p -> new Point((int) (Math.random() * gridSize), (int) (Math.random() * gridSize)))
                .toList();

    }

    static Point[] genMarkers(int gridSize) {

    Point[] markers = new Point[2];

        markers[0] = new Point((int) (Math.random() * gridSize), (int) (Math.random() * gridSize));

        do {
            markers[1] = new Point((int) (Math.random() * gridSize), (int) (Math.random() * gridSize));
        } while (markers[0].equals(markers[1]));

        return markers;

    }

}
