package src;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            String inputPath = "data/input.txt";
            String outputPath = "output.txt";

            Problem problem = new Problem(inputPath);
            problem.printParsedData();

            Solver solver = new Solver(
                    problem.getVideos(),
                    problem.getCacheServers(),
                    problem.getEndpoints(),
                    problem.getRequests()
            );

            solver.solveGreedy();
            solver.writeOutput(outputPath);

            System.out.println("🔍 Running validator...");
            boolean isValid = Validator.validate(inputPath, outputPath);

            if (!isValid) {
                System.err.println("❌ The solution is not valid.");
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
