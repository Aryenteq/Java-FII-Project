package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Candidate implements Comparable<Candidate> {
    double pathLength;
    List<Integer> chromosome;
    double fitness;

    public Candidate(List<Integer> order) {
        this.chromosome = order;
        calculateFitness();
    }

    public Candidate() {
        chromosome = new ArrayList<>();

        Random rand = new Random();
        int index = 0;
        for (int i = Parameters.noOfNodes-1; i>=0; i--) {
            index = rand.nextInt(i+1);
            chromosome.add(index);
        }
        calculateFitness();
    }

    public void calculateFitness() {
        List<Integer> ordered = new ArrayList<>();
        for (int i = 0; i < Parameters.noOfNodes; i++) {
            ordered.add(i);
        }

        //List<Integer> converted = chromosome.stream().map(ordered::get).collect(Collectors.toList());
        List<Integer> converted = new ArrayList<>(Parameters.noOfNodes);
        for (int i = 0; i < Parameters.noOfNodes; i++) {
            converted.add(ordered.get(chromosome.get(i)));
            int index = ordered.indexOf(converted.get(i));
            if (index != -1)
                ordered.remove(index);
        }

        double result = 0;
        for (int i = 0; i < Parameters.noOfNodes - 1; i++) {
            result += Parameters.graph.distances.get(converted.get(i)).get(converted.get(i + 1));
        }
        this.pathLength = result;

        if (result < Parameters.finalPathLength) {
            Parameters.finalPathLength = result;
            Parameters.bestPath = converted;
        }

        this.fitness = 1 / result;
    }

    public double getFitness() {
        return this.fitness;
    }

    public void mutate(double prob) {
        for (int i = 0; i < Parameters.noOfNodes - 1; i++) {
            double probability = Math.random();
            if (probability <= prob) {
                //chromosome.set(i, (chromosome.get(i) + 1) % (Parameters.noOfNodes - i));
            }
        }
        calculateFitness();
    }

    @Override
    public int compareTo(Candidate other) {
        return Double.compare(this.fitness, other.fitness);
    }

    public static Candidate crossover(Candidate p1, Candidate p2) {
        int index1 = (int) (Math.random() * (Parameters.noOfNodes - 2));
        int index2 = (int) (Math.random() * (Parameters.noOfNodes - 2));
        if (index2 < index1) {
            int temp = index1;
            index1 = index2;
            index2 = temp;
        }
        List<Integer> child = new ArrayList<>();
        for (int i = 0; i <= index1; i++)
            child.add(p1.chromosome.get(i));
        for (int i = index1 + 1; i <= index2; i++)
            child.add(p2.chromosome.get(i));
        for (int i = index2 + 1; i < Parameters.noOfNodes; i++)
            child.add(p1.chromosome.get(i));
        return new Candidate(child);
    }




    // Methods for SA
    public void printConverted() {
        List<Integer> ordered = new ArrayList<>();
        for (int i = 0; i < Parameters.noOfNodes; i++) {
            ordered.add(i);
        }

        //List<Integer> converted = chromosome.stream().map(ordered::get).collect(Collectors.toList());
        List<Integer> converted = new ArrayList<>(Parameters.noOfNodes);
        for (int i = 0; i < Parameters.noOfNodes; i++) {
            converted.add(ordered.get(chromosome.get(i)));
            int index = ordered.indexOf(converted.get(i));
            if (index != -1)
                ordered.remove(index);
        }

        for (int i = 0; i < Parameters.noOfNodes; i++)
            System.out.print(STR."\{converted.get(i)} ");
        System.out.println();
    }

    public void changeChromosome(int index, int addedNumber) {
        int moduloBase = Parameters.noOfNodes - index;

        if (addedNumber >= 0)
            this.chromosome.set(index, (this.chromosome.get(index) + addedNumber) % moduloBase);
        else {
            int adjustedNumber = (this.chromosome.get(index) + addedNumber) % moduloBase;
            this.chromosome.set(index, (adjustedNumber + moduloBase) % moduloBase);
        }
    }

}

