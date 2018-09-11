package solver;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import solver.Solver;
import problem.Box;
import problem.RobotConfig;

public class PathBuilder { // CONTAINS ALL FUNCTIONS FOR INTERPOLATING A MOVE OF A BOX/OBSTACLE OR JUST THE ROBOTARM ITSELF - SHOULD BE INSTANTIATED NEW OBJECT FOR EVERY MOVE
	
	
	private List<Point2D> outPutRobotPath; 
	private State state;
	private Solver solver;
	private List<Point2D> outPutBoxPath;
	private List<Node> inputRobotPath; //INPUTTED A-STARRRED PATH OF ROBOT BETWEEN ALL AVAILABLE NODES
	private List<Node> inputBoxPath;   //INPUTTED A-STARRRED PATH OF BOX BETWEEN ALL AVAILABLE NODES
	private final double validStepLength = 0.001;
	
	public PathBuilder(Solver solver, State state, List<Node> robotPath, List<Node> boxPath) { //if robotPath == null - that means that the box is to be moved and the robotpath must be calculated
		this.inputRobotPath=robotPath;                             //If boxPath == null - that means that only the box is to be moved
		this.inputBoxPath=boxPath;
		this.solver=solver;
		this.state=state;
	}
	
	
	
	
	
	// PATHBUILDER TO DO:
	//1) FINISH GENERATEROTATION
	//2) MAKE METHOD FOR BOX ACCOMPANIED BY ROBOT
	//3) FINISH RETURNSTRINGBULKFROMMOVINGBOXANDROBOT AND THE OTHER LONG SHIT
	//4) IMPLEMENT WAYS TO HANDLE THIS STRING IN SOLVER
	
	
	
	
	
	
	
	
	

	public String returnStringBulkFromMovingBoxAndRobot() {
		String lines = "";
		// RETURN AN INTERPOLATED COMPLETE STRING CONTAINING THE PATH OF MOVING BOX AND PUSHING ROBOTARM FROM A TO B
		return lines;
	}
	
	public String returnStringBulkFromMovingOnlyRobot() {
		String lines = "";
		// RETURN AN INTERPOLATED COMPLETE STRING CONTAINING THE PATH OF MOVING ROBOT FROM A TO B
		return lines;
	}
	
	public String generateRotation(RobotConfig robot, Box b, Point2D currentPosition, Point2D nextPosition) {
		String line = "";
		double halfwidth = Solver.doubleFormatter(b.getWidth()/2);
		int direction = returnDirection(currentPosition, nextPosition);
		if (direction == 1) { // GOING EASTBOUND
			if(robot.getPos().getY() > b.getRect().getCenterY() +  0.0001) {//ROBOT IS ABOVE THE BOX AND NEEDS TO BE MOVED TO THE LEFT SIDE ANTI-CLOCKWISE
				double x = Solver.doubleFormatter(robot.getPos().getX());
				double y = Solver.doubleFormatter(robot.getPos().getY() + halfwidth);
				Point2D reversePoint = new Point2D.Double(x, y);
				line += returnStepsRobotDirect(currentPosition, reversePoint, robot); // adds the string resulting from reversing
				line += returnStringFromRotating90AntiClockWise(robot); //adds string from rotating
				//The complete string resulting from this move (current -->reversePoint should be added to line
			}
			if(robot.getPos().getY() < b.getRect().getCenterY() -  0.0001) {//ROBOT IS UNDER THE BOX AND NEEDS TO BE MOVED TO THE LEFT SIDE CLOCKWISE
				double x = Solver.doubleFormatter(robot.getPos().getX());
				double y = Solver.doubleFormatter(robot.getPos().getY() - halfwidth);
				Point2D reversePoint = new Point2D.Double(x, y);
				line += returnStepsRobotDirect(currentPosition, reversePoint, robot); // adds the string resulting from reversing
				line += returnStringFromRotating90AntiClockWise(robot); //adds string from rotating
				//The complete string resulting from this move (current -->reversePoint should be added to line
			}
			if(robot.getPos().getX() > b.getRect().getCenterX() +  0.0001) {//ROBOT IS RIGHT SIDE OF THE BOX AND NEEDS TO BE MOVED TO THE LEFT SIDE
				System.out.println("The previous position of the robot is 180 degrees wrong side, fix endposition!");
			}
		}
		
		// GOING EASTBOUND IS NOT FINISHED - IS MISSING MOVING TO LEFT AND UP/DOWN TO FINAL POSITION
		
		
		//if(direction == 2)
		
		//if(direction == 3)
		
		//if(direction == 4)
		// IMPLEMENT SITUATIONS WHERE YOU GO UP, DOWN AND LEFT - ABOVE ONLY APPLIES TO EASTBOUND MOVEMENTS
		
		
		return line;
	}
	
	public String returnStringFromRotating90AntiClockWise(RobotConfig robot) {
		String line = "";
		double currentAlpha = robot.getOrientation();
		double alpha = calculateAlphaChange();
		for (int i =1; i <= calculateNumberOfRotationSteps90Degrees(); i++) {
			robot.setOrientation(currentAlpha +  alpha*i);
			state.setRobotConfig(robot);
			line += state.returnCompleteLineState();
		}
		return line;
	}
	
	public int calculateNumberOfRotationSteps90Degrees() {
		double width = solver.getWidth();
		return (int) Math.ceil(width/validStepLength);
	}
	
	public double calculateAlphaChange() {
		return calculateNumberOfRotationSteps90Degrees()*Math.PI;
	}
	
	public int calculateNumberOfSteps(Point2D from, Point2D to) {
		double distance = from.distance(to);
		int numberOfSteps = (int) Math.floor(distance/validStepLength);
		return numberOfSteps;
	}
	
	
	private int returnDirection(Point2D from, Point2D to) {
		
		if(from.getX() < to.getX()) { 	//Right = 1
			return 1;
		}
		if(from.getX() > to.getX()) {	//Left = 3
			return 3;
		}
		if(from.getY() > to.getY()) {	//Down = 4
			return 4;
		}
		else {							//Up = 2
			return 2;
		}
		
	}
	
	
	
	private String returnStepsRobotDirect(Point2D from, Point2D to, RobotConfig robot){
		
		List<String> currentPath = new ArrayList<>();
		String returnString = "";
		int numberOfSteps = calculateNumberOfSteps(from,to);
		int direction = returnDirection(from, to);
		
		//Move right
		if(direction == 1) {
			for(int i = 1; i <= numberOfSteps; i++) {
				Point2D temporaryPoint = new Point2D.Double(from.getX() + i * validStepLength , from.getY());
				robot.setPos(temporaryPoint);
				state.setRobotConfig(robot);
				currentPath.add(state.returnCompleteLineState());	
			}
			if(!(currentPath.get(currentPath.size()-1).equals(to))) {
				robot.setPos(to);
				state.setRobotConfig(robot);
				currentPath.add(state.returnCompleteLineState());
			}
		}
		//MoveUp
		if(direction == 2) {
			for(int i = 1; i <= numberOfSteps; i++) {
				Point2D temporaryPoint = new Point2D.Double(from.getX(), from.getY() + i * validStepLength);
				robot.setPos(temporaryPoint);
				state.setRobotConfig(robot);
				currentPath.add(state.returnCompleteLineState());		
			}
			if(!(currentPath.get(currentPath.size()-1).equals(to))) {
				robot.setPos(to);
				state.setRobotConfig(robot);
				currentPath.add(state.returnCompleteLineState());
			}
		}
		//MoveLeft
		if(direction == 3) {
			for(int i = 1; i <= numberOfSteps; i++) {
				Point2D temporaryPoint = new Point2D.Double(from.getX() - i * validStepLength, from.getY());
				robot.setPos(temporaryPoint);
				state.setRobotConfig(robot);
				currentPath.add(state.returnCompleteLineState());		
			}
			if(!(currentPath.get(currentPath.size()-1).equals(to))) {
				robot.setPos(to);
				state.setRobotConfig(robot);
				currentPath.add(state.returnCompleteLineState());
			}
		}
		//MoveDown
		if(direction == 4) {
			for(int i = 1; i <= numberOfSteps; i++) {
				Point2D temporaryPoint = new Point2D.Double(from.getX(), from.getY() - i * validStepLength);
				robot.setPos(temporaryPoint);
				state.setRobotConfig(robot);
				currentPath.add(state.returnCompleteLineState());	
			}
			if(!(currentPath.get(currentPath.size()-1).equals(to))) {
				robot.setPos(to);
				state.setRobotConfig(robot);
				currentPath.add(state.returnCompleteLineState());
			}
		}
		
		for (String line: currentPath) {
			returnString += line + "\n";
		}
		return returnString;
	}
	
	

}
