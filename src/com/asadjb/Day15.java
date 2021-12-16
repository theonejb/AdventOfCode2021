package com.asadjb;

import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.io.*;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import java.io.File;
import java.util.*;

public class Day15 {
    static class MyEdge extends DefaultWeightedEdge {
        String label() {
            return String.format("%d", (int)this.getWeight());
        }
    }

    static class Grid {
        static class Loc {
            int row, col;

            public Loc(int row, int col) {
                this.row = row;
                this.col = col;
            }

            @Override
            public String toString() {
                return String.format("(%d, %d)", this.row, this.col);
            }

            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + row;
                result = prime * result + col;
                return result;
            }

            @Override
            public boolean equals(Object obj) {
                Loc other = (Loc) obj;
                return this.col == other.col && this.row == other.row;
            }

            List<Loc> neighbours(int maxRows, int maxCols) {
                int[][] deltas = {
                        {-1, 0},
                        {0, 1},
                        {1, 0},
                        {0, -1}
                };

                List<Loc> neighbours = new ArrayList<>();
                for (int[] delta : deltas) {
                    int rowDelta = delta[0], colDelta = delta[1];
                    int row = this.row - rowDelta,
                            col = this.col - colDelta;
                    if (row < 0 || row >= maxRows) continue;
                    if (col < 0 || col >= maxCols) continue;

                    neighbours.add(new Loc(row, col));
                }

                return neighbours;
            }
        }

        int[][] grid;
        int nRows, nCols;

        public Grid(int[][] grid) {
            this.grid = grid;
            this.nRows = grid.length;
            this.nCols = grid[0].length;
        }

        static Grid fromInput(List<String> input) {
            int nRows = input.size(), nCols = input.get(0).length();
            int[][] grid = new int[nRows][nCols];

            int row = 0;
            for (String rowInput : input) {
                int col = 0;
                for (char colInput : rowInput.toCharArray()) {
                    grid[row][col++] = Integer.parseInt(colInput + "");
                }

                row++;
            }

            return new Grid(grid);
        }

        List<Loc> allLocations() {
            List<Loc> locs = new ArrayList<>();

            for (int row = 0; row < nRows; row++) {
                for (int col = 0; col < nCols; col++) {
                    locs.add(new Loc(row, col));
                }
            }

            return locs;
        }

        int riskAt(Loc loc) {
            return grid[loc.row][loc.col];
        }

        Graph<Loc, DefaultWeightedEdge> toGraph() {
            Graph<Loc, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(MyEdge.class);

            var allLocs = allLocations();
            allLocs.forEach((graph::addVertex));

            for (Loc loc : allLocs) {
                var neighbours = loc.neighbours(nRows, nCols);
                for (Loc neighbour : neighbours) {
                    int risk = riskAt(neighbour);
                    MyEdge edge = new MyEdge();
                    graph.addEdge(loc, neighbour, edge);
                    graph.setEdgeWeight(loc, neighbour, risk);
                }
            }
            return graph;
        }
    }

    static void printGraph(Graph<Grid.Loc, DefaultWeightedEdge> graph) {
        DOTExporter<Grid.Loc, DefaultWeightedEdge> exporter = new DOTExporter<>();
        exporter.setVertexAttributeProvider((v) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("label", DefaultAttribute.createAttribute(v.toString()));
            return map;
        });
        exporter.setEdgeAttributeProvider(e -> {
            var edge = (MyEdge) e;
            Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("label", DefaultAttribute.createAttribute(edge.label()));
            return map;
        });

        var of = new File("./graph.dot");
        exporter.exportGraph(graph, of);
    }

    static void main() {
        List<String> input = Utils.readInput("./day15.txt");
        Grid grid = Grid.fromInput(input);

        part1(grid);
    }

    static void part1(Grid grid) {
        Graph<Grid.Loc, DefaultWeightedEdge> graph = grid.toGraph();
        Grid.Loc start = new Grid.Loc(0, 0),
                end = new Grid.Loc(grid.nRows - 1, grid.nCols - 1);
        var path = DijkstraShortestPath.findPathBetween(graph, start, end);

        var edges = path.getVertexList();
        var weight = path.getWeight() + grid.riskAt(end);

        System.out.println(Arrays.toString(edges.toArray()));
        System.out.println(weight);
    }
}
