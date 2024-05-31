package GA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import Data.Graph;

/* Keep in mind that 0 is the first node, always
    Therefore, some restrictions appear in the code
    Such as indexes starting from 1 (I know, horrible)
    And injecting the node 0 in the chromosome
    */

public class Candidate implements Comparable<Candidate> {
    double pathLength;
    List<Integer> chromosome;
    double fitness;

    public Candidate(List<Integer> order) {
        this.chromosome = new ArrayList<>(order);
        if (chromosome.get(0) != 0) {
            throw new IllegalArgumentException("First node is not 0 - good luck");
        }
        calculateFitness();
    }

    public Candidate() {
        chromosome = new ArrayList<>();
        List<Integer> nodes = new ArrayList<>();
        for (int i = 1; i < Graph.nodesNumber; i++) {
            nodes.add(i);
        }
        Collections.shuffle(nodes);
        chromosome.add(0);
        chromosome.addAll(nodes);
        calculateFitness();
    }

    public void calculateFitness() {
        double result = 0;
        for (int i = 0; i < Graph.nodesNumber - 1; i++) {
            if(i != 0 && i % Parameters.nodesPerVehicle == 0) {
                result += Graph.distances.get(chromosome.get(i)).getFirst();
                result += Graph.distances.getFirst().get(chromosome.get(i + 1));
            } else {
                result += Graph.distances.get(chromosome.get(i)).get(chromosome.get(i + 1));
            }
        }
        result += Graph.distances.get(chromosome.get(Graph.nodesNumber - 1)).get(chromosome.getFirst());
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
    public static Candidate crossoverPMX(Candidate p1, Candidate p2) {
        Random rand = new Random();
        int start = rand.nextInt(Graph.nodesNumber - 1) + 1;
        int end = rand.nextInt(Graph.nodesNumber - start) + start;

        List<Integer> childChromosome = new ArrayList<>(Collections.nCopies(Graph.nodesNumber, -1));
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

        for (int i = 1; i < Graph.nodesNumber; i++) {
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
        int newValue = (chromosome.get(index) + addedNumber) % Graph.nodesNumber;
        if (newValue < 0) {
            newValue += Graph.nodesNumber;
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
        return Graph.distances.get(a).get(c) + Graph.distances.get(b).get(d) -
                (Graph.distances.get(a).get(b) + Graph.distances.get(c).get(d));
    }

    private void apply2OptSwap(int i, int j) {
        while (i < j) {
            Collections.swap(chromosome, i, j);
            i++;
            j--;
        }
    }





    // Order Crossover (OX) - not used, PMX is better
    public static Candidate crossoverOX(Candidate p1, Candidate p2) {
        Random rand = new Random();
        int start = rand.nextInt(Graph.nodesNumber - 1) + 1;
        int end = rand.nextInt(Graph.nodesNumber - start) + start;

        List<Integer> childChromosome = new ArrayList<>(Collections.nCopies(Graph.nodesNumber, -1));
        childChromosome.set(0, 0);
        for (int i = start; i <= end; i++) {
            childChromosome.set(i, p1.chromosome.get(i));
        }

        int currentIndex = (end + 1) % Graph.nodesNumber;
        for (int i = 1; i < Graph.nodesNumber; i++) {
            int index = (end + 1 + i) % Graph.nodesNumber;
            int gene = p2.chromosome.get(index);
            if (!childChromosome.contains(gene)) {
                childChromosome.set(currentIndex, gene);
                currentIndex = (currentIndex + 1) % Graph.nodesNumber;
            }
        }

        return new Candidate(childChromosome);
    }

}