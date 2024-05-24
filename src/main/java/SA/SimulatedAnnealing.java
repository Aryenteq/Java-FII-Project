package SA;

import GA.Candidate;
import GA.Parameters;

import java.util.List;
import java.util.Random;

public class SimulatedAnnealing {
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
}
