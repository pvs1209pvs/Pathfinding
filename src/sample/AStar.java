package sample;

import java.awt.*;
import java.util.*;
import java.util.List;

public class AStar {

    private final Vertex[][] grid;

    AStar(int size) {

        grid = new Vertex[size][size];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                grid[i][j] = new Vertex(new Point(i, j));
            }
        }

    }

    public List<Point> shortestPath(Point s, Point e) {

        grid[s.x][s.y].type = VertexType.PATH;
        grid[e.x][e.y].type = VertexType.PATH;

        PriorityQueue<Vertex> openSet = new PriorityQueue<>();
        Map<Point, Point> cameFrom = new HashMap<>();

        openSet.add(grid[s.x][s.y]);

        grid[s.x][s.y].g = 0;
        grid[s.x][s.y].f = grid[s.x][s.y].g + hScore(grid[s.x][s.y].pos, e);

        while (!openSet.isEmpty()) {

            Vertex current = openSet.poll();

            if (current.pos == e) {
                return null;
            }

            List<Vertex> neighbors = getNeighbors(current);

            for (Vertex neighbor : neighbors) {

                double t = current.g + 1;

                if (t < neighbor.g) {
                    cameFrom.put(neighbor.pos, current.pos);
                    neighbor.g = t;
                    neighbor.f = neighbor.g + hScore(neighbor.pos, e);

                    if (openSet.stream().noneMatch(u -> u.pos == neighbor.pos)) {
                        openSet.add(neighbor);
                    }
                }


            }

        }

        return cameFrom(cameFrom, e);
    }

    private List<Point> cameFrom(Map<Point, Point> prev, Point end) {

        final List<Point> shortestPath = new LinkedList<>();

        while (end != null) {
            shortestPath.add(0, end);
            end = prev.get(end);
        }

        return shortestPath;

    }

    private List<Vertex> getNeighbors(Vertex current) {

        final List<Vertex> neighbors = new ArrayList<>();
        final List<Point> explorationDir = List.of(
                new Point(-1, 0),
                new Point(1, 0),
                new Point(0, -1),
                new Point(0, 1));


        for (Point dir : explorationDir) {
            int newX = current.pos.x + dir.x;
            int newY = current.pos.y + dir.y;

            if (newX >= 0 && newX < grid.length && newY >= 0 && newY < grid.length && grid[newX][newY].getType() == VertexType.PATH) {
                neighbors.add(grid[newX][newY]);
            }
        }

        return neighbors;


    }

    private double hScore(Point current, Point e) {
        return current.distance(e);
    }

    public Vertex getVertex(int x, int y) {
        return grid[x][y];
    }

    static class Vertex implements Comparable<Vertex> {

        private final Point pos;
        private VertexType type;
        private double g;
        private double f;

        Vertex(Point pos) {
            this.pos = pos;
            this.g = Integer.MAX_VALUE;
            this.f = Integer.MAX_VALUE;
            this.type = VertexType.PATH;
        }

        public VertexType getType() {
            return type;
        }

        public void setType(VertexType type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "(" + pos.x + "," + pos.y + " f:" + f + " g:" + g + ")";
        }

        @Override
        public int compareTo(Vertex vertex) {
            return Double.compare(this.f, vertex.f);
        }

    }

    public enum VertexType {
        PATH, WALL
    }

}
