package src;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Problem problem = new Problem("data/input.txt");
            problem.printParsedData();
        } catch (IOException e){
            System.err.println("Error: " + e.getMessage());
        }
    }
}
