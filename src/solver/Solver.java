package solver;


import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import problem.*;


public class Solver {
	private ProblemSpec ps;
	private List<Node> nodes;
	private final double width;
	private final double fakeWidth;
	private final double fakeHalfWidth;
	private HashMap<Box,List<Node>> boxNodes;
	private HashMap<StaticObstacle, List<Node>> staticObstacleNodes;
	private final double halfWidth;
	private HashMap<StaticObstacle, List<StaticObstacle>> staticConnectedStatic; //HashMap of which static obstacles are connected to other static obstacles.
	private HashMap<StaticObstacle, List<Box>> staticConnectedBox; //HashMap of which static obstacles are connected to other boxes.
	private HashMap<Box, List<Box>> boxConnectedBox; //HashMap of which boxes are connected to other boxes.
	
	
	public Solver(ProblemSpec ps) {
		this.ps= ps;
		nodes = new ArrayList<>();
		halfWidth = doubleFormatter(ps.getRobotWidth()/2);
		width = doubleFormatter(ps.getRobotWidth());
		fakeWidth = doubleFormatter(ps.getRobotWidth() + 0.0001);
		fakeHalfWidth = doubleFormatter((ps.getRobotWidth()/2) + 0.0001);
		boxNodes = new HashMap<>();
		staticObstacleNodes  = new HashMap<>();
		staticConnectedStatic = new HashMap<>();
		staticConnectedBox = new HashMap<>();
		boxConnectedBox = new HashMap<>();
		initiateHashMaps();
	}
	
	
public void initiateHashMaps() {
	for(Box b : ps.getMovingBoxes()) {
		boxConnectedBox.put(b, new ArrayList<>());
	}
	
	for(StaticObstacle so : ps.getStaticObstacles()) {
		staticConnectedStatic.put(so, new ArrayList<>());
		staticConnectedBox.put(so, new ArrayList<>());
	}
}


/**
 * Makes the initial sampling of the free space.
 * Retrieves center point of the box
 * Sample nodes at each corner of an obstacle if point not in collision.
 * i = 1 --> bottom left point
 * i = 2 --> bottom right point
 * i = 3 --> top left point
 * i = 4 --> top right point
 * 
 * Connects all nodes created to the box. 
 * 
 */
	
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
 * Checks whether the Box is a movingBox or movingObstacle.
 * 
 * Creates a 2DPoint at such distance from the box so that a movingBox just could get around.
 * Referenced from center-point.
 * 
 * i = 1 means bottom left corner
 * i = 2 means bottom right corner
 * i = 3 means top left corner
 * i = 4 means top right corner
 * 
 * @return Node
 */

public Node addNode(Box b, Point2D center, int i) {
	if(b instanceof MovingBox) {
		if(i==1) {
			Double x = doubleFormatter(center.getX() - (fakeWidth));
			Double y = doubleFormatter(center.getY() - (fakeWidth));
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				Node node = new BoxNode(p);
				nodes.add(node);
				return node;
			}
		}
		if(i==2) {
			Double x = doubleFormatter(center.getX() + (fakeWidth));
			Double y = doubleFormatter(center.getY() - (fakeWidth));
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				Node node = new BoxNode(p);
				nodes.add(node);
				return node;
			}
		}
		if(i==3) {
			Double x = doubleFormatter(center.getX() - (fakeWidth));
			Double y = doubleFormatter(center.getY() + (fakeWidth));
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				Node node = new BoxNode(p);
				nodes.add(node);
				return node;
			}
		}
		if(i==4) {
			Double x = doubleFormatter(center.getX() + (fakeWidth));
			Double y = doubleFormatter(center.getY() + (fakeWidth));
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				Node node = new BoxNode (p);
				nodes.add(node);
				return node;
			}
		}
		
	}
	if(b instanceof MovingObstacle){
		double obswidth = doubleFormatter(b.getWidth());
		double obswidthHalf = doubleFormatter(obswidth/2);
		
		if(i==1) {
			Double x = doubleFormatter(center.getX() - (obswidthHalf + fakeHalfWidth));
			Double y = doubleFormatter(center.getY() - (obswidthHalf + fakeHalfWidth));
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				Node node = new BoxNode(p);
				nodes.add(node);
				return node;
			}
		}
		if(i==2) {
			Double x = doubleFormatter(center.getX() + (obswidthHalf + fakeHalfWidth));
			Double y = doubleFormatter(center.getY() - (obswidthHalf + fakeHalfWidth));
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				Node node = new BoxNode(p);
				nodes.add(node);
				return node;
			}
		}
		if(i==3) {
			Double x = doubleFormatter(center.getX() - (obswidthHalf + fakeHalfWidth));
			Double y = doubleFormatter(center.getY() + (obswidthHalf + fakeHalfWidth));
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				Node node = new BoxNode(p);
				nodes.add(node);
				return node;
			}
		}
		if(i==4) {
			Double x = doubleFormatter(center.getX() + (obswidthHalf + fakeHalfWidth));
			Double y = doubleFormatter(center.getY() + (obswidthHalf + fakeHalfWidth));
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

/**
 * 
 * @param obstacle
 * @param i
 * @return Node
 * 
 * Create a 2DPoint at each corner of the static obstacle and make it a node sample if collision free.
 *
 * 
 */

public Node addNode(StaticObstacle obstacle, int i) {
	//BOTTOM LEFT
	if(i==1) {
		Point2D bl = new Point2D.Double(doubleFormatter(obstacle.getRect().getMinX() - (fakeHalfWidth)), doubleFormatter(obstacle.getRect().getMinY()-(fakeHalfWidth)));
		if(isCollisionFreePoint(bl)) {
			Node node = new ObstacleNode(bl);
			nodes.add(node);
			return node;
		}
	}
	
	//BOTTOM RIGHT
	if(i==2) {
		Point2D br = new Point2D.Double(doubleFormatter(obstacle.getRect().getMaxX() + (fakeHalfWidth)), doubleFormatter(obstacle.getRect().getMinY()-(fakeHalfWidth)));
		if(isCollisionFreePoint(br)) {
			Node node = new ObstacleNode(br);
			nodes.add(node);
			return node;
		}
	}
	
	//TOP LEFT
	if(i==3) {
		Point2D tl = new Point2D.Double(doubleFormatter(obstacle.getRect().getMinX() - (fakeHalfWidth)), doubleFormatter(obstacle.getRect().getMaxY()+(fakeHalfWidth)));
		if(isCollisionFreePoint(tl)) {
			Node node = new ObstacleNode(tl);
			nodes.add(node);
			return node;
		}
	}
	
	//TOP RIGHT
	if(i==4) {
		Point2D tr = new Point2D.Double(doubleFormatter(obstacle.getRect().getMaxX() + (fakeHalfWidth)), doubleFormatter(obstacle.getRect().getMaxY()+(fakeHalfWidth)));
		if(isCollisionFreePoint(tr)) {
			Node node = new ObstacleNode(tr);
			nodes.add(node);
			return node;
		}
	}
	return null;
	
}

/**
 * 
 * @param d
 * @return
 * 
 * Simple helper-method to format double values to 4 decimal points.
 * 
 */

public static Double doubleFormatter(double d) {
	NumberFormat formatter = new DecimalFormat("#0.0000");
	String doubleString = formatter.format(d);
	return Double.parseDouble(doubleString);
}

/**
 * 
 * @param point
 * @return false if not inside board
 * 
 * point is the center-value. 
 * Therefore X value has to be within range [0 + w/2 , 1 - w/2]
 * similarly Y value has to be within range [0 + w/2 , 1 - w/2]
 * 
 * 
 */

public boolean isInsideBoard(Point2D point) {
	if((point.getX() < 0 + (halfWidth)) || (point.getX() > 1 - (halfWidth))) {
		return false;
	}
	if((point.getY() < 0 + (halfWidth)) || (point.getY() > 1 - (halfWidth))) {
		return false;
	}
	return true;
}

/**
 * 
 * @param b
 * @return
 * 
 * Returns the center value of box B.
 * 
 */

public Point2D getCenter(Box b) {
	//NumberFormat formatter = new DecimalFormat("#0.0000");
	Double x = doubleFormatter(b.getPos().getX() + (b.getWidth()/2));
	Double y = doubleFormatter(b.getPos().getY() + (b.getWidth()/2));
	
	//String xValueString = formatter.format(x);
	//String yValueString = formatter.format(y);
	
	//x = Double.parseDouble(xValueString);
	//y = Double.parseDouble(yValueString);
	
	Point2D p = new Point2D.Double(x, y);
	return p;
}

/**
 * 
 * @param Point2D point
 * @return False if a point is outside the board or you can't fit a movingBox around the point without intersection. Else return true
 * 
 * Creates a fakeBox around the point as a center value.
 * Checks whether this fake box intersects some of the obstacles.
 * 
 */

public boolean isCollisionFreePoint(Point2D point) {
	if(!(isInsideBoard(point))){
		return false;
	}
	
	
	Rectangle2D fakeBox = new Rectangle2D.Double(doubleFormatter(point.getX() - (halfWidth)), doubleFormatter(point.getY() - (halfWidth)), width, width);
	
	for (Box b:ps.getMovingBoxes()) {
		//System.out.println("Hi this is the moving box upper left node: " + b.getRect().getMinX() + " " + b.getRect().getMaxY());
		//System.out.println("Hi this is the moving box upper right node: " + b.getRect().getMaxX() + " " + b.getRect().getMaxY());
		if(b.getRect().intersects(fakeBox)) {
			//System.out.println(b.getRect().getCenterX() + " : " + b.getRect().getCenterY() + " nextbox" +
				//fakeBox.getCenterX() + " : " + fakeBox.getCenterY());
			return false;
		}
	}
	for (Box b:ps.getMovingObstacles()) {
		if(b.getRect().intersects(fakeBox)) {
			return false;
		}
	}
	for (StaticObstacle o:ps.getStaticObstacles()) {
		if(o.getRect().intersects(fakeBox)) {
			return false;
		}
	}
	
	return true;
	
}

/**
 * 
 * @param b
 * 
 * Retrieves the nodes connected to the box. Removes them from the nodeslist.
 * 
 */

// IMPLEMENT LOGIC TO ALSO REMOVE EDGES.
public void deleteBoxSampling(Box b) {
	
	List<Node> nodesConnectedToBox = new ArrayList<>();
	nodesConnectedToBox = boxNodes.get(b);
	
	for(Node n : nodesConnectedToBox) {
		nodes.remove(n);
	}
	boxNodes.remove(b);
	

}

/**
 * 
 * @param b
 * 
 * Creates a new sample of nodes around a box.
 * 
 */
public void makeBoxSampling(Box b) {
	Point2D center = getCenter(b);
	List<Node> familyNodes = new ArrayList<>();
	for(int i = 1; i < 5; i++) {
		Node node = addNode(b,center,i);
		familyNodes.add(node);
	}
	boxNodes.put(b, familyNodes);
}


/**
 * 
 * @param Node from
 * @param Node to
 * @return true if collision free edge
 * 
 * 
 * Identifies which nodes to connect.
 * Creates a fakeBox around the node to start from. Checks in each iteration whether the fakeBox position intersects other obstacles.
 * Center point moves width-steps each time.
 * 
 */
public boolean isCollisionFreeEdge(Node from, Node to) {
	
	//Here we could implement logic/methods to find out which type of edge this is.
	
	double distance = doubleFormatter(from.calculateDistance(to));
	
	int discretizations = (int) Math.ceil(distance/width);
	
	if(from.getPos().getX() == to.getPos().getX()) {
		if(from.getPos().getY() > to.getPos().getY()) {
			for(int i = 1 ; i <= discretizations ; i++) {
				Point2D centerFakeBox = new Point2D.Double(doubleFormatter(from.getPos().getX()), doubleFormatter(from.getPos().getY() - (i*width)));
				if(!isCollisionFreePoint(centerFakeBox)) {
					return false;
					}
				}
			}
		if(from.getPos().getY() < to.getPos().getY()) {
			for(int i = 1 ; i <= discretizations ; i++) {
				Point2D centerFakeBox = new Point2D.Double(doubleFormatter(from.getPos().getX()), doubleFormatter(from.getPos().getY() + (i*width)));
				if(!isCollisionFreePoint(centerFakeBox)) {
					return false;
					}
				}
			}
		}
	
	if(from.getPos().getY() == to.getPos().getY()) {
		if(from.getPos().getX() > to.getPos().getX()) {
			for(int i = 1 ; i < discretizations ; i++) {
				Point2D centerFakeBox = new Point2D.Double(doubleFormatter(from.getPos().getX() - (i*width)), doubleFormatter(from.getPos().getY()));
				//System.out.println(centerFakeBox);
				if(!isCollisionFreePoint(centerFakeBox)) {
					return false;
					}
				}
			}
		if(from.getPos().getX() < to.getPos().getX()) {
			for(int i = 1 ; i <= discretizations ; i++) {
				Point2D centerFakeBox = new Point2D.Double(doubleFormatter(from.getPos().getX() + (i*width)), doubleFormatter(from.getPos().getY()));
				if(!isCollisionFreePoint(centerFakeBox)) {
					return false;
					}
				}
			}
		}
	return true;
	}

/**
 * 
 * @param b
 * 
 * Retrieves the list of nodes connected to the box.
 * Identifies which nodes are present.
 * Again:
 * 
 * Node1 = bottom left node
 * Node2 = bottom right node
 * Node3 = top left node
 * Node4 = top right node
 * 
 * 
 * Checks whether the edges between the nodes are collision free, and if so stores the nodes in each others edgeList.
 */

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
		} else {
			System.out.println("Node 4 :" + node4 + " , Node3: " + node3);
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


/**
 * creates initialEdges between nodes connected to boxes.
 */
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

//SuperMethod 
public void createEdgesBetweenAllBoxes() {
	
	for(int i = 0; i < ps.getMovingBoxes().size() ; i++) {
		for(int j = 1; j < ps.getMovingBoxes().size(); j++) {
			if(i == j || boxConnectedBox.get(ps.getMovingBoxes().get(i)).contains(ps.getMovingBoxes().get(j))) {
				continue;
			} else {
				connectTwoBoxes(ps.getMovingBoxes().get(i), ps.getMovingBoxes().get(j));
			}
		}
	}
}



private void connectTwoStaticObstacles(StaticObstacle so1, StaticObstacle so2) {
	
	List<Node> nodesConnectedToSO1 = staticObstacleNodes.get(so1);
	List<Node> nodesConnectedToSO2 = staticObstacleNodes.get(so2);
	
	if(nodesConnectedToSO1 == null || nodesConnectedToSO2 == null) {
		
		
		
		
		
		
	}
	
	
	
	
}

private void connectBoxStaticObstacle(StaticObstacle so, Box b) {
	
}




/**
 * 
 * @param nodeToBeEvaluated
 * @param b
 * @return node closest to the evaluated node, or null if no.
 * 
 * Finds the node closest to this node to a certain box.
 */
private void connectTwoBoxes(Box b1, Box b2) {
	List<Node> nodesConnectedToBox1 = boxNodes.get(b1);
	List<Node> nodesConnectedToBox2 = boxNodes.get(b2);
 	if(nodesConnectedToBox1 == null || nodesConnectedToBox2 == null ) {
		return;
	}
 	Node bestNode1 = null;
	Node bestNode2 = null;
	double distanceBetweenNode = 100;
	
	for(int i = 0; i < nodesConnectedToBox1.size(); i++) {
		for(int j = 0; j < nodesConnectedToBox2.size(); j++) {
			Node currentNode1 = nodesConnectedToBox1.get(i);
			Node currentNode2 = nodesConnectedToBox2.get(j);
			double currentDistance = currentNode1.calculateDistance(currentNode2);
			if(currentDistance < distanceBetweenNode) {
				distanceBetweenNode = currentDistance;
				bestNode1 = currentNode1;
				bestNode2 = currentNode2;
			}
		}
		
	}
	
	List<Box> boxes1 = boxConnectedBox.get(b1);
	List<Box> boxes2 = boxConnectedBox.get(b2);
	
	boxes1.add(b2);
	boxes2.add(b1);
	
	if(bestNode1.getPos().getX() == bestNode2.getPos().getX() || bestNode1.getPos().getY() == bestNode2.getPos().getY()) {
		if(isCollisionFreeEdge(bestNode1, bestNode2)) {
			bestNode1.addEdge(bestNode2);
			bestNode2.addEdge(bestNode1);
			boxConnectedBox.put(b1, boxes1);
			boxConnectedBox.put(b2, boxes2);
		}
	} else {
		if(connectViaHelpNode(bestNode1, bestNode2)) {
			boxConnectedBox.put(b1, boxes1);
			boxConnectedBox.put(b2, boxes2);
		}
		return;
	}
	
}

private boolean connectViaHelpNode(Node bestNode1, Node bestNode2) {
	
	Point2D helpPoint = new Point2D.Double(bestNode1.getPos().getX(), bestNode2.getPos().getY());
	if(!isCollisionFreePoint(helpPoint)) {
		helpPoint.setLocation(bestNode2.getPos().getX(), bestNode1.getPos().getY());
		if(!isCollisionFreePoint(helpPoint)) {
			return false;
		}
	}

	Node helpNode = new HelpNode(helpPoint);
	
	if(isCollisionFreeEdge(bestNode1, helpNode) && isCollisionFreeEdge(helpNode, bestNode2)) {
		nodes.add(helpNode);
		bestNode1.addEdge(helpNode);
		helpNode.addEdge(bestNode1);
		bestNode2.addEdge(helpNode);
		helpNode.addEdge(bestNode2);
		return true;
	}
	return false;	
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
	 * 
	 * 	Strategy for creating edges between boxes:
	 * 		For each node connected to each box, create an edge to the closest node of every box/obstacle that is not a part of the same obstacle. Do this for all obstacles.
	 * 		Create help nodes that serve to maintain left-right-up-down constraint, discard if in collision (but should not be if we´re thinking right)
	 * 		
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

