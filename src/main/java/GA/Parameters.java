package GA;

import java.util.List;

public class Parameters {
    public static int noOfNodes;
    static double finalPathLength = Double.MAX_VALUE;
    static List<Integer> bestPath;
    static Graph graph;
    static int populationSize = 200;
    static int generations = 2000;
    static double mutationProbability = 0.01;
    static double hypermutationProbability = 0.1;
    static int maxStagnationUntilHypermutation = 50;
    static double crossoverProbability = 0.95;
    static double selectionPressureAdjustment = 0.2;
    static int elitism = 4;
    static int maxStagnationUntilSA = 100;
    static double extinctProbability = 0.7644;

    static boolean Elitism = true;
    static boolean ReverseElitism = true;
    static boolean Stagnation = true;
    static boolean Hypermutation = true;


    static String fileName = "berlin52.tsp";
}
