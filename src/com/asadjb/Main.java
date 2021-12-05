package com.asadjb;

public class Main {

    public static void main(String[] args) {
        switch (args[0].toLowerCase()) {
            case "day1":
                Day1.main();
                break;
            case "day2":
                Day2.main();
                break;
            case "day3":
                Day3.main();
                break;
        }
    }
}
