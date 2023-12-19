package aoc;

import java.awt.Point;
import java.time.zone.ZoneRules;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.plaf.IconUIResource;

import static java.lang.Integer.*;

// Put the return type of the generator in Base<>
public class Year2023_Day19 extends Base<Year2023_Day19.Workflow> {

    public class Workflow {
        public HashMap<String, List<Rule>> workflows = new HashMap<>();
        public List<Parts> machineParts = new ArrayList();
    }

    public class Rule {
        public boolean hasCondition = false;
        public String operand = "";
        public String operator = "";
        public int comparator = 0;
        public String resultWorkflow = "";
    }

    public class Parts {
        public int x;
        public int m;
        public int a;
        public int s;
    }

    // The generator's role is to turn the input file into something usable by part 1 and part 2.
    void generator() {
        // this must have the same type as the return type of this method
        var parts = this.input.split("\n\n");
        processed = new Workflow();

        //workflows
        for (var line: parts[0].split("\n")) {
            var subParts = line.split("[{}]");
            var workflowName = subParts[0];
            var subSubParts = subParts[1].split(",");
            List<Rule> ruleList = new ArrayList<>();
            for (var subSubPart: subSubParts) {
                if (subSubPart.contains(":")) {
                    var ruleParts = subSubPart.split("[<>:]");
                    Rule rule = new Rule();
                    rule.hasCondition = true;
                    rule.operator = ruleParts[0];
                    rule.operand = subSubPart.contains("<") ? "<" : ">";
                    rule.comparator = parseInt(ruleParts[1]);
                    rule.resultWorkflow = ruleParts[2];
                    ruleList.add(rule);
                } else {
                    Rule rule = new Rule();
                    rule.resultWorkflow = subSubPart;
                    ruleList.add(rule);
                }
            }
            processed.workflows.put(workflowName, ruleList);
        }

        //parts
        for (var line: parts[1].split("\n")) {
            Pattern pattern = Pattern.compile("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)}");
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            Parts newPart = new Parts();
            newPart.x = parseInt(matcher.group(1));
            newPart.m = parseInt(matcher.group(2));
            newPart.a = parseInt(matcher.group(3));
            newPart.s = parseInt(matcher.group(4));
            processed.machineParts.add(newPart);
        }
    }
    
    void part1() {
        List<Parts> accepted = new ArrayList<>();
        for (var part: processed.machineParts) {
            var curFlow = "in";

            while (!curFlow.equals("A") && !curFlow.equals("R")) {
                var rules = processed.workflows.get(curFlow);
                for (var rule : rules) {
                    if (!rule.hasCondition) {
                        curFlow = rule.resultWorkflow;
                        break;
                    } else {
                        var x = 0;

                        if (rule.operator.equals("a"))
                            x = part.a;
                        else if (rule.operator.equals("x"))
                            x = part.x;
                        else if (rule.operator.equals("m"))
                            x = part.m;
                        else if (rule.operator.equals("s"))
                            x = part.s;

                        boolean answer = false;
                        if (rule.operand.equals(">"))
                            answer = x > rule.comparator;
                        else
                            answer = x < rule.comparator;

                        if (answer) {
                            curFlow = rule.resultWorkflow;
                            break;
                        }
                    }
                }
            }

            if (curFlow.equals("A")) accepted.add(part);
        }

        var total = 0;
        for (var accept: accepted) {
            total += accept.x + accept.m + accept.a + accept.s;
        }

        // print out the result
        System.out.printf("Part 1: %d\n", total);
    }

    class RangeTest {
         public String workflowName;
         public Point x;
         public Point m;
         public Point a;
         public Point s;
    }

    void part2() {
        List<RangeTest> list = new ArrayList<>();
        List<RangeTest> accepted = new ArrayList<>();
        RangeTest start = new RangeTest();
        start.workflowName = "in";
        start.x = new Point(1, 4000);
        start.m = new Point(1, 4000);
        start.a = new Point(1, 4000);
        start.s = new Point(1, 4000);
        list.add(start);

        while (!list.isEmpty()) {
            //pop the top one off and process it through
            var cur = list.remove(0);

            if (cur.workflowName.equals("A")) {
                accepted.add(cur);
                continue;
            } else if (cur.workflowName.equals("R")) {
                continue;
            }

            var rules = processed.workflows.get(cur.workflowName);
            for (var rule : rules) {
                if (!rule.hasCondition) {
                    cur.workflowName = rule.resultWorkflow;
                    //put it back on
                    list.add(cur);
                    break;
                } else {
                    //add a clone to the list for the result
                    RangeTest clone = new RangeTest();
                    clone.workflowName = rule.resultWorkflow;
                    clone.x = (Point)cur.x.clone();
                    clone.m = (Point)cur.m.clone();
                    clone.s = (Point)cur.s.clone();
                    clone.a = (Point)cur.a.clone();

                    if (rule.operand.equals(">")) {
                        if (rule.operator.equals("a")) {
                            clone.a.x = rule.comparator + 1;
                            cur.a.y = rule.comparator;
                        }
                        else if (rule.operator.equals("x")) {
                            clone.x.x = rule.comparator + 1;
                            cur.x.y = rule.comparator;
                        }
                        else if (rule.operator.equals("m")) {
                            clone.m.x = rule.comparator + 1;
                            cur.m.y = rule.comparator;
                        }
                        else if (rule.operator.equals("s")) {
                            clone.s.x = rule.comparator + 1;
                            cur.s.y = rule.comparator;
                        }
                    }
                    else {
                        if (rule.operator.equals("a")) {
                            clone.a.y = rule.comparator - 1;
                            cur.a.x = rule.comparator;
                        }
                        else if (rule.operator.equals("x")) {
                            clone.x.y = rule.comparator - 1;
                            cur.x.x = rule.comparator;
                        }
                        else if (rule.operator.equals("m")) {
                            clone.m.y = rule.comparator - 1;
                            cur.m.x = rule.comparator;
                        }
                        else if (rule.operator.equals("s")) {
                            clone.s.y = rule.comparator - 1;
                            cur.s.x = rule.comparator;
                        }
                    }

                    list.add(clone);
                }
            }
        }

        long total = 0L;
        for (var accept: accepted) {
            total +=
              ((long)(accept.a.y - accept.a.x + 1)) *
                ((long)(accept.m.y - accept.m.x + 1)) *
                ((long)(accept.x.y - accept.x.x + 1)) *
                ((long)(accept.s.y - accept.s.x + 1));

        }

        // print out the result
        System.out.printf("Part 2: %d\n", total);
    }
}
