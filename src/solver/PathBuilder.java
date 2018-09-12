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
	
	
	//HELP METHODS USED IN GENERATEROTATION
	public String moveRobotRightHalfWidth(RobotConfig robot, double halfwidth) {
		String line = "";
		double x2 = Solver.doubleFormatter(robot.getPos().getX() + halfwidth);
		double y2 = robot.getPos().getY();
		Point2D goLeftPoint = new Point2D.Double(x2,y2);
		line += returnStepsRobotDirect(robot.getPos(), goLeftPoint, robot);
		return line;
	}
	
	public String moveRobotLeftHalfWidth(RobotConfig robot, double halfwidth) {
		String line = "";
		double x2 = Solver.doubleFormatter(robot.getPos().getX() - halfwidth);
		double y2 = Solver.doubleFormatter(robot.getPos().getY());
		Point2D goLeftPoint = new Point2D.Double(x2,y2);
		line += returnStepsRobotDirect(robot.getPos(), goLeftPoint, robot);
		return line;
	}
	
	public String moveRobotUpHalfWidth(RobotConfig robot, double halfwidth) {
		String line = "";
		double x2 = Solver.doubleFormatter(robot.getPos().getX());
		double y2 = Solver.doubleFormatter(robot.getPos().getY() + halfwidth);
		Point2D goLeftPoint = new Point2D.Double(x2,y2);
		line += returnStepsRobotDirect(robot.getPos(), goLeftPoint, robot);
		return line;
	}
	
	public String moveRobotDownHalfWidth(RobotConfig robot, double halfwidth) {
		String line = "";
		double x2 = Solver.doubleFormatter(robot.getPos().getX());
		double y2 = Solver.doubleFormatter(robot.getPos().getY() - halfwidth);
		Point2D goLeftPoint = new Point2D.Double(x2,y2);
		line += returnStepsRobotDirect(robot.getPos(), goLeftPoint, robot);
		return line;
	}
	
	public String generateRotation(RobotConfig robot, Box b, Point2D currentPosition, Point2D nextPosition) {
		String line = "";
		double halfwidth = Solver.doubleFormatter(b.getWidth()/2);
		int direction = returnDirection(currentPosition, nextPosition);
		if (direction == 1) { // GOING EASTBOUND
			if(robot.getPos().getY() > b.getRect().getCenterY() +  0.0001) {//ROBOT IS ABOVE THE BOX AND NEEDS TO BE MOVED TO THE LEFT SIDE ANTI-CLOCKWISE
				line += moveRobotUpHalfWidth(robot, halfwidth); //moves robot up
				
				line += returnStringFromRotating90AntiClockWise(robot);//adds string from rotating
				
				line += moveRobotLeftHalfWidth(robot, halfwidth); //moves robot to the left and adds these strings
				
				line += moveRobotDownHalfWidth(robot, halfwidth); //moves robot down to the correct endpoint

			}
			if(robot.getPos().getY() < b.getRect().getCenterY() -  0.0001) {//ROBOT IS UNDER THE BOX AND NEEDS TO BE MOVED TO THE LEFT SIDE CLOCKWISE
				line += moveRobotDownHalfWidth(robot, halfwidth); // adds the string resulting from reversing
				
				line += returnStringFromRotating90AntiClockWise(robot); //adds string from rotating
				
				line += moveRobotLeftHalfWidth(robot, halfwidth); //moves robot to the left and adds these strings
				
				line += moveRobotUpHalfWidth(robot, halfwidth); //moves robot up to the correct endpoint

				
				//The complete string resulting from this move (current -->reversePoint should be added to line
			}
			if(robot.getPos().getX() > b.getRect().getCenterX() +  0.0001) {//ROBOT IS RIGHT SIDE OF THE BOX AND NEEDS TO BE MOVED TO THE LEFT SIDE
				System.out.println("The previous position of the robot is 180 degrees wrong side, fix endposition!");
			}
		}
		
		if(direction == 3) { //GOING WESTBOUND
			if(robot.getPos().getY() > b.getRect().getCenterY() +  0.0001) { //robot is above the box and needs to be rotated to the right hand side
				line += moveRobotUpHalfWidth(robot, halfwidth); //moves robot up
				
				line += returnStringFromRotating90AntiClockWise(robot);//adds string from rotating
				
				line += moveRobotRightHalfWidth(robot, halfwidth);  //moves robot to the right and adds these strings
				
				line += moveRobotDownHalfWidth(robot, halfwidth); //moves robot down to the correct endpoint
			}
			
			if(robot.getPos().getY() < b.getRect().getCenterY() -  0.0001) { //robot is under the box and needs to be rotated to the right hand side
				line += moveRobotDownHalfWidth(robot, halfwidth); //moves robot down
				
				line += returnStringFromRotating90AntiClockWise(robot);//adds string from rotating
				
				line += moveRobotRightHalfWidth(robot, halfwidth); //moves robot to the right and adds these strings
				
				line += moveRobotUpHalfWidth(robot, halfwidth); //moves robot up to the correct endpoint
			}
			
			if(robot.getPos().getX() < b.getRect().getCenterX() -  0.0001) {//ROBOT IS RIGHT SIDE OF THE BOX AND NEEDS TO BE MOVED TO THE LEFT SIDE
				System.out.println("The previous position of the robot is 180 degrees wrong side, fix endposition!");
				//CAN FIX SO THAT IT CALLS GENERATEROTATION ON ITSELF FIRST
			}
		}
		
		if(direction == 2) { //GOING UP
			if(robot.getPos().getX() < b.getRect().getCenterX() -  0.0001) { //robot is left of the box and needs to be rotated beneath box
				line += moveRobotLeftHalfWidth(robot, halfwidth); //moves robot to the left
				
				line += returnStringFromRotating90AntiClockWise(robot);//adds string from rotating
				
				line += moveRobotDownHalfWidth(robot, halfwidth); //moves down and adds strings
				
				line += moveRobotRightHalfWidth(robot, halfwidth); //moves robot down to the correct endpoint
			}
			
			if(robot.getPos().getX() > b.getRect().getCenterX() +  0.0001) { //robot is right of the box and needs to be rotated beneath box
				line += moveRobotRightHalfWidth(robot, halfwidth); //moves robot to the right
				
				line += returnStringFromRotating90AntiClockWise(robot);//adds string from rotating
				
				line += moveRobotDownHalfWidth(robot, halfwidth); //moves down and adds strings
				
				line += moveRobotLeftHalfWidth(robot, halfwidth); //moves robot down to the correct endpoint
			}
			
			if(robot.getPos().getY() > b.getRect().getCenterY() +  0.0001) {//ROBOT IS RIGHT SIDE OF THE BOX AND NEEDS TO BE MOVED TO THE LEFT SIDE
				System.out.println("The previous position of the robot is 180 degrees wrong side, fix endposition!");
				//CAN FIX SO THAT IT CALLS GENERATEROTATION ON ITSELF FIRST
			}
		}
		
		if(direction == 4) { //GOING DOWN
			if(robot.getPos().getX() < b.getRect().getCenterX() -  0.0001) { //robot is left of the box and needs to be rotated above box
				line += moveRobotLeftHalfWidth(robot, halfwidth); //moves robot to the left
				
				line += returnStringFromRotating90AntiClockWise(robot); //adds string from rotating
				
				line += moveRobotUpHalfWidth(robot, halfwidth); //moves up and adds strings
				
				line += moveRobotRightHalfWidth(robot, halfwidth); //moves robot up to the correct endpoint
			}
			
			if(robot.getPos().getX() > b.getRect().getCenterX() +  0.0001) { //robot is right of the box and needs to be rotated beneath box
				line += moveRobotRightHalfWidth(robot, halfwidth); //moves robot to the right
				
				line += returnStringFromRotating90AntiClockWise(robot); //adds string from rotating
				
				line += moveRobotUpHalfWidth(robot, halfwidth); //moves up and adds strings
				
				line += moveRobotLeftHalfWidth(robot, halfwidth); //moves robot up to the correct endpoint
			}
			
			if(robot.getPos().getY() > b.getRect().getCenterY() +  0.0001) {//ROBOT IS RIGHT SIDE OF THE BOX AND NEEDS TO BE MOVED TO THE LEFT SIDE
				System.out.println("The previous position of the robot is 180 degrees wrong side, fix endposition!");
				//CAN FIX SO THAT IT CALLS GENERATEROTATION ON ITSELF FIRST
			}
		}
		

		return line; //RETURNS ENTIRE ROTATIONAL STRING SEGMENT CONTAINING ALL OBJECTS POSITION
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
