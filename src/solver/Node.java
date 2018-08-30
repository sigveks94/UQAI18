package solver;

import java.awt.geom.Point2D;

public class Node {
private Point2D pos;

public Node(Point2D pos) {
	this.pos = pos;
}

public Point2D getPos() {
	return pos;
}

public void setPos(Point2D pos) {
	this.pos = pos;
}


}