package sample;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Random;

class DijkstraPathfinderTest {

    private int initPathfinding(Random random){
        DijkstraPathfinder dijkstraPathfinder = new DijkstraPathfinder(50);

        for (int i = 0; i < dijkstraPathfinder.getSize(); i++) {
            for (int j = 0; j < dijkstraPathfinder.getSize(); j++) {
                if (random.nextDouble() < 0.5) {
                    dijkstraPathfinder.getVertex(i, j).setType(DijkstraPathfinder.VertexType.WALL);
                }
            }
        }

        Point s = new Point(0, 0);
        dijkstraPathfinder.getVertex(0, 0).setType(DijkstraPathfinder.VertexType.PATH);

        Point e = new Point(dijkstraPathfinder.getSize()-1, dijkstraPathfinder.getSize()-1);
        dijkstraPathfinder.getVertex(dijkstraPathfinder.getSize()-1, dijkstraPathfinder.getSize()-1).setType(DijkstraPathfinder.VertexType.PATH);

        return dijkstraPathfinder.shortestPath(s,e).size();
    }
    @Test
    void shortestPathTest() {

        int passSeed = 0;
        int failSeed = 1;

        Assertions.assertEquals(68, initPathfinding(new Random(passSeed)));
        Assertions.assertEquals(1, initPathfinding(new Random(failSeed)));



    }


}