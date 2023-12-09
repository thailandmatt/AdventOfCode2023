package aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// Put the return type of the generator in Base<>
public class Year2023_Day09 extends Base<ArrayList<String>> {

    // The generator's role is to turn the input file into something usable by part 1 and part 2.
    void generator() {
        // this must have the same type as the return type of this method
        var lines = this.input.split("\n");
        this.processed = new ArrayList<>();
        // read it line by line
        for (int i = 0; i < lines.length; i++) {
            // for this one we just want an arraylist of numbers
            this.processed.add(lines[i]);
        }
    }

    void part1() {
        int total = 0;

        for (var line: processed) {
            var values = line.split(" ");
            var vals = Arrays.asList(values).stream().map(Integer::parseInt).collect(Collectors.toList());
            List<List<Integer>> lists = new ArrayList<>();
            lists.add(vals);

            while (true) {
                boolean hasNonZero = false;
                List<Integer> newVals = new ArrayList<>();
                for (int i = 0; i < vals.size() - 1; i++) {
                    var x = vals.get(i + 1) - vals.get(i);
                    newVals.add(x);
                    if (x != 0) hasNonZero = true;
                }
                lists.add(newVals);
                vals = newVals;

                if (!hasNonZero) break;
            }

            int next = 0;

            for (int i = lists.size() - 2; i >= 0; i--) {
                var thisList = lists.get(i);
                next = thisList.get(thisList.size() - 1) + next;
            }

            total += next;
        }
        // print out the result
        System.out.printf("Part 1: %d\n", total);
    }

    void part2() {
        int total = 0;

        for (var line: processed) {
            var values = line.split(" ");
            var vals = Arrays.asList(values).stream().map(Integer::parseInt).collect(Collectors.toList());
            List<List<Integer>> lists = new ArrayList<>();
            lists.add(vals);

            while (true) {
                boolean hasNonZero = false;
                List<Integer> newVals = new ArrayList<>();
                for (int i = 0; i < vals.size() - 1; i++) {
                    var x = vals.get(i + 1) - vals.get(i);
                    newVals.add(x);
                    if (x != 0) hasNonZero = true;
                }
                lists.add(newVals);
                vals = newVals;

                if (!hasNonZero) break;
            }

            int next = 0;

            for (int i = lists.size() - 2; i >= 0; i--) {
                var thisList = lists.get(i);
                next = thisList.get(0) - next;
            }

            total += next;
        }
        // print out the result
        System.out.printf("Part 2: %d\n", total);
    }
}