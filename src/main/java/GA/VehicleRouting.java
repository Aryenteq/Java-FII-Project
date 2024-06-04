package GA;

import Data.CustomGraph;

import java.util.*;


public class VehicleRouting {

    public void run() {
        CustomGraph graph = new CustomGraph();
        GA(graph);
    }

    private void GA(CustomGraph graph) {
        Parameters.bestPath = new ArrayList<>();
        int noChange = 0;
        List<Candidate> population = new ArrayList<>(Parameters.populationSize);
        for (int i = 0; i < Parameters.populationSize; i++) {
            population.add(new Candidate(graph));
        }

        double mutationRate = Parameters.mutationProbability;

        for (int i = 0; i < Parameters.generations; i++) {
            double oldFinalPathLength = Parameters.bestPathLength;

            // Selection
            selection(population);

            // Mutation
            for (Candidate candidate : population) {
                candidate.mutate(mutationRate);
            }

            // Conditional Local Search
            if (noChange > Parameters.maxStagnationUntil2Opt) {
                for (Candidate candidate : population) {
                    candidate.localSearch();
                }
            }

            // HyperMutation
            if (Parameters.HyperMutation) {
                if (noChange > Parameters.maxStagnationUntilHyperMutation) {
                    for (Candidate individual : population) {
                        individual.mutate(Parameters.hyperMutationProbability);
                    }
                }
            }

            // Crossover
            Collections.sort(population);
            for (int j = 0; j < Parameters.populationSize - 1; j += 2) {
                double probability = Math.random();
                if (probability < Parameters.crossoverProbability) {
                    if (j < Parameters.populationSize - 1) {
                        population.set(j, Candidate.crossoverPMX(population.get(j), population.get(j + 1), graph));
                    }
                }
            }

            Collections.sort(population);

            // Last five percent will be reset
            if (Parameters.ReverseElitism) {
                for (int j = 0; j < Parameters.elitism; j++) {
                    population.set(j, new Candidate(graph));
                }
            }

            // Wisdom
            if (Parameters.bestPathLength == oldFinalPathLength) {
                noChange++;
                if (noChange > Parameters.maxStagnationUntilWisdom) {
                    List<Integer> consensusPath = wisdomOfCrowds(population);
                    int numConsensus = Parameters.populationSize / 10;
                    for (int j = 0; j < numConsensus; j++) {
                        population.set(j, new Candidate(consensusPath, graph));
                    }
                }
            } else {
                noChange = 0;
                mutationRate = Parameters.mutationProbability; // Reset when improved
            }

            // Adaptive Mutation Rate
            if (noChange > Parameters.maxStagnationUntilAdaptiveMutation) {
                mutationRate *= 1.05; // Increase mutation rate
            }

            double result = 0;
            for (Candidate individual : population) {
                result += individual.getPathLength();
            }
            Parameters.avgPathLength = result / population.size();

//            System.out.println("Generation " + i + " of " + Parameters.generations);
//            System.out.print(Parameters.bestPathLength + " ");
//            showBestPath();
//            System.out.println("Average path length: " + Parameters.avgPathLength);
//            System.out.println();
        }

        // Finish
        System.out.println("Algorithm completed.");
    }


    private void selection(List<Candidate> population) {
        List<Candidate> newPopulation = new ArrayList<>();

        population.sort(Comparator.reverseOrder());
        for (int i = Parameters.populationSize - 1; i >= Parameters.populationSize - 1 - Parameters.elitism; i--) {
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

        int helperToMaintainPopSize = Parameters.elitism;

        Random random = new Random();

        // Binary search doesn't get the same results as this, idk why
        for (int i = 0; i < Parameters.populationSize - 1 - helperToMaintainPopSize; i++) {
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
        for (int i = 0; i < Parameters.bestPath.size(); i++) {
            System.out.print(Parameters.bestPath.get(i) + " ");
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