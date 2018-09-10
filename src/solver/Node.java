package solver;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import problem.Box;

public abstract class Node implements Comparable<Node> {
	
private Point2D pos;
private Node parent;
private double g;
private double h;

private ArrayList<Node> edgeNodes = new ArrayList<>();

//Maybe save the nodes possible to reach if a movable obstacle/box were moved??

public Node(Point2D pos) {
	this.pos = (Point2D) pos;
}

public Node getParent() {
	return parent;
}
public void setParent(Node parent) {
	this.parent = parent;
}

public double getG() {
	return g;
}

public void setG(double cost) {
	this.g=cost;
}

public double getH() {
	return h;
}

public void setH (double h) {
	this.h=h;
}

public double getF() {
	return  h+g;
}

public int compareTo(Node otherNode){
	Double i = this.getF();
	Double o = otherNode.getF();
	
	return i.compareTo(o);
}

public Point2D getPos() {
	return pos;
}

public void addEdge(Node node) {
	if(node == null) {
		return;
	}
	edgeNodes.add(node);
}

public void removeEdge(Node node) {
	int index = edgeNodes.indexOf(node);
	edgeNodes.remove(index);
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

public String toString() {
	return this.pos.toString();
}

}
