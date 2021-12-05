package com.asadjb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Utils {
    public static List<String> readInput(String filename) {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            return stream.toList();
        } catch (IOException e) {
            throw new RuntimeException("Unable to read input file.");
        }
    }
}
