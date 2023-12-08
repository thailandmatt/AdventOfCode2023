package aoc;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

// Put the return type of the generator in Base<>
public class Year2023_Day08 extends Base<ArrayList<String>> {

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
        HashMap<String, String[]> map = new HashMap<>();
        String LRLR = this.processed.get(0);

        String pattern = "(...) = \\((...), (...)\\)";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        for (int i = 2; i < processed.size(); i++) {
            // Now create matcher object.
            Matcher m = r.matcher(processed.get(i));
            m.find();
            map.put(m.group(1), new String[]{m.group(2), m.group(3)});
        }

        int curIndex = -1;
        int steps = 0;
        String curLocation = "AAA";
        while (!curLocation.equals("ZZZ")) {
            curIndex++;
            steps++;
            if (curIndex == LRLR.length()) curIndex = 0;
            String LR = LRLR.charAt(curIndex) + "";
            if (LR.equals("L")) {
                curLocation = map.get(curLocation)[0];
            } else {
                curLocation = map.get(curLocation)[1];
            }
        }

        // print out the result
        System.out.printf("Part 1: %d\n", steps);
    }

    void part2() {
        HashMap<String, String[]> map = new HashMap<>();
        String LRLR = this.processed.get(0);

        String pattern = "(...) = \\((...), (...)\\)";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        for (int i = 2; i < processed.size(); i++) {
            // Now create matcher object.
            Matcher m = r.matcher(processed.get(i));
            m.find();
            map.put(m.group(1), new String[]{m.group(2), m.group(3)});
        }

        List<String> currentSteps = new ArrayList(
                map.keySet().stream().filter(a -> a.charAt(2) == 'A').toList());

        int curIndex = -1;
        List<Integer> stepsList = new ArrayList();

        for (int i = 0; i < currentSteps.size(); i++) {
            int steps = 0;

            while (true) {
                curIndex++;
                steps++;
                if (curIndex == LRLR.length()) curIndex = 0;
                String LR = LRLR.charAt(curIndex) + "";


                if (LR.equals("L")) {
                    currentSteps.set(i, map.get(currentSteps.get(i))[0]);
                } else {
                    currentSteps.set(i, map.get(currentSteps.get(i))[1]);
                }

                var test =
                        currentSteps.get(i).charAt(2) != 'Z';

                if (!test) break;
            }

            stepsList.add(steps);
        }

        var stepsArr = stepsList.stream().map(a -> (long)a).toList().toArray(new Long[0]);
        //LCM
        long answer = lcm(stepsArr);
        // print out the result
        System.out.printf("Part 2: %d\n", answer);
    }

    private static long gcd(long a, long b)
    {
        while (b > 0)
        {
            long temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }

    private static long gcd(long[] input)
    {
        long result = input[0];
        for(int i = 1; i < input.length; i++) result = gcd(result, input[i]);
        return result;
    }

    private static long lcm(long a, long b)
    {
        return a * (b / gcd(a, b));
    }

    private static long lcm(Long[] input)
    {
        long result = input[0];
        for(int i = 1; i < input.length; i++) result = lcm(result, input[i]);
        return result;
    }
}