package solver;


import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import problem.*;


public class Solver {
	private ProblemSpec ps;
	private List<Node> nodes;
	private final double width = ps.getRobotWidth();	
	
	public Solver(ProblemSpec ps) {
		this.ps=ps;
	}
	


public void makeInitialSampling() {
	for(Box movingbox: ps.getMovingBoxes()){
		
		Point2D center = getCenter(movingbox);
		for(int i = 1 ; i < 5 ; i++) {
			addNode(movingbox,center,i);
		}
	}
	
	
	for(Box movingobstacle: ps.getMovingObstacles()) {
		Point2D centerobst = getCenter(movingobstacle);
		for(int i = 1 ; i < 5 ; i++) {
			addNode(movingobstacle,centerobst,i);
		}
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

public void addNode(Box b, Point2D center, int i) {
	if(b instanceof MovingBox) {
		if(i==1) {
			Double x = center.getX() - (width);
			Double y = center.getY() - (width);
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				nodes.add(new Node(p));
			}
		}
		if(i==2) {
			Double x = center.getX() + (width);
			Double y = center.getY() - (width);
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				nodes.add(new Node(p));
			}
		}
		if(i==3) {
			Double x = center.getX() - (width);
			Double y = center.getY() + (width);
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				nodes.add(new Node(p));
			}
		}
		if(i==4) {
			Double x = center.getX() + (width);
			Double y = center.getY() + (width);
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				nodes.add(new Node(p));
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
				nodes.add(new Node(p));
			}
		}
		if(i==2) {
			Double x = center.getX() + (obswidth/2 + width/2);
			Double y = center.getY() - (obswidth/2 + width/2);
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				nodes.add(new Node(p));
			}
		}
		if(i==3) {
			Double x = center.getX() - (obswidth/2 + width/2);
			Double y = center.getY() + (obswidth/2 + width/2);
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				nodes.add(new Node(p));
			}
		}
		if(i==4) {
			Double x = center.getX() + (obswidth/2 + width/2);
			Double y = center.getY() + (obswidth/2 + width/2);
			Point2D p = new Point2D.Double(x, y);
			if(isCollisionFreePoint(p)) {
				nodes.add(new Node(p));
			}
		}
	}
}

public void addNode(StaticObstacle obstacle) {
	//BOTTOM LEFT
	Point2D bl = new Point2D.Double(obstacle.getRect().getMinX() - (width/2), obstacle.getRect().getMinY()-(width/2));
	if(isCollisionFreePoint(bl)) {
		nodes.add(new Node(bl));
	}
	
	
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
}

