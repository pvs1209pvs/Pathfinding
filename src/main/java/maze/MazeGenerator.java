package maze;

import java.awt.*;
import java.util.List;
import java.util.stream.IntStream;

public class MazeGenerator {

    public static List<Point> genRandomWalls(int gridSize, double density) {

        return IntStream.range(0, (int) (gridSize * gridSize * density))
                .mapToObj(p -> new Point((int) (Math.random() * gridSize), (int) (Math.random() * gridSize)))
                .toList();

    }

    public static Point[] genMarkers(int gridSize) {

    Point[] markers = new Point[2];

        markers[0] = new Point((int) (Math.random() * gridSize), (int) (Math.random() * gridSize));

        do {
            markers[1] = new Point((int) (Math.random() * gridSize), (int) (Math.random() * gridSize));
        } while (markers[0].equals(markers[1]));

        return markers;

    }

}
