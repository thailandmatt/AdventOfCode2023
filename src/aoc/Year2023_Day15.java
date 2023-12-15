package aoc;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;

// Put the return type of the generator in Base<>
public class Year2023_Day15 extends Base<ArrayList<String>> {

    // The generator's role is to turn the input file into something usable by part 1 and part 2.
    void generator() {
        // this must have the same type as the return type of this method
        var lines = this.input.split(",");
        this.processed = new ArrayList<>();
        processed.addAll(Arrays.asList(lines));
    }

    void part1() {
        int total = 0;

        for (var code: processed) {
            total += myHash(code);
        }

        // print out the result
        System.out.printf("Part 1: %d\n", total);
    }

    int myHash(String code) {
        code = code.strip();
        int curValue = 0;
        var chars = code.toCharArray();
        for (var c: chars) {
            curValue += c;
            curValue *= 17;
            curValue %= 256;
        }
        return curValue;
    }
    void part2() {
        HashMap<Integer, List<String>> boxes = new HashMap<>();
        HashMap<String, Point> reverseBoxes = new HashMap<>();

        for (var code: processed) {
            var parts = code.split("[-=]");
            var isEquals = code.contains("=");
            var label = parts[0];
            var hash = myHash(label);

            if (isEquals) {
                if (reverseBoxes.containsKey(label)) {
                    //replace
                    var curPos = reverseBoxes.get(label);
                    boxes.get(curPos.x).set(curPos.y, code);
                } else {
                    //add
                    if (!boxes.containsKey(hash)) boxes.put(hash, new ArrayList<>());
                    boxes.get(hash).add(code);
                    var positionInList = boxes.get(hash).size() - 1;
                    reverseBoxes.put(label, new Point(hash, positionInList));
                }
            } else {
                if (reverseBoxes.containsKey(label)) {
                    var curPos = reverseBoxes.get(label);
                    if (curPos.x == hash) {
                        //remove it from the lists
                        var list = boxes.get(hash);
                        list.remove(curPos.y);
                        reverseBoxes.remove(label);
                        for (var i = 0; i < list.size(); i++) {
                            var parts2 = list.get(i).split("=");
                            reverseBoxes.put(parts2[0], new Point(curPos.x, i));
                        }
                    }
                }
            }
        }

        var total = 0;

        for (var label: reverseBoxes.keySet()) {
            var curPos = reverseBoxes.get(label);
            var box = curPos.x;
            var slotIndex = curPos.y;
            var full = boxes.get(box).get(slotIndex);
            var parts = full.split(("="));
            var focalLength = parseInt(parts[1].strip());
            var focusingPower = (box + 1) * (slotIndex + 1) * focalLength;
            total += focusingPower;
        }

        // print out the result
        System.out.printf("Part 2: %d\n", total);
    }
}
