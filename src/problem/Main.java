package problem;

import java.io.IOException;
import java.util.List;

import solver.GoalNode;
import solver.HelpNode;
import solver.Node;
import solver.Program;
import solver.Solver;
import solver.StartBoxNode;

public class Main {
	
    public static void main(String[] args) {
        ProblemSpec ps = new ProblemSpec();
        try {
        	String inputFile = args[0];
        	String outputFile = args[1];
            ps.loadProblem(inputFile);
            Program program = new Program(ps,outputFile);
            double startTime = System.nanoTime();
            program.run();
            double elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elapsed time for generating output file" + elapsedTime/1000000 + " ms");
            ps.loadSolution(outputFile);
        } catch (IOException e) {
            System.out.println("IO Exception occured");
        }
        System.out.println("Finished loading!");

    }
}


