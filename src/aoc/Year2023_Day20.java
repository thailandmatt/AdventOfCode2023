package aoc;

import java.awt.Point;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.Signal;

import static java.lang.Integer.*;

// Put the return type of the generator in Base<>
public class Year2023_Day20 extends Base<Year2023_Day20.Machine> {

    public class Machine {
        public HashMap<String, Year2023_Day20.MyModule> modules = new HashMap<>();

        @Override
        public String toString() {
            StringBuilder x = new StringBuilder();
            for (var key: modules.keySet()) {
                x.append(modules.get(key).toString()).append("~");
            }
            return x.toString();
        }
    }

    public class MyModule {
        public String name;
        public List<String> outputs = new ArrayList<>();

        public void ProcessSignal(String source, boolean signal, List<Pulse> pulseStack) {
            //broadcast to all outputs
            for (var output: outputs) {
                Pulse pulse = new Pulse();
                pulse.source = this.name;
                pulse.destination = output;
                pulse.signal = signal;
                pulseStack.add(pulse);
            }
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public class FlipFlop extends MyModule {
        public boolean status = false;

        @Override
        public void ProcessSignal(String source, boolean signal, List<Pulse> pulseStack) {
            if (!signal) {
                //invert and send signal to all outputs
                status = !status;
                for (var output: outputs) {
                    Pulse pulse = new Pulse();
                    pulse.source = this.name;
                    pulse.destination = output;
                    pulse.signal = status;
                    pulseStack.add(pulse);
                }
            }
        }

        @Override
        public String toString() {
            return name + ":" + (status?"1":"0");
        }
    }

    public class Conjunction extends MyModule {
        public HashMap<String, Boolean> rememberedPulses = new HashMap<String, Boolean>();

        @Override
        public void ProcessSignal(String source, boolean signal, List<Pulse> pulseStack) {
            rememberedPulses.put(source, signal);

            boolean allHigh = true;
            for (var memory: rememberedPulses.values()) {
                if (!memory) {
                    allHigh = false;
                  break;
                }
            }

            for (var output: outputs) {
                Pulse pulse = new Pulse();
                pulse.source = this.name;
                pulse.destination = output;
                pulse.signal = !allHigh;
                pulseStack.add(pulse);
            }
        }

        @Override
        public String toString() {
            StringBuilder x = new StringBuilder();
            for (var key: rememberedPulses.keySet()) {
                x.append(key + ",").append(rememberedPulses.get(key)?"1":"0").append("*");
            }
            return x.toString();
        }
    }

    public class Pulse {
        public String source;
         public String destination;
         public boolean signal;

        @Override
        public String toString() {
            return source + " -" + (signal ? "high": "low") + "-> " + destination;
        }
    }

    // The generator's role is to turn the input file into something usable by part 1 and part 2.
    void generator() {
        // this must have the same type as the return type of this method
        var lines = this.input.split("\n");
        processed = new Machine();
        for (var line: lines) {
            var parts = line.split(" -> ");
            List<String> outputs = new ArrayList<>();
            for (var output: parts[1].split(",")) {
                outputs.add(output.strip());
            }

            if (parts[0].equals("broadcaster")) {
                MyModule myModule = new MyModule();
                myModule.name = parts[0].strip();
                myModule.outputs = outputs;
                processed.modules.put("broadcaster", myModule);
            } else if (parts[0].charAt(0) == '%') {
                MyModule myModule = new FlipFlop();
                myModule.name = parts[0].strip().replace("%", "");
                myModule.outputs = outputs;
                processed.modules.put(myModule.name, myModule);
            } else if (parts[0].charAt(0) == '&') {
                MyModule myModule = new Conjunction();
                myModule.name = parts[0].strip().replace("&", "");
                myModule.outputs = outputs;
                processed.modules.put(myModule.name, myModule);
            }
        }

        //wire up all the conjunctions
        for (var key: processed.modules.keySet()) {
            var module = processed.modules.get(key);
            if (module instanceof Conjunction) {
                for (var otherModules: processed.modules.values()) {
                    if (otherModules.outputs.contains(key)) {
                        ((Conjunction)module).rememberedPulses.put(otherModules.name, false);
                    }
                }
            }
        }
    }
    
    void part1() {
        //push the button however many times until we hit the cycle
        //so we can calculate total for 1000
        var buttonPushes = 0;
        var lowSignalsProcessed = 0;
        var highSignalsProcessed = 0;

        for (int i = 0; i < 1000; i++) {
            //push the button once
            buttonPushes++;

            List<Pulse> pulseStack = new ArrayList<>();
            Pulse pulse = new Pulse();
            pulse.destination = "broadcaster";
            pulse.source = "button";
            pulse.signal = false;
            pulseStack.add(pulse);

            while (!pulseStack.isEmpty()) {
                var pop = pulseStack.remove(0);
                if (pop.signal)
                    highSignalsProcessed++;
                else
                    lowSignalsProcessed++;

                if (processed.modules.containsKey(pop.destination))
                    processed.modules.get(pop.destination).ProcessSignal(pop.source, pop.signal, pulseStack);
            }
        }

        System.out.printf("Part 1: %d %d %d\n", buttonPushes, lowSignalsProcessed, highSignalsProcessed);

        var total = (1000 / buttonPushes) * lowSignalsProcessed * (1000 / buttonPushes) * highSignalsProcessed;
        System.out.printf("Part 1: %d\n", total);
    }

    void part2() {
        //visualization
//        for (var module: processed.modules.values()) {
//            for (var output: module.outputs) {
//                    System.out.println(
//                      module.name + " -- " + output);
//                }
//        }

        //looking at the graph of the nodules, rx will get a low from xn
        //when xn gets a high from each of xf, mp, hn, and fz
        //so let's see how long the cycle is before those each send a high and then
        //we can figure out how long until they'll all collide
        List<String> targets = new ArrayList<>(List.of("xf", "mp", "hn", "fz"));
        HashMap<String, Integer> cycleDetector = new HashMap<>();
        List<Long> cycleCounts = new ArrayList<>();


        var buttonPushes = 0;

        for (int i = 0; i < 10000; i++) {
            //push the button once
            buttonPushes++;

            List<Pulse> pulseStack = new ArrayList<>();
            Pulse pulse = new Pulse();
            pulse.destination = "broadcaster";
            pulse.source = "button";
            pulse.signal = false;
            pulseStack.add(pulse);



            while (!pulseStack.isEmpty()) {
                var pop = pulseStack.remove(0);

                for (var target: targets) {
                    if (pop.source.equals(target) && pop.signal) {
                        if (cycleDetector.containsKey(target)) {
                            cycleCounts.add((long) buttonPushes - (long) cycleDetector.get(target));
                        }

                        cycleDetector.put(target, buttonPushes);
                    }
                }

                if (processed.modules.containsKey(pop.destination))
                    processed.modules.get(pop.destination).ProcessSignal(pop.source, pop.signal, pulseStack);
            }
        }

        var answer = lcm(cycleCounts.toArray(new Long[0]));

        // print out the result
        System.out.printf("Part 2: %s %d\n", cycleCounts, answer);
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

    private static Long gcd(Long[] input)
    {
        Long result = input[0];
        for(int i = 1; i < input.length; i++) result = gcd(result, input[i]);
        return result;
    }

    private static long lcm(long a, long b)
    {
        return a * (b / gcd(a, b));
    }

    private static Long lcm(Long[] input)
    {
        Long result = input[0];
        for(int i = 1; i < input.length; i++) result = lcm(result, input[i]);
        return result;
    }
}
