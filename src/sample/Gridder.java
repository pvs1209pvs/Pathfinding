package sample;

import java.util.*;
public class Gridder {

    private final int SIZE = 50;
    Vertex[][] grid;

    Gridder() {

        grid = new Vertex[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Vertex v = new Vertex(i, j);
                v.dist = Integer.MAX_VALUE;
                grid[i][j] = v;
            }
        }

    }

    List<Vertex> sp(int startX, int startY, int endX, int endY) {


        Queue<Vertex> q = new ArrayDeque<>();
        Map<Vertex, Vertex> prev = new HashMap<>();

        grid[startX][startY].dist = 0;
        q.add(grid[startX][startY]);

        while (!q.isEmpty()) {

            Vertex u = q.remove();

            List<Vertex> neighbors = getNeighbors(u);

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

        return getShortestPath(prev, grid[endX][endY]);

    }

    private List<Vertex> getShortestPath(Map<Vertex, Vertex> prev, Vertex end) {

        List<Vertex> shortestPath = new ArrayList<>();

        while (end != null) {
            shortestPath.add(end);
            end = prev.get(end);
        }

        return shortestPath;

    }

    private List<Vertex> getNeighbors(Vertex u) {

        List<Vertex> neighbors = new ArrayList<>();

        int[] vectorC = new int[]{-1, 1, 0, 0};
        int[] vectorR = new int[]{0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newC = u.c + vectorC[i];
            int newR = u.r + vectorR[i];

            if (newC >= 0 && newC < SIZE && newR >= 0 && newR < SIZE && grid[newC][newR].type == 'p' && !grid[newC][newR].visited) {
                neighbors.add(grid[newC][newR]);
            }
        }

        return neighbors;

    }

    static class Vertex implements Comparable<Vertex> {

        int c;
        int r;
        int dist;
        char type = 'p';
        boolean visited = false;

        public Vertex(int c, int r) {
            this.c = c;
            this.r = r;
        }

        public Vertex() {
            this(-1, -1);
        }

        @Override
        public String toString() {
            return "[" + c + " " + r + " " + dist + "]";
        }

        @Override
        public int compareTo(Vertex o) {
            return Integer.compare(this.dist, o.dist);
        }

    }

}
