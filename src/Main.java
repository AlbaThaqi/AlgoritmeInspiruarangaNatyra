package src;

import src.models.GAOperators;
import src.models.Solution;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            String inputPath = "data/input.txt";
            String originalOutputPath = "output_original.txt";
            String mutatedOutputPath = "output_mutated.txt";

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

            if (validOriginal && validMutated) {
                double fitnessOriginal = Validator.getFitnessScore(problem, initialSolution.getCacheMap());
                double fitnessMutated = Validator.getFitnessScore(problem, mutatedSolution.getCacheMap());

                System.out.printf("Original Fitness: %.4f%n", fitnessOriginal);
                System.out.printf("Mutated  Fitness: %.4f%n", fitnessMutated);

                if (fitnessMutated > fitnessOriginal) {
                    System.out.println("Mutation improved the solution!");
                } else if (fitnessMutated == fitnessOriginal) {
                    System.out.println("Mutation made no difference.");
                } else {
                    System.out.println("Mutation reduced performance. Try another!");
                }
            } else {
                System.err.println("One or both solutions are invalid.");
            }

        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}
