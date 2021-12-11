package com.asadjb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Day11 {
    static class OctopusLocation {
        int row, col;

        public OctopusLocation(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", row, col);
        }

        @Override
        public boolean equals(Object obj) {
            OctopusLocation other = (OctopusLocation) obj;
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

        List<OctopusLocation> getNeighbours(int maxRows, int maxCols) {
            List<OctopusLocation> neighbours = new ArrayList<>();
            int[][] neighbourDeltas = new int[][]{
                    {-1, 0},
                    {-1, 1},
                    {0, 1},
                    {1, 1},
                    {1, 0},
                    {1, -1},
                    {0, -1},
                    {-1, -1}
            };

            for (var nDelta : neighbourDeltas) {
                int neighbourRow = row + nDelta[0],
                        neighbourCol = col + nDelta[1];

                if (neighbourRow < 0 || neighbourRow >= maxRows)
                    continue;
                if (neighbourCol < 0 || neighbourCol >= maxCols)
                    continue;

                neighbours.add(new OctopusLocation(neighbourRow, neighbourCol));
            }

            return neighbours;
        }
    }

    static class OctopusGrid {
        int[][] energyLevels;
        int nRows, nCols;

        public OctopusGrid(String[] input) {
            nRows = input.length;
            nCols = input[0].length();
            energyLevels = new int[nRows][nCols];

            int row = 0, col = 0;
            for (var line :
                    input) {
                for (var level :
                        line.toCharArray()) {
                    energyLevels[row][col++] = Integer.parseInt("" + level);
                }
                col = 0;
                row++;
            }
        }

        OctopusLocation[] allLocations() {
            OctopusLocation[] locations = new OctopusLocation[nRows * nCols];
            for (int row = 0; row < nRows; row++) {
                for (int col = 0; col < nCols; col++) {
                    locations[row * nCols + col] = new OctopusLocation(row, col);
                }
            }

            return locations;
        }

        @Override
        public String toString() {
            return String.format(Arrays.deepToString(energyLevels));
        }

        int getAt(OctopusLocation loc) {
            return energyLevels[loc.row][loc.col];
        }

        void setAt(OctopusLocation loc, int energyLevel) {
            energyLevels[loc.row][loc.col] = energyLevel;
        }

        void incrAt(OctopusLocation loc) {
            energyLevels[loc.row][loc.col]++;
        }

        void print(HashSet<OctopusLocation> flashed) {
            StringBuilder sb = new StringBuilder();

            for (int row = 0; row < nRows; row++) {
                for (int col = 0; col < nCols; col++) {
                    var loc = new OctopusLocation(row, col);
                    if (flashed.contains(loc)) {
                        sb.append(Ansi.Red.format("%d", getAt(loc)));
                    } else {
                        sb.append(getAt(loc));
                    }
                }
                sb.append('\n');
            }

            System.out.println(sb);
        }

        void print() {
            HashSet<OctopusLocation> noFlashed = new HashSet<OctopusLocation>();
            print(noFlashed);
        }

        HashSet<OctopusLocation> step() {
            HashSet<OctopusLocation> flashed = new HashSet<>();
            var allLocations = allLocations();
            for (var loc : allLocations) incrAt(loc);

            boolean anyFlashes;
            do {
                anyFlashes = false;
                for (var loc : allLocations) {
                    if (getAt(loc) > 9 && !flashed.contains(loc)) {
                        flashed.add(loc);
                        anyFlashes = true;

                        var neighbours = loc.getNeighbours(nRows, nCols);
                        for (var n :
                                neighbours) {
                            incrAt(n);
                        }
                    }
                }
            } while (anyFlashes);

            for (var flashedLoc :
                    flashed) {
                setAt(flashedLoc, 0);
            }

            return flashed;
        }
    }

    static void part1(OctopusGrid grid) {
        int totalFlashes = 0;
        for (int i = 0; i < 100; i++) {
            var flashes = grid.step();
            totalFlashes += flashes.size();
            grid.print(flashes);
        }

        System.out.printf("Answer to part 1: %d\n", totalFlashes);
    }

    static void part2(OctopusGrid grid) {
        int step = 0;
        while (true) {
            var flashes = grid.step();
            grid.print(flashes);

            step++;

            if (flashes.size() == grid.nRows * grid.nCols) {
                break;
            }
        }

        System.out.printf("Answer to part 2: %d\n", step);
    }

    static void main() {
        List<String> input = Utils.readInput("./day11.txt");
        String[] inputArray = new String[input.size()];
        input.toArray(inputArray);

        OctopusGrid grid = new OctopusGrid(inputArray);
        part1(grid);
        grid = new OctopusGrid(inputArray);
        part2(grid);
    }
}
