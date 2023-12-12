package aoc;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// Put the return type of the generator in Base<>
public class Year2023_Day10 extends Base<char[][]> {

    // The generator's role is to turn the input file into something usable by part 1 and part 2.
    void generator() {
        // this must have the same type as the return type of this method
        var lines = this.input.split("\n");
        var temp = new ArrayList<>();
        // read it line by line
        for (int i = 0; i < lines.length; i++) {
            // for this one we just want an arraylist of numbers
            temp.add(lines[i].toCharArray());
        }
        this.processed = new char[lines.length][lines[0].length()];
        temp.toArray(processed);
    }

    void part1() {
        int total = 0;

        //find the starting point
        Point start = new Point(-1, -1);
        for (int row = 0; row < processed.length; row++) {
            for (int col = 0; col < processed[0].length; col++) {
                if (processed[row][col] == 'S') {
                    start = new Point(row, col);
                    break;
                }
            }
        }

        //now follow the path and keep track of the whole list
        List<Point> paths = new ArrayList<>();
        paths.add(start);
        paths.add(findNextPointFromStart(start, processed));
        do {
            Point lastBefore = paths.size() > 1 ? paths.get(paths.size() - 2) : new Point(-1, -1);
            Point last = paths.get(paths.size() - 1);
            var next = nextPoint(last, lastBefore, processed);
            paths.add(next);
        } while (!paths.get(paths.size() - 1).equals(start));

        // print out the result
        System.out.printf("Part 1: %d\n", paths.size() / 2);

        //replace anything not
        for (int row = 0; row < processed.length; row++) {
            for (int col = 0; col < processed[0].length; col++) {
                if (!paths.contains(new Point(row, col)) && processed[row][col] != '.') {
                    processed[row][col] = 'X';
                }
            }
        }

        //count edge crossings like polygon counting for any '.'
        //but we only count | and J and L - the "bottom half" of the box
        //so "horizontal lines" don't count
        int totalI = 0;
        for (int row = 0; row < processed.length; row++) {
            for (int col = 0; col < processed[0].length; col++) {
                boolean in = false;
                if (processed[row][col] == '.') {
                    int count = 0;
                    for (int i = 0; i < col; i++) {
                        if (processed[row][i] == '|' && !in) in = true;
                        if (processed[row][i] == '|' && in) in = false;
                        if (processed[row][i] == 'F' && !in) in = true;
                        if (processed[row][i] == '7' && in) in = false;
                        if (processed[row][i] == 'L' && !in) in = true;
                        if (processed[row][i] == 'J' && in) in = false;
                    }

                    if (in) {
                        processed[row][col] = 'I';
                        totalI++;
                    } else {
                        //processed[row][col] = 'O';
                    }
                }
            }
        }

        flood(new Point(0, 0), processed, ' ');

        for (var line: processed) {
            System.out.println(String.valueOf(line));
        }

        System.out.printf("Part 2: %d\n", totalI);
    }

    void flood(Point p, char[][] map, char x) {
        if (map[p.x][p.y] != '.' && map[p.x][p.y] != 'X') return;
        map[p.x][p.y] = x;

        if (p.x > 0) flood(new Point(p.x - 1, p.y), map, x);
        if (p.x < map.length - 1) flood(new Point(p.x + 1,p.y), map, x);
        if (p.y > 0) flood(new Point(p.x, p.y - 1), map, x);
        if (p.y < map[0].length - 1) flood(new Point(p.x, p.y + 1), map, x);
    }

    Point nextPoint(Point p, Point p2, char[][] map) {
        if (map[p.x][p.y] == 'F') {
            if (p2.x == p.x + 1)
                return new Point(p.x, p.y + 1);
            else
                return new Point(p.x + 1, p.y);
        }

        if (map[p.x][p.y] == '|') {
            if (p2.x == p.x + 1)
                return new Point(p.x - 1, p.y);
            else
                return new Point(p.x + 1, p.y);
        }

        if (map[p.x][p.y] == '7') {
            if (p2.x == p.x + 1)
                return new Point(p.x, p.y - 1);
            else
                return new Point(p.x + 1, p.y);
        }

        if (map[p.x][p.y] == '-') {
            if (p2.y == p.y + 1)
                return new Point(p.x, p.y - 1);
            else
                return new Point(p.x, p.y + 1);
        }

        if (map[p.x][p.y] == 'J') {
            if (p2.y == p.y - 1)
                return new Point(p.x - 1, p.y);
            else
                return new Point(p.x, p.y - 1);
        }

        if (map[p.x][p.y] == 'L') {
            if (p2.y == p.y + 1)
                return new Point(p.x - 1, p.y);
            else
                return new Point(p.x, p.y + 1);
        }

        return new Point(-1, -1);
    }

    Point findNextPointFromStart(Point p, char[][] map) {
        //up
        if (p.x > 0) {
            if (map[p.x - 1][p.y] == 'F' ||
                map[p.x - 1][p.y] == '7' ||
                map[p.x - 1][p.y] == '|' ||
                map[p.x - 1][p.y] == 'S')
            {
                return new Point(p.x - 1, p.y);
            }
        }

        //down
        if (p.x < map.length - 1) {
            if (map[p.x + 1][p.y] == 'J' ||
                    map[p.x + 1][p.y] == 'L' ||
                    map[p.x + 1][p.y] == '|' ||
                    map[p.x + 1][p.y] == 'S')
            {
                return new Point(p.x + 1, p.y);
            }
        }

        //left
        if (p.y > 0) {
            if (map[p.x][p.y - 1] == 'L' ||
                    map[p.x][p.y - 1] == '-' ||
                    map[p.x][p.y - 1] == 'F' ||
                    map[p.x][p.y - 1] == 'S')
            {
                return new Point(p.x, p.y - 1);
            }
        }

        //right
        if (p.y < map[0].length) {
            if (map[p.x][p.y + 1] == 'J' ||
                    map[p.x][p.y + 1] == '-' ||
                    map[p.x][p.y + 1] == '7' ||
                    map[p.x][p.y + 1] == 'S')
            {
                return new Point(p.x, p.y + 1);
            }
        }

        return new Point(-1, -1);
    }

    void part2() {
        int total = 0;

        // print out the result
        System.out.printf("none: %d\n", total);
    }
}