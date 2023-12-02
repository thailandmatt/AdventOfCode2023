package aoc;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Integer.parseInt;

// Put the return type of the generator in Base<>
public class Year2023_Day02 extends Base<ArrayList<String>> {

    // The generator's role is to turn the input file into something usable by part 1 and part 2.
    void generator() {
        // this must have the same type as the return type of this method
        this.processed = new ArrayList<>();
        // read it line by line
        for(String line: this.input.split("\n")){
            // for this one we just want an arraylist of numbers
            this.processed.add(line);
        }
    }

    void part1() {
        int count = 0;
        for (String line: this.processed) {
            //split on ; and:
            var parts = line.split("[;:]");
            int gameID = parseInt(parts[0].replace("Game ", "").replace(":", ""));
            boolean ok = true;
            for (int i = 1; i < parts.length; i++) {
                var runParts = parts[i].trim().split(",");
                for (var runPart: runParts) {
                    var colorParts = runPart.trim().split(" ");

                    int num = parseInt(colorParts[0]);
                    String color = colorParts[1];

                    int max = 0;
                    if (color.equals("red"))
                        max = 12;
                    else if (color.equals("green"))
                        max = 13;
                    else if (color.equals("blue"))
                        max = 14;

                    if (num > max) ok = false;
                }
            }

            if (ok) count += gameID;
        }

        // print out the result
        System.out.printf("Part 1: %d\n", count);
    }

    void part2() {
        // print out the result
        int count = 0;
        for (String line: this.processed) {
            //split on ; and:
            var parts = line.split("[;:]");
            int gameID = parseInt(parts[0].replace("Game ", "").replace(":", ""));
            int maxRed = 0;
            int maxGreen = 0;
            int maxBlue = 0;

            for (int i = 1; i < parts.length; i++) {
                var runParts = parts[i].trim().split(",");
                for (var runPart: runParts) {
                    var colorParts = runPart.trim().split(" ");

                    int num = parseInt(colorParts[0]);
                    String color = colorParts[1];

                    if (color.equals("red"))
                        maxRed = Math.max(maxRed, num);
                    else if (color.equals("green"))
                        maxGreen = Math.max(maxGreen, num);
                    else if (color.equals("blue"))
                        maxBlue = Math.max(maxBlue, num);
                }
            }

            count += (maxRed * maxGreen * maxBlue);
        }

        // print out the result
        System.out.printf("Part 2: %d\n", count);
    }
}