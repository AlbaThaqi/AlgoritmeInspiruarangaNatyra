package src;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Main <input_file>");
            return;
        }

        String inputFile = args[0];
        String outputFile = "output.txt";

        try {
            Problem problem = new Problem(inputFile);
            problem.solve();
            problem.writeOutput(outputFile);
            System.out.println("Solution written to " + outputFile);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
