package solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import java.awt.geom.Point2D;

public class AStar {

private boolean finished;
private Queue<Node> queue;
private Node endNode;
private List<Node> path;
private Node end;
private HashMap<Point2D, Node> visited;

public Node getEndNode() {
	return endNode;
}
public int getLength() {
	return this.visited.size();
}

public void setEnd(Node end) {
	this.end=end;
}

public boolean isFinished() {
	return finished;
}


public void setFinished(boolean finished) {
	this.finished = finished;
}


public void setInitialQueue(Node node) {
	this.queue.add(node);
}

public AStar(){
	queue=new PriorityQueue<Node>();
	finished=false;
	path = new ArrayList<Node>();
	visited = new HashMap<Point2D, Node>();
}


public void addNodeToQueue(Node node) {
	if (visited.containsKey(node.getPos())) {
		if(visited.get(node.getPos()).getG() > node.getG()) {
			visited.get(node.getPos()).setG(node.getG());
			visited.get(node.getPos()).setParent(node.getParent());
		}
	}
	else {
		queue.add(node);
		visited.put(node.getPos(), node);
		if (node.getPos().equals(end.getPos())) {
			finished=true;
			endNode = node;
			path = getPath(endNode);
		}
	}
}

public void generateNeighbours(Node node) {
	
	for (Node neighbor: node.getEdges()) {
		neighbor.setParent(node);
		neighbor.setG(this.g(neighbor));
		neighbor.setH(this.h(neighbor));
		addNodeToQueue(neighbor);
	}
	queue.remove(node);
}

public double h(Node node){
	return node.calculateDistance(endNode);
}

public double g(Node node) {
	double i;
	i= node.getParent().getG() + node.calculateDistance(node.getParent());
	return i;
}

public void find() {
	int o = 0;
	while (!(finished) && !(queue.isEmpty())) {
		generateNeighbours(queue.poll());
		o++;
		if (o>5000000) {
			System.out.println("over 50000");
					this.finished=true;
				}
			}
		}
		
	public List<Node> getPath(Node endNode){
		while(endNode!=null) {
			path.add(endNode);
			endNode = endNode.getParent();
		}
		return path;
	}
	
	public List<Node> getPath(){
		return path;
	}
}



