package aoc;

import java.awt.Point;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.*;

// Put the return type of the generator in Base<>
public class Year2023_Day18 extends Base<List<String>> {

    // The generator's role is to turn the input file into something usable by part 1 and part 2.
    void generator() {
        // this must have the same type as the return type of this method
        var lines = this.input.split("\n");
        this.processed = new ArrayList();
        processed.addAll(List.of(lines));
    }
    
    void part1() {
        List<Point> path = new ArrayList<>();
        path.add(new Point(0, 0));

        for (var line: processed) {
            var parts = line.split(" ");
            var dir = parts[0].equals("R") ? new Point(0, 1) :
                    parts[0].equals("L") ? new Point(0, -1) :
                    parts[0].equals("U") ? new Point(-1, 0) :
                    parts[0].equals("D") ? new Point(1, 0): new Point(0, 0);


            for (int i = 0; i < parseInt(parts[1]); i++) {
                var last = path.get(path.size() - 1);
                path.add(new Point(last.x + dir.x, last.y + dir.y));
            }
        }

        //reset to 0, 0 so we can print it out because its fun
        Point min = new Point(MAX_VALUE, MAX_VALUE);
        Point max = new Point(MIN_VALUE, MIN_VALUE);
        for (var point: path) {
            min.x = Math.min(min.x, point.x);
            min.y = Math.min(min.y, point.y);
            max.x = Math.max(max.x, point.x);
            max.y = Math.max(max.y, point.y);
        }

        max.x = max.x - min.x;
        max.y = max.y - min.y;

        for (var point: path) {
            point.x = point.x - min.x;
            point.y = point.y - min.y;
        }

        List<String> map = new ArrayList<>();
        for (int x = 0; x <= max.x; x++) {
            map.add("_".repeat(max.y));
        }

        for (var point: path) {
            var x = map.get(point.x);
            x = x.substring(0, point.y) + 'X' + (point.y +1 < x.length() ? x.substring(point.y + 1) : "");
            map.set(point.x, x);
        }

        System.out.println("OUTLINE");
        for (var line: map) {
            System.out.println(line);
        }

        //flood fill from the first "inside" spot we encounter
        for (var x = 1; x < map.size(); x++) {
            var s = map.get(x);
            boolean inside = false;
            boolean didFlood = false;

            for (var y = 0; y < s.length(); y++) {
                if (s.charAt(y) == 'X')
                    inside = !inside;
                else if (inside && s.charAt(y) == '_')
                {
                    Flood(map, new Point(x, y));
                    didFlood = true;
                    break;
                }
            }
            if (didFlood) break;
        }

        System.out.println("FLOOD");
        for (var line: map) {
            System.out.println(line);
        }

        var total = 0;
        for (var x = 0; x < map.size(); x++) {
            for (var y = 0; y < map.get(x).length(); y++) {
                if (map.get(x).charAt(y) == 'X') total++;
            }
        }

        // print out the result
        System.out.printf("Part 1: %d\n", total);
    }

    void Flood(List<String> map, Point start)  {
        List<Point> floodPoints = new ArrayList<>();
        floodPoints.add(start);
        while (!floodPoints.isEmpty()) {
            var x = floodPoints.remove(0);
            var s = map.get(x.x);
            if (s.charAt(x.y) == '_') {
                s = s.substring(0, x.y) + 'X' + (x.y + 1 < s.length() ? s.substring(x.y + 1) : "");
                map.set(x.x, s);

                if (x.y > 0)
                    floodPoints.add(new Point(x.x, x.y - 1));
                if (x.y < s.length() - 1)
                    floodPoints.add(new Point(x.x, x.y + 1));
                if (x.x > 0)
                    floodPoints.add(new Point(x.x - 1, x.y));
                if (x.x < map.size() - 1)
                    floodPoints.add(new Point(x.x + 1, x.y));
            }
        }
    }

    public class LongPoint {
        public Long x;
        public Long y;

        public LongPoint(Long x, Long y) {
            this.x = x;
            this.y = y;
        }
    }

    void part2() {
        //yikes
        List<LongPoint> path = new ArrayList<>();
        path.add(new LongPoint(0L, 0L));

        Long perimeter = 0L;

        for (var line: processed) {
            var parts = line.split("[ (#)]");
            var hex = parts[parts.length - 1];

            var dir = new LongPoint(0L,0L);
            if (hex.charAt(hex.length() - 1) == '0')
                dir = new LongPoint(0L, 1L);
            if (hex.charAt(hex.length() - 1) == '3') dir =
              new LongPoint(-1L, 0L);
            if (hex.charAt(hex.length() - 1) == '2') dir =
              new LongPoint(0L, -1L);
            if (hex.charAt(hex.length() - 1) == '1') dir =
              new LongPoint(1L, 0L);

            var length = Long.decode("0x" + hex.substring(0, hex.length() - 1));
            perimeter += length;


            var last = path.get(path.size() - 1);
            var newPath = new LongPoint(last.x + (dir.x * length), last.y + (dir.y * length));
            path.add(newPath);
        }

        //run shoelace
        var total = 0L;
        for (int i = 1; i < path.size(); i++) {
            //add cross product to total
            var last = i - 1;
            var x = path.get(i);
            var y = path.get(last);

            var crossProduct = ((y.x * x.y) - (y.y * x.x));
            total += crossProduct;
        }
//
//        var x = path.get(0);
//        var y = path.get(path.size() - 1);
//        total += ((x.x * y.y) - (x.y * y.x));

        total = Math.abs(total);
        total /= 2L;

        total += (perimeter / 2) + 1;

        // print out the result
        System.out.printf("Part 2: %d\n", total);
    }
}
