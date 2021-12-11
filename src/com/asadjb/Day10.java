package com.asadjb;

import java.util.*;

public class Day10 {
    static void main() {
        List<String> input = Utils.readInput("./day10.txt");
        part1(input);
        part2(input);
    }

    static boolean isOpeningCharacter(char c) {
        return c == '(' || c == '[' || c == '{' || c == '<';
    }

    static boolean doesPairMatch(char opening, char closing) {
        switch (opening) {
            case '(':
                return closing == ')';
            case '[':
                return closing == ']';
            case '{':
                return closing == '}';
            case '<':
                return closing == '>';
            default:
                throw new RuntimeException("This is not a valid closing char.");
        }
    }

    static Character findInvalidCharacter(String chunks) {
        Stack<Character> openingCharacters = new Stack<>();

        for (char c :
                chunks.toCharArray()) {
            if (isOpeningCharacter(c)) {
                openingCharacters.push(c);
            } else {
                if (doesPairMatch(openingCharacters.pop(), c)) {
                    continue;
                } else {
                    return c;
                }
            }
        }

        return null;
    }

    static int invalidCharScore(char closingChar) {
        switch (closingChar) {
            case ')':
                return 3;
            case ']':
                return 57;
            case '}':
                return 1197;
            case '>':
                return 25137;
            default:
                throw new RuntimeException("This is not a valid closing char.");
        }
    }

    static void part1(List<String> input) {
        int syntaxScore = 0;

        for (String line :
                input) {
            Character invalidChar = findInvalidCharacter(line);
            if (invalidChar != null) {
                syntaxScore += invalidCharScore(invalidChar);
            }
        }

        System.out.printf("Answer for part 1: %d\n", syntaxScore);
    }

    static boolean isInvalidLine(String line) {
        return findInvalidCharacter(line) != null;
    }

    static long completeAndReturnScore(String line) {
        Stack<Character> openingCharacters = new Stack<>();

        for (char c :
                line.toCharArray()) {
            if (isOpeningCharacter(c))
                openingCharacters.push(c);
            else
                openingCharacters.pop();
        }

        long score = 0;
        while (!openingCharacters.isEmpty()) {
            char openingChar = openingCharacters.pop();

            score *= 5;
            switch (openingChar) {
                case '(':
                    score += 1;
                    break;
                case '[':
                    score += 2;
                    break;
                case '{':
                    score += 3;
                    break;
                case '<':
                    score += 4;
                    break;
            }
        }

        return score;
    }

    static void part2(List<String> input) {
        List<Long> completionScores = new ArrayList<>();

        for (String line :
                input) {
            if (isInvalidLine(line)) continue;

            long score = completeAndReturnScore(line);
            completionScores.add(score);
        }

        Collections.sort(completionScores);
        long midScore = completionScores.get(((completionScores.size()+1) / 2) - 1);
        System.out.printf("Answer to part 2: %d\n", midScore);
    }
}
