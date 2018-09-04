package problem;

import java.io.IOException;

import solver.Solver;

public class Main {
    public static void main(String[] args) {
        ProblemSpec ps = new ProblemSpec();
        try {
            ps.loadProblem("input1.txt");
            Solver solver = new Solver(ps);
            ps.loadSolution("output1.txt");
        } catch (IOException e) {
            System.out.println("IO Exception occured");
        }
        System.out.println("Finished loading!");

    }
}

