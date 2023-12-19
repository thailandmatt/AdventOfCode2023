package aoc;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

// Put the return type of the generator in Base<>
public class Year2023_Day17 extends Base<int[][]> {

    // The generator's role is to turn the input file into something usable by part 1 and part 2.
    void generator() {
        // this must have the same type as the return type of this method
        var lines = this.input.split("\n");
        this.processed = new int[lines.length][lines[0].length()];
        for (int i = 0; i < processed.length; i++) {
            for (int j = 0; j < processed[0].length; j++) {
                processed[i][j] = Character.getNumericValue(lines[i].charAt(i));
            }
        }
    }

    void part1() {
        // print out the result
        System.out.printf("Part 1: %d\n", 0);
    }

    void part2() {


        // print out the result
        System.out.printf("Part 2: %d\n", 0);
    }
}
