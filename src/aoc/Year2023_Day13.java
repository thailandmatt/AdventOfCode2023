package aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;

// Put the return type of the generator in Base<>
public class Year2023_Day13 extends Base<ArrayList<ArrayList<String>>> {

    // The generator's role is to turn the input file into something usable by part 1 and part 2.
    void generator() {
        // this must have the same type as the return type of this method
        var lines = this.input.split("\n\n");
        this.processed = new ArrayList<>();
        for (var line: lines) {
            var group = line.split("\n");
            ArrayList<String> test = new ArrayList<>();
            test.addAll(Arrays.asList(group));
            processed.add(test);
        }
    }

    void part1() {

        List<Integer> rowMirrors = new ArrayList<>();
        List<Integer> colMirrors = new ArrayList<>();

        for (var group: processed) {
            boolean found = false;
            var height = group.size();
            var width = group.get(0).length();

            //check horizontal
            for (var row = 1; row < height; row++) {
                boolean isValid = true;

                for (int offset = 0; row - offset > 0 && row + offset < height; offset++) {
                    var prevRow = group.get(row - 1 - offset);
                    var thisRow = group.get(row + offset);
                    for (var col = 0; col < width; col++) {
                        if (prevRow.charAt(col) != thisRow.charAt(col)) {
                            isValid = false;
                            break;
                        }
                    }

                    if (!isValid) break;
                }

                if (isValid) {
                    //answer for this one
                    rowMirrors.add(row);
                    found = true;
                    break;
                }
            }

            //check vertical
            for (var col = 1; col < width; col++) {
                boolean isValid = true;

                for (int offset = 0; col - offset > 0 && col + offset < width; offset++) {
                    for (var row = 0; row < height; row++) {
                        var thisRow = group.get(row);
                        if (thisRow.charAt(col - 1 - offset) != thisRow.charAt(col + offset)) {
                            isValid = false;
                            break;
                        }
                    }

                    if (!isValid) break;
                }

                if (isValid) {
                    //answer for this one
                    colMirrors.add(col);
                    found = true;
                    break;
                }
            }

            if (!found)
                System.out.println("BAD");
        }

        var total = (rowMirrors.stream().mapToInt(Integer::intValue).sum() * 100) +
                    (colMirrors.stream().mapToInt(Integer::intValue).sum());

        // print out the result
        System.out.printf("Part 1: %d\n", total);
    }

    void part2() {
        List<Integer> rowMirrors = new ArrayList<>();
        List<Integer> colMirrors = new ArrayList<>();

        for (var group: processed) {
            boolean found = false;
            var height = group.size();
            var width = group.get(0).length();

            //check horizontal
            for (var row = 1; row < height; row++) {
                boolean isValid = true;

                int badCount = 0;
                for (int offset = 0; row - offset > 0 && row + offset < height; offset++) {
                    var prevRow = group.get(row - 1 - offset);
                    var thisRow = group.get(row + offset);
                    for (var col = 0; col < width; col++) {
                        if (prevRow.charAt(col) != thisRow.charAt(col)) {
                            badCount++;
                            if (badCount > 1) {
                                isValid = false;
                                break;
                            }
                        }
                    }

                    if (!isValid) break;
                }

                if (isValid && badCount == 1) {
                    //answer for this one
                    rowMirrors.add(row);
                    found = true;
                    break;
                }
            }

            //check vertical
            for (var col = 1; col < width; col++) {
                boolean isValid = true;

                int badCount = 0;
                for (int offset = 0; col - offset > 0 && col + offset < width; offset++) {
                    for (var row = 0; row < height; row++) {
                        var thisRow = group.get(row);
                        if (thisRow.charAt(col - 1 - offset) != thisRow.charAt(col + offset)) {
                            badCount++;
                            if (badCount > 1) {
                                isValid = false;
                                break;
                            }
                        }
                    }

                    if (!isValid) break;
                }

                if (isValid && badCount == 1) {
                    //answer for this one
                    colMirrors.add(col);
                    found = true;
                    break;
                }
            }

            if (!found)
                System.out.println("BAD");
        }

        var total = (rowMirrors.stream().mapToInt(Integer::intValue).sum() * 100) +
                (colMirrors.stream().mapToInt(Integer::intValue).sum());

        // print out the result
        System.out.printf("Part 2: %d\n", total);
    }
}
