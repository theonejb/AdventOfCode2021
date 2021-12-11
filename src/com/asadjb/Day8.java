package com.asadjb;

import java.util.*;

public class Day8 {
    public static void main() {
        List<String> inputLines = Utils.readInput("./day8.txt");
        List<LineOfInput> inputs = new ArrayList<>();
        for (var inputLine : inputLines) {
            inputs.add(new LineOfInput(inputLine));
        }

        part1(inputs);
        part2(inputs);
    }

    static void part1(List<LineOfInput> inputs) {
        int countsOfRelevantOutputs = 0;

        for (var input :
                inputs) {
            for (var output :
                    input.outputValues) {
                if (output.length() == 2 || output.length() == 4 || output.length() == 3 || output.length() == 7) {
                    countsOfRelevantOutputs++;
                }
            }
        }

        System.out.printf("Answer to part 1: %d\n", countsOfRelevantOutputs);
    }

    static String[] generatePossibleSegments(String inputSignals, HashMap<Character, ArrayList<Character>> segmentToPossibleSignals) {
        List<String> possibleSegments = new ArrayList<>();
        possibleSegments.add("");

        HashMap<Character, ArrayList<Character>> signalToPossibleSegments = new HashMap<>();
        for (var segment :
                segmentToPossibleSignals.keySet()) {
            var possibleSignals = segmentToPossibleSignals.get(segment);
            for (var signal :
                    possibleSignals) {
                if (!signalToPossibleSegments.containsKey(signal)) {
                    signalToPossibleSegments.put(signal, new ArrayList<>());
                }
                signalToPossibleSegments.get(signal).add(segment);
            }
        }

        System.out.println(signalToPossibleSegments);

        for (char signal : inputSignals.toCharArray()) {
            var possibleSegmentsForThisSignal = signalToPossibleSegments.get(signal);

            List<String> nextPossibleSegments = new ArrayList<>();
            for (var segment : possibleSegmentsForThisSignal) {
                for (int i = 0; i < possibleSegments.size(); i++) {
                    nextPossibleSegments.add(possibleSegments.get(i) + segment);
                }
            }

            possibleSegments = nextPossibleSegments;
        }

        var returnArray = new String[possibleSegments.size()];
        possibleSegments.toArray(returnArray);
        return returnArray;
    }

    static HashMap<Character, ArrayList<Character>> mapSignalsToSegments(LineOfInput input) {
        String signalsFor1 = input.getFirstInputMatchingLength(2),
                signalsFor7 = input.getFirstInputMatchingLength(3),
                signalsFor4 = input.getFirstInputMatchingLength(4),
                signalsFor8 = input.getFirstInputMatchingLength(7);

        HashSet<Character> mappedSignals = new HashSet<>();

        HashMap<Character, ArrayList<Character>> mappingFromSegmentToSignal = new HashMap<>();
        for (char segment :
                "ABCDEFG".toCharArray()) {
            mappingFromSegmentToSignal.put(segment, new ArrayList<>());
        }

        for (char signal : signalsFor1.toCharArray()) {
            mappingFromSegmentToSignal.get('C').add(signal);
            mappingFromSegmentToSignal.get('F').add(signal);

            mappedSignals.add(signal);
        }

        for (char signal : signalsFor7.toCharArray()) {
            if (mappedSignals.contains(signal)) continue;

            mappingFromSegmentToSignal.get('A').add(signal);
            mappedSignals.add(signal);
        }

        for (char signal : signalsFor4.toCharArray()) {
            if (mappedSignals.contains(signal)) continue;

            mappingFromSegmentToSignal.get('B').add(signal);
            mappingFromSegmentToSignal.get('D').add(signal);

            mappedSignals.add(signal);
        }

        for (char signal : signalsFor8.toCharArray()) {
            if (mappedSignals.contains(signal)) continue;

            mappingFromSegmentToSignal.get('E').add(signal);
            mappingFromSegmentToSignal.get('G').add(signal);

            mappedSignals.add(signal);
        }

        return mappingFromSegmentToSignal;
    }

    static String findValidSegmentCombination(String[] possibleSegments) {
        String[] validSegments = {
                "ABCEFG",
                "CF",
                "ACDEG",
                "ACDFG",
                "BCDF",
                "ABDFG",
                "ABDEFG",
                "ACF",
                "ABCDEFG",
                "ABCDFG"
        };
        var validSegmentsSet = new HashSet<String>();
        for (var segment : validSegments) validSegmentsSet.add(segment);

        for (var segment :
                possibleSegments) {
            var stringChars = segment.toCharArray();
            Arrays.sort(stringChars);
            var sortedSegment = new String(stringChars);
            if (validSegmentsSet.contains(sortedSegment))
                return segment;
        }

        return null;
    }

    static void part2(List<LineOfInput> inputs) {
        for (var input :
                inputs) {
            var mappingOfSegmentsToPossibleSignals = mapSignalsToSegments(input);
            System.out.println(mappingOfSegmentsToPossibleSignals);
            for (var signal : input.signalPatterns) {
                var possibleSegments = generatePossibleSegments("ab", mappingOfSegmentsToPossibleSignals);
                System.out.println(Arrays.toString(possibleSegments));
                var validSegments = findValidSegmentCombination(possibleSegments);

                System.out.println(signal);
                System.out.println(validSegments);

                break;
            }
            break;
        }
    }
}

class LineOfInput {
    String[] signalPatterns, outputValues;

    public LineOfInput(String input) {
        String[] parts = input.split(" \\| ");
        signalPatterns = parts[0].split("\s+");
        outputValues = parts[1].split("\s+");
    }

    @Override
    public String toString() {
        return String.format("%s | %s", Arrays.toString(signalPatterns), Arrays.toString(outputValues));
    }

    String getFirstInputMatchingLength(int length) {
        for (var input :
                signalPatterns) {
            if (input.length() == length)
                return input;
        }

        throw new RuntimeException("Unable to find input matching length.");
    }
}
