package algorithm;

import java.awt.*;
import java.util.*;
import java.util.List;

public class AStar {

    public static List<Point> shortestPath(Vertex[][] grid, Point s, Point e) {

        final PriorityQueue<Vertex> openSet = new PriorityQueue<>();
        final Map<Point, Point> prev = new HashMap<>();

        openSet.add(grid[s.x][s.y]);

        grid[s.x][s.y].g = 0;
        grid[s.x][s.y].f = grid[s.x][s.y].g + hScore(grid[s.x][s.y].pos, e);

        while (!openSet.isEmpty()) {

            final Vertex current = openSet.poll();

            if (current.pos == e) {
                return new ArrayList<>();
            }

            final List<Vertex> neighbors = getNeighbors(grid, current);

            for (Vertex neighbor : neighbors) {

                final double t = current.g + 1;

                if (t < neighbor.g) {
                    prev.put(neighbor.pos, current.pos);
                    neighbor.g = t;
                    neighbor.f = neighbor.g + hScore(neighbor.pos, e);

                    if (openSet.stream().noneMatch(u -> u.pos == neighbor.pos)) {
                        openSet.add(neighbor);
                    }
                }

            }

        }
        List<Point> shortestPath = cameFrom(prev, e);
        shortestPath.remove(s);
        shortestPath.remove(e);
        return shortestPath;
    }

    private static List<Point> cameFrom(Map<Point, Point> prev, Point end) {

        final List<Point> shortestPath = new LinkedList<>();

        while (end != null) {
            shortestPath.add(0, end);
            end = prev.get(end);
        }

        return shortestPath;

    }

    private static List<Vertex> getNeighbors(Vertex[][] grid, Vertex current) {

        final List<Vertex> neighbors = new ArrayList<>();
        final List<Point> explorationDir = new ArrayList<>();

        explorationDir.add(new Point(0, -1));
        explorationDir.add(new Point(-1, 0));
        explorationDir.add(new Point(1, 0));
        explorationDir.add(new Point(0, 1));

        for (Point dir : explorationDir) {
            int newX = current.pos.x + dir.x;
            int newY = current.pos.y + dir.y;

            if (newX >= 0 && newX < grid.length && newY >= 0 && newY < grid.length && grid[newX][newY].getType() == VertexType.PATH) {
                neighbors.add(grid[newX][newY]);
            }
        }

        return neighbors;


    }

    private static double hScore(Point current, Point e) {
        return current.distance(e);
    }

    public static class Vertex implements Comparable<Vertex>, GridVertex {

        private final Point pos;
        private VertexType type;
        private double g;
        private double f;

        public Vertex(Point pos) {
            this.pos = pos;
            this.g = Integer.MAX_VALUE;
            this.f = Integer.MAX_VALUE;
            this.type = VertexType.PATH;
        }

        public double getF() {
            return f;
        }

        public double getG() {
            return g;
        }

        public VertexType getType() {
            return type;
        }

        public void setType(VertexType type) {
            this.type = type;
        }

//        @Override
//        public String toString() {
//            return "(" + pos.x + "," + pos.y + " f:" + f + " g:" + g + ")";
//        }
        public String toString() {
            return "(" + pos.x + "," + pos.y + ")";
        }

        @Override
        public int compareTo(Vertex vertex) {
            return Double.compare(this.f, vertex.f);
        }

        @Override
        public double getScore() {
            return f;
        }
    }


}
