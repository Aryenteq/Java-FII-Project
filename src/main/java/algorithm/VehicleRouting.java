package algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;


public class VehicleRouting {

    public static void run() {
        Parameters.graph = readFile();
        GA();
    }

    public static void SA(List<Candidate> population) {
        for (Candidate it : population) {
            System.out.print("old: ");
            it.printConverted();

            double bestPath, t = 1, temperature = 100;
            int index = 0;
            do {
                int contor = 0;
                boolean contor2 = false;
                do {
                    Candidate bestNeighbour = it;
                    bestPath = Double.MAX_VALUE;

                    // Get the best neighbour
                    for (int i = 0; i < Parameters.noOfNodes - 1; i++) {
                        // Mutate (get a neighbour)
                        it.changeChromosome(i, 1);
                        it.calculateFitness();
                        if (it.getFitness() < bestPath)
                            bestPath = it.getFitness();
                        index = i;

                        // Reset
                        it.changeChromosome(i, -1);
                    }
                    // Reset fitness
                    it.calculateFitness();
                    bestNeighbour.changeChromosome(index, 1);
                    bestNeighbour.calculateFitness();

                    // We've found a better neighbour
                    if (bestNeighbour.getFitness() < it.getFitness())
                        it = bestNeighbour;
                    else if ((new Random().nextInt(1000) % 1000) / 1000.0 < Math.exp(-Math.abs(it.getFitness() - bestNeighbour.getFitness()) / temperature))
                        it = bestNeighbour;
                    else
                        contor++;
                } while (contor < 3 && !contor2);
                System.out.println(temperature);
                temperature *= 0.95;
            } while (temperature > 10e-5);

            System.out.print("new: ");
            it.printConverted();
        }
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
                            Integer.parseInt(tokens[0]),
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
                                graph.distances.get(i).set(j, (double) Math.round(distance));
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
                        population.set(j, Candidate.crossover(population.get(j), population.get(j + 1)));
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
                //SA(population); -- ?? doesn't work, algorithm gets stuck here
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
}

//    static void OptimizedPath() {
//        try (Scanner file = new Scanner(Objects.requireNonNull(VehicleRouting.class.getResourceAsStream(STR."/Routes\{Parameters.fileName}")))) {
//            boolean tourSectionFound = false;
//            int lastNumber = -1;
//            double confirmedBestPath = 0;
//            while (file.hasNextLine()) {
//                String line = file.nextLine();
//                if (line.contains("TOUR_SECTION")) {
//                    tourSectionFound = true;
//                    continue;
//                }
//                if (tourSectionFound) {
//                    String[] tokens = line.trim().split("\\s+");
//                    for (String token : tokens) {
//                        int number = Integer.parseInt(token);
//                        if (lastNumber != -1) {
//                            confirmedBestPath += Parameters.graph.distances.get(lastNumber - 1).get(number - 1);
//                            lastNumber = number;
//                        } else {
//                            lastNumber = number;
//                        }
//                    }
//                    if (tokens[tokens.length - 1].equals("-1"))
//                        break;
//                }
//            }
//            System.out.println("\nReal best path distance: " + confirmedBestPath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
