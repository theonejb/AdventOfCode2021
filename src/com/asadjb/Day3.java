package com.asadjb;

import java.util.ArrayList;
import java.util.List;

public class Day3 {
    final static int NUMBER_OF_BITS = 12;

    public static void main() {
        List<String> inputLines = Utils.readInput("day3.txt");
        Day3.part1(inputLines);
        Day3.part2(inputLines);
    }

    static int[] getOneCounts(List<String> binaryNumbers) {
        int[] oneCounts = new int[Day3.NUMBER_OF_BITS];
        for (int i = 0; i < Day3.NUMBER_OF_BITS; i++) {
            oneCounts[i] = 0;
        }

        for (String binaryNumber :
                binaryNumbers) {
            for (int i = 0; i < Day3.NUMBER_OF_BITS; i++) {
                if (binaryNumber.charAt(i) == '1') {
                    oneCounts[i]++;
                }
            }
        }

        return oneCounts;
    }

    static void part1(List<String> binaryNumbers) {
        int[] oneCounts = Day3.getOneCounts(binaryNumbers);

        StringBuilder gamaRateSb = new StringBuilder();
        StringBuilder epsilonRateSb = new StringBuilder();

        int totalInputs = binaryNumbers.size();
        int majority = totalInputs / 2;

        for (int i = 0; i < Day3.NUMBER_OF_BITS; i++) {
            if (oneCounts[i] > majority) {
                gamaRateSb.append('1');
                epsilonRateSb.append('0');
            } else {
                gamaRateSb.append('0');
                epsilonRateSb.append('1');
            }
        }

        String gamaRateBinary = gamaRateSb.toString();
        int gamaRate = Integer.parseInt(gamaRateBinary, 2);

        String epsilonRateBinary = epsilonRateSb.toString();
        int epsilonRate = Integer.parseInt(epsilonRateBinary, 2);

        System.out.printf("Answer to part 1: %d\n", gamaRate * epsilonRate);
    }

    static List<String> filterForOxygenGeneratorRatings(List<String> binaryNumbers, int bitPosition) {
        int oneCounts = 0, totalNumbers = binaryNumbers.size();
        List<String> numbersWithOneInBitPosition = new ArrayList<>(),
                numbersWithZeroInBitPosition = new ArrayList<>();

        for (String binaryNumber : binaryNumbers) {
            if (binaryNumber.charAt(bitPosition) == '1') {
                oneCounts++;
                numbersWithOneInBitPosition.add(binaryNumber);
            } else {
                numbersWithZeroInBitPosition.add(binaryNumber);
            }
        }

        int zeroCounts = totalNumbers - oneCounts;
        if (oneCounts >= zeroCounts) {
            return numbersWithOneInBitPosition;
        } else {
            return numbersWithZeroInBitPosition;
        }
    }

    static List<String> filterForCO2ScrubberRatings(List<String> binaryNumbers, int bitPosition) {
        int oneCounts = 0, totalNumbers = binaryNumbers.size();
        List<String> numbersWithOneInBitPosition = new ArrayList<>(),
                numbersWithZeroInBitPosition = new ArrayList<>();

        for (String binaryNumber : binaryNumbers) {
            if (binaryNumber.charAt(bitPosition) == '1') {
                oneCounts++;
                numbersWithOneInBitPosition.add(binaryNumber);
            } else {
                numbersWithZeroInBitPosition.add(binaryNumber);
            }
        }

        int zeroCounts = totalNumbers - oneCounts;
        if (oneCounts >= zeroCounts) {
            return numbersWithZeroInBitPosition;
        } else {
            return numbersWithOneInBitPosition;
        }
    }

    static void part2(List<String> binaryNumbers) {
        List<String> oxygenGeneratorRatings = binaryNumbers;
        for (int bitPosition = 0; bitPosition < Day3.NUMBER_OF_BITS; bitPosition++) {
            if (oxygenGeneratorRatings.size() == 1) {
                break;
            }

            oxygenGeneratorRatings = Day3.filterForOxygenGeneratorRatings(oxygenGeneratorRatings, bitPosition);
        }

        List<String> CO2ScrubberRatings = binaryNumbers;
        for (int bitPosition = 0; bitPosition < Day3.NUMBER_OF_BITS; bitPosition++) {
            if (CO2ScrubberRatings.size() == 1) {
                break;
            }

            CO2ScrubberRatings = Day3.filterForCO2ScrubberRatings(CO2ScrubberRatings, bitPosition);
        }

        int oxygenGeneratorRating = Integer.parseInt(oxygenGeneratorRatings.get(0), 2);
        int CO2ScrubberRating = Integer.parseInt(CO2ScrubberRatings.get(0), 2);

        System.out.printf("Answer for part 2: %d\n", oxygenGeneratorRating * CO2ScrubberRating);
    }
}
