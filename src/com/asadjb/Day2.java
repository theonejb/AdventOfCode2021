package com.asadjb;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Day2 {
    public static void main() {
        try (Stream<String> stream = Files.lines(Paths.get("./day2.txt"))) {
            var lines = stream.toList();

            Day2.part1(lines);
            Day2.part2(lines);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Instruction parseInstruction(String instruction) {
        String[] parts = instruction.split("\\s+");
        String directionString = parts[0];
        int units = Integer.parseInt(parts[1]);

        Direction direction;
        switch (directionString) {
            case "forward":
                direction = Direction.FORWARD;
                break;
            case "down":
                direction = Direction.DOWN;
                break;
            case "up":
                direction = Direction.UP;
                break;
            default:
                throw new RuntimeException("Unknown direction type");
        }

        return new Instruction(direction, units);
    }

    public static void part1(List<String> instructions) {
        var subPosition = new Position();
        for (String instructionString :
                instructions) {
            var instruction = parseInstruction(instructionString);
            subPosition.updateFromInstruction(instruction);
        }

        System.out.printf("Result for part 1: %d\n", subPosition.horizontal * subPosition.depth);
    }

    public static void part2(List<String> instructions) {
        var subPosition = new Position();
        for (String instructionString :
                instructions) {
            var instruction = parseInstruction(instructionString);
            subPosition.updateFromInstructionV2(instruction);
        }

        System.out.printf("Result for part 2: %d\n", subPosition.horizontal * subPosition.depth);
    }
}

enum Direction {
    UP, DOWN, FORWARD
}
class Instruction {
    Direction direction;
    int units;

    public Instruction(Direction direction, int units) {
        this.direction = direction;
        this.units = units;
    }

    @Override
    public String toString() {
        return String.format("%s %d", this.direction, this.units);
    }
}

class Position {
    int horizontal, depth, aim;

    public Position() {
        this.horizontal = 0;
        this.depth = 0;
        this.aim = 0;
    }

    public void updateFromInstruction(Instruction instruction) {
        switch (instruction.direction) {
            case UP:
                this.depth -= instruction.units;
                break;
            case DOWN:
                this.depth += instruction.units;
                break;
            case FORWARD:
                this.horizontal += instruction.units;
                break;
        }
    }

    public void updateFromInstructionV2(Instruction instruction) {
        switch (instruction.direction) {
            case UP:
                this.aim -= instruction.units;
                break;
            case DOWN:
                this.aim += instruction.units;
                break;
            case FORWARD:
                this.horizontal += instruction.units;
                this.depth += this.aim * instruction.units;
                break;
        }
    }

    @Override
    public String toString() {
        return String.format("H: %d D: %d", this.horizontal, this.depth);
    }
}