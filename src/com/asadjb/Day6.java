package com.asadjb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Day6 {
    public static void main() {
        List<String> input = Utils.readInput("./day6.txt");
        String[] statesInput = input.get(0).split(",");

        List<LanternFish> school = new ArrayList<>();
        for (String stateInput :
                statesInput) {
            int timer = Integer.parseInt(stateInput);
            school.add(new LanternFish(timer, 0));
        }

        Day6.part1(school);
        Day6.part2(school);
    }

    static long simulateSchoolGrowth(List<LanternFish> school, int endT) {
        long[] fishPerTimer = new long[9];
        long[] nextFishPerTimer = new long[9];

        for (int i = 0; i < fishPerTimer.length; i++) {
            fishPerTimer[i] = 0;
        }

        for (var fish :
                school) {
            fishPerTimer[fish.timer]++;
        }

        for (int timer = 0; timer < endT; timer++) {
            nextFishPerTimer[0] = fishPerTimer[1];
            nextFishPerTimer[1] = fishPerTimer[2];
            nextFishPerTimer[2] = fishPerTimer[3];
            nextFishPerTimer[3] = fishPerTimer[4];
            nextFishPerTimer[4] = fishPerTimer[5];
            nextFishPerTimer[5] = fishPerTimer[6];
            nextFishPerTimer[6] = fishPerTimer[7] + fishPerTimer[0];
            nextFishPerTimer[7] = fishPerTimer[8];
            nextFishPerTimer[8] = fishPerTimer[0];

            for (int i = 0; i < fishPerTimer.length; i++) {
                fishPerTimer[i] = nextFishPerTimer[i];
            }
        }

        long finalCount = 0;
        for (var fishCount :
                fishPerTimer) {
            finalCount += fishCount;
        }

        return finalCount;
    }

    static void part1(List<LanternFish> school) {
        System.out.printf("Answer for part 1: %d\n", Day6.simulateSchoolGrowth(school, 80));
    }

    static void part2(List<LanternFish> school) {
        System.out.printf("Answer for part 2: %d\n", Day6.simulateSchoolGrowth(school, 256));
    }
}

class LanternFish {
    int timer, birthTime;

    public LanternFish(int timer, int birthTime) {
        this.timer = timer;
        this.birthTime = birthTime;
    }

    public LanternFish(int birthTime) {
        this.timer = 8;
        this.birthTime = birthTime;
    }

    public boolean tick(int t) {
        if (t <= this.birthTime)
            return false;

        if (this.timer == 0) {
            this.timer = 6;
            return true;
        }

        this.timer--;
        return false;
    }

    @Override
    public String toString() {
        return String.format("%d", this.timer);
    }
}