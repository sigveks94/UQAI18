package solver;

import java.awt.geom.Point2D;

import problem.Box;
import problem.MovingBox;

public class GoalNode extends Node{
	
	Box goalBox;

	public GoalNode(Point2D pos) {
		super(pos);
		goalBox = null;
	}
	
	public void setGoalBox(Box b) {
		if(!(b instanceof MovingBox)) {
			return;
		} else {
			goalBox = b;
		}
	}
	
	public Box getGoalBox() {
		return goalBox;
	}
	
	
	

}
