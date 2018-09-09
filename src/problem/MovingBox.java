package problem;

import problem.Box;
import solver.Node;

import java.awt.geom.Point2D;


/**
 * This class represents one of the moving boxes in Assignment 1.
 * 
 * @author Sergiy Dudnikov
 */
public class MovingBox extends Box {
	
	Node goalNode;
	Node startNode;

	/**
	 * Constructs a Moving box at a position width a side width
	 * 
	 * @param pos
	 *            the position of the box
	 * @param width
	 *            the width (and height) of the box
	 */
    public MovingBox(Point2D pos, double width) {
        super(pos, width);
        goalNode = null;
        startNode = null;
    }
    
    public void setStartNode(Node node) {
    	this.startNode = node;
    }
    
    public Node getStartNode() {
    	return startNode;
    }
    
    public void setGoalNode(Node node) {
    	this.goalNode = node;
    }
    
    public Node getGoalNode() {
    	return goalNode;
    }
    
    
}
