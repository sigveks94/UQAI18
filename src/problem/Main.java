package problem;

import java.io.IOException;
import java.util.List;

import solver.GoalNode;
import solver.HelpNode;
import solver.Node;
import solver.Solver;
import solver.StartBoxNode;

public class Main {
	
    public static void main(String[] args) {
        ProblemSpec ps = new ProblemSpec();
        try {
            ps.loadProblem("inputExample.txt");
            Solver solver = new Solver(ps);
            solver.initiate();
            List<Node> nodes = solver.getAllNodes();
            for(Node n : nodes) {
            	if(n instanceof StartBoxNode) {
            		System.out.println("StartNode " + n + " has edges " + n.getEdges());
            	}
            	if(n instanceof GoalNode) {
            		System.out.println("GoalNode " + n + " has edges " + n.getEdges());
            	}
            }
            ps.loadSolution("output3.txt");
        } catch (IOException e) {
            System.out.println("IO Exception occured");
        }
        System.out.println("Finished loading!");

    }
}


