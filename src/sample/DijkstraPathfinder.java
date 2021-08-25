package sample;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;

public class DijkstraPathfinder {

    private final Vertex[][] grid;

    DijkstraPathfinder(int size) {

        grid = new Vertex[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                setVertex(new Vertex(i, j));
            }
        }

    }

    List<Point> shortestPath(Point s, Point e) {

        final Queue<Vertex> q = new ArrayDeque<>();
        final Map<Point, Point> prev = new HashMap<>();

        grid[s.x][s.y].type = VERTEX_TYPE.PATH;
        grid[e.x][e.y].type = VERTEX_TYPE.PATH;

        grid[s.x][s.y].dist = 0;
        q.add(grid[s.x][s.y]);

        while (!q.isEmpty()) {

            final Vertex u = q.remove();
            final List<Vertex> neighbors = getNeighbors(u);

            for (Vertex v : neighbors) {

                v.visited = true;
                q.add(v);

                int altDist = u.dist + 1;
                if (altDist < v.dist) {
                    v.dist = altDist;
                    prev.put(v.getPos(), u.getPos());
                }

            }

        }

        return getShortestPath(prev, grid[e.x][e.y].getPos());

    }

    private List<Point> getShortestPath(Map<Point, Point> prev, Point end) {

        final List<Point> shortestPath = new LinkedList<>();

        while (end != null) {
            shortestPath.add(0, end);
            end = prev.get(end);
        }

        return shortestPath;

    }

    private List<Vertex> getNeighbors(Vertex u) {

        final List<Vertex> neighbors = new ArrayList<>();
        final List<Point> explorationDir = List.of(new Point(-1,0), new Point(1,0), new Point(0,-1), new Point(0,1));

        for (Point dir : explorationDir) {
            int newC = u.c + dir.x;
            int newR = u.r + dir.y;

            if (newC >= 0 && newC < grid.length && newR >= 0 && newR < grid.length && grid[newC][newR].type == VERTEX_TYPE.PATH && !grid[newC][newR].visited) {
                neighbors.add(grid[newC][newR]);
            }
        }

        return neighbors;

    }

    public void setVertex(Vertex v) {
        this.grid[v.c][v.r] = v;
    }

    public Vertex getVertex(int x, int y) {
        return this.grid[x][y];
    }

    public int getSize() {
        return this.grid.length;
    }

    enum VERTEX_TYPE {
        WALL, PATH
    }

    static class Vertex {

        private final int c;
        private final int r;
        private int dist;
        private boolean visited;
        private VERTEX_TYPE type;

        Vertex(int c, int r) {
            this.c = c;
            this.r = r;
            this.dist = Integer.MAX_VALUE;
            this.visited = false;
            this.type = VERTEX_TYPE.PATH;
        }

        public Point getPos() {
            return new Point(c, r);
        }

        public void setType(VERTEX_TYPE type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "[" + c + " " + r + " " + dist + " " + this.type + "]";
        }


    }

}
