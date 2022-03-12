package maze;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MazeGenerator {


    public static List<Point> generateMaze(int gridSize) {

        try {
            new ProcessBuilder("sh", "-c", "g++ -o src/main/c++/rbt.out src/main/c++/recursive_bracktracker.cpp").start().waitFor();
            new ProcessBuilder("sh", "-c", "src/main/c++/rbt.out -w " + gridSize + " -h " + gridSize + " > src/main/java/maze/ascii_maze.txt").start().waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        List<Point> walls = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader("src/main/java/maze/ascii_maze.txt"))) {

            String line;
            int i = 0;
            while ((line = r.readLine()) != null) {

                char[] arr = line.toCharArray();
                for (int j = 0; j < arr.length; j++) {
                    if (arr[j] == 'x') {
                        walls.add(new Point(i, j / 2));
                    }
                }
                ++i;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return walls;
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
