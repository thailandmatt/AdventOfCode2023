package aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;

// Put the return type of the generator in Base<>
public class Year2023_Day04 extends Base<ArrayList<String>> {

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
        int count = 0;

        for (int i = 0; i < processed.size(); i++) {
            var parts = processed.get(i).split(" ");
            List<Integer> winners = new ArrayList();
            List<Integer> mine = new ArrayList();
            boolean onWinners = true;
            for (int j = 0; j < parts.length; j++) {
                if (parts[j].trim().equals(""))
                    continue;
                else if (parts[j].contains(":"))
                    continue;
                else if (parts[j].contains("Card"))
                    continue;
                else if (parts[j].equals("|"))
                    onWinners = false;
                else if (onWinners)
                    winners.add(parseInt(parts[j]));
                else
                    mine.add(parseInt(parts[j]));
            }

            var answer =
                    mine.stream().filter(winners::contains).count();

            count += Math.pow(2, answer - 1);
        }

        // print out the result
        System.out.printf("Part 1: %d\n", count);
    }

    void part2() {
        int count = 0;

        HashMap<Integer, Integer> copies = new HashMap<Integer, Integer>();
        for (int i = 0; i < processed.size(); i++)
            copies.put(i, 1);

        for (int i = 0; i < processed.size(); i++) {
            var parts = processed.get(i).split(" ");
            List<Integer> winners = new ArrayList();
            List<Integer> mine = new ArrayList();
            boolean onWinners = true;
            for (int j = 0; j < parts.length; j++) {
                if (parts[j].trim().equals(""))
                    continue;
                else if (parts[j].contains(":"))
                    continue;
                else if (parts[j].contains("Card"))
                    continue;
                else if (parts[j].equals("|"))
                    onWinners = false;
                else if (onWinners)
                    winners.add(parseInt(parts[j]));
                else
                    mine.add(parseInt(parts[j]));
            }

            var answer =
                    mine.stream().filter(winners::contains).count();

            if (copies.containsKey(i) && copies.get(i) > 0) {
                for (int j = 0; j < answer; j++) {
                    if (!copies.containsKey(i + j + 1))
                        copies.put(i + j + 1, 0);

                    copies.put(i + j + 1, copies.get(i + j + 1) + copies.get(i));
                }
            }
        }

        for (var x: copies.values()) {
            count += x;
        }

        // print out the result
        System.out.printf("Part 2: %d\n", count);
    }
}