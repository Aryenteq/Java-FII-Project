package GA;

import java.util.List;

public class Parameters {
    public static int nodesPerVehicle = 10;
    static double finalPathLength = Double.MAX_VALUE;
    static List<Integer> bestPath;
    static int populationSize = 300;
    static int generations = 1000;
    static double mutationProbability = 0.001;
    static double hyperMutationProbability = 0.1;
    static double crossoverProbability = 0.8;
    static int maxStagnationUntilAdaptiveMutation = 30;
    static int maxStagnationUntilHyperMutation = 50;
    static int maxStagnationUntilWisdom = 100;
    static int maxStagnationUntil2Opt = 150;


    static int elitism = 5 * populationSize / 100;
    static boolean ReverseElitism = false;
    static boolean HyperMutation = false;

    public static String fileName = "berlin52.tsp";
    // public static String fileName = "rat783.tsp";
    public static boolean useGraph4j = false;

    public static void setNodesPerVehicle(int nodesPerVehicle) {
        Parameters.nodesPerVehicle = nodesPerVehicle;
    }

    public static void setPopulationSize(int populationSize) {
        Parameters.populationSize = populationSize;
    }

    public static void setGenerations(int generations) {
        Parameters.generations = generations;
    }

    public static void setMutationProbability(double mutationProbability) {
        Parameters.mutationProbability = mutationProbability;
    }

    public static void setHyperMutationProbability(double hyperMutationProbability) {
        Parameters.hyperMutationProbability = hyperMutationProbability;
    }

    public static void setCrossoverProbability(double crossoverProbability) {
        Parameters.crossoverProbability = crossoverProbability;
    }

    public static void setElitism(int elitism) {
        Parameters.elitism = elitism;
    }

    public static void setMaxStagnationUntilAdaptiveMutation(int maxStagnationUntilAdaptiveMutation) {
        Parameters.maxStagnationUntilAdaptiveMutation = maxStagnationUntilAdaptiveMutation;
    }

    public static void setMaxStagnationUntilHyperMutation(int maxStagnationUntilHyperMutation) {
        Parameters.maxStagnationUntilHyperMutation = maxStagnationUntilHyperMutation;
    }

    public static void setMaxStagnationUntilWisdom(int maxStagnationUntilWisdom) {
        Parameters.maxStagnationUntilWisdom = maxStagnationUntilWisdom;
    }

    public static void setMaxStagnationUntil2Opt(int maxStagnationUntil2Opt) {
        Parameters.maxStagnationUntil2Opt = maxStagnationUntil2Opt;
    }

    public static void setReverseElitism(boolean reverseElitism) {
        ReverseElitism = reverseElitism;
    }

    public static void setHyperMutation(boolean hyperMutation) {
        HyperMutation = hyperMutation;
    }

    public static void setFileName(String fileName) {
        Parameters.fileName = fileName;
    }
}
