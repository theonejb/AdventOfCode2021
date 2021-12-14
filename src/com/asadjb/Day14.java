package com.asadjb;

import java.util.*;

public class Day14 {
    static class Polymer {
        String chain;

        public Polymer(String chain) {
            this.chain = chain;
        }

        List<String> get2Grams() {
            List<String> grams = new ArrayList<>();
            for (int i = 0; i < this.chain.length() - 1; i++) {
                grams.add(this.chain.substring(i, i + 2));
            }

            return grams;
        }

        HashMap<Character, Integer> elementCounts() {
            HashMap<Character, Integer> counter = new HashMap<>();
            for (var c :
                    this.chain.toCharArray()) {
                if (!counter.containsKey(c)) counter.put(c, 0);

                counter.put(c, counter.get(c) + 1);
            }

            return counter;
        }
    }

    static void main() {
        List<String> inputs = Utils.readInput("./day14.txt");

        Polymer template = new Polymer(inputs.get(0));
        HashMap<String, Character> rules = new HashMap<>();
        for (String ruleInput :
                inputs.subList(2, inputs.size())) {
            String[] parts = ruleInput.split(" -> ");
            rules.put(parts[0], parts[1].charAt(0));
        }

        part1(template, rules);
        part2(template, rules);
    }

    static void part1(Polymer template, HashMap<String, Character> rules) {
        Polymer p = template;

        for (int i = 0; i < 10; i++) {
            List<String> grams = p.get2Grams();
            List<String> newElements = new ArrayList<>();

            for (String gram :
                    grams) {
                newElements.add(rules.get(gram).toString());
            }

            StringBuilder sb = new StringBuilder();

            var newElIt = newElements.iterator();
            for (var c :
                    p.chain.toCharArray()) {
                sb.append(c).append(newElIt.hasNext() ? newElIt.next() : "");
            }

            p = new Polymer(sb.toString());
        }

        var counter = p.elementCounts();
        Integer[] values = counter.values().toArray(new Integer[0]);
        Arrays.sort(values);
        System.out.printf("Answer to part 1: %d\n", values[values.length - 1] - values[0]);
    }

    static <T> void incrementCounter(HashMap<T, Long> counter, T key) {
        counter.put(key, counter.getOrDefault(key, 0L) + 1);
    }

    static <T> void incrementCounterBy(HashMap<T, Long> counter, T key, Long inc) {
        counter.put(key, counter.getOrDefault(key, 0L) + inc);
    }

    static <T> void decrementCounterBy(HashMap<T, Long> counter, T key, Long dec) {
        counter.put(key, counter.get(key) - dec);
    }

    static void part2(Polymer template, HashMap<String, Character> rules) {
        HashMap<Character, Long> elementCounter = new HashMap<>();
        HashMap<String, Long> gramCounter = new HashMap<>();

        for (char c :
                template.chain.toCharArray()) {
            incrementCounter(elementCounter, c);
        }

        for (String gram :
                template.get2Grams()) {
            incrementCounter(gramCounter, gram);
        }


        for (int i = 0; i < 40; i++) {
            var nextGramCounter = (HashMap<String, Long>) gramCounter.clone();

            for (String gram :
                    gramCounter.keySet()) {
                Character newElement = rules.get(gram);
                if (newElement != null) {
                    String newGram1 = gram.substring(0, 1) + newElement;
                    String newGram2 = newElement + gram.substring(1, 2);

                    Long numberOfGrams = gramCounter.get(gram);
                    decrementCounterBy(nextGramCounter, gram, numberOfGrams);
                    incrementCounterBy(nextGramCounter, newGram1, numberOfGrams);
                    incrementCounterBy(nextGramCounter, newGram2, numberOfGrams);

                    incrementCounterBy(elementCounter, newElement, numberOfGrams);
                }
            }

            gramCounter = nextGramCounter;
        }

        Long[] values = elementCounter.values().toArray(new Long[0]);
        Arrays.sort(values);
        System.out.printf("Answer to part 2: %d\n", values[values.length - 1] - values[0]);
    }
}
