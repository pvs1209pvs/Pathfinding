package sample;

import java.awt.*;
import java.util.*;
import java.util.List;

public class AStar implements Pathfinder {

    private final Vertex[][] grid;

    AStar(int size) {

        grid = new Vertex[size][size];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                System.out.print(i + "," + j + "  ");
                grid[i][j] = new Vertex(new Point(i, j));
            }
            System.out.println();
        }

        grid[1][1].setType(VertexType.WALL);
        grid[1][2].setType(VertexType.WALL);
        grid[2][1].setType(VertexType.WALL);
        grid[3][3].setType(VertexType.WALL);
        grid[4][3].setType(VertexType.WALL);
        grid[0][2].setType(VertexType.WALL);

    }

    public List<Point> shortestPath(Point s, Point e) {

        PriorityQueue<Vertex> openSet = new PriorityQueue<>();
        Map<Point, Point> cameFrom = new HashMap<>();

        openSet.add(grid[s.x][s.y]);

        grid[s.x][s.y].g = 0;
        grid[s.x][s.y].f = grid[s.x][s.y].g + hScore(grid[s.x][s.y].pos, e);

        while (!openSet.isEmpty()) {

//            System.out.println("openset " + openSet);

            Vertex current = openSet.poll();
//            System.out.println("current " + current);

            if (current.pos == e) {
//                System.out.println("path found");
                return null;
            }


            List<Vertex> neighbors = getNeighbors(current);
//            System.out.println("neighbors " + neighbors);

            for (Vertex neighbor : neighbors) {

                double t = current.g + 1;

                if (t < neighbor.g) {
                    cameFrom.put(neighbor.pos, current.pos);
                    neighbor.g = t;
                    neighbor.f = neighbor.g + hScore(neighbor.pos, e);
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
//                    System.out.println("neig" + neighbor);
                }

            }

//            System.out.println();
        }

        return cameFrom(cameFrom, e);
    }

    List<Point> cameFrom(Map<Point, Point> prev, Point end) {

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
                new Point(1, -1),
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

    static class Vertex implements Comparable<Vertex> {

        private double g;
        private double f;
        private final Point pos;
        private VertexType type;

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
