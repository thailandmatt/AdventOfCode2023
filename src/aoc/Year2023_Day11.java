package aoc;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

// Put the return type of the generator in Base<>
public class Year2023_Day11 extends Base<ArrayList<String>> {

    // The generator's role is to turn the input file into something usable by part 1 and part 2.
    void generator() {
        // this must have the same type as the return type of this method
        var lines = this.input.split("\n");
        this.processed = new ArrayList<>();
        // read it line by line
        // for this one we just want an arraylist of numbers
        this.processed.addAll(Arrays.asList(lines));
    }

    void part1() {
        //expand
        for (int i = 0; i < processed.size(); i++) {
            if (!processed.get(i).contains("#")) {
                processed.add(i, processed.get(i));
                i++;
            }
        }

        for (int i = 0; i < processed.get(0).length(); i++) {
            boolean has = false;
            for (int j = 0; j < processed.size(); j++) {
                if (processed.get(j).charAt(i) == '#') {
                    has = true;
                    break;
                }
            }

            if (!has) {
                for (int j = 0; j < processed.size(); j++) {
                    processed.set(j,
                      processed.get(j).substring(0, i) + "." +
                        processed.get(j).substring(i));
                }

                i++;
            }
        }

        //locate
        List<Point> locations = new ArrayList<>();
        for (int i = 0; i < processed.size(); i++) {
            for (int j = 0; j < processed.get(i).length(); j++) {
                if (processed.get(i).charAt(j) == '#') {
                    locations.add(new Point(i, j));
                }
            }
        }

        //calculate distance between all
        List<Integer> lengths = new ArrayList<>();

        for (int i = 0; i < locations.size(); i++) {
            for (int j = i + 1; j < locations.size(); j++) {
                var x = locations.get(i);
                var y = locations.get(j);
                var total = Math.abs(x.y - y.y) + Math.abs(x.x - y.x);

                lengths.add(total);
            }
        }

        // print out the result
        System.out.printf("Part 1: %d\n", lengths.stream().mapToInt(Integer::intValue).sum());
    }

    void part2() {
        //expand
        List<Integer> blankRows = new ArrayList<>();
        List<Integer> blankCols = new ArrayList<>();

        for (int i = 0; i < processed.size(); i++) {
            if (!processed.get(i).contains("#")) {
                blankRows.add(i);
            }
        }

        for (int i = 0; i < processed.get(0).length(); i++) {
            boolean has = false;
            for (int j = 0; j < processed.size(); j++) {
                if (processed.get(j).charAt(i) == '#') {
                    has = true;
                    break;
                }
            }

            if (!has) {
                blankCols.add(i);
            }
        }

        //locate
        List<Point> locations = new ArrayList<>();
        for (int i = 0; i < processed.size(); i++) {
            for (int j = 0; j < processed.get(i).length(); j++) {
                if (processed.get(i).charAt(j) == '#') {
                    locations.add(new Point(i, j));
                }
            }
        }

        //calculate distance between all
        List<Long> lengths = new ArrayList<>();

        for (int i = 0; i < locations.size(); i++) {
            for (int j = i + 1; j < locations.size(); j++) {
                var x = locations.get(i);
                var y = locations.get(j);

                int manhattan = Math.abs(x.y - y.y) + Math.abs(x.x - y.x);

                //see how many times we cross rows and cols
                var rowCross = blankRows.stream().filter(z -> (z > x.x && z < y.x) || (z > y.x && z < x.x)).count();
                var colCross = blankCols.stream().filter(z -> (z > x.y && z < y.y) || (z > y.y && z < x.y)).count();

                long factor = 1000000L;
                factor = factor - 1;
                var total = (factor * rowCross) + (factor * colCross) + manhattan;

                lengths.add(total);
            }
        }

        // print out the result
        System.out.printf("Part 2: %d\n", lengths.stream().mapToLong(Long::longValue).sum());
    }
}