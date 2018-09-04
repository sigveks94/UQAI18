package solver;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import problem.*;

public class Solver {
	ProblemSpec ps;
	
	public Solver(ProblemSpec ps) {
		this.ps=ps;
	}
	
private List<Node> nodes;
private final double width = ps.getRobotWidth();

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

}

