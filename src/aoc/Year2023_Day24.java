package aoc;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

// Put the return type of the generator in Base<>
public class Year2023_Day24 extends Base<List<Hailstone>> {
    // The generator's role is to turn the input file into something usable by part 1 and part 2.
    void generator() {
        // this must have the same type as the return type of this method
        var lines = this.input.split("\n");
        processed = new ArrayList<>();
        for (var line: lines) {
            processed.add(new Hailstone(line));
        }
    }

    //p1 = point, n1 = vector
    //p2 = point, n2 = vector
    static Point3dF lineLineIntersectionXY(Point3d A, Point3d B, Point3d C, Point3d D)
    {
        // Line AB represented as a1x + b1y = c1
        float a1 = B.y - A.y;
        float b1 = A.x - B.x;
        float c1 = a1*(A.x) + b1*(A.y);

        // Line CD represented as a2x + b2y = c2
        float a2 = D.y - C.y;
        float b2 = C.x - D.x;
        float c2 = a2*(C.x)+ b2*(C.y);

        float determinant = a1*b2 - a2*b1;

        if (determinant == 0)
        {
            // The lines are parallel. This is simplified
            // by returning a pair of FLT_MAX
            return new Point3dF(Float.MAX_VALUE, Float.MAX_VALUE, 0);
        }
        else
        {
            float x = (b2*c1 - b1*c2)/determinant;
            float y = (a1*c2 - a2*c1)/determinant;
            return new Point3dF(x, y, 0);
        }
    }

    void part1() {
        List<Point3dF> xyIntersections = new ArrayList<>();

        //consider only x,y - see where they intersect
        for (int i = 0; i < processed.size(); i++) {
            for (int j = i + 1; j < processed.size(); j++) {
                var a = processed.get(i);
                var b = processed.get(j);
                var a2 = new Point3d(a.point.x + a.velocity.x, a.point.y + a.velocity.y, a.point.z + a.velocity.z);
                var b2 = new Point3d(b.point.x + b.velocity.x, b.point.y + b.velocity.y, b.point.z + b.velocity.z);
                var inter = lineLineIntersectionXY(a.point, a2, b.point, b2);

                if (inter.x != Float.MAX_VALUE) {
                    //see if it is in the right direction
                    var axDir = (inter.x - a.point.x) / Math.abs((inter.x - a.point.x));
                    var ayDir = (inter.y - a.point.y) / Math.abs((inter.y - a.point.y));
                    var testaxDir = a.velocity.x / Math.abs(a.velocity.x);
                    var testayDir = a.velocity.y / Math.abs(a.velocity.y);

                    var bxDir = (inter.x - b.point.x) / Math.abs((inter.x - b.point.x));
                    var byDir = (inter.y - b.point.y) / Math.abs((inter.y - b.point.y));
                    var testbxDir = b.velocity.x / Math.abs(b.velocity.x);
                    var testbyDir = b.velocity.y / Math.abs(b.velocity.y);

                    if (axDir == testaxDir && ayDir == testayDir &&
                        bxDir == testbxDir && byDir == testbyDir) {
                        if (inter.x >= 200000000000000F && inter.x <= 400000000000000F &&
                            inter.y >= 200000000000000F && inter.y <= 400000000000000F) {
                            xyIntersections.add(inter);
                        }
                    }
                }
            }
        }

        System.out.printf("Part 1: %d\n", xyIntersections.size());
    }

    void part2() {

        //part 2 have to use z3 - went to python

        System.out.printf("Part 2: %d\n", 0);
    }
}

class Point3dF {
    float x;
    float y;
    float z;

    public Point3dF(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

class Point3d {
    public long x;
    public long y;
    public long z;

    public Point3d(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

class Hailstone {
    public Point3d point;
    public Point3d velocity;

    public Hailstone(String s) {
        var parts = s.replace(" ","").split("[, @]");
        this.point = new Point3d(
                parseLong(parts[0]), parseLong(parts[1]), parseLong(parts[2]));
        this.velocity = new Point3d(
                parseLong(parts[3]), parseLong(parts[4]), parseLong(parts[5]));
    }
}