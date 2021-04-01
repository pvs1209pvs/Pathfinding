package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Arrays;

public class MazeSolver {

    public char[][] maze = new char[20][20];

    MazeSolver() {

        for (char[] chars : maze) {
            Arrays.fill(chars, ' ');
        }

//        maze = createMaze(
//                "  ##################" +
//                        "    #     #   #     " +
//                        "# # # ##### # ### ##" +
//                        "# #   # # # #       " +
//                        "# # ### # ### # ### " +
//                        "# #   # #   # # # # " +
//                        "# ### # # ### # # ##" +
//                        "# # #       # # #   " +
//                        "# # ### # ##### # ##" +
//                        "#   #   # #     # # " +
//                        "####### ### # ### # " +
//                        "#     #     #   #   " +
//                        "### ### ####### ### " +
//                        "#       #           " +
//                        "### ######### # ### " +
//                        "#   #     #   #   # " +
//                        "### # # ########### " +
//                        "#     #       # # # " +
//                        "#     #     #       " +
//                        "###################e");


    }

    public void printAns() {
        for (char[] chars : maze) {
            for (char aChar : chars) {
                System.out.print(aChar);
            }
            System.out.println();
        }
    }

    public boolean solve(char[][] m, Point dir, GraphicsContext g) {

        if (!isValid(m, dir)) {
            return false;
        } else if (m[dir.first][dir.second] == 'e') {
            return true;
        } else {
            m[dir.first][dir.second] = '*';

            g.setFill(Color.rgb(78,165,210));
            g.setStroke(Color.GRAY);
            g.setLineWidth(0.1);
            g.strokeRect(dir.second*25, dir.first*25, 25, 25);
            g.fillRect(dir.second*25, dir.first*25, 25, 25);



            if (solve(m, new Point(dir.first, dir.second + 1), g) ||
                    solve(m, new Point(dir.first + 1, dir.second), g) ||
                    solve(m, new Point(dir.first, dir.second - 1), g) ||
                    solve(m, new Point(dir.first - 1, dir.second), g)

            ) {
                return true;
            } else {
                m[dir.first][dir.second] = '.';

                g.setFill(Color.rgb(89,83,134));
                g.setStroke(Color.GRAY);
                g.setLineWidth(0.1);
                g.strokeRect(dir.second*25, dir.first*25, 25, 25);
                g.fillRect(dir.second*25, dir.first*25, 25, 25);



                return false;
            }
        }
    }

    private boolean isValid(char[][] m, Point dir) {
        return !(dir.first < 0 || dir.first >= m.length ||
                dir.second < 0 || dir.second >= m.length ||
                m[dir.first][dir.second] == '#' ||
                m[dir.first][dir.second] == '*' ||
                m[dir.first][dir.second] == '.');
    }

    private char[][] createMaze(String s) {

        int size = (int) Math.sqrt(s.length());
        char[][] m = new char[size][size];

        int itr = -1;

        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                m[i][j] = s.charAt(++itr);
            }
        }

        return m;

    }

    public static class Point {

        public int first;
        public int second;

        public Point(int first, int second) {
            this.first = first;
            this.second = second;
        }

        public Point() {
            this.first = 0;
            this.second = 0;
        }

    }

}
