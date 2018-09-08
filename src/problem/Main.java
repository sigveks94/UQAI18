package problem;

import java.io.IOException;
import java.util.List;

import solver.HelpNode;
import solver.Node;
import solver.Solver;

public class Main {
	
    public static void main(String[] args) {
        ProblemSpec ps = new ProblemSpec();
        try {
            ps.loadProblem("input3.txt");
            Solver solver = new Solver(ps);
            solver.makeInitialSampling();
            solver.makeInitialEdges();
            solver.createEdgesBetweenAllBoxes();
            List<Node> nodes = solver.getAllNodes();
            for(Node n : nodes) {
            	System.out.println("Node " + n + " has edges " + n.getEdges());
            }
            ps.loadSolution("output3.txt");
        } catch (IOException e) {
            System.out.println("IO Exception occured");
        }
        System.out.println("Finished loading!");

    }
}


