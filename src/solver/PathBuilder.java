package solver;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class PathBuilder {
	
	private String path = "";
	
	private List<Node> nodePath;
	
	private final double validStepLength = 0.001;
	
	public PathBuilder(List<Node> path) {
		nodePath = path;
	}
	
	private int calculateNumberOfSteps(Node from, Node to) {
		double distance = from.calculateDistance(to);
		int numberOfSteps = (int) Math.floor(distance/validStepLength);
		return numberOfSteps;
	}
	
	//Right = 1
	//Up = 2
	//Left = 3
	//Down = 4
	
	private int returnDirection(Node from, Node to) {
		
		if(from.getPos().getX() < to.getPos().getX()) {
			return 1;
		}
		if(from.getPos().getX() > to.getPos().getX()) {
			return 3;
		}
		if(from.getPos().getY() > to.getPos().getY()) {
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
	
	private List<Point2D> returnSteps(Node from, Node to){
		
		List<Point2D> currentPath = new ArrayList<>();
		
		int numberOfSteps = calculateNumberOfSteps(from,to);
		int direction = returnDirection(from, to);
		
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
