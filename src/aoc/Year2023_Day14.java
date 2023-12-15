package aoc;

import java.awt.*;
import java.util.*;
import java.util.List;

// Put the return type of the generator in Base<>
public class Year2023_Day14 extends Base<ArrayList<String>> {

    // The generator's role is to turn the input file into something usable by part 1 and part 2.
    void generator() {
        // this must have the same type as the return type of this method
        var lines = this.input.split("\n");
        this.processed = new ArrayList<>();
        processed.addAll(Arrays.asList(lines));
    }

    void part1() {

        List<String> copy = new ArrayList<>();
        copy.addAll(processed);

        //tilt north
        MoveNorth(copy);

        for (var line: copy) {
            System.out.println(line);
        }

        var total = 0;
        for (int row = 0; row < copy.size(); row++) {
            //count 0s
            total += (copy.get(row).chars().filter(x -> x == 'O').count() * (copy.size() - row));
        }

        // print out the result
        System.out.printf("Part 1: %d\n", total);
    }

    void MoveNorth(List<String> x) {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int row = 0; row < x.size() - 1; row++) {
                String cur = x.get(row);
                String next = x.get(row + 1);
                for (int col = 0; col < cur.length(); col++) {
                    if (cur.charAt(col) == '.' && next.charAt(col) == 'O') {
                        changed = true;
                        cur = cur.substring(0, col) + "O" + cur.substring(col + 1);
                        next = next.substring(0, col) + "." + next.substring(col + 1);
                        x.set(row, cur);
                        x.set(row + 1, next);
                    }
                }
            }
        }
    }

    void MoveSouth(List<String> x) {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int row = x.size() - 1; row > 0; row--) {
                String cur = x.get(row);
                String next = x.get(row - 1);
                for (int col = 0; col < cur.length(); col++) {
                    if (cur.charAt(col) == '.' && next.charAt(col) == 'O') {
                        changed = true;
                        cur = cur.substring(0, col) + "O" + cur.substring(col + 1);
                        next = next.substring(0, col) + "." + next.substring(col + 1);
                        x.set(row, cur);
                        x.set(row - 1, next);
                    }
                }
            }
        }
    }

    void MoveEast(List<String> x) {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int col = x.get(0).length() - 1; col > 0; col--) {
                for (int row = 0; row < x.size(); row++) {
                    String cur = x.get(row);
                    if (cur.charAt(col) == '.' && cur.charAt(col - 1) == 'O') {
                        changed = true;
                        cur = cur.substring(0, col - 1) + ".O" + cur.substring(col + 1);
                        x.set(row, cur);
                    }
                }
            }
        }
    }

    void MoveWest(List<String> x) {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int col = 0; col < x.get(0).length() - 1; col++) {
                for (int row = 0; row < x.size(); row++) {
                    String cur = x.get(row);
                    if (cur.charAt(col) == '.' && cur.charAt(col + 1) == 'O') {
                        changed = true;
                        cur = cur.substring(0, col) + "O." + cur.substring(col + 2);
                        x.set(row, cur);
                    }
                }
            }
        }
    }

    void part2() {
        List<String> copy = new ArrayList<>();
        copy.addAll(processed);

        String original = String.join("~",processed);
        HashMap<String, Integer> cycleHash = new HashMap<>();
        List<String> cycleMemory = new ArrayList<>();

        cycleHash.put(original, -1);

        int offset = -1;
        int lastCycle = -1;
        int cycleLength = -1;

        for (int cycle = 0; cycle < 1000000000; cycle++) {
            if (cycle % 100000 == 0) System.out.println(cycle);
            //tilt north
            MoveNorth(copy);
            //tilt west
            MoveWest(copy);
            //tilt south
            MoveSouth(copy);
            //tilt east
            MoveEast(copy);

            //check to see if we match the original
            String test = String.join("~",copy);
            if (cycleHash.containsKey(test)) {
                cycleMemory.add(test);
                offset = cycleHash.get(test);
                lastCycle = cycle;
                cycleLength = cycle - offset;
                break;
            }
            cycleHash.put(test, cycle);
            cycleMemory.add(test);
        }

        //calculate where it would be at on 1000000000
        var remaining = 1000000000 - 1 - lastCycle;
        var remainingMod = remaining % cycleLength;

        var ending = (1000000000 - offset) % (cycleLength);
        var last = Arrays.stream(cycleMemory.get(offset + remainingMod).split("~")).toList();
        for (var line: last) {
            System.out.println(line);
        }

        var total = 0;
        for (int row = 0; row < last.size(); row++) {
            //count 0s
            total += (last.get(row).chars().filter(x -> x == 'O').count() * (last.size() - row));
        }

        // print out the result
        System.out.printf("Part 2: %d\n", total);
    }
}
