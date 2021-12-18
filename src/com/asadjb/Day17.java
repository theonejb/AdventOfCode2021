package com.asadjb;

public class Day17 {
    static class Range {
        int start, end;

        public Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        boolean inRange(int value) {
            int start = min(),
                    end = max();

            return value >= start && value <= end;
        }

        int min() {
            return Math.min(this.start, this.end);
        }

        int max() {
            return Math.max(this.start, this.end);
        }
    }

    static class Probe {
        int x = 0, y = 0;
        int dx, dy;

        public Probe(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        void step() {
            x += dx;
            y += dy;

            if (dx < 0) dx += 1;
            else if (dx > 0) dx -= 1;

            dy -= 1;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", x, y);
        }
    }

    public static void main() {
//        Range xTarget = new Range(20, 30),
//                yTarget = new Range(-10, -5);
        Range xTarget = new Range(236, 262),
                yTarget = new Range(-78, -58);

        part1(xTarget, yTarget);
        part2(xTarget, yTarget);
    }

    static boolean probeOvershotTargetArea(Range xTarget, Range yTarget, Probe probe) {
        return probe.x > xTarget.max() || probe.y < yTarget.min();
    }

    static boolean probeInTargetArea(Range xTarget, Range yTarget, Probe probe) {
        return xTarget.inRange(probe.x) && yTarget.inRange(probe.y);
    }

    static void part1(Range xTarget, Range yTarget) {
        int highestY = Integer.MIN_VALUE;

        for (int dx = 0; dx < 5000; dx++) {
            for (int dy = 0; dy < 5000; dy++) {
                var probe = new Probe(dx, dy);
                int highestYForProbe = Integer.MIN_VALUE;

                while (!probeOvershotTargetArea(xTarget, yTarget, probe)) {
                    probe.step();
                    highestYForProbe = Math.max(highestYForProbe, probe.y);

                    if (probeInTargetArea(xTarget, yTarget, probe)) {
                        highestY = Math.max(highestY, highestYForProbe);
                        break;
                    }
                }
            }
        }

        System.out.printf("Answer to part 1: %d\n", highestY);
    }

    static void part2(Range xTarget, Range yTarget) {
        int shotsOnTarget = 0;

        for (int dx = 0; dx < 500; dx++) {
            for (int dy = -1000; dy < 1000; dy++) {
                var probe = new Probe(dx, dy);

                while (!probeOvershotTargetArea(xTarget, yTarget, probe)) {
                    probe.step();

                    if (probeInTargetArea(xTarget, yTarget, probe)) {
                        shotsOnTarget++;
                        break;
                    }
                }
            }
        }

        System.out.printf("Answer to part 2: %d\n", shotsOnTarget);
    }
}
