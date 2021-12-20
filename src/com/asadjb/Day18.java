package com.asadjb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day18 {
    static abstract class SnailfishNumber {
        SnailfishNumberPair parent;
        boolean highlight = false;

        abstract int getValue();
    }

    static class SnailfishNumberRegular extends SnailfishNumber {
        int value;

        public SnailfishNumberRegular(int value) {
            this.value = value;
        }

        int getValue() {
            return value;
        }

        SnailfishNumberPair split() {
            return new SnailfishNumberPair(
                    new SnailfishNumberRegular(Math.floorDiv(this.value, 2)),
                    new SnailfishNumberRegular((int) Math.ceil(this.value / 2.0))
            );
        }

        @Override
        public String toString() {
            String representation = String.format("%d", value);
            if (highlight) {
                highlight = false;
                return Ansi.Red.colorize(representation);
            }
            else return representation;
        }
    }

    static class SnailfishNumberPair extends SnailfishNumber {
        @Override
        int getValue() {
            return 3 * this.left.getValue() + 2 * this.right.getValue();
        }

        enum Side {LEFT, RIGHT}

        static int colorCounter = -1;

        Ansi myColor;
        SnailfishNumber left, right;

        public SnailfishNumberPair(SnailfishNumber left, SnailfishNumber right) {
            this.left = left;
            this.left.parent = this;

            this.right = right;
            this.right.parent = this;

            myColor = randomColor();
        }

        static Ansi randomColor() {
            class NopAnsi extends Ansi {
                @Override
                public String colorize(String original) {
                    return original;
                }
            }

//            return new NopAnsi();

            colorCounter = (colorCounter + 1) % 7 + 1;

            switch (colorCounter) {
                case 1:
                    return Ansi.Red;
                case 2:
                    return Ansi.Green;
                case 3:
                    return Ansi.Yellow;
                case 4:
                    return Ansi.Blue;
                case 5:
                    return Ansi.Magenta;
                case 6:
                    return Ansi.Cyan;
                case 7:
                    return Ansi.White;
                default:
                    return Ansi.Black;
            }
        }

        @Override
        public String toString() {
            String representation = String.format("%s, %s", left, right);
            representation = myColor.colorize("[") + representation + myColor.colorize("]");

            if (highlight) {
                highlight = false;
                return Ansi.Red.colorize(representation);
            }
            else return representation;
        }

        SnailfishNumberRegular leafOnSide(Side side) {
            switch (side) {
                case LEFT -> {
                    if (this.left instanceof SnailfishNumberRegular)
                        return (SnailfishNumberRegular) this.left;
                    else
                        return ((SnailfishNumberPair) this.left).leafOnSide(Side.LEFT);
                }
                case RIGHT -> {
                    if (this.right instanceof SnailfishNumberRegular)
                        return (SnailfishNumberRegular) this.right;
                    else
                        return ((SnailfishNumberPair) this.right).leafOnSide(Side.RIGHT);
                }
            }

            throw new RuntimeException();
        }

        SnailfishNumberRegular findFirstNumberToSide(Side side, SnailfishNumber caller) {
            switch (side) {
                case LEFT -> {
                    if (this.left == caller) {
                        if (this.parent == null) return null;

                        return this.parent.findFirstNumberToSide(side, this);
                    } else {
                        if (this.left instanceof SnailfishNumberRegular)
                            return (SnailfishNumberRegular) this.left;
                        else {
                            // After we've found a parent that has the 'other' side, we want to find the leaf on the
                            // side closest to us, which is Right when we are searching for something on the left of us.
                            return ((SnailfishNumberPair) this.left).leafOnSide(Side.RIGHT);
                        }
                    }
                }
                case RIGHT -> {
                    if (this.right == caller) {
                        if (this.parent == null) return null;

                        return this.parent.findFirstNumberToSide(side, this);
                    } else {
                        if (this.right instanceof SnailfishNumberRegular)
                            return (SnailfishNumberRegular) this.right;
                        else {
                            // After we've found a parent that has the 'other' side, we want to find the leaf on the
                            // side closest to us, which is Left when we are searching for something on the right of us.
                            return ((SnailfishNumberPair) this.right).leafOnSide(Side.LEFT);
                        }
                    }
                }
            }

            throw new RuntimeException();
        }

        void addAllNumbersToList(List<SnailfishNumber> list) {
            list.add(this);

            if (this.left instanceof SnailfishNumberRegular)
                list.add(this.left);
            else
                ((SnailfishNumberPair) this.left).addAllNumbersToList(list);

            if (this.right instanceof SnailfishNumberRegular)
                list.add(this.right);
            else
                ((SnailfishNumberPair) this.right).addAllNumbersToList(list);
        }

        List<SnailfishNumber> getAllNumbers() {
            List<SnailfishNumber> list = new ArrayList<>();
            addAllNumbersToList(list);

            return list;
        }

        SnailfishNumberPair add(SnailfishNumberPair other) {
            return new SnailfishNumberPair(this, other);
        }

        void replaceWith(SnailfishNumber caller, SnailfishNumber replacement) {
            if (this.left == caller) this.left = replacement;
            else if (this.right == caller) this.right = replacement;
        }

        boolean handleExplosions(int depth) {
            if (this.left instanceof SnailfishNumberPair)
                if (((SnailfishNumberPair) this.left).handleExplosions(depth + 1)) return true;

            if (this.right instanceof SnailfishNumberPair)
                if (((SnailfishNumberPair) this.right).handleExplosions(depth + 1)) return true;

            if (depth == 4) {
//                System.out.println(Ansi.Green.colorize("Explode: " + this.toString()));

                SnailfishNumberRegular leftNumber = this.parent.findFirstNumberToSide(Side.LEFT, this);
                SnailfishNumberRegular rightNumber = this.parent.findFirstNumberToSide(Side.RIGHT, this);

                if (leftNumber != null) leftNumber.value += ((SnailfishNumberRegular) this.left).value;
                if (rightNumber != null) rightNumber.value += ((SnailfishNumberRegular) this.right).value;

                var replacement = new SnailfishNumberRegular(0);
                replacement.highlight = true;
                this.parent.replaceWith(this, replacement);
                return true;
            }

            return false;
        }

        boolean handleSplits() {
            if (this.left instanceof SnailfishNumberPair)
                if (((SnailfishNumberPair) this.left).handleSplits()) return true;

            if (this.left instanceof SnailfishNumberRegular) {
                var leftNumber = (SnailfishNumberRegular) this.left;

                if (leftNumber.value >= 10) {
//                    System.out.println(Ansi.Green.colorize("Split: " + this.left.toString()));

                    this.left = leftNumber.split();
                    this.left.highlight = true;
                    this.left.parent = this;
                    return true;
                }
            }

            if (this.right instanceof SnailfishNumberPair)
                if (((SnailfishNumberPair) this.right).handleSplits()) return true;

            if (this.right instanceof SnailfishNumberRegular) {
                var rightNumber = (SnailfishNumberRegular) this.right;

                if (rightNumber.value >= 10) {
//                    System.out.println(Ansi.Green.colorize("Split: " + this.right.toString()));

                    this.right = rightNumber.split();
                    this.right.highlight = true;
                    this.right.parent = this;
                    return true;
                }
            }

            return false;
        }

        boolean reduce() {
            return this.handleExplosions(0) || this.handleSplits();
        }

    }

    static int findPairEndIndex(String input) {
        int opens = 1;
        for (int i = 1; i < input.length(); i++) {
            if (input.charAt(i) == ']')
                opens--;
            else if (input.charAt(i) == '[')
                opens++;

            if (opens == 0)
                return i + 1;
        }

        throw new RuntimeException();
    }

    static SnailfishNumberPair parsePair(String input) {
        int opens = 1;
        int commaIndex = -1;

        charLoop:
        for (int i = 1; i < input.length(); i++) {
            char c = input.charAt(i);

            switch (c) {
                case '[':
                    opens++;
                    break;
                case ']':
                    opens--;
                    break;
                case ',':
                    if (opens == 1) {
                        commaIndex = i;
                        break charLoop;
                    }
            }
        }

        String leftElement = input.substring(1, commaIndex),
                rightElement = input.substring(commaIndex + 1, input.length() - 1);

        return new SnailfishNumberPair(parseInput(leftElement), parseInput(rightElement));
    }

    static SnailfishNumber parseInput(String input) {
        if (input.charAt(0) != '[')
            return new SnailfishNumberRegular(Integer.parseInt(input));
        else {
            int endOfPair = findPairEndIndex(input);
            String pair = input.substring(0, endOfPair);
            return parsePair(pair);
        }
    }

    public static void main() {
        List<String> input = Utils.readInput("./day18.txt");
        part1(input);
        part2(input);
    }

    static void part1(List<String> input) {
        SnailfishNumberPair result = parsePair(input.get(0));

        for (int i = 1; i < input.size(); i++) {
            SnailfishNumberPair nextNumber = parsePair(input.get(i));
            result = result.add(nextNumber);
            while (result.reduce()) {}
        }

        System.out.printf("Answer to part 1: %s\n", result.getValue());
    }

    static void part2(List<String> input) {
        int maxMagnitude = Integer.MIN_VALUE, i = 0;
        for (var input1 :
                input) {
            for (var input2 :
                    input) {
                if (input1 == input2)
                    continue;

                SnailfishNumberPair number1 = parsePair(input1),
                        number2 = parsePair(input2);

                var sum = ((SnailfishNumberPair) number1).add((SnailfishNumberPair) number2);
                while (sum.reduce());
                var magnitude = sum.getValue();

                if (magnitude > maxMagnitude) {
                    maxMagnitude = magnitude;
                }

                i++;
                if (i % 100 == 0) System.out.println(i);
            }
        }

        System.out.printf("Answer to part 2: %d\n", maxMagnitude);
    }
}
