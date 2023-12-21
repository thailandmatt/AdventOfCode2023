package aoc;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Put the return type of the generator in Base<>
public class Year2023_Day21 extends Base<List<String>> {

    // The generator's role is to turn the input file into something usable by part 1 and part 2.
    void generator() {
        // this must have the same type as the return type of this method
        var lines = this.input.split("\n");
        processed = new ArrayList<>();
        processed.addAll(List.of(lines));
    }
    
    void part1() {
        var startString = processed.stream().filter(x -> x.contains("S")).findFirst().get();
        var start = new Point(processed.indexOf(startString), startString.indexOf("S"));

        List<Point> pointList = new ArrayList<>();
        pointList.add(start);

        for (var i = 0; i < 10; i++) {
            List<Point> newPointList = new ArrayList<>();
            for (var point: pointList) {
                var north = new Point(point.x - 1, point.y);
                var east = new Point(point.x, point.y + 1);
                var south = new Point(point.x + 1, point.y);
                var west = new Point(point.x, point.y - 1);

                var possibilities = List.of(north, east, south, west);
                for (var poss: possibilities) {
                    if (poss.x >= 0 && poss.x < processed.size() &&
                        poss.y >= 0 && poss.y < processed.get(0).length() &&
                            (
                                    processed.get(poss.x).charAt(poss.y) == '.' ||
                                    processed.get(poss.x).charAt(poss.y) == 'S'
                            )) {
                        if (!newPointList.contains(poss))
                            newPointList.add(poss);
                    }
                }
            }

            pointList = newPointList;
        }

        System.out.printf("Part 1: %d\n", pointList.size());
    }

    void part2() {
        var startString = processed.stream().filter(x -> x.contains("S")).findFirst().get();
        var start = new Point(processed.indexOf(startString), startString.indexOf("S"));

        //so we can figure out which ones are reachable by seeing if they are odd/even when you add them together
        //and by how far they are from the center
        //we start at 65,65 - adds up to even
        //so (4,5) (5,6) (6,5) (5,4) all add up to odd and are 1 away from start - so accessible on any odd
        //iteration after iteration 1
        //our target number is 26501365 - so we can get in a diamond all the way from
        //(65 - 26501365, 65) to (65 + 26501365, 65)
        //and
        //(65, 65 - 26501365) to (65, 65 + 26501365)
        //
        //now for the infinite portion, let's think about that
        //(-1,-1) is the same as (130, 130)
        //(0, 131) (0, 261) (0, 391) (0, 521) should be the same as (0, 0) - (x % 130 - 1, y % 130 - 1)
        //so (0, 500) translates to (0, 109)
        //
        //after much soul searching in excel, it turns out that every GRID SIZE + 1 repeats in a
        //2nd order polynomial.  So I ran this brute force until I could get 3 points that had the
        //same mod N+1 as the desired large number, and then plugged it into the equation to get the
        //answer
        //y = 0.890740632830724 * x^2 + 1.71515566816304 * x + 9.09375
        //x = 26501365, y = 625587097150084

        List<Point> pointList = new ArrayList<>();
        pointList.add(start);

        List<Point> results = new ArrayList<>();

        var size = processed.size();

        for (var i = 0; i < 500; i++) {
            List<Point> newPointList = new ArrayList<>();
            for (var point: pointList) {
                var north = new Point(point.x - 1, point.y);
                var east = new Point(point.x, point.y + 1);
                var south = new Point(point.x + 1, point.y);
                var west = new Point(point.x, point.y - 1);

                var possibilities = List.of(north, east, south, west);
                for (var poss: possibilities) {

                    var testX = poss.x % size;
                    var testY = poss.y % size;
                    if (testX < 0) testX += size;
                    if (testY < 0) testY += size;

                    if (processed.get(testX).charAt(testY) == '.' || processed.get(testX).charAt(testY) == 'S') {
                        if (!newPointList.contains(poss))
                            newPointList.add(poss);
                    }
                }
            }
            var result= new Point(i + 1, newPointList.size());
            System.out.println(result.x + "\t" + result.y);

            results.add(result);
            pointList = newPointList;
        }

        // print out the result
        System.out.printf("Part 2: %d\n", 0);
    }
}
