package main;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class GeneticSolver {

    private final Random random = new Random();

    private final int iterations;
    private final int populationSize;
    private final double keepRate;
    private final double crossoverRate;
    private final double mutationRate;

    private final Knapsack knapsack;

    private List<Chromosome> population = new ArrayList<>();
    private int bestFitness = 0;
    private Chromosome bestResult;

    public GeneticSolver(int iterations, int populationSize, double keepRate, double crossoverRate, double mutationRate, Knapsack knapsack) {
        this.iterations = iterations;
        this.populationSize = populationSize;
        this.keepRate = keepRate;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.knapsack = knapsack;
    }

    public void solve() throws Exception {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.txt"), StandardCharsets.UTF_8))) {
            System.out.println("Generating initial population");
            generateRandomPopulation();
            System.out.printf("Best fitness at initial population: %d%n", bestFitness);
            writer.write(String.format("%d\n", bestFitness));

            int currentIterations = 0;
            while (currentIterations < iterations) {
                currentIterations++;
                System.out.printf("Evolving next generation (iteration %d)%n", currentIterations);

                evolveNextGeneration();
                System.out.printf("Best fitness at iteration %d: %d%n", currentIterations, bestFitness);
                writer.write(String.format("%d\n", bestFitness));
            }
        }
    }

    public int getBestValue() {
        return bestFitness;
    }

    public List<Item> getBestResult() {
        return bestResult.getItems().values().stream().toList();
    }

    private void generateRandomPopulation() {
        for (int i = 0; i < populationSize; i++) {
            Chromosome chromosome = new Chromosome(knapsack.getCapacity());

            Item randomItem = getRandomItem(chromosome);
            while (chromosome.getTotalWeight() + randomItem.getWeight() < knapsack.getCapacity()) {
                chromosome.addItem(randomItem);
                randomItem = getRandomItem(chromosome);
            }

            population.add(chromosome);
            if (chromosome.getFitness() > bestFitness || bestResult == null) {
                bestFitness = chromosome.getFitness();
                bestResult = chromosome;
            }
        }
    }

    private Item getRandomItem(Chromosome chromosome) {
        int itemIndex;
        do {
            itemIndex = random.nextInt(knapsack.getAllItems().size());
        } while (chromosome.contains(itemIndex));

        return knapsack.getAllItems().get(itemIndex);
    }

    private void evolveNextGeneration() {
        bestFitness = 0;
        bestResult = null;
        List<Chromosome> newPopulation = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            Chromosome firstParent = rouletteWheelSelectParent();
            Chromosome child = Chromosome.copy(firstParent, knapsack.getCapacity());

            double keepChance = random.nextDouble();
            if (keepChance >= keepRate) {
                crossover(child);
                mutate(child);
            }

            newPopulation.add(child);
            if (child.getFitness() > bestFitness) {
                bestFitness = child.getFitness();
                bestResult = child;
            }
        }

        this.population = newPopulation;
    }

    private void crossover(Chromosome child) {
        double crossoverChance = random.nextDouble();
        if (crossoverChance >= crossoverRate) {
            return;
        }

        Chromosome secondParent = rouletteWheelSelectParent();

        for (Item item : knapsack.getAllItems()) {
            int randomValue = random.nextInt() % 2;
            if (randomValue == 1) {
                if (secondParent.contains(item) && !child.contains(item)) {
                    child.addItem(item);
                } else if (!secondParent.contains(item) && child.contains(item)) {
                    child.removeItem(item);
                }
            }
        }
    }

    private void mutate(Chromosome child) {
        for (Item item : knapsack.getAllItems()) {
            double mutationChance = random.nextDouble();
            if (mutationChance >= mutationRate) {
                continue;
            }

            if (child.contains(item)) {
                child.removeItem(item);
            } else {
                child.addItem(item);
            }
        }
    }

    private Chromosome rouletteWheelSelectParent() {
        long fitnessSum = 0;
        for (Chromosome chromosome : population) {
            fitnessSum += chromosome.getFitness();
        }

        double randomFitness = random.nextDouble() * fitnessSum;
        for (Chromosome chromosome : population) {
            randomFitness -= chromosome.getFitness();
            if (randomFitness < 0) {
                return chromosome;
            }
        }

        return null;
    }
}
