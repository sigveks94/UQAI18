package solver;


import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import problem.*;


public class Solver {
	private ProblemSpec ps;
	private List<Node> nodes;
	private final double width;
	private HashMap<Box,List<Node>> boxNodes;
	private HashMap<StaticObstacle, List<Node>> staticObstacleNodes;
	
	
	public Solver(ProblemSpec ps) {
		this.ps= ps;
		nodes = new ArrayList<>();
		width = ps.getRobotWidth();
		boxNodes = new HashMap<>();
		staticObstacleNodes  = new HashMap<>();
	}

	
public void makeInitialSampling() {
	for(Box movingbox: ps.getMovingBoxes()){
		
		Point2D center = getCenter(movingbox);
		List<Node> familyNodes = new ArrayList<>();
		for(int i = 1 ; i < 5 ; i++) {
			Node node = addNode(movingbox,center,i);
			if(node != null) {
				familyNodes.add(node);
			}
		}
		boxNodes.put(movingbox, familyNodes);
	}
	
	
	for(Box movingobstacle: ps.getMovingObstacles()) {
		
		Point2D centerobst = getCenter(movingobstacle);
		List<Node> familyNodes = new ArrayList<>();
		for(int i = 1 ; i < 5 ; i++) {
			Node node = addNode(movingobstacle,centerobst,i);
			if(node != null) {
				familyNodes.add(node);
			}
		}
		boxNodes.put(movingobstacle, familyNodes);
	}
	
	for(StaticObstacle so : ps.getStaticObstacles()) {
		List<Node> familyNodes = new ArrayList<>();
		for(int i = 1; i < 5; i++) {
			Node node = addNode(so,i);
			if(node != null) {
				familyNodes.add(node);
			}
		}
		staticObstacleNodes.put(so, familyNodes);
	}
	
}

/**
 * 
 * @param b
 * @param center
 * @param i
 * 
 * i = 1 means bottom left corner
 * i = 2 means bottom right corner
 * i = 3 means top left corner
 * i = 4 means top right corner
 */

public Node addNode(Box b, Point2D center, int i) {
	if(b instanceof MovingBox) {
		if(i==1) {
			Double x = center.getX() - (width);
			Double y = center.getY() - (width);
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				Node node = new BoxNode(p);
				nodes.add(node);
				return node;
			}
		}
		if(i==2) {
			Double x = center.getX() + (width);
			Double y = center.getY() - (width);
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				Node node = new BoxNode(p);
				nodes.add(node);
				return node;
			}
		}
		if(i==3) {
			Double x = center.getX() - (width);
			Double y = center.getY() + (width);
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				Node node = new BoxNode(p);
				nodes.add(node);
				return node;
			}
		}
		if(i==4) {
			Double x = center.getX() + (width);
			Double y = center.getY() + (width);
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				Node node = new BoxNode (p);
				nodes.add(node);
				return node;
			}
		}
		
	}
	if(b instanceof MovingObstacle){
		double obswidth = b.getWidth();
		if(i==1) {
			Double x = center.getX() - (obswidth/2 + width/2);
			Double y = center.getY() - (obswidth/2 + width/2);
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				Node node = new BoxNode(p);
				nodes.add(node);
				return node;
			}
		}
		if(i==2) {
			Double x = center.getX() + (obswidth/2 + width/2);
			Double y = center.getY() - (obswidth/2 + width/2);
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				Node node = new BoxNode(p);
				nodes.add(node);
				return node;
			}
		}
		if(i==3) {
			Double x = center.getX() - (obswidth/2 + width/2);
			Double y = center.getY() + (obswidth/2 + width/2);
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				Node node = new BoxNode(p);
				nodes.add(node);
				return node;
			}
		}
		if(i==4) {
			Double x = center.getX() + (obswidth/2 + width/2);
			Double y = center.getY() + (obswidth/2 + width/2);
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				Node node = new BoxNode(p);
				nodes.add(node);
				return node;
			}
		}
	}
	return null;
}

public Node addNode(StaticObstacle obstacle, int i) {
	//BOTTOM LEFT
	if(i==1) {
		Point2D bl = new Point2D.Double(obstacle.getRect().getMinX() - (width/2), obstacle.getRect().getMinY()-(width/2));
		if(isCollisionFreePoint(bl)) {
			Node node = new ObstacleNode(bl);
			nodes.add(node);
			return node;
		}
	}
	
	//BOTTOM RIGHT
	if(i==2) {
		Point2D br = new Point2D.Double(obstacle.getRect().getMaxX() + (width/2), obstacle.getRect().getMinY()-(width/2));
		if(isCollisionFreePoint(br)) {
			Node node = new ObstacleNode(br);
			nodes.add(node);
			return node;
		}
	}
	
	//TOP LEFT
	if(i==3) {
		Point2D tl = new Point2D.Double(obstacle.getRect().getMinX() - (width/2), obstacle.getRect().getMaxY()+(width/2));
		if(isCollisionFreePoint(tl)) {
			Node node = new ObstacleNode(tl);
			nodes.add(node);
			return node;
		}
	}
	
	//TOP RIGHT
	if(i==4) {
		Point2D tr = new Point2D.Double(obstacle.getRect().getMaxX() + (width/2), obstacle.getRect().getMaxY()+(width/2));
		if(isCollisionFreePoint(tr)) {
			Node node = new ObstacleNode(tr);
			nodes.add(node);
			return node;
		}
	}
	return null;
	
}

public boolean isInsideBoard(Point2D point) {
	if((point.getX() < 0 + (width/2)) || (point.getX() > 1 - (width/2))) {
		return false;
	}
	if((point.getY() < 0 + (width/2)) || (point.getY() > 1 - (width/2))) {
		return false;
	}
	return true;
}

public Point2D getCenter(Box b) {
	Double x = b.getPos().getX() + (b.getWidth()/2);
	Double y = b.getPos().getY() + (b.getWidth()/2);
	Point2D p = new Point2D.Double(x, y);
	return p;
}

public boolean isCollisionFreePoint(Point2D point) {
	if(!(isInsideBoard(point))){
		return false;
	}
	Rectangle2D rect = new Rectangle2D.Double(point.getX()-(width/2), point.getY() - (width/2), width, width);
	for (Box b:ps.getMovingBoxes()) {
		if(b.getRect().intersects(rect)) {
			return false;
		}
	}
	for (Box b:ps.getMovingObstacles()) {
		if(b.getRect().intersects(rect)) {
			return false;
		}
	}
	for (StaticObstacle o:ps.getStaticObstacles()) {
		if(o.getRect().intersects(rect)) {
			return false;
		}
	}
	
	return true;
	
}

public void deleteBoxSampling(Box b) {
	
	List<Node> nodesConnectedToBox = new ArrayList<>();
	nodesConnectedToBox = boxNodes.get(b);
	
	for(Node n : nodesConnectedToBox) {
		nodes.remove(n);
	}
	boxNodes.remove(b);
	

}

public void makeBoxSampling(Box b) {
	Point2D center = getCenter(b);
	List<Node> familyNodes = new ArrayList<>();
	for(int i = 1; i < 5; i++) {
		Node node = addNode(b,center,i);
		familyNodes.add(node);
	}
	boxNodes.put(b, familyNodes);
}

public boolean isCollisionFreeEdge(Node from, Node to) {
	
	//Here we could implement logic/methods to find out which type of edge this is.
	
	double distance = from.calculateDistance(to);
	
	int discretizations = (int) Math.ceil(distance/width);
	
	if(from.getPos().getX() == to.getPos().getX()) {
		if(from.getPos().getY() > to.getPos().getY()) {
			for(int i = 1 ; i <= discretizations ; i++) {
				Point2D centerFakeBox = new Point2D.Double(from.getPos().getX(), from.getPos().getY() - (i*width));
				if(!isCollisionFreePoint(centerFakeBox)) {
					return false;
					}
				}
			}
		if(from.getPos().getY() < to.getPos().getY()) {
			for(int i = 1 ; i <= discretizations ; i++) {
				Point2D centerFakeBox = new Point2D.Double(from.getPos().getX(), from.getPos().getY() + (i*width));
				if(!isCollisionFreePoint(centerFakeBox)) {
					return false;
					}
				}
			}
		}
	
	if(from.getPos().getY() == to.getPos().getY()) {
		if(from.getPos().getX() > to.getPos().getX()) {
			for(int i = 1 ; i <= discretizations ; i++) {
				Point2D centerFakeBox = new Point2D.Double(from.getPos().getX() - (i*width), from.getPos().getY());
				if(!isCollisionFreePoint(centerFakeBox)) {
					return false;
					}
				}
			}
		if(from.getPos().getX() < to.getPos().getX()) {
			for(int i = 1 ; i <= discretizations ; i++) {
				Point2D centerFakeBox = new Point2D.Double(from.getPos().getX() + (i*width), from.getPos().getY());
				if(!isCollisionFreePoint(centerFakeBox)) {
					return false;
					}
				}
			}
		}
	return true;
	}

public void createBoxEdges(Box b){
	
	List<Node> nodesToBox = boxNodes.get(b);
	
	Node node1 = null;
	Node node2 = null;
	Node node3 = null;
	Node node4 = null;
	
	if(nodesToBox.isEmpty()) {
		return;
	}
	
	for(Node n : nodesToBox) {
		if(n == null) {
			return;
		}
		if(n.getPos().getX() < getCenter(b).getX()) {
			if(n.getPos().getY() < getCenter(b).getY()) {
				node1 = n;
			}
			else {
				node3 = n;
			}
		}
		else {
			if(n.getPos().getY() < getCenter(b).getY()) {
				node2 = n;
			}
			else {
				node4 = n;
			}
		}
	}
	
	//Lower edge
	if(node1 != null && node2 != null) {
		if(isCollisionFreeEdge(node1, node2)) {
			node1.addEdge(node2);
			node2.addEdge(node1);
		}
	}
	
	//Right edge
	if(node2 != null && node4 != null) {
		if(isCollisionFreeEdge(node2, node4)) {
			node2.addEdge(node4);
			node4.addEdge(node2);
		}
	}
	
	//Upper edge
	if(node4 != null && node3 != null) {
		if(isCollisionFreeEdge(node4, node3)) {
			node4.addEdge(node3);
			node3.addEdge(node4);
		}
	}
	
	//Left edge
	if(node3 != null && node1 != null) {
		if(isCollisionFreeEdge(node3, node1)) {
			node3.addEdge(node1);
			node1.addEdge(node3);
		}
	}
	
}

public void createStaticObstacleEdges(StaticObstacle so){
	
	List<Node> nodesToStaticObstacle = staticObstacleNodes.get(so);
	
	Node node1 = null;
	Node node2 = null;
	Node node3 = null;
	Node node4 = null;
	
	if(nodesToStaticObstacle.isEmpty()) {
		return;
	}
	
	for(Node n : nodesToStaticObstacle) {
		if(n.getPos().getX() < so.getRect().getCenterX()) {
			if(n.getPos().getY() < so.getRect().getCenterY()) {
				node1 = n;
			}
			else {
				node3 = n;
			}
		}
		else {
			if(n.getPos().getY() < so.getRect().getCenterY()) {
				node2 = n;
			}
			else {
				node4 = n;
			}
		}
	}
	
	//Lower edge
	if(node1 != null && node2 != null) {
		if(isCollisionFreeEdge(node1, node2)) {
			node1.addEdge(node2);
			node2.addEdge(node1);
		}
	}
	
	//Right edge
	if(node2 != null && node4 != null) {
		if(isCollisionFreeEdge(node2, node4)) {
			node2.addEdge(node4);
			node4.addEdge(node2);
		}
	}
	
	//Upper edge
	if(node4 != null && node3 != null) {
		if(isCollisionFreeEdge(node4, node3)) {
			node4.addEdge(node3);
			node3.addEdge(node4);
		}
	}
	
	//Left edge
	if(node3 != null && node1 != null) {
		if(isCollisionFreeEdge(node3, node1)) {
			node3.addEdge(node1);
			node1.addEdge(node3);
		}
	}
	
}

public void makeInitialEdges() {
	for(Box b : ps.getMovingBoxes()) {
		createBoxEdges(b);
	}
	
	for(Box b : ps.getMovingObstacles()) {
		createBoxEdges(b);
	}
	
	for(StaticObstacle so : ps.getStaticObstacles()) {
		createStaticObstacleEdges(so);
	}
}

public List<Node> getAllNodes(){
	return nodes;
}


public HashMap<Box, List<Node>> getBoxNodes() {
	return boxNodes;
}


public HashMap<StaticObstacle, List<Node>> getStaticObstacleNodes() {
	return staticObstacleNodes;
}

}


	/*
	 * 
	 * Pseudocode: 
	 * 		
	 * Phase1 - Sampling phase
	 * 
	 * 	Sample initial node (deduced from intialRobotConfig) and goal nodes
	 * 	Sample nodes at corners of all obstacles, discard nodes in collision
	 * 	
	 * 	Collision detection:
	 * 		
	 * 		Simply check whether 2DPoint is occupied by box. A box spans a certain area in 2D.
	 *  
	 * 	
	 *  
	 * 	
	 * 	
	 * 
	 * Phase2 - Connecting phase 
	 * 	
	 * 	An edge is represented within a node with an edgeNodes list.
	 * 	Connect all nodes connected to the same box if collision free path.
	 * 	Create help nodes that serve to maintain left-right-up-down constraint, discard if in collision
	 * 	
	 * 
	 * 		
	 * 
	 * 	Collision detection:
	 * 
	 * 		Discretesize the line connecting two nodes by a density of w length.
	 * 		Check if collision with obstacle.
	 * 
	 * 
	 * 		
	 * 	 
	 * 
	 * Phase3 - Path phase
	 * 
	 * 	A* search with cost to current node and simple distance to goal heuristic.
	 * 		If no solution:
	 * 			
	 * 			Try to move the other box first. 
	 * 			
	 * 
	 * 			If still no solution; move obstacle.
	 * 
	 * 		
	 * Phase4 - Move phase 
	 * 
	 * 	remember new samples of nodes connected to moved box
	 * 	
	 * 	
	 * 
	 * Phase5 - Solution phase
	 * 	
	 * 	
	 *
	 * 
	 * 
	 * 
	 */

