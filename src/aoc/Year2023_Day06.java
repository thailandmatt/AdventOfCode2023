package aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

// Put the return type of the generator in Base<>
public class Year2023_Day06 extends Base<ArrayList<String>> {

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

        String pattern = "\\d+";
        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);
        // Now create matcher object.
        Matcher time = r.matcher(processed.get(0));
        Matcher distance = r.matcher(processed.get(1));
        List<Integer> times = new ArrayList<>();
        while (time.find()) {
            times.add(parseInt(time.group()));
        }
        List<Integer> distances = new ArrayList<>();
        while (distance.find()) {
            distances.add(parseInt(distance.group()));
        }
        List<Integer> winCounts = new ArrayList<>();

        for (int i = 0; i < times.size(); i++) {
            int winCount = 0;
            int maxTime = times.get(i);
            int targetDistance = distances.get(i);

            for (int j = 0; j < maxTime; j++) {
                if ((j * (maxTime - j)) > targetDistance) winCount++;
            }
            winCounts.add(winCount);
        }

        // print out the result
        System.out.printf("Part 1: %d\n", winCounts.stream().reduce(1, (a, b) -> a * b));
    }

    void part2() {
        Long time = parseLong(processed.get(0).replace(" ", "").replace("Time:", ""));
        Long distance = parseLong(processed.get(1).replace(" ", "").replace("Distance:", ""));
        var lowerBound = Math.ceilDiv(distance, time);
        for (int i = 0; i < time; i++) {
            if (i * (time - i) > distance) {
                lowerBound = i;
                break;
            }
        }
        var upperBound = time - lowerBound;
        var total = upperBound - lowerBound + 1;
        // print out the result
        System.out.printf("Part 2: " + total + "\n");
    }
}