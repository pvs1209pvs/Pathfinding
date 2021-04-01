package sample;

import java.util.*;
import java.util.stream.IntStream;

public class Gridder {

    private final int SIZE = 3;
    private Vertex[][] grid;

    Gridder() {
        grid = new Vertex[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Vertex v = new Vertex(i, j);
                v.dist = Integer.MAX_VALUE;
                grid[i][j] = v;
            }
        }

        grid[1][1].type='w';
        grid[0][1].type='w';
        grid[1][2].type='w';

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(grid[i][j] +" ");
            }
            System.out.println();
        }
        System.out.println();

    }

    void sp() {

        List<Vertex> q = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if(grid[i][j].type=='p'){
                    q.add(grid[i][j]);
                }
            }
        }

        System.out.println(q+"\n");

        Map<Vertex,Vertex> prev = new HashMap<>();

        grid[0][0].dist = 0;

        while (!q.isEmpty()) {

            Vertex u = Collections.min(q);
            q.remove(u);

            System.out.println("u " + u);
            List<Vertex> neighbors = getNeighbors(u);

            //System.out.println(neighbors);

            for (Vertex v : neighbors) {
                v.visited= true;
                int altDist = u.dist + 1;
                if (altDist < v.dist) {
                    v.dist = altDist;
                    prev.put(v,u);
                }
            }

            System.out.println(q+"\n");
            //System.out.println(neighbors);
            //System.out.println();
        }

//        System.out.println(getShortestPath(prev, grid[2][2]));
//        System.out.println();

         for (int i = 0; i < SIZE; i++) {
             for (int j = 0; j < SIZE; j++) {
                 System.out.print(grid[i][j] +" ");
             }
             System.out.println();
         }

    }

    private List<Vertex> getShortestPath(Map<Vertex,Vertex> prev, Vertex end){

        List<Vertex> shortestPath = new ArrayList<>();

        while(end!=null){
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

            if (newC >= 0 && newC < SIZE && newR >= 0 && newR < SIZE && grid[newC][newR].type=='p' && !grid[newC][newR].visited){
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
