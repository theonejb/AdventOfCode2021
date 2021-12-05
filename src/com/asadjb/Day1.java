package com.asadjb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day1 {
    public static void main() {
        try (Stream<String> stream = Files.lines(Paths.get("./day1.txt"))) {
            var lines = stream.toList();

            List<Integer> depths = new ArrayList<>();
            for (var depthString :
                    lines) {
                try {
                    depths.add(Integer.parseInt(depthString));
                } catch (NumberFormatException e) {}
            }

            Day1.part1(depths);
            Day1.part2(depths);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void part1(List<Integer> depths) {
        int numberOfTimesDepthIncreases = 0;
        for (int i = 1; i < depths.size(); i++) {
            if (depths.get(i) > depths.get(i-1)) {
                numberOfTimesDepthIncreases++;
            }
        }

        System.out.printf("Answer to part 1: %d\n", numberOfTimesDepthIncreases);
    }

    static void part2(List<Integer> depths) {
        int numberOfTimesDepthIncreases = 0;
        int windowEnd = 2;
        int windowSum = depths.get(0) + depths.get(1) + depths.get(2);

        for (windowEnd++; windowEnd < depths.size(); windowEnd++) {
            int newWindowSum = windowSum - depths.get(windowEnd - 3) + depths.get(windowEnd);
            if (newWindowSum > windowSum) {
                numberOfTimesDepthIncreases++;
            }
        }

        System.out.printf("Answer to part 2: %d\n", numberOfTimesDepthIncreases);
    }
}
