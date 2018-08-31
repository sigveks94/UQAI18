package solver;

import java.awt.geom.Point2D;

public class Node {
	
private Point2D pos;

public Node(Point2D pos) {
	this.pos = (Point2D) pos.clone();
}

public Point2D getPos() {
	return pos;
}

public void setPos(Point2D pos) {
	this.pos = (Point2D) pos.clone();
}

public double calculateDistance(Node node) {
	return this.pos.distance(node.getPos());
}

}