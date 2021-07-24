package sample;

import java.awt.*;
import java.util.*;
import java.util.List;

public class DijkstraPathfinder {

    private final int SIZE = 50;
    private final Vertex[][] grid = new Vertex[SIZE][SIZE];

    DijkstraPathfinder() {

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = new Vertex(i, j, Integer.MAX_VALUE);
            }
        }

    }

    List<Vertex> shortestPath(Point end, Point start) {

        final Queue<Vertex> q = new ArrayDeque<>();
        final Map<Vertex, Vertex> prev = new HashMap<>();

        grid[end.x][end.y].dist = 0;
        q.add(grid[end.x][end.y]);

        while (!q.isEmpty()) {

            final Vertex u = q.remove();
            final List<Vertex> neighbors = getNeighbors(u);

            for (Vertex v : neighbors) {
                q.add(v);
                v.visited = true;
                int altDist = u.dist + 1;
                if (altDist < v.dist) {
                    v.dist = altDist;
                    prev.put(v, u);
                }
            }

        }

        return getShortestPath(prev, grid[start.x][start.y]);

    }

    private List<Vertex> getShortestPath(Map<Vertex, Vertex> prev, Vertex end) {

        final List<Vertex> shortestPath = new ArrayList<>();

        while (end != null) {
            shortestPath.add(end);
            end = prev.get(end);
        }

        return shortestPath;

    }

    private List<Vertex> getNeighbors(Vertex u) {

        final List<Vertex> neighbors = new ArrayList<>();

        final int[] vectorC = new int[]{-1, 1, 0, 0, -1, -1, 1, 1};
        final int[] vectorR = new int[]{0, 0, -1, 1, -1, 1, -1, 1};

        for (int i = 0; i < 8; i++) {
            int newC = u.c + vectorC[i];
            int newR = u.r + vectorR[i];

            if (newC >= 0 && newC < SIZE && newR >= 0 && newR < SIZE && grid[newC][newR].type == VERTEX_TYPE.PATH && !grid[newC][newR].visited) {
                neighbors.add(grid[newC][newR]);
            }
        }

        return neighbors;

    }

    public void setVertex(int x, int y, Vertex v) {
        this.grid[x][y] = v;
    }

    public Vertex getVertex(int x, int y) {
        return this.grid[x][y];
    }

    enum VERTEX_TYPE {
        WALL,
        PATH
    }

    static class Vertex implements Comparable<Vertex> {

        private final int c;
        private final int r;
        private int dist;
        private boolean visited = false;
        private VERTEX_TYPE type = VERTEX_TYPE.PATH;

        Vertex(int c, int r, int dist) {
            this.c = c;
            this.r = r;
            this.dist = dist;
        }

        @Override
        public String toString() {
            return "[" + c + " " + r + " " + dist + "]";
        }

        @Override
        public int compareTo(Vertex o) {
            return Integer.compare(this.dist, o.dist);
        }

        public int getC() {
            return c;
        }

        public int getR() {
            return r;
        }

        public void setType(VERTEX_TYPE type) {
            this.type = type;
        }

    }

}
