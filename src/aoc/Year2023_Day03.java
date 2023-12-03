package aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;

// Put the return type of the generator in Base<>
public class Year2023_Day03 extends Base<char[][]> {

    // The generator's role is to turn the input file into something usable by part 1 and part 2.
    void generator() {
        // this must have the same type as the return type of this method
        var lines = this.input.split("\n");
        this.processed = new char [lines.length][lines[0].length()];
        // read it line by line
        for (int i = 0; i < lines.length; i++) {
            // for this one we just want an arraylist of numbers
            this.processed[i] = lines[i].toCharArray();
        }
    }

    void part1() {
        int count = 0;
        for (int row = 0; row < processed.length; row++) {
            for (int col = 0; col < processed[0].length; col++) {
                if (Character.isDigit(this.processed[row][col])) {
                    //start a new part number - add as far right as we can go
                    String partNumberStr = "";

                    //check for touching a symbol
                    boolean symbol = false;

                    while (col < processed[row].length) {
                        if (!Character.isDigit(processed[row][col])) break;
                        partNumberStr += String.valueOf(processed[row][col]);
                        if (checkTouchingSymbol(row, col)) {
                            symbol = true;
                        }
                        col++;
                    }

                    if (symbol)
                        count += parseInt(partNumberStr);
                }
            }
        }

        // print out the result
        System.out.printf("Part 1: %d\n", count);
    }

    boolean checkTouchingSymbol(int row, int col) {
        if (row > 0 && col > 0) {
            if (!Character.isDigit(this.processed[row - 1][col - 1]) &&
                    this.processed[row - 1][col - 1] != '.')
                return true;
        }

        if (row > 0) {
            if (!Character.isDigit(this.processed[row - 1][col]) &&
                    this.processed[row - 1][col] != '.')
                return true;
        }

        if (row > 0 && col < processed[0].length - 1) {
            if (!Character.isDigit(this.processed[row - 1][col + 1]) &&
                    this.processed[row - 1][col + 1] != '.')
                return true;
        }

        if (col > 0) {
            if (!Character.isDigit(this.processed[row][col - 1]) &&
                    this.processed[row][col - 1] != '.')
                return true;
        }

        if (col < processed[0].length - 1) {
            if (!Character.isDigit(this.processed[row][col + 1]) &&
                    this.processed[row][col + 1] != '.')
                return true;
        }

        if (row < processed.length - 1 && col > 0) {
            if (!Character.isDigit(this.processed[row + 1][col - 1]) &&
                    this.processed[row + 1][col - 1] != '.')
                return true;
        }

        if (row < processed.length - 1) {
            if (!Character.isDigit(this.processed[row + 1][col]) &&
                    this.processed[row + 1][col] != '.')
                return true;
        }

        if (row < processed.length - 1 && col < processed[0].length - 1) {
            if (!Character.isDigit(this.processed[row + 1][col + 1]) &&
                    this.processed[row + 1][col + 1] != '.')
                return true;
        }

        return false;
    }

    List<String> getTouchingStars(int row, int col) {
        List<String> result = new ArrayList<>();

        if (row > 0 && col > 0) {
            if (this.processed[row - 1][col - 1] == '*')
                result.add((row - 1) + "," + (col - 1));
        }

        if (row > 0) {
            if (this.processed[row - 1][col] == '*')
                result.add((row - 1) + "," + (col));
        }

        if (row > 0 && col < processed[0].length - 1) {
            if (this.processed[row - 1][col + 1] == '*')
                result.add((row - 1) + "," + (col + 1));
        }

        if (col > 0) {
            if (this.processed[row ][col - 1] == '*')
                result.add((row) + "," + (col - 1));
        }

        if (col < processed[0].length - 1) {
            if (this.processed[row ][col + 1] == '*')
                result.add((row) + "," + (col + 1));
        }

        if (row < processed.length - 1 && col > 0) {
            if (this.processed[row + 1][col - 1] == '*')
                result.add((row + 1) + "," + (col - 1));
        }

        if (row < processed.length - 1) {
            if (this.processed[row + 1][col] == '*')
                result.add((row + 1) + "," + (col));
        }

        if (row < processed.length - 1 && col < processed[0].length - 1) {
            if (this.processed[row + 1][col + 1] == '*')
                result.add((row + 1) + "," + (col + 1));
        }

        return result;
    }

    void part2() {
        // print out the result
        int count = 0;
        HashMap<String, List<Integer>> starsMap = new HashMap<>();

        for (int row = 0; row < processed.length; row++) {
            for (int col = 0; col < processed[0].length; col++) {
                if (Character.isDigit(this.processed[row][col])) {
                    //start a new part number - add as far right as we can go
                    String partNumberStr = "";

                    //check for touching a star
                    List<String> stars = new ArrayList<>();

                    while (col < processed[row].length) {
                        if (!Character.isDigit(processed[row][col])) break;
                        partNumberStr += String.valueOf(processed[row][col]);

                        var starsForThisOne = getTouchingStars(row, col);
                        stars.addAll(starsForThisOne);
                        col++;
                    }

                    if (stars.size() > 0) {
                        String finalPartNumberStr = partNumberStr;
                        stars.stream().distinct().forEach(starLocation -> {
                            if (!starsMap.containsKey(starLocation))
                                starsMap.put(starLocation, new ArrayList());

                            starsMap.get(starLocation).add(parseInt(finalPartNumberStr));
                            }
                        );
                    }
                }
            }
        }

        for (var entry : starsMap.entrySet()) {
            String key = entry.getKey();
            List<Integer> value = entry.getValue();
            if (value.size() == 2) {
                count += (value.get(0) * value.get(1));
            }
        }

        // print out the result
        System.out.printf("Part 1: %d\n", count);
    }
}