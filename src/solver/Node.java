package solver;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import problem.Box;

public abstract class Node {
	
private Point2D pos;

private ArrayList<Node> edgeNodes;

//Maybe save the nodes possible to reach if a movable obstacle/box were moved??

public Node(Point2D pos) {
	this.pos = (Point2D) pos;
}

public Point2D getPos() {
	return pos;
}

public void addEdge(Node node) {
	edgeNodes.add(node);
}

public ArrayList<Node> getEdges(){
	return edgeNodes;
}

public void setPos(Point2D pos) {
	this.pos = (Point2D) pos.clone();
}

public double calculateDistance(Node node) {
	return this.pos.distance(node.getPos());
}

}
