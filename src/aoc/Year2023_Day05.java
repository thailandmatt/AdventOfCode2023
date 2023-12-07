package aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

// Put the return type of the generator in Base<>
public class Year2023_Day05 extends Base<ArrayList<String>> {

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

    class LongRangeMutation {
        Long sourceStart;
        Long destinationStart;
        Long length;
        Long sourceEnd;
        Long destinationEnd;
    }

    class LongRange {
        Long start;
        Long length;
        Long end;

        public LongRange() {

        }

        public LongRange(Long start, Long end) {
            this.start = start;
            this.length = end - start;
            this.end = end;
        }
    }

    void part1() {
        List<Long> targetSeeds;
        var seedsString =
                processed.get(0).replace("seeds: ", "").split(" ");
        targetSeeds = new ArrayList<>(Arrays.stream(seedsString).map(x -> parseLong(x)).collect(Collectors.toList()));

        List<List<LongRangeMutation>> maps = new ArrayList<>();
        List<LongRangeMutation> current = new ArrayList<>();
        for (int i = 1; i < processed.size(); i++) {
            if (processed.get(i).trim().equals("")) continue;
            if (Character.isAlphabetic(processed.get(i).charAt(0))) {
                var parts = processed.get(i).split(" ")[0].split("-");
                current = new ArrayList<>();
                maps.add(current);
            } else {
                LongRangeMutation range = new LongRangeMutation();
                var parts = processed.get(i).split(" ");
                range.destinationStart = parseLong(parts[0]);
                range.sourceStart = parseLong(parts[1]);
                range.length = parseLong(parts[2]);
                range.sourceEnd = range.sourceStart + range.length;
                range.destinationEnd = range.destinationStart + range.length;

                current.add(range);
            }
        }

        //now find the targets
        List<Long> locations = new ArrayList<>();

        for (var target: targetSeeds) {
            var curTarget = target;
            for (int i = 0; i < maps.size(); i++) {
                //find in this list
                for (var oneRange : maps.get(i)) {
                    if (curTarget >= oneRange.sourceStart && curTarget <= oneRange.sourceStart + oneRange.length) {
                        curTarget = oneRange.destinationStart + (curTarget - oneRange.sourceStart);
                        break;
                    }
                }
            }
            locations.add(curTarget);
        }

        var answer = locations.stream().mapToLong(x -> x).min().getAsLong();

        // print out the result
        System.out.printf("Part 1: %d\n", answer);
    }

    void part2() {
        List<Long> targetSeeds;
        var seedsString =
                processed.get(0).replace("seeds: ", "").split(" ");
        targetSeeds = new ArrayList<>(Arrays.stream(seedsString).map(x -> parseLong(x)).collect(Collectors.toList()));
        List<LongRange> targetRanges = new ArrayList<>();
        for (int i = 0; i < targetSeeds.size(); i=i+2) {
            var l = new LongRange();
            l.start = targetSeeds.get(i);
            l.length = targetSeeds.get(i + 1);
            l.end = l.start + l.length - 1;
            targetRanges.add(l);
        }

        List<List<LongRangeMutation>> maps = new ArrayList<>();
        List<LongRangeMutation> current = new ArrayList<>();
        for (int i = 1; i < processed.size(); i++) {
            if (processed.get(i).trim().equals("")) continue;
            if (Character.isAlphabetic(processed.get(i).charAt(0))) {
                var parts = processed.get(i).split(" ")[0].split("-");
                current = new ArrayList<>();
                maps.add(current);
            } else {
                LongRangeMutation range = new LongRangeMutation();
                var parts = processed.get(i).split(" ");
                range.destinationStart = parseLong(parts[0]);
                range.sourceStart = parseLong(parts[1]);
                range.length = parseLong(parts[2]);
                range.sourceEnd = range.sourceStart + range.length - 1;
                range.destinationEnd = range.destinationStart + range.length - 1;

                current.add(range);
            }
        }

        //split the ranges all the way through
        for (int i = 1; i < maps.size(); i++) {
            //split
            List<LongRange> newTargetRanges = new ArrayList<>();
            for (var t: targetRanges) {
                if (t.start > t.end) continue;

                //find all overlapping ones and deal with them
                var overlapping = maps.get(i).stream().filter(x -> {
                    return
                    (x.destinationStart <= t.start && x.destinationEnd >= t.start && x.destinationEnd <= t.end) ||
                    (t.start <= x.destinationStart && t.end >= x.destinationStart && t.end <= x.destinationEnd) ||
                    (t.start <= x.destinationStart && t.end >= x.destinationEnd) ||
                    (x.destinationStart <= t.start && x.destinationEnd >= t.end);
                }).toList();

                if (overlapping.size() == 0) {
                    newTargetRanges.add(t);
                } else {
                    for (var x: overlapping) {
                        if (x.destinationStart <= t.start && x.destinationEnd >= t.start && x.destinationEnd <= t.end) {
                            newTargetRanges.add(new LongRange(x.destinationStart, t.start - 1));
                            newTargetRanges.add(new LongRange(t.start, x.destinationEnd));
                            newTargetRanges.add(new LongRange(x.destinationEnd + 1, t.end));

                        } else if (t.start <= x.destinationStart && t.end >= x.destinationStart && t.end <= x.destinationEnd) {
                            newTargetRanges.add(new LongRange(t.start, x.destinationStart - 1));
                            newTargetRanges.add(new LongRange(x.destinationStart, t.end));
                            newTargetRanges.add(new LongRange(t.end + 1, x.destinationEnd));

                        } else if (t.start <= x.destinationStart && t.end >= x.destinationEnd) {
                            newTargetRanges.add(new LongRange(t.start, x.destinationStart - 1));
                            newTargetRanges.add(new LongRange(x.destinationStart, x.destinationEnd));
                            newTargetRanges.add(new LongRange(x.destinationEnd + 1, t.end));

                        } else if (x.destinationStart <= t.start && x.destinationEnd >= t.end) {
                            newTargetRanges.add(new LongRange(x.destinationStart, t.start - 1));
                            newTargetRanges.add(new LongRange(t.start, t.end));
                            newTargetRanges.add(new LongRange(t.end + 1, x.destinationEnd));
                        }
                    }
                }
            }

            //mutate
            for (var t: newTargetRanges) {
                //maps.get(i).stream().findFirst(x -> x.
            }
        }

        // print out the result
        System.out.printf("Part 2: %d\n", 0);
    }
}