package aoc;

import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.Integer.*;

// Put the return type of the generator in Base<>
public class Year2023_Day23 extends Base<List<String>> {
    // The generator's role is to turn the input file into something usable by part 1 and part 2.
    void generator() {
        // this must have the same type as the return type of this method
        var lines = this.input.split("\n");
        processed = new ArrayList<>();
        Collections.addAll(processed, lines);
    }
    
    void part1() {
        Point start = new Point(processed.size() - 1, processed.get(processed.size() - 1).indexOf("."));
        Point end = new Point(0, processed.get(0).indexOf("."));

        List<List<Point>> paths = new ArrayList<>();
        paths.add(new ArrayList<>());
        paths.get(0).add(start);

        List<List<Point>> completePaths = new ArrayList<>();

        while (!paths.isEmpty()) {
            for (var i = 0; i < paths.size(); i++) {
                var path = paths.get(i);
                var cur = path.get(path.size() - 1);

                if (cur.x == end.x && cur.y == end.y) {
                    completePaths.add(path);
                    paths.remove(path);
                    i--;
                    continue;
                }

                var no = new Point(cur.x - 1, cur.y);
                var ea = new Point(cur.x, cur.y + 1);
                var so = new Point(cur.x + 1, cur.y);
                var we = new Point(cur.x, cur.y - 1);

                List<Point> validSteps = new ArrayList<>();

                if (no.x >= 0 && no.x < processed.size() && no.y >= 0 && no.y < processed.get(0).length() && !path.contains(no) &&
                    (processed.get(no.x).charAt(no.y) == '.' || processed.get(no.x).charAt(no.y) == 'v')) {
                    validSteps.add(no);
                }

                if (so.x >= 0 && so.x < processed.size() && so.y >= 0 && so.y < processed.get(0).length() && !path.contains(so) &&
                        (processed.get(so.x).charAt(so.y) == '.' || processed.get(so.x).charAt(so.y) == '^')) {
                    validSteps.add(so);
                }

                if (ea.x >= 0 && ea.x < processed.size() && ea.y >= 0 && ea.y < processed.get(0).length() && !path.contains(ea) &&
                        (processed.get(ea.x).charAt(ea.y) == '.' || processed.get(ea.x).charAt(ea.y) == '<')) {
                    validSteps.add(ea);
                }

                if (we.x >= 0 && we.x < processed.size() && we.y >= 0 && we.y < processed.get(0).length() && !path.contains(we) &&
                        (processed.get(we.x).charAt(we.y) == '.' || processed.get(we.x).charAt(we.y) == '>')) {
                    validSteps.add(we);
                }

                if (validSteps.size() == 0) {
                    //dead end
                    paths.remove(i);
                    i--;
                } else if (validSteps.size() == 1) {
                    //one way forward
                    path.add(validSteps.get(0));
                } else {
                    //split
                    for (var j = 1; j < validSteps.size(); j++) {
                        List<Point> clone = new ArrayList<>();
                        clone.addAll(path);
                        clone.add(validSteps.get(j));
                        paths.add(clone);
                    }

                    path.add(validSteps.get(0));
                }
            }
        }

        int max = 0;
        for (var path: completePaths) {
            max = Math.max(path.size(), max);
        }

        //-1 start doesn't count
        System.out.printf("Part 1: %d\n", max - 1);
    }

    public class Node {

        public Node(Point p) {
            this.p = p;
        }
        public  Point p;
        public List<Edge> edges = new ArrayList<>();
    }

    public class Edge {

        public Edge (Node d, int w) {
            this.destination = d;
            this.weight = w;
        }
        private Node destination;
        private int weight;
    }

    public List<Point> getValidNextSteps(Point cur, List<Point> path) {
        List<Point> validSteps = new ArrayList<>();

        var no = new Point(cur.x - 1, cur.y);
        var ea = new Point(cur.x, cur.y + 1);
        var so = new Point(cur.x + 1, cur.y);
        var we = new Point(cur.x, cur.y - 1);

        if (no.x >= 0 && no.x < processed.size() && no.y >= 0 && no.y < processed.get(0).length() && !path.contains(no) &&
                (processed.get(no.x).charAt(no.y) != '#')) {
            validSteps.add(no);
        }

        if (so.x >= 0 && so.x < processed.size() && so.y >= 0 && so.y < processed.get(0).length() && !path.contains(so) &&
                (processed.get(so.x).charAt(so.y) != '#')) {
            validSteps.add(so);
        }

        if (ea.x >= 0 && ea.x < processed.size() && ea.y >= 0 && ea.y < processed.get(0).length() && !path.contains(ea) &&
                (processed.get(ea.x).charAt(ea.y) != '#')) {
            validSteps.add(ea);
        }

        if (we.x >= 0 && we.x < processed.size() && we.y >= 0 && we.y < processed.get(0).length() && !path.contains(we) &&
                (processed.get(we.x).charAt(we.y) != '#')) {
            validSteps.add(we);
        }

        return validSteps;
    }
    public List<Point> findPathToNextDecisionPoint(List<Point> path, Point end) {

        while (true) {
            var cur = path.get(path.size() - 1);

            if (cur.x == end.x && cur.y == end.y) {
                return path;
            }

            var validSteps = getValidNextSteps(cur, path);

            if (validSteps.size() == 0) {
                return null;
            } else if (validSteps.size() == 1) {
                //one way forward
                path.add(validSteps.get(0));
            } else {
                return path;
            }
        }
    }

    void part2() {
        Point start = new Point(0, processed.get(0).indexOf("."));
        Point end = new Point(processed.size() - 1, processed.get(processed.size() - 1).indexOf("."));

        Map<Point, Node> nodes = new HashMap();
        nodes.put(start, new Node(start));
        nodes.put(end, new Node(end));

        //find all the decision points
        for (var x = 0; x < processed.size(); x++) {
            for (var y = 0; y < processed.get(0).length(); y++) {
                if (processed.get(x).charAt(y) != '#') {
                    var p = new Point(x, y);
                    var nextSteps = getValidNextSteps(p, new ArrayList<>());
                    if (nextSteps.size() > 2) nodes.put(p, new Node(p));
                }
            }
        }

        List<Point> stack = new ArrayList<>();
        stack.add(start);

        for (int i = 0; i < stack.size(); i++) {
            var cur = stack.get(i);

            //find the path to the next decision point - for each possible path from this
            //point
            var nextSteps = getValidNextSteps(cur, new ArrayList<>());
            for (var next: nextSteps) {
                List<Point> startPath = new ArrayList();
                startPath.add(cur);
                startPath.add(next);
                var path = findPathToNextDecisionPoint(startPath, end);
                if (path != null) {
                    var target = path.get(path.size() - 1);

                    var curNode = nodes.get(cur);
                    var targetNode = nodes.get(target);
                    curNode.edges.add(new Edge(targetNode, path.size() - 1));

                    if (!stack.contains(target))
                        stack.add(target);
                }
            }
        }

        //visualize graph
        for (var node: nodes.values()) {
            for (var edge: node.edges) {
                System.out.println("\""+ node.p.x + "," + node.p.y + "\"" + " -> " + "\""+ edge.destination.p.x + "," + edge.destination.p.y + "\"" + " [weight=" + edge.weight + "]");
            }
        }

        var total = findLongestPath(nodes, new ArrayList<>(), nodes.get(start), end);

        System.out.printf("Part 2: %d\n", total);
    }

    int findLongestPath(Map<Point, Node> nodes, List<Node> visited, Node curNode, Point end) {
        int maxSoFar = 0;

        if (curNode.p == end) return 0;

        visited.add(curNode);

        for (var edge: curNode.edges) {
            if (!visited.contains(edge.destination)) {
                int test = findLongestPath(nodes, visited, edge.destination, end);
                if (test + edge.weight > maxSoFar) maxSoFar = test + edge.weight;
            }
        }

        visited.remove(curNode);

        return maxSoFar;
    }
}
