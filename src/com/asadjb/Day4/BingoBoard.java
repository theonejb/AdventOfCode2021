package com.asadjb.Day4;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class BingoBoard {
    int[][] board;
    boolean[][] marked;
    HashSet<Integer> availableNumbers;
    int lastDrawnNumber = -1;

    public BingoBoard(List<String> input) {
        this.board = new int[5][5];
        this.marked = new boolean[5][5];
        this.availableNumbers = new HashSet<>();

        int rowIndex = 0;
        for (String row :
                input) {
            String[] cols = row.split("\s+");
            for (int i = 0; i < 5; i++) {
                int number = Integer.parseInt(cols[i]);
                this.board[rowIndex][i] = number;
                this.marked[rowIndex][i] = false;
                this.availableNumbers.add(number);
            }
            rowIndex++;
        }
    }

    public boolean checkIfWon() {
        for (int row = 0; row < 5; row++) {
            boolean allMarked = true;
            for (int col = 0; col < 5; col++) {
                if (!this.marked[row][col]) {
                    allMarked = false;
                    break;
                }
            }

            if (allMarked) {
                return true;
            }
        }

        for (int col = 0; col < 5; col++) {
            boolean allMarked = true;
            for (int row = 0; row < 5; row++) {
                if (!this.marked[row][col]) {
                    allMarked = false;
                    break;
                }
            }

            if (allMarked) {
                return true;
            }
        }

        return false;
    }

    public void handleDrawnNumber(int drawnNumber) {
        if (this.availableNumbers.contains(drawnNumber)) {
            this.lastDrawnNumber = drawnNumber;

            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {
                    if (this.board[row][col] == drawnNumber) {
                        this.marked[row][col] = true;
                        return;
                    }
                }
            }
        }
    }

    public void clear() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                this.marked[row][col] = false;
            }
        }
    }

    public int score() {
        int sumOfUnmarkedNumbers = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (!this.marked[row][col]) {
                    sumOfUnmarkedNumbers += this.board[row][col];
                }
            }
        }

        return sumOfUnmarkedNumbers * this.lastDrawnNumber;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (this.marked[row][col]) {
                    sb.append(String.format("*%02d*", this.board[row][col]));
                } else {
                    sb.append(String.format(" %2d ", this.board[row][col]));
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
