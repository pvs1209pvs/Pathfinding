package sample;

import java.util.*;
import java.util.stream.IntStream;

public class Gridder {

    private final int SIZE = 10;
    private Vertex[][] grid;
    private char[][] map;

    Gridder() {
        grid = new Vertex[SIZE][SIZE];
        map = new char[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {

                Vertex v = new Vertex(i, j);
                v.dist = Integer.MAX_VALUE;

                if(Math.random()<0.3){
                    v.type='w';
                }

                grid[i][j] = v;

            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j].type == 'w') {
                    map[i][j] = '#';
                } else {
                    map[i][j] = '.';
                }
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
        System.out.println();

    }

    void sp() {

        List<Vertex> q = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j].type == 'p') {
                    q.add(grid[i][j]);
                }
            }
        }

        Map<Vertex, Vertex> prev = new HashMap<>();

        grid[0][0].dist = 0;

        while (!q.isEmpty()) {

            Vertex u = Collections.min(q);
            q.remove(u);

            List<Vertex> neighbors = getNeighbors(u);
            
            for (Vertex v : neighbors) {
                v.visited = true;
                int altDist = u.dist + 1;
                if (altDist < v.dist) {
                    v.dist = altDist;
                    prev.put(v, u);
                }
            }

        }

        List<Vertex> s = getShortestPath(prev, grid[SIZE-1][SIZE-1]);

        for(Vertex x : s){
            map[x.c][x.r] = '*';
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
        System.out.println();

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

    private class Vertex implements Comparable<Vertex> {

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
