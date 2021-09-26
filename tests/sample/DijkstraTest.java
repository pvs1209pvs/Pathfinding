package sample;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Random;

class DijkstraTest {

    private int initPathfinding(Random random) {

        Dijkstra.Vertex[][] grid = new Dijkstra.Vertex[50][50];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                grid[i][j] = new Dijkstra.Vertex(new Point(i, j));
            }
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (random.nextDouble() < 0.3) {
                    grid[i][j].setType(Dijkstra.VertexType.WALL);
                }
            }
        }

        grid[0][0].setType(Dijkstra.VertexType.PATH);
        grid[grid.length-1][grid.length-1].setType(Dijkstra.VertexType.PATH);

        return Dijkstra.shortestPath(grid, new Point(0,0), new Point(grid.length-1, grid.length-1)).size();
    }

    @Test
    void shortestPathTest() {

        int passSeed = 1;
        int failSeed = 0;

        Assertions.assertEquals(103, initPathfinding(new Random(passSeed)));
        Assertions.assertEquals(1, initPathfinding(new Random(failSeed)));


    }


}