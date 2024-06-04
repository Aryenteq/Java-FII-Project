package GA;

import Data.CustomGraph;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/* The graph object is always passed because graph4j
    doesn't accept static objects - which is pretty fair
    */

public class Candidate implements Comparable<Candidate> {
    private final CustomGraph graph;
    private double pathLength;
    private final List<Integer> chromosome;
    private double fitness;


    // Used in the crossover for example
    public Candidate(List<Integer> order, CustomGraph graph) {
        this.graph = graph;
        this.chromosome = new ArrayList<>(order);
        calculateFitness();
    }

    // Random permutation
    public Candidate(CustomGraph graph) {
        this.graph = graph;
        chromosome = new ArrayList<>();
        List<Integer> nodes = new ArrayList<>();
        for (int i = 1; i < graph.getNodesNumber(); i++) {
            nodes.add(i);
        }
        Collections.shuffle(nodes);
        chromosome.addAll(nodes);
        calculateFitness();
    }

    // Partially Mapped Crossover (PMX)
    public static Candidate crossoverPMX(Candidate p1, Candidate p2, CustomGraph graph) {
        Random rand = new Random();
        int start = rand.nextInt(p1.chromosome.size());
        int end = rand.nextInt(p1.chromosome.size() - start) + start;

        List<Integer> childChromosome = new ArrayList<>(Collections.nCopies(graph.getNodesNumber() - 1, -1));
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

        for (int i = 0; i < graph.getNodesNumber() - 1; i++) {
            if (childChromosome.get(i) == -1) {
                childChromosome.set(i, p2.chromosome.get(i));
            }
        }

        return new Candidate(childChromosome, graph);
    }

    public void calculateFitness() {
        double result = 0;

        // Add the depot
        List<Integer> normalizedChromosome = getNormalizedChromosome();

        // Get the distances based on the type of data structure used (custom graph || graph4j)
        if (!Parameters.isUseGraph4j()) {
            for (int i = 0; i < normalizedChromosome.size() - 1; i++) {
                result += graph.getDistances().get(normalizedChromosome.get(i)).get(normalizedChromosome.get(i + 1));
            }
        } else {
            for (int i = 0; i < normalizedChromosome.size() - 1; i++) {
                result += graph.getGraph().getEdgeWeight(normalizedChromosome.get(i), normalizedChromosome.get(i + 1));
            }
        }

        this.pathLength = result;
        if (result < Parameters.getBestPathLength()) {
            Parameters.setBestPathLength(result);
            Parameters.setBestPath(normalizedChromosome);
            Parameters.setBestPathChanged(true);
//            Parameters.bestPath = new ArrayList<>(normalizedChromosome);
//            Parameters.bestPathChanged = true;
        }

        this.fitness = 1 / result;
    }

    @NotNull
    private List<Integer> getNormalizedChromosome() {
        List<Integer> normalizedChromosome = new ArrayList<>(chromosome);
        int insertions = 0;
        for (int i = 0; i <= chromosome.size(); i += Parameters.getNodesPerVehicle()) {
            if (insertions == 0) {
                normalizedChromosome.add(i + insertions, 0);
                i++;
            } else {
                normalizedChromosome.add(i + insertions - 1, 0);
            }
            insertions++;
        }
        if (normalizedChromosome.getLast() != 0) {
            normalizedChromosome.add(0);
        }
        return normalizedChromosome;
    }

    // Swap two alleles
    public void mutate(double prob) {
        Random rand = new Random();
        for (int i = 0; i < chromosome.size(); i++) {
            if (rand.nextDouble() <= prob) {
                int j = rand.nextInt(chromosome.size());
                Collections.swap(chromosome, i, j);
            }
        }
        calculateFitness();
    }

    @Override
    public int compareTo(Candidate other) {
        return Double.compare(this.fitness, other.fitness);
    }

    // Used only in SA
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

        if (!Parameters.isUseGraph4j()) {
            // Custom graph
            return graph.getDistances().get(a).get(c) + graph.getDistances().get(b).get(d) - (graph.getDistances().get(a).get(b) + graph.getDistances().get(c).get(d));
        } else {
            // Graph4j - a lot of iterators...
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

    public List<Integer> getChromosome() {
        return chromosome;
    }

    public double getFitness() {
        return this.fitness;
    }

    public double getPathLength() {
        return pathLength;
    }
}