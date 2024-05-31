package GA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import Data.CustomGraph;

/* Keep in mind that 0 is the first node, always
    Therefore, some restrictions appear in the code
    Such as indexes starting from 1 (I know, horrible)
    And injecting the node 0 in the chromosome
    */

public class Candidate implements Comparable<Candidate> {
    double pathLength;
    List<Integer> chromosome;
    double fitness;
    CustomGraph graph;

    public Candidate(List<Integer> order, CustomGraph graph) {
        this.graph = graph;
        this.chromosome = new ArrayList<>(order);
        if (chromosome.getFirst() != 0) {
            throw new IllegalArgumentException("First node is not 0 - good luck");
        }
        calculateFitness();
    }

    public Candidate(CustomGraph graph) {
        this.graph = graph;
        chromosome = new ArrayList<>();
        List<Integer> nodes = new ArrayList<>();
        for (int i = 1; i < graph.getNodesNumber(); i++) {
            nodes.add(i);
        }
        Collections.shuffle(nodes);
        chromosome.add(0);
        chromosome.addAll(nodes);
        calculateFitness();
    }

    public void calculateFitness() {
        double result = 0;

        // Get the distances based on the type of data structure used (custom graph || graph4j)
        if (!Parameters.useGraph4j) {
            for (int i = 0; i < graph.getNodesNumber() - 1; i++) {
                if (i != 0 && i % Parameters.nodesPerVehicle == 0) {
                    result += graph.getDistances().get(chromosome.get(i)).getFirst();
                    result += graph.getDistances().getFirst().get(chromosome.get(i + 1));
                } else {
                    result += graph.getDistances().get(chromosome.get(i)).get(chromosome.get(i + 1));
                }
            }
            result += graph.getDistances().get(chromosome.get(graph.getNodesNumber() - 1)).getFirst();
        } else {
            for (int i = 0; i < graph.getNodesNumber() - 1; i++) {
                if (i != 0 && i % Parameters.nodesPerVehicle == 0) {
                    result += graph.getGraph().getEdgeWeight(chromosome.get(i), 0);
                    result += graph.getGraph().getEdgeWeight(0, chromosome.get(i + 1));
                } else {
                    result += graph.getGraph().getEdgeWeight(chromosome.get(i), chromosome.get(i + 1));
                }
            }
            result += graph.getGraph().getEdgeWeight(chromosome.get(graph.getNodesNumber() - 1), 0);
        }

        this.pathLength = result;

        if (result < Parameters.finalPathLength) {
            Parameters.finalPathLength = result;
            Parameters.bestPath = new ArrayList<>(chromosome);
        }

        this.fitness = 1 / result;
    }

    public double getFitness() {
        return this.fitness;
    }

    public void mutate(double prob) {
        Random rand = new Random();
        for (int i = 1; i < chromosome.size(); i++) {
            if (rand.nextDouble() <= prob) {
                int j = rand.nextInt(chromosome.size() - 1) + 1;
                Collections.swap(chromosome, i, j);
            }
        }
        calculateFitness();
    }

    @Override
    public int compareTo(Candidate other) {
        return Double.compare(this.fitness, other.fitness);
    }

    // Partially Mapped Crossover (PMX)
    public static Candidate crossoverPMX(Candidate p1, Candidate p2, CustomGraph graph) {
        Random rand = new Random();
        int start = rand.nextInt(graph.getNodesNumber() - 1) + 1;
        int end = rand.nextInt(graph.getNodesNumber() - start) + start;

        List<Integer> childChromosome = new ArrayList<>(Collections.nCopies(graph.getNodesNumber(), -1));
        childChromosome.set(0, 0);
        for (int i = start; i <= end; i++) {
            childChromosome.set(i, p1.chromosome.get(i));
        }

        for (int i = start; i <= end; i++) {
            int gene = p2.chromosome.get(i);
            if (!childChromosome.contains(gene)) {
                int position = i;
                while (start <= position && position <= end) {
                    position = p2.chromosome.indexOf(p1.chromosome.get(position));
                }
                childChromosome.set(position, gene);
            }
        }

        for (int i = 1; i < graph.getNodesNumber(); i++) {
            if (childChromosome.get(i) == -1) {
                childChromosome.set(i, p2.chromosome.get(i));
            }
        }

        return new Candidate(childChromosome, graph);
    }

    public void changeChromosome(int index, int addedNumber) {
        int newValue = (chromosome.get(index) + addedNumber) % graph.getNodesNumber();
        if (newValue < 0) {
            newValue += graph.getNodesNumber();
        }
        chromosome.set(index, newValue);
    }

    /* Magic */
    public void localSearch() {
        boolean improvement = true;
        while (improvement) {
            improvement = false;
            for (int i = 1; i < chromosome.size() - 1; i++) {
                for (int j = i + 1; j < chromosome.size(); j++) {
                    double delta = calculate2OptSwapDelta(i, j);
                    if (delta < 0) {
                        apply2OptSwap(i, j);
                        improvement = true;
                        calculateFitness();
                    }
                }
            }
        }
    }

    private double calculate2OptSwapDelta(int i, int j) {
        int a = chromosome.get(i - 1);
        int b = chromosome.get(i);
        int c = chromosome.get(j);
        int d = chromosome.get((j + 1) % chromosome.size());

        if (!Parameters.useGraph4j) {
            // Use distances matrix
            return graph.getDistances().get(a).get(c) + graph.getDistances().get(b).get(d) -
                    (graph.getDistances().get(a).get(b) + graph.getDistances().get(c).get(d));
        } else {
            // Use Graph4J graph object
            double distanceAC = 0;
            double distanceBD = 0;
            double distanceAB = 0;
            double distanceCD = 0;

            for (var it = graph.getGraph().neighborIterator(a); it.hasNext(); ) {
                int neighbor = it.next();
                if (neighbor == c) {
                    distanceAC = it.getEdgeWeight();
                }
                if (neighbor == b) {
                    distanceAB = it.getEdgeWeight();
                }
            }

            for (var it = graph.getGraph().neighborIterator(b); it.hasNext(); ) {
                int neighbor = it.next();
                if (neighbor == d) {
                    distanceBD = it.getEdgeWeight();
                }
            }

            for (var it = graph.getGraph().neighborIterator(c); it.hasNext(); ) {
                int neighbor = it.next();
                if (neighbor == d) {
                    distanceCD = it.getEdgeWeight();
                }
            }

            return distanceAC + distanceBD - (distanceAB + distanceCD);
        }
    }

    private void apply2OptSwap(int i, int j) {
        while (i < j) {
            Collections.swap(chromosome, i, j);
            i++;
            j--;
        }
    }





    // Order Crossover (OX) - not used, PMX is better
    public Candidate crossoverOX(Candidate p1, Candidate p2) {
        Random rand = new Random();
        int start = rand.nextInt(graph.getNodesNumber() - 1) + 1;
        int end = rand.nextInt(graph.getNodesNumber() - start) + start;

        List<Integer> childChromosome = new ArrayList<>(Collections.nCopies(graph.getNodesNumber(), -1));
        childChromosome.set(0, 0);
        for (int i = start; i <= end; i++) {
            childChromosome.set(i, p1.chromosome.get(i));
        }

        int currentIndex = (end + 1) % graph.getNodesNumber();
        for (int i = 1; i < graph.getNodesNumber(); i++) {
            int index = (end + 1 + i) % graph.getNodesNumber();
            int gene = p2.chromosome.get(index);
            if (!childChromosome.contains(gene)) {
                childChromosome.set(currentIndex, gene);
                currentIndex = (currentIndex + 1) % graph.getNodesNumber();
            }
        }

        return new Candidate(childChromosome, graph);
    }

}