package solver;

import java.awt.geom.Point2D;
import java.util.List;

import problem.Box;
import problem.ProblemSpec;
import problem.RobotConfig;

public class State {
	private ProblemSpec problemSpec;
	private RobotConfig robotConfig;
	private List<Box> boxes;
	private List<Box> obstacles;

//CLASS THAT CONTAINS THE STATE FOR ALL OBJECTS IN THE WORLD - EVEN UPDATES THE OBJECTS WHILE MOVING IN REAL TME

public State(ProblemSpec problemSpec) {
	this.problemSpec=problemSpec;
	this.robotConfig=problemSpec.getInitialRobotConfig();
	this.boxes=problemSpec.getMovingBoxes();
	this.obstacles=problemSpec.getMovingObstacles();
}

public void setRobotConfig(RobotConfig robot) {
	this.robotConfig= robot;
}

public void setBoxState(Box b, Point2D point) {
	int index = problemSpec.getMovingBoxes().indexOf(b);
	boxes.get(index).setRect(point);
}

public void setObstacleState(Box b, Point2D point) {
	int index = problemSpec.getMovingObstacles().indexOf(b);
	obstacles.get(index).setRect(point);
}


public RobotConfig getRobotConfig() {
	return robotConfig;
}

public String returnRobotString() {
	return "" + robotConfig.getPos().getX() + " " + robotConfig.getPos().getY() + " " + robotConfig.getOrientation() + " ";
}

public String returnBoxString(Box b) {
	return "" + b.getRect().getCenterX() + " " + b.getRect().getCenterY() + " ";
}

public String returnBoxesString() {
	String line = "";
	for (Box b: boxes) {
		line += returnBoxString(b);
	}
	return line;
}

public String returnObstacleString(Box b) {
	return "" + b.getRect().getCenterX() + " " + b.getRect().getCenterY();
}

public String returnObstaclesString() {
	String line = "";
	for (Box b: obstacles) {
		line += returnObstacleString(b);
	}
	return line;
}

public String returnCompleteLineState() {
	String line = "" + returnRobotString() + returnBoxesString() + returnObstaclesString();
	return line;
}

public List<Box> getBoxes() {
	return boxes;
}

public List<Box> getObstacles() {
	return obstacles;
}


}