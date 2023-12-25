package aoc;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

// Put the return type of the generator in Base<>
public class Year2023_Day25 extends Base<List<String>> {

    // The generator's role is to turn the input file into something usable by part 1 and part 2.
    void generator() {
        // this must have the same type as the return type of this method
        var lines = this.input.split("\n");
        processed = new ArrayList<>();
        for (var line: lines) {
            processed.add(line);
        }
    }

    class Node {
        public String name;
        public List<Node> otherNodes = new ArrayList<>();
    }

    void part1() {
        HashMap<String, List<String>> edges = new HashMap<>();
        HashSet<String> nodes = new HashSet<>();

        for (var line: processed) {
            var parts = line.split(":");
            var others = parts[1].strip().split(" ");
            var a = parts[0];
            if (!nodes.contains(a)) nodes.add(a);
            if (!edges.containsKey(a)) edges.put(a, new ArrayList<>());

            for (var b: others) {
                edges.get(a).add(b);

                if (!edges.containsKey(b)) edges.put(b, new ArrayList<>());
                edges.get(b).add(a);
                if (!nodes.contains(b)) nodes.add(b);
            }
        }

        //used a visualizer and removed these connections
        //bqp->fqr
        //cnr->hcd
        //fhv->zsp

        HashSet<String> partA = new HashSet<>();

        //start at bqp
        List<String> stack = new ArrayList();
        stack.add("bqp");
        while (!stack.isEmpty()) {
            var cur = stack.remove(0);
            if (!partA.contains(cur)) {
                partA.add(cur);
                stack.addAll(edges.get(cur));
            }
        }

        System.out.printf("Part 1: %d %d %d\n", nodes.size(), partA.size(), nodes.size() - partA.size());
    }

    void part2() {
        System.out.printf("Part 2: %d\n", 0);
    }
}