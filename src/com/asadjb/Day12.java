package com.asadjb;

import java.util.*;

public class Day12 {
    static class CaveMap extends HashMap<String, List<String>> {
        class NoPathForward extends Exception {
        }

        static class CavePath {
            List<String> path;

            static CavePath sum(CavePath a, CavePath b) {
                var cp = new CavePath();
                cp.addCavePath(a);
                cp.addCavePath(b);

                return cp;
            }

            @Override
            public String toString() {
                return Arrays.toString(this.path.toArray());
            }

            public CavePath() {
                path = new ArrayList<>();
            }

            void addNextCave(String cave) {
                path.add(cave);
            }

            void addCavePath(CavePath other) {
                path.addAll(other.path);
            }

            CavePath addCavePathImmutable(CavePath other) {
                CavePath cp = new CavePath();
                cp.addCavePath(this);
                cp.addCavePath(other);
                return cp;
            }
        }

        static CaveMap fromInputEdgeList(List<String> inputs) {
            CaveMap map = new CaveMap();

            for (var edgeListInput :
                    inputs) {
                map.addEdgeFromInput(edgeListInput);
            }

            return map;
        }

        void addEdgeFromInput(String input) {
            String[] nodes = input.split("-");
            this.addEdge(nodes[0], nodes[1]);
            this.addEdge(nodes[1], nodes[0]);
        }

        void addEdge(String from, String to) {
            if (!this.containsKey(from))
                this.put(from, new ArrayList<>());

            this.get(from).add(to);
        }

        List<CavePath> findAllPaths(String from, String to, HashSet<String> visitedCaves, boolean canRepeatOnce) throws NoPathForward {
            var nextCaves = this.get(from);
            if (nextCaves == null) throw new NoPathForward();

            var visitableNextCaves = new ArrayList<String>();
            for (var cave :
                    nextCaves) {
                if (cave.equals("start")) continue;

                if (!visitedCaves.contains(cave) || canRepeatOnce) visitableNextCaves.add(cave);
            }

            var paths = new ArrayList<CavePath>();
            for (var nextCave :
                    visitableNextCaves) {

                var path = new CavePath();
                path.addNextCave(nextCave);

                boolean canDownstreamRepeatOnce = canRepeatOnce && !visitedCaves.contains(nextCave);

                var cavesVisitedForThisPath = (HashSet<String>) visitedCaves.clone();
                if (!nextCave.equals(nextCave.toUpperCase()))
                    cavesVisitedForThisPath.add(nextCave);

                if (!nextCave.equals(to)) {
                    try {
                        var downstreamPaths = this.findAllPaths(
                                nextCave, to, cavesVisitedForThisPath, canDownstreamRepeatOnce
                        );
                        for (var dsPath :
                                downstreamPaths) {
                            paths.add(path.addCavePathImmutable(dsPath));
                        }
                    } catch (NoPathForward e) {
                        continue;
                    }
                } else {
                    paths.add(path);
                }
            }

            if (paths.isEmpty()) throw new NoPathForward();
            return paths;
        }
    }

    static void main() {
        List<String> edgeListInput = Utils.readInput("./day12.txt");
        CaveMap map = CaveMap.fromInputEdgeList(edgeListInput);

        part1(map);
        part2(map);
    }

    static void part1(CaveMap map) {
        try {
            List<CaveMap.CavePath> paths = map.findAllPaths("start", "end", new HashSet<>(), false);
            System.out.printf("Answer to part 1: %d\n", paths.size());
        } catch (CaveMap.NoPathForward e) {
            System.out.println("No path found");
        }
    }

    static void part2(CaveMap map) {
        try {
            List<CaveMap.CavePath> paths = map.findAllPaths("start", "end", new HashSet<>(), true);
            System.out.printf("Answer to part 1: %d\n", paths.size());
        } catch (CaveMap.NoPathForward e) {
            System.out.println("No path found");
        }
    }
}
