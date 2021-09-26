package sample;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;

public class Dijkstra {

    private final Vertex[][] grid;

    Dijkstra(int size) {

        grid = new Vertex[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                setVertex(new Vertex(new Point(i, j)));
            }
        }

    }

    public List<Point> shortestPath(Point s, Point e) {

        final Queue<Vertex> q = new ArrayDeque<>();
        final Map<Point, Point> prev = new HashMap<>();

        grid[s.x][s.y].dist = 0;
        q.add(grid[s.x][s.y]);

        while (!q.isEmpty()) {

            final Vertex u = q.remove();
            final List<Vertex> neighbors = getNeighbors(u);

            for (Vertex v : neighbors) {

                v.visited = true;
                q.add(v);

                final int altDist = u.dist + 1;
                if (altDist < v.dist) {
                    v.dist = altDist;
                    prev.put(v.pos, u.pos);
                }

            }

        }

        return cameFrom(prev, grid[e.x][e.y].pos);

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
            if (newX >= 0 && newX < grid.length && newY >= 0 && newY < grid.length && grid[newX][newY].type == VertexType.PATH && !grid[newX][newY].visited) {
                neighbors.add(grid[newX][newY]);
            }
        }

        return neighbors;

    }

    public void setVertex(Vertex v) {
        this.grid[v.pos.x][v.pos.y] = v;
    }

    public Vertex getVertex(int x, int y) {
        return this.grid[x][y];
    }

    public int getSize() {
        return this.grid.length;
    }

    enum VertexType {
        WALL, PATH
    }

    static class Vertex {

        private final Point pos;
        private VertexType type;
        private int dist;
        private boolean visited;

        Vertex(Point pos) {
            this.pos = pos;
            this.dist = Integer.MAX_VALUE;
            this.visited = false;
            this.type = VertexType.PATH;
        }

        public void setType(VertexType type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "[" + pos + " " + dist + " " + this.type + "]";
        }


    }

}
