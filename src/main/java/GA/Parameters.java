package GA;

import java.util.List;

public class Parameters {
    public static int noOfVehicles;
    public static int nodesPerVehicle = 10;
    static double finalPathLength = Double.MAX_VALUE;
    static List<Integer> bestPath;
    static int populationSize = 500;
    static int generations = 800;
    static double mutationProbability = 0.001;
    static double hypermutationProbability = 0.1;
    static double crossoverProbability = 0.8;
    static int elitism = 5 * populationSize / 100;
    static int maxStagnationUntilHypermutation = 50;
    static int maxStagnationUntilWisdom = 100;
    static int maxStagnationUntilAdaptiveMutation = 50;
    static int maxStagnationUntil2Opt = 150;


    static boolean Elitism = true;
    static boolean ReverseElitism = true;
    static boolean Hypermutation = true;

    public static String fileName = "berlin52.tsp";
    //static String fileName = "rat783.tsp";
}
