package GA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Candidate implements Comparable<Candidate> {
    double pathLength;
    List<Integer> chromosome;
    double fitness;

    public Candidate(List<Integer> order) {
        this.chromosome = new ArrayList<>(order);
        calculateFitness();
    }

    public Candidate() {
        chromosome = new ArrayList<>();
        List<Integer> nodes = new ArrayList<>();
        for (int i = 0; i < Parameters.noOfNodes; i++) {
            nodes.add(i);
        }
        Collections.shuffle(nodes);
        chromosome.addAll(nodes);
        calculateFitness();
    }

    public void calculateFitness() {
        double result = 0;
        for (int i = 0; i < Parameters.noOfNodes - 1; i++) {
            result += Parameters.graph.distances.get(chromosome.get(i)).get(chromosome.get(i + 1));
        }
        result += Parameters.graph.distances.get(chromosome.get(Parameters.noOfNodes - 1)).get(chromosome.get(0));
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

    // Order Crossover (OX)
    public static Candidate crossoverOX(Candidate p1, Candidate p2) {
        Random rand = new Random();
        int start = rand.nextInt(Parameters.noOfNodes);
        int end = rand.nextInt(Parameters.noOfNodes - start) + start;

        List<Integer> childChromosome = new ArrayList<>(Collections.nCopies(Parameters.noOfNodes, -1));
        for (int i = start; i <= end; i++) {
            childChromosome.set(i, p1.chromosome.get(i));
        }

        int currentIndex = (end + 1) % Parameters.noOfNodes;
        for (int i = 0; i < Parameters.noOfNodes; i++) {
            int index = (end + 1 + i) % Parameters.noOfNodes;
            int gene = p2.chromosome.get(index);
            if (!childChromosome.contains(gene)) {
                childChromosome.set(currentIndex, gene);
                currentIndex = (currentIndex + 1) % Parameters.noOfNodes;
            }
        }

        return new Candidate(childChromosome);
    }

    // Partially Mapped Crossover (PMX)
    public static Candidate crossoverPMX(Candidate p1, Candidate p2) {
        Random rand = new Random();
        int start = rand.nextInt(Parameters.noOfNodes);
        int end = rand.nextInt(Parameters.noOfNodes - start) + start;

        List<Integer> childChromosome = new ArrayList<>(Collections.nCopies(Parameters.noOfNodes, -1));
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

        for (int i = 0; i < Parameters.noOfNodes; i++) {
            if (childChromosome.get(i) == -1) {
                childChromosome.set(i, p2.chromosome.get(i));
            }
        }

        return new Candidate(childChromosome);
    }

    // Print the converted chromosome
    public void printConverted() {
        for (Integer gene : chromosome) {
            System.out.print(gene + " ");
        }
        System.out.println();
    }

    public void changeChromosome(int index, int addedNumber) {
        int newValue = (chromosome.get(index) + addedNumber) % Parameters.noOfNodes;
        if (newValue < 0) {
            newValue += Parameters.noOfNodes;
        }
        chromosome.set(index, newValue);
    }
}
