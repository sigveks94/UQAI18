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
	private Box movingBox;
	
	public PathBuilder(Solver solver, State state, List<Node> robotPath, List<Node> boxPath, Box box) { //if robotPath == null - that means that the box is to be moved and the robotpath must be calculated
		this.inputRobotPath=robotPath;                             //If boxPath == null - that means that only the box is to be moved
		this.inputBoxPath=boxPath;
		this.solver=solver;
		this.state=state;
		this.movingBox=box;
	}
	
	
	
	
	
	// PATHBUILDER TO DO:
	//1) FINISH GENERATEROTATION
	//2) MAKE METHOD FOR BOX ACCOMPANIED BY ROBOT
	//3) FINISH RETURNSTRINGBULKFROMMOVINGBOXANDROBOT AND THE OTHER LONG SHIT
	//4) IMPLEMENT WAYS TO HANDLE THIS STRING IN SOLVER
	
	
	
	
	
	
	
	
	

	public String returnStringBulkFromMovingBoxAndRobot() {
		double halfwidth = solver.getHalfWidth();
		RobotConfig robot = state.getRobotConfig();
		String lines = "";
		Point2D firsPoint = inputBoxPath.get(0).getPos();
		Point2D secondPoint = inputBoxPath.get(1).getPos();
		int currentDirection = returnDirection(firsPoint, secondPoint);
		
		for (int i= 0 ; i <= inputBoxPath.size() -2 ; i++) {
			Point2D fromPoint = inputBoxPath.get(i).getPos();
			Point2D toPoint = inputBoxPath.get(i+1).getPos();
			if (!(currentDirection == returnDirection(fromPoint, toPoint))) { //rotate robot first
				currentDirection = returnDirection(fromPoint, toPoint);
				if (currentDirection == 1){ //move right
					Point2D robotPoint = new Point2D.Double(fromPoint.getX()-halfwidth, fromPoint.getY());
					lines += generateRotation(robot, movingBox, robot.getPos(), robotPoint);
				}
				if (currentDirection == 3) { //move left
					Point2D robotPoint = new Point2D.Double(fromPoint.getX()+halfwidth, fromPoint.getY());
					lines += generateRotation(robot, movingBox, robot.getPos(), robotPoint);
				}
				if(currentDirection == 2) { // move up
					Point2D robotPoint = new Point2D.Double(fromPoint.getX(), fromPoint.getY()- halfwidth);
					lines += generateRotation(robot, movingBox, robot.getPos(), robotPoint);
				}
				if (currentDirection == 4) { //move down
					Point2D robotPoint = new Point2D.Double(fromPoint.getX(), fromPoint.getY()+halfwidth);
					lines += generateRotation(robot, movingBox, robot.getPos(), robotPoint);
				}
				else {
					System.out.println("Something went wrong in currentdirection");
				}
			}
			
			lines += moveBoxAndRobotAlongSingleEdge(robot, movingBox, inputBoxPath.get(i), inputBoxPath.get(i+1), halfwidth);
		}
		
		// RETURN AN INTERPOLATED COMPLETE STRING CONTAINING THE PATH OF MOVING BOX AND PUSHING ROBOTARM FROM A TO B
		return lines;
	}
	
	public String returnStringBulkFromMovingOnlyRobot() { //NEEDS TO FIND WAY TO RIGHT POSITION OF BOX AS WELL!!!!
		String lines = "";
		// RETURN AN INTERPOLATED COMPLETE STRING CONTAINING THE PATH OF MOVING ROBOT FROM A TO B
		return lines;
	}
	
	
	public String moveBoxAndRobotAlongSingleEdge(RobotConfig robot, Box box, Node fromNode, Node toNode, double halfwidth) {
		
		List<String> currentPath = new ArrayList<>();
		String returnString = "";
		Point2D fromBoxPoint = new Point2D.Double(fromNode.getPos().getX(), fromNode.getPos().getY());
		Point2D toBoxPoint = new Point2D.Double(toNode.getPos().getX(), toNode.getPos().getY());
		int numberOfSteps = calculateNumberOfSteps(fromBoxPoint,toBoxPoint);
		int direction = returnDirection(fromBoxPoint, toBoxPoint);
		
		//Move right
		if(direction == 1) {
			Point2D fromRobotPoint = new Point2D.Double(fromBoxPoint.getX()-halfwidth, fromBoxPoint.getY());
			Point2D toRobotPoint = new Point2D.Double(toBoxPoint.getX()-halfwidth, toBoxPoint.getY());
			for(int i = 1; i <= numberOfSteps; i++) {
				Point2D temporaryBoxPoint = new Point2D.Double(fromBoxPoint.getX() + i * validStepLength , fromBoxPoint.getY());
				Point2D temporaryRobotPoint = new Point2D.Double(fromRobotPoint.getX() + i * validStepLength , fromRobotPoint.getY());
				robot.setPos(temporaryRobotPoint); //moving robot single step
				box.setPos(temporaryBoxPoint); //moving box single step
				currentPath.add(state.returnCompleteLineState());	//adding resulting stringline from this step to the list of steps
			}
			if(!(box.getPos().equals(toBoxPoint))) {
				box.setPos(toBoxPoint);
				robot.setPos(toRobotPoint);
				currentPath.add(state.returnCompleteLineState());
			}
		}
		//MoveUp
		if(direction == 2) {
			Point2D fromRobotPoint = new Point2D.Double(fromBoxPoint.getX(), fromBoxPoint.getY()-halfwidth);
			Point2D toRobotPoint = new Point2D.Double(toBoxPoint.getX(), toBoxPoint.getY()-halfwidth);
			for(int i = 1; i <= numberOfSteps; i++) {
				Point2D temporaryBoxPoint = new Point2D.Double(fromBoxPoint.getX() + i * validStepLength , fromBoxPoint.getY());
				Point2D temporaryRobotPoint = new Point2D.Double(fromRobotPoint.getX() + i * validStepLength , fromRobotPoint.getY());
				robot.setPos(temporaryRobotPoint); //moving robot single step
				box.setPos(temporaryBoxPoint); //moving box single step
				currentPath.add(state.returnCompleteLineState());	//adding resulting stringline from this step to the list of steps
			}
			if(!(box.getPos().equals(toBoxPoint))) {
				box.setPos(toBoxPoint);
				robot.setPos(toRobotPoint);
				currentPath.add(state.returnCompleteLineState());
			}
		}
		//MoveLeft
		if(direction == 3) {
			Point2D fromRobotPoint = new Point2D.Double(fromBoxPoint.getX()+halfwidth, fromBoxPoint.getY());
			Point2D toRobotPoint = new Point2D.Double(toBoxPoint.getX()+halfwidth, toBoxPoint.getY());
			for(int i = 1; i <= numberOfSteps; i++) {
				Point2D temporaryBoxPoint = new Point2D.Double(fromBoxPoint.getX() + i * validStepLength , fromBoxPoint.getY());
				Point2D temporaryRobotPoint = new Point2D.Double(fromRobotPoint.getX() + i * validStepLength , fromRobotPoint.getY());
				robot.setPos(temporaryRobotPoint); //moving robot single step
				box.setPos(temporaryBoxPoint); //moving box single step
				currentPath.add(state.returnCompleteLineState());	//adding resulting stringline from this step to the list of steps
			}
			if(!(box.getPos().equals(toBoxPoint))) {
				box.setPos(toBoxPoint);
				robot.setPos(toRobotPoint);
				currentPath.add(state.returnCompleteLineState());
			}
		}
		//MoveDown
		if(direction == 4) {
			Point2D fromRobotPoint = new Point2D.Double(fromBoxPoint.getX(), fromBoxPoint.getY()+halfwidth);
			Point2D toRobotPoint = new Point2D.Double(toBoxPoint.getX(), toBoxPoint.getY()+halfwidth);
			for(int i = 1; i <= numberOfSteps; i++) {
				Point2D temporaryBoxPoint = new Point2D.Double(fromBoxPoint.getX() + i * validStepLength , fromBoxPoint.getY());
				Point2D temporaryRobotPoint = new Point2D.Double(fromRobotPoint.getX() + i * validStepLength , fromRobotPoint.getY());
				robot.setPos(temporaryRobotPoint); //moving robot single step
				box.setPos(temporaryBoxPoint); //moving box single step
				currentPath.add(state.returnCompleteLineState());	//adding resulting stringline from this step to the list of steps
			}
			if(!(box.getPos().equals(toBoxPoint))) {
				box.setPos(toBoxPoint);
				robot.setPos(toRobotPoint);
				currentPath.add(state.returnCompleteLineState());
			}
		}
		
		for (String line: currentPath) {
			returnString += line + "\n";
		}
		return returnString;
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
			line += state.returnCompleteLineState() + "\n";
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
