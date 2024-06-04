package GA;

import Data.CustomGraph;

import java.util.*;


public class VehicleRouting {

    public void run() {
        CustomGraph graph = new CustomGraph();
        GA(graph);
    }

    private void GA(CustomGraph graph) {
        //Parameters.setBestPath(new ArrayList<>());
        int noChange = 0;
        List<Candidate> population = new ArrayList<>(Parameters.getPopulationSize());
        for (int i = 0; i < Parameters.getPopulationSize(); i++) {
            Candidate candidate = new Candidate(graph);
            population.add(candidate);
        }

        double mutationRate = Parameters.getMutationProbability();
        for (int i = 0; i < Parameters.getGenerations(); i++) {
            // System.out.println("Generation " + i);

            Parameters.setCurrentGeneration(i);
            double oldFinalPathLength = Parameters.getBestPathLength();

            // Selection
            selection(population);

            // Mutation
            for (Candidate candidate : population) {
                candidate.mutate(mutationRate);
            }

            // Conditional Local Search
            if (noChange > Parameters.getMaxStagnationUntil2Opt()) {
                for (Candidate candidate : population) {
                    candidate.localSearch();
                }
            }

            // HyperMutation
            if (Parameters.isHyperMutation()) {
                if (noChange > Parameters.getMaxStagnationUntilHyperMutation()) {
                    for (Candidate individual : population) {
                        individual.mutate(Parameters.getHyperMutationProbability());
                    }
                }
            }

            // Crossover
            Collections.sort(population);
            for (int j = 0; j < Parameters.getPopulationSize() - 1; j += 2) {
                double probability = Math.random();
                if (probability < Parameters.getCrossoverProbability()) {
                    if (j < Parameters.getPopulationSize() - 1) {
                        population.set(j, Candidate.crossoverPMX(population.get(j), population.get(j + 1), graph));
                    }
                }
            }

            Collections.sort(population);

            // Last five percent will be reset
            if (Parameters.isReverseElitism()) {
                for (int j = 0; j < Parameters.getElitism(); j++) {
                    Candidate candidate = new Candidate(graph);
                    population.set(j, candidate);
                }
            }

            // Wisdom
            if (Parameters.getBestPathLength() == oldFinalPathLength) {
                noChange++;
                if (noChange > Parameters.getMaxStagnationUntilWisdom()) {
                    List<Integer> consensusPath = wisdomOfCrowds(population);
                    int numConsensus = Parameters.getPopulationSize() / 10;
                    for (int j = 0; j < numConsensus; j++) {
                        population.set(j, new Candidate(consensusPath, graph));
                    }
                }
            } else {
                noChange = 0;
                mutationRate = Parameters.getMutationProbability(); // Reset when improved
            }

            // Adaptive Mutation Rate
            if (noChange > Parameters.getMaxStagnationUntilAdaptiveMutation()) {
                mutationRate *= 1.05; // Increase mutation rate
            }

            double result = 0;
            for (Candidate individual : population) {
                result += individual.getPathLength();
            }
            Parameters.setAvgPathLength(result/ population.size());
//            System.out.println("Generation " + i + " of " + Parameters.getGenerations());
//            System.out.print(Parameters.getBestPathLength() + " ");
//            showBestPath();
//            System.out.println("Average path length: " + Parameters.getAvgPathLength());
//            System.out.println();
        }

        // Finish
        System.out.println("Algorithm completed.");
        Parameters.setDone(true);
    }


    private void selection(List<Candidate> population) {
        List<Candidate> newPopulation = new ArrayList<>();

        population.sort(Comparator.reverseOrder());
        for (int i = Parameters.getPopulationSize() - 1; i >= Parameters.getPopulationSize() - 1 - Parameters.getElitism(); i--) {
            newPopulation.add(population.get(i));
            population.remove(i);
        }

        double fitnessSum = population.stream().mapToDouble(Candidate::getFitness).sum();

        List<Double> prob = new ArrayList<>(population.size());
        List<Double> q = new ArrayList<>(population.size());
        q.add(0.0);

        for (int i = 0; i < population.size(); i++) {
            prob.add(population.get(i).getFitness() / fitnessSum);
            q.add(q.get(i) + prob.get(i));
        }

        int helperToMaintainPopSize = Parameters.getElitism();

        Random random = new Random();

        // Binary search doesn't get the same results as this, idk why
        for (int i = 0; i < Parameters.getPopulationSize() - 1 - helperToMaintainPopSize; i++) {
            double probability = random.nextDouble();
            int selectedIndex = 0;
            for (int j = 0; j < q.size() - 1; j++) {
                if (probability >= q.get(j) && probability < q.get(j + 1)) {
                    selectedIndex = j;
                    break;
                }
            }
            newPopulation.add(population.get(selectedIndex));
        }

        population.clear();
        population.addAll(newPopulation);
    }

    private void showBestPath() {
        for (int i = 0; i < Parameters.getBestPath().size(); i++) {
            System.out.print(Parameters.getBestPath());
        }
        System.out.println();
    }

    // Wisdom of Crowds
    private List<Integer> wisdomOfCrowds(List<Candidate> population) {
        Map<Integer, Integer> nodeFrequency = new HashMap<>();
        for (Candidate candidate : population) {
            for (Integer node : candidate.getChromosome()) {
                nodeFrequency.put(node, nodeFrequency.getOrDefault(node, 0) + 1);
            }
        }

        List<Map.Entry<Integer, Integer>> sortedNodes = new ArrayList<>(nodeFrequency.entrySet());
        sortedNodes.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        List<Integer> consensusPath = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : sortedNodes) {
            consensusPath.add(entry.getKey());
        }

        return consensusPath;
    }
}