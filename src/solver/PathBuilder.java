package solver;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import problem.RobotConfig;

public class PathBuilder {
	
	
	private List<Point2D> outPutRobotPath;
	private List<Point2D> outPutBoxPath;
	private List<Node> inputRobotPath;
	private List<Node> inputBoxPath;
	
	//private List<List<String>> completePath = "";
	//private String subPath = "";
	
	private String path;
	private List<Node> nodePath;
	
	private final double validStepLength = 0.001;
	
	
	/**
	 * 
	 * @param path
	 * 
	 * The constructor takes in a path that consists of a list of nodes.
	 * 
	 */
	public PathBuilder(List<Node> robotPath, List<Node> boxPath) {
		this.inputRobotPath=robotPath;
		this.inputBoxPath=boxPath;
	}
	
	private String translatePointToString(Point2D point) {
		String returnString =  point.getX() + " " + point.getY();
		return returnString;	
	}
	
	private void translatePathToString(List<Point2D> points) {
		
		for(Point2D point : points) {
			if(point.equals(points.get(points.size()-1))) {
				path += this.translatePointToString(point);
			}
			else {
				path += this.translatePointToString(point) + "\n";
			}
		}
	}
	
	public String getCompletePath() {
		translatePathToString(createAllSteps());
		return path;
	}
	
	private int calculateNumberOfSteps(Point2D from, Point2D to) {
		double distance = from.distance(to);
		int numberOfSteps = (int) Math.floor(distance/validStepLength);
		return numberOfSteps;
	}
	
	//Right = 1
	//Up = 2
	//Left = 3
	//Down = 4
	
	private int returnDirection(Point2D from, Point2D to) {
		
		if(from.getX() < to.getX()) {
			return 1;
		}
		if(from.getX() > to.getX()) {
			return 3;
		}
		if(from.getY() > to.getY()) {
			return 4;
		}
		else {
			return 2;
		}
		
	}
	
	public List<Point2D> createAllSteps(){
		
		List<Point2D> finalPath = new ArrayList<>();
		
		for(int i = 0; i < nodePath.size() - 1; i ++) {
			finalPath.addAll(returnSteps(nodePath.get(i), nodePath.get(i+1)));
		}
		
		return finalPath;
	}
	
	private List<Point2D> returnSteps(Point2D from, Point2D to){
		
		List<Point2D> currentPath = new ArrayList<>();
		
		int numberOfSteps = calculateNumberOfSteps(from,to);
		int direction = returnDirection(from, to);
		
		Point2D fromPoint = from;
		currentPath.add(fromPoint);
		
		//Move right
		if(direction == 1) {
			for(int i = 1; i <= numberOfSteps; i++) {
				Point2D temporaryPoint = new Point2D.Double(fromPoint.getX() + i * validStepLength , fromPoint.getY());
				currentPath.add(temporaryPoint);	
			}
			if(!(currentPath.get(currentPath.size()-1).equals(to))) {
				currentPath.add(to);
			}
		}
		//MoveUp
		if(direction == 2) {
			for(int i = 1; i <= numberOfSteps; i++) {
				Point2D temporaryPoint = new Point2D.Double(fromPoint.getX(), fromPoint.getY() + i * validStepLength);
				currentPath.add(temporaryPoint);	
			}
			if(!(currentPath.get(currentPath.size()-1).equals(to))) {
				currentPath.add(to);
			}
		}
		//MoveLeft
		if(direction == 3) {
			for(int i = 1; i <= numberOfSteps; i++) {
				Point2D temporaryPoint = new Point2D.Double(fromPoint.getX() - i * validStepLength, fromPoint.getY());
				currentPath.add(temporaryPoint);	
			}
			if(!(currentPath.get(currentPath.size()-1).equals(to))) {
				currentPath.add(to);
			}
		}
		//MoveDown
		if(direction == 4) {
			for(int i = 1; i <= numberOfSteps; i++) {
				Point2D temporaryPoint = new Point2D.Double(fromPoint.getX(), fromPoint.getY() - i * validStepLength);
				currentPath.add(temporaryPoint);	
			}
			if(!(currentPath.get(currentPath.size()-1).equals(to))) {
				currentPath.add(to);
			}
		}
		
		return currentPath;
	}
	
	private List<Point2D> returnSteps(Node from, Node to){
		
		List<Point2D> currentPath = new ArrayList<>();
		
		int numberOfSteps = calculateNumberOfSteps(from.getPos(),to.getPos());
		int direction = returnDirection(from.getPos(), to.getPos());
		
		Point2D fromPoint = from.getPos();
		currentPath.add(fromPoint);
		
		//Move right
		if(direction == 1) {
			for(int i = 1; i <= numberOfSteps; i++) {
				Point2D temporaryPoint = new Point2D.Double(fromPoint.getX() + i * validStepLength , fromPoint.getY());
				currentPath.add(temporaryPoint);	
			}
			if(!(currentPath.get(currentPath.size()-1).equals(to.getPos()))) {
				currentPath.add(to.getPos());
			}
		}
		//MoveUp
		if(direction == 2) {
			for(int i = 1; i <= numberOfSteps; i++) {
				Point2D temporaryPoint = new Point2D.Double(fromPoint.getX(), fromPoint.getY() + i * validStepLength);
				currentPath.add(temporaryPoint);	
			}
			if(!(currentPath.get(currentPath.size()-1).equals(to.getPos()))) {
				currentPath.add(to.getPos());
			}
		}
		//MoveLeft
		if(direction == 3) {
			for(int i = 1; i <= numberOfSteps; i++) {
				Point2D temporaryPoint = new Point2D.Double(fromPoint.getX() - i * validStepLength, fromPoint.getY());
				currentPath.add(temporaryPoint);	
			}
			if(!(currentPath.get(currentPath.size()-1).equals(to.getPos()))) {
				currentPath.add(to.getPos());
			}
		}
		//MoveDown
		if(direction == 4) {
			for(int i = 1; i <= numberOfSteps; i++) {
				Point2D temporaryPoint = new Point2D.Double(fromPoint.getX(), fromPoint.getY() - i * validStepLength);
				currentPath.add(temporaryPoint);	
			}
			if(!(currentPath.get(currentPath.size()-1).equals(to.getPos()))) {
				currentPath.add(to.getPos());
			}
		}
		
		return currentPath;
	}
	
	

}
