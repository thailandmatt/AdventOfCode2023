package aoc;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static java.lang.Integer.parseInt;

// Put the return type of the generator in Base<>
public class Year2023_Day22 extends Base<List<Year2023_Day22.Range3d>> {

    public class Point3d {
        public int x;
        public int y;
        public int z;

        public Point3d(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point3d(String x) {
            var parts = x.split(",");
            this.x = parseInt(parts[0]);
            this.y = parseInt(parts[1]);
            this.z = parseInt(parts[2]);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point3d point3d = (Point3d) o;
            return x == point3d.x && y == point3d.y && z == point3d.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override
        public String toString() {
            return x + "," + y + "," + z;
        }
    }
    public class Range3d {
        public Point3d start;
        public Point3d end;

        public void check() {
            if (start.x + start.y + start.z < end.x + end.y + end.z) {
                var x = start;
                start = end;
                end = x;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Range3d range3d = (Range3d) o;
            return Objects.equals(start, range3d.start) && Objects.equals(end, range3d.end);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }

        @Override
        public String toString() {
            return start.toString() + " - " + end.toString();
        }
    }

    // The generator's role is to turn the input file into something usable by part 1 and part 2.
    void generator() {
        // this must have the same type as the return type of this method
        var lines = this.input.split("\n");
        processed = new ArrayList<>();

        for (var line: lines) {
            var parts = line.split("~");
            Range3d r = new Range3d();
            r.start = new Point3d(parts[0]);
            r.end = new Point3d(parts[1]);
            r.check();
            processed.add(r);
        }
    }
    
    void part1() {
        //sort smallest z to biggest
        processed.sort((a, b) -> {
            var z = ((Integer) a.start.z).compareTo((Integer) b.start.z);
            var x = ((Integer) a.start.x).compareTo((Integer) b.start.x);
            var y = ((Integer) a.start.x).compareTo((Integer) b.start.y);

            if (z != 0) return z;
            if (x != 0) return x;
            return y;
        });

        for (var x: processed) {

        }

        System.out.printf("Part 1: %d\n", 0);
    }

    void part2() {
        // print out the result
        System.out.printf("Part 2: %d\n", 0);
    }
}
