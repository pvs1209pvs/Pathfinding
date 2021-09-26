package sample;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Random;

class DijkstraTest {

    private int initPathfinding(Random random){
        Dijkstra dijkstra = new Dijkstra(50);

        for (int i = 0; i < dijkstra.getSize(); i++) {
            for (int j = 0; j < dijkstra.getSize(); j++) {
                if (random.nextDouble() < 0.5) {
                    dijkstra.getVertex(i, j).setType(Dijkstra.VertexType.WALL);
                }
            }
        }

        Point s = new Point(0, 0);
        dijkstra.getVertex(0, 0).setType(Dijkstra.VertexType.PATH);

        Point e = new Point(dijkstra.getSize()-1, dijkstra.getSize()-1);
        dijkstra.getVertex(dijkstra.getSize()-1, dijkstra.getSize()-1).setType(Dijkstra.VertexType.PATH);

        return dijkstra.shortestPath(s,e).size();
    }
    @Test
    void shortestPathTest() {

        int passSeed = 0;
        int failSeed = 1;

        Assertions.assertEquals(68, initPathfinding(new Random(passSeed)));
        Assertions.assertEquals(1, initPathfinding(new Random(failSeed)));



    }


}