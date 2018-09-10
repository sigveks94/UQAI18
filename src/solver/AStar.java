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
private Node endNode = null;
private List<Node> path;
private Node end = null;
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

public void generateNeighbours(Node node) {
	for (Node neighbor: node.getEdges()) {
		double cost = node.getG() + neighbor.calculateDistance(node);
		if(visited.containsKey(neighbor.getPos())) {
			if(visited.get(neighbor.getPos()).getG() > cost) {
				neighbor.setG(cost);
				neighbor.setParent(node);
			}
		}
		else {
			if(neighbor.getPos().equals(end.getPos())) {
				finished = true;
				neighbor.setParent(node);
				endNode = neighbor;
				path = getPath(endNode);
				return;
			}
			neighbor.setParent(node);
			neighbor.setG(cost);
			neighbor.setH(this.h(neighbor));
			visited.put(neighbor.getPos(), neighbor);
			queue.add(neighbor);
		}
	}
}


public void addToVisited(Node node) {
	visited.put(node.getPos(), node);
}

public double h(Node node){
	return node.calculateDistance(end);
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
		if (o>5000) {
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



