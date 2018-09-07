package problem;

import java.io.IOException;
import java.util.List;

import solver.Node;
import solver.Solver;

public class Main {
	
    public static void main(String[] args) {
        ProblemSpec ps = new ProblemSpec();
        try {
            ps.loadProblem("input2.txt");
            Solver solver = new Solver(ps);
            solver.makeInitialSampling();
            solver.makeInitialEdges();
            List<Node> nodes = solver.getAllNodes();
            for(Node n : nodes) {
            	n.getEdges();
            }
            ps.loadSolution("output2.txt");
        } catch (IOException e) {
            System.out.println("IO Exception occured");
        }
        System.out.println("Finished loading!");

    }
}


