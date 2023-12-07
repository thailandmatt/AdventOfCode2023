package aoc;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

// Put the return type of the generator in Base<>
public class Year2023_Day07 extends Base<ArrayList<String>> {

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

    int convert(char c) {
        if (Character.isDigit(c)) return parseInt(c + "");
        if (c == 'T') return 10;
        if (c == 'J') return 11;
        if (c == 'Q') return 12;
        if (c == 'K') return 13;
        if (c == 'A') return 14;
        return 0;
    }

    int convertPart2(char c) {
        if (Character.isDigit(c)) return parseInt(c + "");
        if (c == 'T') return 10;
        if (c == 'J') return 0;
        if (c == 'Q') return 12;
        if (c == 'K') return 13;
        if (c == 'A') return 14;
        return 0;
    }

    class handBid {
        int[] hand;
        int bid;

        public handBid() {

        }

        public handBid(int[] hand, int bid) {
            this.hand = hand;
            this.bid = bid;
        }

        public int score() {
            HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
            for (int i = 0; i < 5; i++) {
                counts.put(hand[i], counts.getOrDefault(hand[i], 0) + 1);
            }

            int score = 0;

            boolean hasJoker = Arrays.stream(hand).anyMatch(x -> x == 0);
            int jokerCount = counts.getOrDefault(0, 0);

            if (counts.size() == 1) {
                // 5 of a kind
                score = 700;
            } else if (counts.size() == 2) {
                var max = counts.values().stream().max(Integer::compare).get();
                if (max == 4) {
                    //4 of a kind
                    score = hasJoker ? 700 : 600;
                } else {
                    //full house
                    score = hasJoker ? (jokerCount >= 2 ? 700 : 600): 500;
                }

            } else if (counts.size() == 3) {
                var max = counts.values().stream().max(Integer::compare).get();
                if (max == 3) {
                    //3 of a kind
                    score = hasJoker ? 600 : 400;
                } else {
                    //2 pair
                    score = hasJoker ? (jokerCount == 2 ? 600 : 500): 300;
                }

            } else if (counts.size() == 4) {
                //1 pair
                score = hasJoker ? 400: 200;
            } else if (counts.size() == 5) {
                //high card
                score = hasJoker ? 200 : 100;
            }

            return score;
        }
    }

    public class handBidComparator implements Comparator<handBid> {
        public int compare(handBid o1, handBid o2) {
            var s1 = o1.score();
            var s2 = o2.score();
            if (s1 == s2) {
                for (int i = 0; i < 5; i++) {
                    if (o1.hand[i] < o2.hand[i]) return -1;
                    if (o1.hand[i] > o2.hand[i]) return 1;
                }
            } else {
                return s1 > s2 ? 1 : -1;
            }
            return 0;
        }
    }

    void part1() {
        List<handBid> list = new ArrayList<>();

        for (var line: processed) {
            var parts = line.split(" ");
            int[] hand = new int[5];
            for (int i = 0; i < 5; i++)
                hand[i] = convert(parts[0].charAt(i));
            int bid = parseInt(parts[1]);
            list.add(new handBid(hand, bid));
        }

        Collections.sort(list, new handBidComparator());

        Long total = 0L;
        for (int i = 0; i < list.size(); i++) {
            total += ((i + 1) * list.get(i).bid);
        }

        // print out the result
        System.out.printf("Part 1: %d\n", total);
    }

    void part2() {
        List<handBid> list = new ArrayList<>();

        for (var line: processed) {
            var parts = line.split(" ");
            int[] hand = new int[5];
            for (int i = 0; i < 5; i++)
                hand[i] = convertPart2(parts[0].charAt(i));
            int bid = parseInt(parts[1]);
            var x = new handBid(hand, bid);
            list.add(x);
        }

        Collections.sort(list, new handBidComparator());

        Long total = 0L;
        for (int i = 0; i < list.size(); i++) {
            total += ((i + 1) * list.get(i).bid);
        }

        // print out the result
        System.out.printf("Part 2: %d\n", total);
    }
}