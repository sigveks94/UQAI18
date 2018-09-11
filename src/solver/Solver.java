package solver;


import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


import problem.*;


public class Solver {
	private ProblemSpec ps;
	private State state;
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
		this.state = new State(this.ps);
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
	
	public double getWidth() {
		return this.width;
	}

	public List<Node> makePath(Node start, Node goal) {
		AStar astar = new AStar();
		start.setParent(null);
		start.setG(0);
		start.setH(start.calculateDistance(goal));
		astar.setInitialQueue(start);
		astar.addToVisited(start);
		astar.setEnd(goal);
		astar.find();
		List<Node> path = astar.getPath();
		Collections.reverse(path);
		return path;
		
	}
	
public void initiateHashMaps() {
	for(Box b : ps.getMovingBoxes()) {
		boxConnectedBox.put(b, new ArrayList<>());
	}
	
	for(StaticObstacle so : ps.getStaticObstacles()) {
		staticConnectedStatic.put(so, new ArrayList<>());
		staticConnectedBox.put(so, new ArrayList<>());
	}
	for(Box b : ps.getMovingObstacles()) {
		boxConnectedBox.put(b, new ArrayList<>());
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

public static double doubleFormatter(double d) {
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
 * @return center value of box B.
 * 
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
 * @param LowerLeftPoint
 * @return center value of Point2D
 */
public Point2D getCenter(Point2D LowerLeftPoint) {
	Double x = doubleFormatter(LowerLeftPoint.getX() + (width/2));
	Double y = doubleFormatter(LowerLeftPoint.getY() + (width/2));
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


public void deleteBoxSampling(Box b) {
	
	List<Node> nodesConnectedToBox = new ArrayList<>();
	nodesConnectedToBox = boxNodes.get(b);
	
	for(Node n : nodesConnectedToBox) {
		nodes.remove(n);
	}
	
	for(int i = 0; i < nodesConnectedToBox.size(); i++) {
		Node n = nodesConnectedToBox.get(i);
		List<Node> edges = n.getEdges();
		for(int j = 0; j < edges.size(); j++) {
			Node m = edges.get(j);
			n.removeEdge(m);
			m.removeEdge(n);
		}
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
	
	int discretizations = (int) Math.ceil(distance/width) - 2;
	
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
			for(int i = 1 ; i <= discretizations ; i++) {
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

/**
 * 
 * @param b
 * 
 * Retrieves the list of nodes connected to the static obstacle.
 * Identifies which nodes are present.
 * 
 * Node1 = bottom left node
 * Node2 = bottom right node
 * Node3 = top left node
 * Node4 = top right node
 * 
 * 
 * Checks whether the edges between the nodes are collision free, and if so stores the nodes in each others edgeList.
 */


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

/**
 * 
 * @return list of all nodes
 */
public List<Node> getAllNodes(){
	return nodes;
}


/**
 * Iterates through every combination of Moving Box - Moving Box, Moving Box - Moving Obstacle, Moving Box - Static Obstacle
 * Moving Obstacle - Moving Obstacle, Moving Obstacle - Static Obstacle and Static Obstacle - Static Obstacle to connect
 * free-path edges between all combinations.
 */

//SuperMethod 
public void createEdgesBetweenAllBoxes() {
	
	for(int i = 0; i < ps.getMovingBoxes().size() ; i++) {
		for(int j = 1; j < ps.getMovingBoxes().size(); j++) {
			if(i == j || boxConnectedBox.get(ps.getMovingBoxes().get(i)).contains(ps.getMovingBoxes().get(j))) {
				continue;
			} else {
				connectTwoBoxes(ps.getMovingBoxes().get(i), ps.getMovingBoxes().get(j)); //connecting Moving Boxes to Moving Boxes
			}
		}
		for(int j = 0; j < ps.getMovingObstacles().size(); j++) {
			connectTwoBoxes(ps.getMovingBoxes().get(i), ps.getMovingObstacles().get(j)); //connecting Moving Boxes to Moving Obstacles
			
		}
		for(int j = 0; j < ps.getStaticObstacles().size(); j++) {
			connectBoxStaticObstacle(ps.getStaticObstacles().get(j), ps.getMovingBoxes().get(i)); //connecting Moving Boxes to Static Obstacles
		}
	}
	
	for(int i = 0; i < ps.getMovingObstacles().size(); i++) {
		for(int j = 1; j < ps.getMovingObstacles().size(); j++) {
			if(i == j || boxConnectedBox.get(ps.getMovingObstacles().get(i)).contains(ps.getMovingObstacles().get(j))) {
				continue;
			} else {
				connectTwoBoxes(ps.getMovingObstacles().get(i), ps.getMovingObstacles().get(j));	//connecting Moving obstacles to Moving obstacles
			}
		}
		for(int j = 0; j < ps.getStaticObstacles().size(); j++) {
			connectBoxStaticObstacle(ps.getStaticObstacles().get(j), ps.getMovingObstacles().get(i)); //connecting Moving obstacles to static obstacles
		}
	}
	
	for(int i = 0; i < ps.getStaticObstacles().size(); i++) {
		for(int j = 1; j < ps.getStaticObstacles().size(); j++) {
			if(i == j || staticConnectedStatic.get(ps.getStaticObstacles().get(i)).contains(ps.getStaticObstacles().get(j))) {
				continue;
			} else {
				connectTwoStaticObstacles(ps.getStaticObstacles().get(i), ps.getStaticObstacles().get(j)); //connecting static obstacles to static obstacles
			}
		}
	}
}

/**
 * 
 * @param StaticObstacle 1
 * @param StaticObstacle 2
 * 
 * Retrieves the nodes connected to each SO. If either of them are null, there are no possible edges.
 * Finds the best pair of nodes to connect (best meaning the shortest distance).
 * Connects this pair of nodes via a help-node if collision-free path.
 * 
 */


private void connectTwoStaticObstacles(StaticObstacle so1, StaticObstacle so2) {
	
	List<Node> nodesConnectedToSO1 = staticObstacleNodes.get(so1);
	List<Node> nodesConnectedToSO2 = staticObstacleNodes.get(so2);
	
	if(nodesConnectedToSO1 == null || nodesConnectedToSO2 == null) {
		return;
	}
	
	Node bestNode1 = null;
	Node bestNode2 = null;
	double distanceBetweenNode = 100;
	
	for(int i = 0; i < nodesConnectedToSO1.size(); i++) {
		for(int j = 0; j < nodesConnectedToSO2.size(); j++) {
			Node currentNode1 = nodesConnectedToSO1.get(i);
			Node currentNode2 = nodesConnectedToSO2.get(j);
			double currentDistance = currentNode1.calculateDistance(currentNode2);
			if(currentDistance < distanceBetweenNode) {
				distanceBetweenNode = currentDistance;
				bestNode1 = currentNode1;
				bestNode2 = currentNode2;
			}
		}
	}
	
	List<StaticObstacle> boxes1 = staticConnectedStatic.get(so1);
	List<StaticObstacle> boxes2 = staticConnectedStatic.get(so2);
	
	boxes1.add(so2);
	boxes2.add(so1);
	
	if(bestNode1.getPos().getX() == bestNode2.getPos().getX() || bestNode1.getPos().getY() == bestNode2.getPos().getY()) {
		if(isCollisionFreeEdge(bestNode1, bestNode2)) {
			bestNode1.addEdge(bestNode2);
			bestNode2.addEdge(bestNode1);
			staticConnectedStatic.put(so1, boxes1);
			staticConnectedStatic.put(so2, boxes2);
		}
	} else {
		if(connectViaHelpNode(bestNode1, bestNode2)) {
			staticConnectedStatic.put(so1, boxes1);
			staticConnectedStatic.put(so2, boxes2);
		}
		return;
	}
	
	
	
	
	
}

/**
 * 
 * @param StaticObstacle
 * @param Box
 * 
 * Retrieves the nodes connected to SO and to box. If either of them are null, there are no possible edges.
 * Finds the best pair of nodes to connect (best meaning the shortest distance).
 * Connects this pair of nodes via a help-node if collision-free path.
 * 
 */


private void connectBoxStaticObstacle(StaticObstacle so, Box b) {
	
	List<Node> nodesConnectedToSO = staticObstacleNodes.get(so);
	List<Node> nodesConnectedToBox = boxNodes.get(b);
	
	if(nodesConnectedToSO == null || nodesConnectedToBox == null) {
		return;
	}
	
	Node bestNode1 = null;
	Node bestNode2 = null;
	double distanceBetweenNode = 100;
	
	for(int i = 0; i < nodesConnectedToSO.size(); i++) {
		for(int j = 0; j < nodesConnectedToBox.size(); j++) {
			Node currentNode1 = nodesConnectedToSO.get(i);
			Node currentNode2 = nodesConnectedToBox.get(j);
			double currentDistance = currentNode1.calculateDistance(currentNode2);
			if(currentDistance < distanceBetweenNode) {
				distanceBetweenNode = currentDistance;
				bestNode1 = currentNode1;
				bestNode2 = currentNode2;
			}
		}
	}
	
	List<Box> boxes = staticConnectedBox.get(so);
	
	boxes.add(b);
	
	if(bestNode1.getPos().getX() == bestNode2.getPos().getX() || bestNode1.getPos().getY() == bestNode2.getPos().getY()) {
		if(isCollisionFreeEdge(bestNode1, bestNode2)) {
			bestNode1.addEdge(bestNode2);
			bestNode2.addEdge(bestNode1);
			staticConnectedBox.put(so, boxes);
		}
	} else {
		if(connectViaHelpNode(bestNode1, bestNode2)) {
			staticConnectedBox.put(so, boxes);
		}
		return;
	}
}




/**
 * 
 * @param Box 1
 * @param Box 2
 * 
 * Retrieves the nodes connected to each box. If either of them are null, there are no possible edges.
 * Finds the best pair of nodes to connect (best meaning the shortest distance).
 * Connects this pair of nodes via a help-node if collision-free path.
 * 
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

/**
 * 
 * @param bestNode1
 * @param bestNode2
 * @return true if successfully made an edge, otherwise false
 * 
 * 
 * Figures out the position of the helpPoint. First takes the X-value of first parameter and Y-value of second parameter.
 * If that is a collisionPoint --> switch to opposite helpPoint. If this is also in collision, return.
 * 
 * Create a helpNode at the helpPoint.
 * 
 * If both the path from node1 to helpNode AND the path from helpNode to node2, then add the helpNode to nodes and create the edges.
 * 
 * 
 */

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


/**
 * 
 * @param movingBox
 * 
 * Figures out which index the movingBox has in the list of movingBoxes from ProblemSpec.
 * Creates a Point2D at the center of the goalPosition to this movingBox.
 * Creates a GoalNode of the goalPoint.
 * Sets the goalNode of this box to goalNode.
 * Sets the belonging box of this goalNode to this box.
 * 
 * 
 */

private void connectBoxToGoal(MovingBox movingBox) {	
	int index = ps.getMovingBoxes().indexOf(movingBox);
	Point2D center = getCenter(ps.getMovingBoxEndPositions().get(index));
	GoalNode goalNode = new GoalNode(center);
	nodes.add(goalNode);
	movingBox.setGoalNode(goalNode);
	goalNode.setGoalBox(movingBox);
}

/**
 * 
 * @param movingBox
 * @return the Node created
 * 
 * Creates a node at the center of the MovingBox, making this the start node for the movingBox.
 */

private Node createStartBoxNode(MovingBox b) {
	
	Point2D centerPoint = new Point2D.Double(b.getRect().getCenterX(), b.getRect().getCenterY());
	StartBoxNode centerNode = new StartBoxNode(centerPoint);
	nodes.add(centerNode);
	centerNode.setStartBox(b);
	b.setStartNode(centerNode);
	return centerNode;
}


/**
 * 
 * @param box
 * 
 * connects the goalNode of the box to the rest of the path-tree via the node in the path-tree that is closest to the goalNode via a helpNode.
 * 
 */

private void connectGoalNode(Box b) {
	
	MovingBox movingBox = (MovingBox) b;
	Node goalNode = movingBox.getGoalNode();
	Node closestNode = null;
	double distance = 100;
	
	for(Node n : nodes) {
		if(n.equals(goalNode)) {
			continue;
		}
		double currentDistance = n.calculateDistance(goalNode);
		if(currentDistance < distance) {
			distance = currentDistance;
			closestNode = n;
		}
	}
	connectViaHelpNode(goalNode, closestNode);
}

/**
 * 
 * @param startBox
 * 
 * Connects the startNode of the box to one of the nodes around this box. Creates vertical and horizontal edge.
 */

private void connectStartBoxNode(MovingBox startBox) {
	Node connectNode = boxNodes.get(startBox).get(0);
	Node startNode = createStartBoxNode(startBox);
	Point2D helpPoint = new Point2D.Double(startNode.getPos().getX(), connectNode.getPos().getY());
	Node helpNode = new HelpNode(helpPoint);
	nodes.add(helpNode);
	startNode.addEdge(helpNode);
	helpNode.addEdge(startNode);
	connectNode.addEdge(helpNode);
	helpNode.addEdge(connectNode);
}

/**
 * Main method to make all operations in correct order.
 */

public void initiate() {
	makeInitialSampling();
    makeInitialEdges();
    createEdgesBetweenAllBoxes();
    MovingBox mb = (MovingBox) ps.getMovingBoxes().get(0);
    connectBoxToGoal(mb);
    connectGoalNode(mb);
    connectStartBoxNode(mb);
    Node startNode = mb.getStartNode();
    Node goalNode = mb.getGoalNode();
    List<Node> path = makePath(startNode, goalNode);
    
    //PathBuilder pb = new PathBuilder(path);
    //String outPutString = generateOutputMove(path, pb);
    //System.out.println(outPutString);
    
    
    //ONE TEMPORARY MOVE!! NEEDS AN IMPLEMENTATION FOR EACH MOVE LATER ON!!!!!
    PathBuilder pb = new PathBuilder(this, state, null, path);
    
}

public State getState() {
	return this.state;
}

private Node sampleRobotNode(RobotConfig robotPosition) {
	Node robotNode = new RobotNode(robotPosition.getPos());
	return robotNode;
}

/**
 * 
 * @return hashMap of nodes connected to boxes
 */
public HashMap<Box, List<Node>> getBoxNodes() {
	return boxNodes;
}

/**
 * 
 * @return hashMap of nodes connected to staticObstacles
 */
public HashMap<StaticObstacle, List<Node>> getStaticObstacleNodes() {
	return staticObstacleNodes;
}



////Have to consider the case where the robot should rotate. In this period the box will have to stand still.
//private String generateOutputMove(List<Node> path, PathBuilder pb) {
//	
//	//StartBoxNode startBoxNode = (StartBoxNode) path.get(0);
//	//int index = ps.getMovingBoxes().indexOf(startBoxNode.getStartBox());
//	String robot = "0.001 0.001 0.0";
//	String box2 = "0.2 0.8";
//	String obst1 = "0.5 0.7";
//	String line= "";
//	String[] box1 = pb.getPath().split("\n");
//	int numberOfLines = box1.length;
//	for(int i = 0; i < numberOfLines ; i++) {
//		if(i == numberOfLines - 1) {
//			line += robot + " " +  box1[i] + " " + box2 + " " + obst1;
//		}
//		else {
//			line += robot + " " +  box1[i] + " " + box2 + " " + obst1 + "\n";
//		}
//	}
//	
//	return line;
//	
	
	
	
//}

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
	 * 
	 * Managed to find a path for movingBoxes.
	 * 	A* search with cost to current node and simple distance to goal heuristic.
	 * 		If no solution:
	 * 			
	 * 			Try to move the other box first. 
	 * 			
	 * 
	 * 			If still no solution; move obstacle.
	 * 
	 * Have to find a path for the robot to the active movingBox.
	 * 
	 * 	Sample a RobotNode from initialRobotConfig
	 * 	Connect this node to the closest node in tree via helpNode.
	 * 	Sample a node at the center of the active movingBox in the direction the box is to be moved.
	 * 	Use A* search with RobotNode as startNode and the newly sampled centerEdgeNode as goalNode.
	 * 		
	 * 		
	 * 	
	 * 		
	 * 
	 * Phase4 - Move phase 
	 * 
	 * 	Translate the robotPath into movingSteps for the robot.
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

