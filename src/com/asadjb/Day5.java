package com.asadjb;

import java.util.ArrayList;
import java.util.List;

public class Day5 {
    public static void main() {
        List<Line> lines = new ArrayList<>();
        int minX = 50000, maxX = -50000, minY = 50000, maxY = -50000;

        List<String> input = Utils.readInput("./day5.txt");
        for (String lineInput :
                input) {
            var line = Line.fromInput(lineInput);
            lines.add(line);

            if (line.x1 < minX)
                minX = line.x1;
            if (line.x2 < minX)
                minX = line.x2;

            if (line.x1 > maxX)
                maxX = line.x1;
            if (line.x2 > maxX)
                maxX = line.x2;

            if (line.y1 < minY)
                minY = line.y1;
            if (line.y2 < minY)
                minY = line.y2;

            if (line.y1 > maxY)
                maxY = line.y1;
            if (line.y2 > maxY)
                maxY = line.y2;
        }

        var bounds = new Bounds(minX, minY, maxX, maxY);

        Day5.part1(lines, bounds);
        Day5.part2(lines, bounds);
    }

    static void part1(List<Line> lines, Bounds bounds) {
        int pointsWithAtLeastTwoLinesPassing = 0;

        for (int x = bounds.minX; x <= bounds.maxX; x++) {
            yLoop:
            for (int y = bounds.minY; y <= bounds.maxY; y++) {
                int linesPassingThrough = 0;

                for (Line line : lines) {
                    if ((line.isHorizontal || line.isVertical) && line.passesThrough(x, y)) {
                        linesPassingThrough++;
                    }

                    if (linesPassingThrough == 2) {
                        pointsWithAtLeastTwoLinesPassing++;
                        continue yLoop;
                    }
                }
            }
        }

        System.out.printf("Answer to part 1: %d\n", pointsWithAtLeastTwoLinesPassing);
    }

    static void part2(List<Line> lines, Bounds bounds) {
        int pointsWithAtLeastTwoLinesPassing = 0;

        for (int y = bounds.minY; y <= bounds.maxY; y++) {
            xLoop:
            for (int x = bounds.minX; x <= bounds.maxX; x++) {
                int linesPassingThrough = 0;

                for (Line line : lines) {
                    if (line.passesThrough(x, y)) {
                        linesPassingThrough++;
                    }

                    if (linesPassingThrough == 2) {
                        pointsWithAtLeastTwoLinesPassing++;
                        continue xLoop;
                    }
                }
            }
        }

        System.out.printf("Answer to part 2: %d\n", pointsWithAtLeastTwoLinesPassing);
    }
}

class Bounds {
    int minX, minY, maxX, maxY;

    public Bounds(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public boolean contains(int x, int y) {
        return (x >= this.minX && x <= this.maxX) && (y >= this.minY && y <= this.maxY);
    }
}

class Line {
    public int x1, y1, x2, y2;
    int m = 0, c = 0;
    public boolean isVertical, isHorizontal;
    Bounds bounds;

    public static Line fromInput(String input) {
        String[] pointsInput = input.split(" -> ");
        String[] point1Coords = pointsInput[0].split(",");
        String[] point2Coords = pointsInput[1].split(",");

        return new Line(
                Integer.parseInt(point1Coords[0]), Integer.parseInt(point1Coords[1]),
                Integer.parseInt(point2Coords[0]), Integer.parseInt(point2Coords[1])
        );
    }


    public Line(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        this.isVertical = x1 == x2;
        this.isHorizontal = y1 == y2;

        if (!this.isVertical && !this.isHorizontal) {
            this.m = (y2 - y1) / (x2 - x1);
            this.c = y1 - this.m * x1;
        }

        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);

        this.bounds = new Bounds(minX, minY, maxX, maxY);
    }

    public boolean passesThrough(int x, int y) {
        if (!this.bounds.contains(x, y))
            return false;

        if (this.isVertical)
            return x == this.x1;
        if (this.isHorizontal)
            return y == this.y1;

        return y == this.m * x + this.c;
    }

    @Override
    public String toString() {
        if (this.isHorizontal) {
            return String.format("y = %d", this.y1);
        } else if (this.isVertical) {
            return String.format("x = %d", this.x1);
        } else {
            return String.format("y = %dx + %df", this.m, this.c);
        }
    }
}