package com.asadjb;

import java.util.*;

public class Day9 {
    static class HeightMap {
        int[][] map;
        int nRows, nCols;

        public HeightMap(int[][] map) {
            this.map = map;

            nRows = map.length;
            nCols = map[0].length;
        }

        int getLoc(HeightMapLocation loc) {
            return map[loc.row][loc.col];
        }
    }

    static class HeightMapLocation {
        int row, col;

        @Override
        public String toString() {
            return String.format("(%d, %d)", row, col);
        }

        @Override
        public boolean equals(Object obj) {
            HeightMapLocation other = (HeightMapLocation) obj;
            return row == other.row && col == other.col;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + col;
            result = prime * result + row;
            return result;
        }

        public HeightMapLocation(int row, int col) {
            this.row = row;
            this.col = col;
        }

        List<HeightMapLocation> getNeighbours(int maxRows, int maxCols) {
            List<HeightMapLocation> neighbours = new ArrayList<>();
            int[][] neighbourDeltas = new int[][]{
                    {-1, 0},
                    {0, 1},
                    {1, 0},
                    {0, -1}
            };

            for (var nDelta : neighbourDeltas) {
                int neighbourRow = row + nDelta[0],
                        neighbourCol = col + nDelta[1];

                if (neighbourRow < 0 || neighbourRow >= maxRows)
                    continue;
                if (neighbourCol < 0 || neighbourCol >= maxCols)
                    continue;

                neighbours.add(new HeightMapLocation(neighbourRow, neighbourCol));
            }

            return neighbours;
        }
    }

    static HeightMap parseInput(List<String> input) {
        int height = input.size(),
                width = input.get(0).length();
        int[][] heightMap = new int[height][width];

        int row = 0, col = 0;
        for (String line :
                input) {
            for (char floorHeight :
                    line.toCharArray()) {
                heightMap[row][col++] = Integer.parseInt("" + floorHeight);
            }

            col = 0;
            row++;
        }

        return new HeightMap(heightMap);
    }

    static boolean isLowest(HeightMap heightMap, HeightMapLocation loc) {
        List<HeightMapLocation> validNeighbours = loc.getNeighbours(heightMap.nRows, heightMap.nCols);

        int locHeight = heightMap.getLoc(loc);
        boolean isLow = true;
        for (var neighbourLoc :
                validNeighbours) {
            if (heightMap.getLoc(neighbourLoc) <= locHeight) {
                isLow = false;
                break;
            }
        }

        return isLow;
    }

    static void part1(HeightMap map) {
        int riskLevelSum = 0;
        for (int row = 0; row < map.nRows; row++) {
            for (int col = 0; col < map.nCols; col++) {
                var loc = new HeightMapLocation(row, col);
                if (isLowest(map, loc)) {
                    riskLevelSum += map.getLoc(loc) + 1;
                }
            }
        }

        System.out.printf("Answer to part 1: %d\n", riskLevelSum);
    }

    static void part2(HeightMap map) {
        List<Integer> basinSizes = new ArrayList<>();

        for (int row = 0; row < map.nRows; row++) {
            for (int col = 0; col < map.nCols; col++) {
                var loc = new HeightMapLocation(row, col);
                if (isLowest(map, loc)) {
                    int basinSize = calculateBasinSize(map, loc);
                    basinSizes.add(basinSize);
                }
            }
        }

        Collections.sort(basinSizes);
        Collections.reverse(basinSizes);
        int result = basinSizes.get(0) * basinSizes.get(1) * basinSizes.get(2);

        System.out.printf("Answer to part 2: %d\n", result);
    }

    static int calculateBasinSize(HeightMap map, HeightMapLocation lowestLoc) {
        HashSet<HeightMapLocation> seenLocations = new HashSet<>();
        Queue<HeightMapLocation> pointsInBasin = new ArrayDeque<>();

        pointsInBasin.add(lowestLoc);
        seenLocations.add(lowestLoc);

        while (!pointsInBasin.isEmpty()) {
            var nextLoc = pointsInBasin.remove();

            var validNeighbours = nextLoc.getNeighbours(map.nRows, map.nCols);
            for (var neighbour :
                    validNeighbours) {
                if (map.getLoc(neighbour) == 9)
                    continue;

                if (seenLocations.contains(neighbour))
                    continue;

                pointsInBasin.add(neighbour);
                seenLocations.add(neighbour);
            }
        }

        return seenLocations.size();
    }

    static void main() {
        var input = Utils.readInput("./day9.txt");
        var heightMap = parseInput(input);
        part1(heightMap);
        part2(heightMap);
    }
}
