package main;

import java.time.Duration;
import java.time.Instant;

public class Main {

    private static void printHelp() {
        System.out.println("----- MD1 - Knapsack with genetic algorithm -----");
        System.out.println();
        System.out.println("Usage: java -jar md1.jar <input_file> <algorithm_parameters>");
        System.out.println();
        System.out.println("    * input_file - path to input file describing knapsack instance");
        System.out.println("    * algorithm_parameters (in this order):");
        System.out.println("       * iterations       - integer for evolution iteration count");
        System.out.println("       * population_size  - size of the population in each evolution stage");
        System.out.println("       * keep_rate        - chance for child to be left unmodified");
        System.out.println("       * crossover_rate   - chance for child to be crossed over with another parent (uniform crossover)");
        System.out.println("       * mutationRate     - chance for each item to be toggled in child");
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 5 || "help".equals(args[0])) {
            printHelp();
            return;
        }

        String inputFilePath = args[0];
        int iterations = Integer.parseInt(args[1]);
        int populationSize = Integer.parseInt(args[2]);
        double keepRate = Double.parseDouble(args[3]);
        double crossoverRate = Double.parseDouble(args[4]);
        double mutationRate = Double.parseDouble(args[5]);

        Knapsack knapsack = Knapsack.fromFile(inputFilePath);

        Instant timeBefore = Instant.now();

        GeneticSolver solver = new GeneticSolver(iterations, populationSize, keepRate, crossoverRate, mutationRate, knapsack);
        solver.solve();

        Instant timeAfter = Instant.now();
        Duration duration = Duration.between(timeBefore, timeAfter);

        System.out.println("-------------------- Result --------------------");
        System.out.println("Time taken (in milliseconds): " + duration.toMillis());
        System.out.println("Best total value: " + solver.getBestValue());
        System.out.println("Best item set: " + solver.getBestResult());
    }
}
