package solver;

import java.awt.geom.Point2D;

import problem.Box;
import problem.MovingBox;

public class StartBoxNode extends Node {

	Box startBox;
	
	public StartBoxNode(Point2D pos) {
		super(pos);
		startBox = null;
	}
	
	public void setStartBox(MovingBox b) {
		startBox = b;
	}
	
	public Box getStartBox() {
		return startBox;
	}
	
	
	
	
	

}
