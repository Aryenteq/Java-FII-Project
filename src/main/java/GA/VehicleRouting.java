package GA;

import java.io.InputStream;
import java.util.*;

public class VehicleRouting {

    public static void run() {
        Parameters.graph = readFile();
        printDistances(Parameters.graph);
        GA();
    }

    static Graph readFile() {
        Graph graph = new Graph();
        String filePath = "/Routes/" + Parameters.fileName;
        try (InputStream inputStream = VehicleRouting.class.getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + filePath);
            }
            try (Scanner file = new Scanner(inputStream)) {
                while (file.hasNextLine()) {
                    String line = file.nextLine();
                    if (line.contains("NAME")) {
                        graph.name = line.split(":")[1].trim();
                    } else if (line.contains("DIMENSION")) {
                        graph.nodesNumber = Integer.parseInt(line.split(":")[1].trim());
                        graph.nodeArr = new ArrayList<>(graph.nodesNumber);
                        graph.distances = new ArrayList<>(graph.nodesNumber);
                        for (int i = 0; i < graph.nodesNumber; i++)
                            graph.distances.add(new ArrayList<>(Collections.nCopies(graph.nodesNumber, 0.0)));
                    } else if (line.contains("EDGE_WEIGHT_TYPE")) {
                        graph.edgeWeightType = line.split(":")[1].trim();
                    } else if (line.contains("NODE_COORD_SECTION")) {
                        break;
                    }
                }

                for (int i = 0; i < graph.nodesNumber; i++) {
                    String[] tokens = file.nextLine().trim().split("\\s+");
                    if (tokens.length != 3) {
                        throw new IllegalArgumentException("Invalid node data format: " + Arrays.toString(tokens));
                    }
                    graph.nodeArr.add(new Node(i,
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    ));
                }

                if (graph.edgeWeightType.equals("EUC_2D")) {
                    for (int i = 0; i < graph.nodesNumber; i++) {
                        for (int j = 0; j < graph.nodesNumber; j++) {
                            if (i != j) {
                                Node n1 = graph.nodeArr.get(i);
                                Node n2 = graph.nodeArr.get(j);

                                double distance = Math.sqrt(Math.pow(n1.x - n2.x, 2) + Math.pow(n1.y - n2.y, 2));
                                graph.distances.get(i).set(j, distance);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Parameters.noOfNodes = graph.nodesNumber;
        return graph;
    }

    static void printDistances(Graph graph) {
        System.out.println("Distance Matrix:");
        for (int i = 0; i < graph.nodesNumber; i++) {
            for (int j = 0; j < graph.nodesNumber; j++) {
                System.out.print(graph.distances.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }

    static void GA() {
        Parameters.bestPath = new ArrayList<>();
        int noChange = 0;
        List<Candidate> population = new ArrayList<>(Parameters.populationSize);
        for (int i = 0; i < Parameters.populationSize; i++) {
            population.add(new Candidate());
        }

        for (int i = 0; i < Parameters.generations; i++) {
            double oldFinalPathLength = Parameters.finalPathLength;

            // Selection
            selection(population, 0.5);

            // Mutation
            for (Candidate candidate : population) {
                candidate.mutate(Parameters.mutationProbability);
            }

            // Hypermutation
            if (Parameters.Hypermutation) {
                if (noChange % Parameters.maxStagnationUntilHypermutation == 0 && noChange != 0) {
                    for (Candidate individual : population) {
                        individual.mutate(Parameters.hypermutationProbability);
                    }
                }
            }

            // Crossover
            Collections.sort(population);
            for (int j = 0; j < Parameters.populationSize - 1; j += 2) {
                double probability = Math.random();
                if (probability < Parameters.crossoverProbability) {
                    if (j != Parameters.populationSize - 1) {
                        population.set(j, Candidate.crossoverPMX(population.get(j), population.get(j + 1)));
                    }
                }
            }

            Collections.sort(population);

            // Last five percent will be reset
            if (Parameters.ReverseElitism) {
                for (int j = 0; j < Parameters.elitism; j++) {
                    population.set(j, new Candidate());
                }
            }

            // Reset the rest of the population if there's too much stagnation
            if (Parameters.finalPathLength == oldFinalPathLength) {
                noChange++;
            } else {
                // SA(population); // -- ?? doesn't work, algorithm gets stuck here
                noChange = 0;
            }

            if (noChange == Parameters.maxStagnationUntilSA && Parameters.Stagnation) {
                for (int j = Parameters.elitism; j < Parameters.populationSize - Parameters.elitism; j++) {
                    double probability = Math.random();
                    if (probability < Parameters.extinctProbability) {
                        population.set(j, new Candidate());
                    }
                }
                noChange = 0;
            }

            // Wisdom of Crowds
            if (noChange > Parameters.maxStagnationUntilWisdom) {
                List<Integer> consensusPath = wisdomOfCrowds(population);
                population.set(0, new Candidate(consensusPath));
                noChange = 0;
            }

            System.out.print(Parameters.finalPathLength + " ");
            showBestPath();
            System.out.println("Generation " + i + " of " + Parameters.generations);
        }

        // Ensure program exits correctly
        System.out.println("Algorithm completed.");
    }

    static void selection(List<Candidate> population, double selectionPressure) {
        List<Candidate> newPopulation = new ArrayList<>();

        if (Parameters.Elitism) {
            population.sort(Comparator.reverseOrder());
            for (int i = Parameters.populationSize - 1; i >= Parameters.populationSize - 1 - Parameters.elitism; i--) {
                newPopulation.add(population.get(i));
                population.remove(i);
            }
        }

        double fitnessSum = population.stream().mapToDouble(Candidate::getFitness).sum();

        List<Double> prob = new ArrayList<>(population.size());
        List<Double> q = new ArrayList<>(population.size());
        q.add(0.0);

        for (int i = 0; i < population.size(); i++) {
            prob.add(population.get(i).getFitness() / fitnessSum);
            q.add(q.get(i) + prob.get(i));
        }

        int helperToMaintainPopSize = 0;
        if (Parameters.Elitism) {
            helperToMaintainPopSize = Parameters.elitism;
        }

        Random random = new Random();

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

        selectionPressure *= Parameters.selectionPressureAdjustment;
        population.clear();
        population.addAll(newPopulation);
    }

    static void showBestPath() {
        for (int i = 0; i < Parameters.noOfNodes; i++) {
            System.out.print(Parameters.bestPath.get(i) + " ");
        }
        System.out.println();
    }

    // Wisdom of Crowds
    static List<Integer> wisdomOfCrowds(List<Candidate> population) {
        Map<Integer, Integer> nodeFrequency = new HashMap<>();
        for (Candidate candidate : population) {
            for (Integer node : candidate.chromosome) {
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
