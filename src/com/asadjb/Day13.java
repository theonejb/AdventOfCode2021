package com.asadjb;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Day13 {
    static class DotLocation {
        int x, y;

        public DotLocation(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + x;
            result = prime * result + y;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            DotLocation other = (DotLocation) obj;
            return this.x == other.x && this.y == other.y;
        }

        static DotLocation fromInput(String input) {
            String[] coords = input.split(",");

            var x = Integer.parseInt(coords[0]);
            var y = Integer.parseInt(coords[1]);
            return new DotLocation(x, y);
        }

        DotLocation fold(FoldingInstruction instruction) {
            int newX = this.x, newY = this.y;

            switch (instruction.foldAcross) {
                case X -> {
                    if (this.x > instruction.lineCoordinate) {
                        newX = (this.x - instruction.lineCoordinate) * -1 + instruction.lineCoordinate;
                    }
                }
                case Y -> {
                    if (this.y > instruction.lineCoordinate) {
                        newY = (this.y - instruction.lineCoordinate) * -1 + instruction.lineCoordinate;
                    }
                }
            }

            return new DotLocation(newX, newY);
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", this.x, this.y);
        }
    }

    static class FoldingInstruction {
        enum FoldAcross {X, Y}

        FoldAcross foldAcross;
        int lineCoordinate;

        public FoldingInstruction(FoldAcross foldAcross, int lineCoordinate) {
            this.foldAcross = foldAcross;
            this.lineCoordinate = lineCoordinate;
        }

        static FoldingInstruction fromInput(String input) {
            String[] parts = input.split("=");
            char axis = parts[0].charAt(11);
            int coordinates = Integer.parseInt(parts[1]);

            return new FoldingInstruction(axis == 'x' ? FoldAcross.X : FoldAcross.Y, coordinates);
        }

        @Override
        public String toString() {
            return String.format("%s=%d", this.foldAcross, this.lineCoordinate);
        }
    }

    static void main() {
        List<String> input = Utils.readInput("./day13.txt");
        List<DotLocation> dotLocations = new ArrayList<>();
        List<FoldingInstruction> foldingInstructions = new ArrayList<>();

        boolean locationsRead = false;
        for (int i = 0; i < input.size(); i++) {
            String line = input.get(i);
            if (line.isEmpty()) {
                locationsRead = true;
                continue;
            }

            if (!locationsRead) dotLocations.add(DotLocation.fromInput(line));
            else foldingInstructions.add(FoldingInstruction.fromInput(line));
        }

        part1(dotLocations, foldingInstructions);
        part2(dotLocations, foldingInstructions);
    }

    static void part1(List<DotLocation> dots, List<FoldingInstruction> folds) {
        var ins = folds.get(0);
        HashSet<DotLocation> foldedDots = new HashSet<>();

        dots.forEach((dot) -> {
            foldedDots.add(dot.fold(ins));
        });

        System.out.printf("Answer to part 1: %d\n", foldedDots.size());
    }

    static void part2(List<DotLocation> dots, List<FoldingInstruction> folds) {
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (var dot :
                dots) {
            if (dot.x > maxX) maxX = dot.x;
            if (dot.y > maxY) maxY = dot.y;
        }

        for (var fold : folds) {
            List<DotLocation> foldedDots = new ArrayList<>();
            for (var dot :
                    dots) {
                foldedDots.add(dot.fold(fold));
            }
            dots = foldedDots;
        }

        saveDotsImage(dots, maxX, maxY, "dots.bmp");
    }

    static void saveDotsImage(List<DotLocation> dots, int maxX, int maxY, String fname) {
        HashSet<DotLocation> dotsLookup = new HashSet<>();
        dotsLookup.addAll(dots);

        BufferedImage img = new BufferedImage(maxX + 1, maxY + 1, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                var d = new DotLocation(x, y);
                if (dotsLookup.contains(d)) img.setRGB(x, y, Color.RED.getRGB());
                else img.setRGB(x, y, Color.WHITE.getRGB());
            }
        }

        try {
            ImageIO.write((RenderedImage) img, "bmp", new File("./" + fname));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
