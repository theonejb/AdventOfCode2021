package com.asadjb.Day4;

import com.asadjb.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class Day4 {
    public static void main() {
        List<String> input = Utils.readInput("./day4.txt");

        String drawnNumbersInput = input.get(0);
        List<Integer> drawnNumbers = new ArrayList<>();
        for (String numberString :
                drawnNumbersInput.split(",")) {
            drawnNumbers.add(Integer.parseInt(numberString));
        }

        List<BingoBoard> boards = new ArrayList<>();

        List<String> boardsInput = input.subList(2, input.size());
        List<String> currentBoardInput = new ArrayList<>();
        for (String line :
                boardsInput) {
            line = line.trim();
            if (line.isEmpty()) {
                boards.add(new BingoBoard(currentBoardInput));
                currentBoardInput.clear();
            } else {
                currentBoardInput.add(line);
            }
        }
        boards.add(new BingoBoard(currentBoardInput));

        Day4.part1(boards, drawnNumbers);

        for (BingoBoard board :
                boards) {
            board.clear();
        }

        Day4.part2(boards, drawnNumbers);
    }

    static void part1(List<BingoBoard> boards, List<Integer> drawnNumbers) {
        for (int drawnNumber :
                drawnNumbers) {
            for (BingoBoard board :
                    boards) {
                board.handleDrawnNumber(drawnNumber);
                if (board.checkIfWon()) {
                    System.out.printf("Answer to part 1: %d\n", board.score());
                    return;
                }
            }
        }
    }

    static void part2(List<BingoBoard> boards, List<Integer> drawnNumbers) {
        int boardsLeftToWin = boards.size();

        for (int drawnNumber :
                drawnNumbers) {
            for (BingoBoard board :
                    boards) {
                if (board.checkIfWon()) {
                    continue;
                }

                board.handleDrawnNumber(drawnNumber);
                if (board.checkIfWon()) {
                    boardsLeftToWin--;

                    if (boardsLeftToWin == 0) {
                        System.out.printf("Answer to part 2: %d\n", board.score());
                        return;
                    }
                }
            }
        }
    }
}
