package GA;

import java.util.List;

public class Parameters {
    public static int nodesPerVehicle = 1000;
    static double finalPathLength = Double.MAX_VALUE;
    static List<Integer> bestPath;
    static int populationSize = 300;
    static int generations = 1000;
    static double mutationProbability = 0.001;
    static double hyperMutationProbability = 0.1;
    static double crossoverProbability = 0.8;
    static int elitism = 5 * populationSize / 100;
    static int maxStagnationUntilAdaptiveMutation = 30;
    static int maxStagnationUntilHyperMutation = 50;
    static int maxStagnationUntilWisdom = 100;
    static int maxStagnationUntil2Opt = 150;


    static boolean Elitism = true;
    static boolean ReverseElitism = true;
    static boolean HyperMutation = true;

    public static String fileName = "berlin52.tsp";
    // public static String fileName = "rat783.tsp";


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

    public static void setElitism(boolean elitism) {
        Elitism = elitism;
    }

    public static void setReverseElitism(boolean reverseElitism) {
        ReverseElitism = reverseElitism;
    }

    public static void setHyperMutation(boolean hyperMutation) {
        HyperMutation = hyperMutation;
    }
    // 14 parametrii
    public static void setFileName(String fileName) {
        Parameters.fileName = fileName;
    }

    public static double getCrossoverProbability() {
        return crossoverProbability;
    }

    public static double getFinalPathLength() {
        return finalPathLength;
    }

    public static double getHyperMutationProbability() {
        return hyperMutationProbability;
    }

    public static double getMutationProbability() {
        return mutationProbability;
    }

    public static int getElitism() {
        return elitism;
    }

    public static int getGenerations() {
        return generations;
    }

    public static int getMaxStagnationUntil2Opt() {
        return maxStagnationUntil2Opt;
    }

    public static int getMaxStagnationUntilAdaptiveMutation() {
        return maxStagnationUntilAdaptiveMutation;
    }

    public static int getMaxStagnationUntilHyperMutation() {
        return maxStagnationUntilHyperMutation;
    }

    public static int getMaxStagnationUntilWisdom() {
        return maxStagnationUntilWisdom;
    }

    public static int getNodesPerVehicle() {
        return nodesPerVehicle;
    }

    public static int getPopulationSize() {
        return populationSize;
    }

    public static boolean isReverseElitism() {
        return ReverseElitism;
    }

    public static boolean isHyperMutation() {
        return HyperMutation;
    }
}
