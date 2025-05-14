package src;

import src.models.GAOperators;
import src.models.HarmonySearch;
import src.models.Solution;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            String inputPath = "data/input.txt";
            String originalOutputPath = "output_original.txt";
            String mutatedOutputPath = "output_mutated.txt";
            String harmonyOutputPath = "output_harmony.txt";

            Problem problem = new Problem(inputPath);

            Solver solver = new Solver(
                    problem.getVideos(),
                    problem.getCacheServers(),
                    problem.getEndpoints(),
                    problem.getRequests()
            );

            Solution initialSolution = solver.solveGreedy();
            initialSolution.writeToFile(originalOutputPath);

            Solution mutatedSolution = GAOperators.mutateSolution(
                    initialSolution,
                    problem.getVideos(),
                    problem.getCacheCapacity()
            );
            mutatedSolution.writeToFile(mutatedOutputPath);

            System.out.println("Validating original solution...");
            boolean validOriginal = Validator.validate(inputPath, originalOutputPath);

            System.out.println("🔍 Validating mutated solution...");
            boolean validMutated = Validator.validate(inputPath, mutatedOutputPath);

            HarmonySearch hs = new HarmonySearch(problem);
            Solution harmonySolution = hs.run();
            harmonySolution.writeToFile(harmonyOutputPath);

            boolean validHs = Validator.validate(inputPath, harmonyOutputPath);

            if (validOriginal && validMutated && validHs) {
                double fitnessOriginal = Validator.getFitnessScore(problem, initialSolution.getCacheMap());
                double fitnessMutated = Validator.getFitnessScore(problem, mutatedSolution.getCacheMap());
                double fitnessHS = Validator.getFitnessScore(problem, harmonySolution.getCacheMap());

                System.out.print("Final Fitness Comparison: " +  fitnessOriginal + ", " + fitnessMutated + ", " + fitnessHS);

                if (fitnessHS > Math.max(fitnessOriginal, fitnessMutated)) {
                    System.out.println("Harmony Search produced the best result!");
                } else if (fitnessMutated > fitnessOriginal) {
                    System.out.println("Mutation improved the Greedy baseline.");
                } else {
                    System.out.println("Greedy remained the top performer.");
                }
            } else {
                System.err.println("One or more solutions are invalid.");
            }

        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}
