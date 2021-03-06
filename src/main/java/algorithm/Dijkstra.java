package algorithm;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;

public class Dijkstra {

    /**
     * Returns the shortest path from start to finish.
     *
     * @param s Starting point.
     * @param e Ending point.
     * @return Shortest path.
     */
    public static List<Point> shortestPath(Vertex[][] grid, Point s, Point e) {

        final Queue<Vertex> q = new ArrayDeque<>();
        final Map<Point, Point> prev = new HashMap<>();

        grid[s.x][s.y].dist = 0;
        q.add(grid[s.x][s.y]);

        while (!q.isEmpty()) {

            final Vertex u = q.remove();
            final List<Vertex> neighbors = getNeighbors(grid, u);

            for (Vertex v : neighbors) {

                v.visited = true;
                q.add(v);

                final int altDist = u.dist + 1;
                if (altDist < v.dist) {
                    v.dist = altDist;
                    prev.put(v.pos, u.pos);
                }

            }

        }


        List<Point> shortestPath = cameFrom(prev, grid[e.x][e.y].pos);
        shortestPath.remove(s);
        shortestPath.remove(e);
        return shortestPath;
    }

    /**
     * Find whom the current vertex came from
     *
     * @param prev Parent vertex.
     * @param end  Child vertex.
     * @return List of the shortest path.
     */
    private static List<Point> cameFrom(Map<Point, Point> prev, Point end) {

        final List<Point> shortestPath = new LinkedList<>();

        while (end != null) {
            shortestPath.add(0, end);
            end = prev.get(end);
        }

        return shortestPath;

    }

    /**
     * Returns the neighbors of the vertex.
     *
     * @param current Current vertex
     * @return Neighbors of the vertex.
     */
    private static List<Vertex> getNeighbors(Vertex[][] grid, Vertex current) {

        final List<Vertex> neighbors = new ArrayList<>();
        final List<Point> explorationDir = new ArrayList<>();

               explorationDir.add(new Point(0, -1));
               explorationDir.add(new Point(-1, 0));
               explorationDir.add(new Point(1, 0));
               explorationDir.add(new Point(0, 1));



        for (Point dir : explorationDir) {
            int newX = current.pos.x + dir.x;
            int newY = current.pos.y + dir.y;
            if (newX >= 0 && newX < grid.length && newY >= 0 && newY < grid.length && grid[newX][newY].type == VertexType.PATH && !grid[newX][newY].visited) {
                neighbors.add(grid[newX][newY]);
            }
        }

        return neighbors;

    }

    public static class Vertex implements GridVertex {

        public final Point pos;
        private VertexType type;
        private int dist;
        public boolean visited;

        public Vertex(Point pos) {
            this.pos = pos;
            this.dist = Integer.MAX_VALUE;
            this.visited = false;
            this.type = VertexType.PATH;
        }

        public void setType(VertexType type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "[" + pos + " " + dist + " " + this.type + "]";
        }

        @Override
        public double getScore() {
            return dist;
        }
    }

}
