import algorithm.AStar;
import algorithm.VertexType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Random;

class AStarTest {


    private int initPathfinding(Random random) {

        AStar.Vertex[][] grid = new AStar.Vertex[50][50];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                grid[i][j] = new AStar.Vertex(new Point(i, j));
            }
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (random.nextDouble() < 0.3) {
                    grid[i][j].setType(VertexType.WALL);
                }
            }
        }

        grid[0][0].setType(VertexType.PATH);
        grid[grid.length - 1][grid.length - 1].setType(VertexType.PATH);

        return AStar.shortestPath(grid, new Point(0, 0), new Point(grid.length - 1, grid.length - 1)).size();
    }

    @Test
    void shortestPathTest() {

        final int passSeed = 1;
        final int failSeed = 0;

        Assertions.assertEquals(56, initPathfinding(new Random(passSeed)));

    }


}