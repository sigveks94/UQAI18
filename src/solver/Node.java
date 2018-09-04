package solver;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Node {
	
private Point2D pos;

private ArrayList<Node> edgeNodes;

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
