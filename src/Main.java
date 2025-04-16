package src;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Problem problem = new Problem("data/input.txt");
            problem.printParsedData();

            Solver solver = new Solver(
                    problem.getVideos(),
                    problem.getCacheServers(),
                    problem.getEndpoints(),
                    problem.getRequests()
            );
            solver.solveGreedy();
            solver.writeOutput("output.txt");
        } catch (IOException e){
            System.err.println("Error: " + e.getMessage());
        }
    }
}
