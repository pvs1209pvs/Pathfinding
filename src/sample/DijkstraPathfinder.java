package sample;

import java.awt.*;
import java.util.*;
import java.util.List;

public class DijkstraPathfinder {

    private int size;
    private Vertex[][] grid;

    DijkstraPathfinder(int size) {
        this.size = size;

        grid = new Vertex[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new Vertex(i, j, Integer.MAX_VALUE);
            }
        }
    }


    private String pointPrinter(Point p) {
        return "[" + p.x + "," + p.y + "] ";
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

        final int[] vectorR = new int[]{-1, 0, 1, 1, 1, 0, -1, -1};
        final int[] vectorC = new int[]{-1, -1, -1, 0, 1, 1, 1, 0};

        for (int i = 0; i < 8; i++) {
            int newC = u.c + vectorC[i];
            int newR = u.r + vectorR[i];

            if (newC >= 0 && newC < size && newR >= 0 && newR < size && grid[newC][newR].type == VERTEX_TYPE.PATH && !grid[newC][newR].visited) {
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

    public int getSize(){
        return this.size;
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

        Vertex(int c, int r, int dist) {
            this.c = c;
            this.r = r;
            this.dist = dist;
            this.visited = false;
            this.type = VERTEX_TYPE.PATH;
        }

        public Point getPos() {
            return new Point(c, r);
        }

        @Override
        public String toString() {
            return "[" + c + " " + r + " " + dist + " " + this.type + "]";
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
