package GA;

import java.util.ArrayList;
import java.util.List;

public class Parameters {
    private static double avgPathLength = Double.MAX_VALUE;
    private static int nodesPerVehicle = 10;
    private static String fileName = "berlin52.tsp";
    private static boolean useGraph4j = false;
    private static boolean useDatabase = false;
    private static double bestPathLength = Double.MAX_VALUE;
    private static boolean bestPathChanged = false;
    private static List<Integer> bestPath = new ArrayList<Integer>();
    private static int populationSize = 300;
    private static int generations = 1000;
    private static int currentGeneration = 0;
    private static double mutationProbability = 0.001;
    private static double hyperMutationProbability = 0.1;
    private static double crossoverProbability = 0.8;
    private static int maxStagnationUntilAdaptiveMutation = 30;
    private static int maxStagnationUntilHyperMutation = 50;
    private static int maxStagnationUntilWisdom = 80;
    private static int maxStagnationUntil2Opt = 100;
    private static int elitism = 5 * populationSize / 100;
    private static boolean ReverseElitism = false;
    private static boolean HyperMutation = false;
    private static boolean done = false;

    public static synchronized void reset()
    {
        done = false;
        currentGeneration = 0;
        bestPath = new ArrayList<>();
        bestPathChanged = true;
        bestPathLength = Double.MAX_VALUE;

    }

    public static synchronized double getAvgPathLength() {
        return avgPathLength;
    }

    public static synchronized void setAvgPathLength(double avgPathLength) {
        Parameters.avgPathLength = avgPathLength;
    }

    public static synchronized double getBestPathLength() {
        return bestPathLength;
    }

    public static synchronized void setBestPathLength(double bestPathLength) {
        Parameters.bestPathLength = bestPathLength;
    }

    public static synchronized int getCurrentGeneration() {
        return currentGeneration;
    }

    public static synchronized void setCurrentGeneration(int currentGeneration) {
        Parameters.currentGeneration = currentGeneration;
    }

    public static synchronized String getFileName() {
        return fileName;
    }

    public static synchronized void setFileName(String fileName) {
        Parameters.fileName = fileName;
    }

    public static synchronized boolean isUseGraph4j() {
        return useGraph4j;
    }

    public static synchronized void setUseGraph4j(boolean useGraph4j) {
        Parameters.useGraph4j = useGraph4j;
    }

    public static synchronized boolean isUseDatabase() {
        return useDatabase;
    }

    public static synchronized void setUseDatabase(boolean useDatabase) {
        Parameters.useDatabase = useDatabase;
    }

    public static synchronized boolean isDone() {
        return done;
    }

    public static synchronized void setDone(boolean done) {
        Parameters.done = done;
    }

    public static synchronized boolean isBestPathChanged() {
        return bestPathChanged;
    }

    public static synchronized void setBestPathChanged(boolean bestPathChanged) {
        Parameters.bestPathChanged = bestPathChanged;
    }

    public static synchronized List<Integer> getBestPath() {
        return bestPath;
    }

    public static synchronized void setBestPath(List<Integer> bestPath) {
        Parameters.bestPath = bestPath;
    }

    public static synchronized double getCrossoverProbability() {
        return crossoverProbability;
    }

    public static synchronized void setCrossoverProbability(double crossoverProbability) {
        Parameters.crossoverProbability = crossoverProbability;
    }


    public static synchronized double getHyperMutationProbability() {
        return hyperMutationProbability;
    }

    public static synchronized void setHyperMutationProbability(double hyperMutationProbability) {
        Parameters.hyperMutationProbability = hyperMutationProbability;
    }

    public static synchronized double getMutationProbability() {
        return mutationProbability;
    }

    public static synchronized void setMutationProbability(double mutationProbability) {
        Parameters.mutationProbability = mutationProbability;
    }

    public static synchronized int getElitism() {
        return elitism;
    }

    public static synchronized void setElitism(int elitism) {
        Parameters.elitism = elitism;
    }

    public static synchronized int getGenerations() {
        return generations;
    }

    public static synchronized void setGenerations(int generations) {
        Parameters.generations = generations;
    }

    public static synchronized int getMaxStagnationUntil2Opt() {
        return maxStagnationUntil2Opt;
    }

    public static synchronized void setMaxStagnationUntil2Opt(int maxStagnationUntil2Opt) {
        Parameters.maxStagnationUntil2Opt = maxStagnationUntil2Opt;
    }

    public static synchronized int getMaxStagnationUntilAdaptiveMutation() {
        return maxStagnationUntilAdaptiveMutation;
    }

    public static synchronized void setMaxStagnationUntilAdaptiveMutation(int maxStagnationUntilAdaptiveMutation) {
        Parameters.maxStagnationUntilAdaptiveMutation = maxStagnationUntilAdaptiveMutation;
    }

    public static synchronized int getMaxStagnationUntilHyperMutation() {
        return maxStagnationUntilHyperMutation;
    }

    public static synchronized void setMaxStagnationUntilHyperMutation(int maxStagnationUntilHyperMutation) {
        Parameters.maxStagnationUntilHyperMutation = maxStagnationUntilHyperMutation;
    }

    public static synchronized int getMaxStagnationUntilWisdom() {
        return maxStagnationUntilWisdom;
    }

    public static synchronized void setMaxStagnationUntilWisdom(int maxStagnationUntilWisdom) {
        Parameters.maxStagnationUntilWisdom = maxStagnationUntilWisdom;
    }

    public static synchronized int getNodesPerVehicle() {
        return nodesPerVehicle;
    }

    public static synchronized void setNodesPerVehicle(int nodesPerVehicle) {
        Parameters.nodesPerVehicle = nodesPerVehicle;
    }

    public static synchronized int getPopulationSize() {
        return populationSize;
    }

    public static synchronized void setPopulationSize(int populationSize) {
        Parameters.populationSize = populationSize;
    }

    public static synchronized boolean isReverseElitism() {
        return ReverseElitism;
    }

    public static synchronized void setReverseElitism(boolean reverseElitism) {
        ReverseElitism = reverseElitism;
    }

    public static synchronized boolean isHyperMutation() {
        return HyperMutation;
    }

    public static synchronized void setHyperMutation(boolean hyperMutation) {
        HyperMutation = hyperMutation;
    }
}
