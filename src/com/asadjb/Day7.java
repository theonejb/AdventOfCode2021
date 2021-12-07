package com.asadjb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Day7 {
    public static void main() {
        List<String> input = Utils.readInput("./day7.txt");
        String[] positionsInput = input.get(0).split(",");

        List<Integer> positions = new ArrayList<>();
        for (String positionString :
                positionsInput) {
            positions.add(Integer.parseInt(positionString));
        }

        Collections.sort(positions);

        part1(positions);
        part2(positions);
    }

    static int calculateFuelCostSimple(List<Integer> crabPositions, int destination) {
        int cost = 0;
        for (var position :
                crabPositions) {
            cost += Math.abs(position - destination);
        }

        return cost;
    }

    static int calculateFuelCostComplex(List<Integer> crabPositions, int destination) {
        double cost = 0;
        for (var position :
                crabPositions) {
            double numberOfSteps = Math.abs(position - destination);
            // The fuel cost is an arithmetic series, starting at one and increasing by 1
            // for each term. The number of terms is the number of steps we want to take.
            // The sum of this series is n/2 * (2a + (n-1) * d).
            // n is number of steps. a is the first term. d is the difference between consecutive
            // terms.
            double costOfMove = (numberOfSteps / 2.0) * (2 * 1 + (numberOfSteps - 1) * 1);

            cost += costOfMove;
        }

        return (int) cost;
    }

    static void part1(List<Integer> positions) {
        int minPosition = positions.get(0), maxPosition = positions.get(positions.size()-1);

        int bestPosition = positions.get(0), minFuel = 100_000_000;

        for (int positionCandidate = minPosition; positionCandidate <= maxPosition; positionCandidate++) {
            int fuelCost = calculateFuelCostSimple(positions, positionCandidate);

            if (fuelCost < minFuel) {
                bestPosition = positionCandidate;
                minFuel = fuelCost;
            }
        }

        System.out.printf("Answer to part 1: Position: %d, Cost: %d\n", bestPosition, minFuel);
    }

    static void part2(List<Integer> positions) {
        int bestPosition = positions.get(0);
        int minFuel = 100_000_000;

        int minPosition = positions.get(0);
        int maxPosition = positions.get(positions.size()-1);

        for (int positionCandidate = minPosition; positionCandidate <= maxPosition; positionCandidate++) {
            int fuelCost = calculateFuelCostComplex(positions, positionCandidate);

            if (fuelCost < minFuel) {
                bestPosition = positionCandidate;
                minFuel = fuelCost;
            }
        }

        System.out.printf("Answer to part 2: Position: %d Cost: %d\n", bestPosition, minFuel);
    }
}
