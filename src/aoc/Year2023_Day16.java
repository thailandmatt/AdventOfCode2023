package aoc;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static java.lang.Integer.*;

// Put the return type of the generator in Base<>
public class Year2023_Day16 extends Base<ArrayList<String>> {

    // The generator's role is to turn the input file into something usable by part 1 and part 2.
    void generator() {
        // this must have the same type as the return type of this method
        var lines = this.input.split("\n");
        this.processed = new ArrayList<>();
        processed.addAll(Arrays.asList(lines));
    }

    public class Beam {
        public Point start;
        public Point direction;

        public Beam(Point s, Point d) {
            this.start = s;
            this.direction = d;
        }

        @Override
        public boolean equals(Object obj) {
            return ((Beam) obj).start.equals(start) &&
              ((Beam) obj).direction.equals(direction);
        }
    }

    int DoBeam(Point startPoint, Point dir, boolean debug) {
        Beam start = new Beam(startPoint, dir);
        Queue<Beam> beamsToProcess = new LinkedList<>();
        beamsToProcess.add(start);

        List<Beam> processedBeams = new ArrayList<>();

        boolean[][] energized = new boolean[processed.size()][processed.get(0).length()];

        while (!beamsToProcess.isEmpty()) {
            var cur = beamsToProcess.remove();
            if (processedBeams.contains(cur))
                continue;
            processedBeams.add(cur);

            var location = (Point) cur.start.clone();
            while (true) {
                if (location.x < 0 || location.y < 0 ||
                  location.x >= processed.size() || location.y >= processed.get(0).length())
                    break;

                energized[location.x][location.y] = true;

                if (processed.get(location.x).charAt(location.y) == '|' &&
                  cur.direction.y != 0) {
                    beamsToProcess.add(new Beam(location, new Point(-1, 0)));
                    beamsToProcess.add(new Beam(location, new Point(1, 0)));
                    break;
                }
                else if (processed.get(location.x).charAt(location.y) == '-' &&
                  cur.direction.x != 0) {
                    beamsToProcess.add(new Beam(location, new Point(0, -1)));
                    beamsToProcess.add(new Beam(location, new Point(0, 1)));
                    break;
                }

                Point newDir = new Point(0, 0);

                if (processed.get(location.x).charAt(location.y) == '\\' &&
                  cur.direction.y == 1) {
                    newDir = new Point(1, 0);
                }
                else if (processed.get(location.x).charAt(location.y) == '\\' &&
                  cur.direction.y == -1) {
                    newDir = new Point(-1, 0);
                }
                else if (processed.get(location.x).charAt(location.y) == '\\' &&
                  cur.direction.x == 1) {
                    newDir = new Point(0, 1);
                }
                else if (processed.get(location.x).charAt(location.y) == '\\' &&
                  cur.direction.x == -1) {
                    newDir = new Point(0, -1);
                }
                else if (processed.get(location.x).charAt(location.y) == '/' &&
                  cur.direction.x == 1) {
                    newDir = new Point(0, -1);
                }
                else if (processed.get(location.x).charAt(location.y) == '/' &&
                  cur.direction.x == -1) {
                    newDir = new Point(0, 1);
                }
                else if (processed.get(location.x).charAt(location.y) == '/' &&
                  cur.direction.y == 1) {
                    newDir = new Point(-1, 0);
                }
                else if (processed.get(location.x).charAt(location.y) == '/' &&
                  cur.direction.y == -1) {
                    newDir = new Point(1, 0);
                }

                if (newDir.x != 0 || newDir.y != 0) {
                    location.x += newDir.x;
                    location.y += newDir.y;
                    beamsToProcess.add(new Beam(location, newDir));
                    break;
                }

                location.x += cur.direction.x;
                location.y += cur.direction.y;
            }
        }

        int total = 0;
        for (int i = 0; i < energized.length; i++) {
            for (int j = 0; j < energized[0].length; j++) {
                if (debug) System.out.print(energized[i][j] ? "X" : ".");
                if (energized[i][j])
                    total++;
            }
            if (debug) System.out.print("\n");
        }
        if (debug) System.out.print("\n");

        return total;
    }


    void part1() {
        var total = DoBeam(new Point(0, 0), new Point(0, 1), true);

        // print out the result
        System.out.printf("Part 1: %d\n", total);
    }

    void part2() {

        int max = 0;

        //from left
        for (int row = 0; row < processed.size(); row++) {
            var test = DoBeam(new Point(row, 0), new Point(0, 1), false);
            max = Math.max(test, max);
        }

        //from top
        for (int col = 0; col < processed.get(0).length(); col++) {
            var test = DoBeam(new Point(0, col), new Point(1, 0), false);
            max = Math.max(test, max);
        }

        //from right
        for (int row = 0; row < processed.size(); row++) {
            var test = DoBeam(new Point(row, processed.get(0).length() - 1), new Point(0, -1), false);
            max = Math.max(test, max);
        }

        //from bottom
        for (int col = 0; col < processed.get(0).length(); col++) {
            var test = DoBeam(new Point(processed.size() - 1, col), new Point(-1, 0), false);
            max = Math.max(test, max);
        }

        // print out the result
        System.out.printf("Part 2: %d\n", max);
    }
}
