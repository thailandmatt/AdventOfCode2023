package aoc;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

// Put the return type of the generator in Base<>
public class Year2023_Day01 extends Base<ArrayList<String>> {

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
            int first = -1;
            int last = -1;
            for (int i = 0; i < line.length(); i++) {
                if (Character.isDigit(line.charAt(i))) {
                    first =  Character.getNumericValue(line.charAt(i));
                    break;
                }
            }

            for (int i = line.length() - 1; i >= 0; i--) {
                if (Character.isDigit(line.charAt(i))) {
                    last =  Character.getNumericValue(line.charAt(i));
                    break;
                }
            }

            count += parseInt(String.valueOf(first) + String.valueOf(last));
        }

        // print out the result
        System.out.printf("Part 1: %d\n", count);
    }

    void part2() {

        // print out the result
        String[] validWords =
                {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};

        int count = 0;
        for (String line: this.processed) {

            int first = -1;
            int last = -1;
            for (int i = 0; i < line.length(); i++) {
                if (Character.isDigit(line.charAt(i))) {
                    first =  Character.getNumericValue(line.charAt(i));
                    break;
                } else {
                    boolean found = false;
                    for (int j = 0; j < validWords.length; j++) {
                        var word = validWords[j];
                        if (i + word.length() <= line.length()) {
                            if (line.substring(i, i + word.length()).equals(word)) {
                                first = j + 1;
                                found = true;
                                break;
                            }
                        }
                    }
                    if (found) break;
                }
            }

            for (int i = line.length() - 1; i >= 0; i--) {
                if (Character.isDigit(line.charAt(i))) {
                    last =  Character.getNumericValue(line.charAt(i));
                    break;
                } else {
                    boolean found = false;
                    for (int j = 0; j < validWords.length; j++) {
                        var word = validWords[j];
                        if (i - word.length() >= -1) {
                            if (line.substring(i - word.length() + 1, i + 1).equals(word)) {
                                last = j + 1;
                                found = true;
                                break;
                            }
                        }
                    }
                    if (found) break;
                }
            }

            var x = parseInt(String.valueOf(first) + String.valueOf(last));
            count += x;
        }

        // print out the result
        System.out.printf("Part 2: %d\n", count);
    }
}
