package aoc;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;

// Put the return type of the generator in Base<>
public class Year2023_Day12 extends Base<ArrayList<String>> {

    // The generator's role is to turn the input file into something usable by part 1 and part 2.
    void generator() {
        // this must have the same type as the return type of this method
        var lines = this.input.split("\n");
        this.processed = new ArrayList<>();
        // read it line by line
        // for this one we just want an arraylist of numbers
        this.processed.addAll(Arrays.asList(lines));
    }

    int getBit(int n, int k) {
        return (n >> k) & 1;
    }

    String getRepresentation(String s) {
        var parts = s.split("\\.");
        String answer = "";
        for (var part: parts) {
            if (part.length() > 0) {
                answer += (answer.length() > 0 ? "," : "") + part.length();
            }
        }
        return answer;
    }

    void part1() {
        List<Integer> answers = new ArrayList<>();

        for (var line : processed) {
            var parts = line.split(" ");

            List<Integer> questionMarkIndices = new ArrayList();
            for (int i = 0; i < parts[0].length(); i++) {
                if (parts[0].charAt(i) == '?') questionMarkIndices.add(i);
            }

            int possibilityCount = (int)Math.pow(2, questionMarkIndices.size());
            int possibilitiesThatFit = 0;
            for (var i = 0; i < possibilityCount; i++) {
                String test = parts[0];
                for (int j = 0; j < questionMarkIndices.size(); j++) {
                    var replacement = getBit(i, j) == 0 ? '.' : '#';
                    var idx = questionMarkIndices.get(j);
                    test = test.substring(0, idx) + replacement
                            + test.substring(idx + 1);
                }

                if (getRepresentation(test).equals(parts[1]))
                    possibilitiesThatFit++;
            }
            answers.add(possibilitiesThatFit);
        }

        var total = answers.stream().mapToInt(Integer::intValue).sum();

        // print out the result
        System.out.printf("Part 1: %d\n", total);
    }

    void part2() {
        List<Long> answers = new ArrayList<>();

        for (var line : processed) {
            HashMap<String, Long> dp = new HashMap<>();

            var parts = line.split(" ");

            var newPart1 = "";
            var newPart2 = "";

            for (int i = 0; i < 5; i++) {
                newPart1 += (newPart1.length() == 0 ? "" : "?") + parts[0];
                newPart2 += (newPart2.length() == 0 ? "" : ",") + parts[1];
            }

            var nums = Arrays.stream(newPart2.split(",")).mapToInt(x -> parseInt(x)).toArray();
            var score = doDp(dp, newPart1, nums, 0, 0, 0);
            answers.add(score);
        }

        var total = answers.stream().mapToLong(Long::longValue).sum();

        // print out the result
        System.out.printf("Part 2: %d\n", total);
    }

    long doDp(HashMap<String, Long> dp,
                              String pattern,
                              int[] numbers,
                              int patternPosition,
                              int numbersPosition,
                              int curLengthOfHashtags) {
        var key = patternPosition + "," + numbersPosition + "," + curLengthOfHashtags;
        if (dp.containsKey(key)) return dp.get(key);


        if (patternPosition == pattern.length()) {
            //if we're at the end of the pattern
            if (numbersPosition == numbers.length && curLengthOfHashtags == 0) {
                //and we got through all the numbers then this is a good possibility
                return 1;
            } else if (numbersPosition == numbers.length - 1 && numbers[numbersPosition] == curLengthOfHashtags) {
                //and we got through all the numbers and this last group is good
                //then its a good possibility
                return 1;
            } else {
                //otherwise not a good possibility
                return 0;
            }
        }

        var answer = 0L;

        //replace with a . and a # and see what happens
        //recursively fill out the DP
        var replacements = new char[] {'.', '#'};
        for (var replacement: replacements) {
            if (pattern.charAt(patternPosition) == replacement ||
                pattern.charAt(patternPosition) == '?') {
                if (replacement == '.' && curLengthOfHashtags == 0) {
                    answer += doDp(dp, pattern, numbers, patternPosition + 1, numbersPosition, 0);
                } else if (replacement == '.' && curLengthOfHashtags > 0 &&
                            numbersPosition < numbers.length &&
                            numbers[numbersPosition] == curLengthOfHashtags) {
                    answer += doDp(dp, pattern, numbers, patternPosition + 1, numbersPosition + 1, 0);
                } else if (replacement == '#') {
                    answer += doDp(dp, pattern, numbers, patternPosition + 1, numbersPosition, curLengthOfHashtags + 1);
                }
            }
        }

        //memoize / remember
        dp.put(key, answer);
        return answer;
    }
}
