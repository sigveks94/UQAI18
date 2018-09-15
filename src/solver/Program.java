package solver;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import problem.Box;
import problem.MovingBox;
import problem.ProblemSpec;
import problem.RobotConfig;

public class Program {
	
	ProblemSpec ps;
	State state;
	String outPutFileName;
	double[] mbPositions;
	double[] moPositions;
	double[] robotPosition;
	double robotOrientation;
	
	public Program(ProblemSpec ps, String fileName) {
		this.ps = ps;
		this.state = new State(ps);
		this.outPutFileName = fileName;
		mbPositions = saveMovingBoxPositions(ps);
		moPositions = saveMovingObstaclePositions(ps);
		robotPosition = saveRobotPosition(ps);
		robotOrientation = saveRobotOrientation(ps);
	}

	public void run() throws IOException {
		
		String outPutString = state.returnCompleteLineState() + "\n";
		while(!isFinished()){
			List<Box> movingBoxesLeft = getMovingBoxes(ps);
			int size = movingBoxesLeft.size();
			String possibleSolution = "";
			for(int i = 0; i < size; i++) {
				int numberOfMovingBoxesLeft = movingBoxesLeft.size();
				Random rand = new Random();
				int random = rand.nextInt(numberOfMovingBoxesLeft);
				MovingBox mb = (MovingBox) movingBoxesLeft.get(random);
				movingBoxesLeft.remove(mb);
				Solver solver = new Solver(ps);
				String subSolution = solver.initiate(mb);
				if(subSolution.equals("")) {
					break;
				}
				possibleSolution += subSolution;
			}
			if(isFinished()) {
				outPutString += possibleSolution;
				break;
			}
			else {
				setOriginalPositions(ps);
			}
		}
		
	
	/*public void run() throws IOException {
		String outPutString = state.returnCompleteLineState() + "\n";
		Box number1 = ps.getMovingBoxes().get(1);
		Box number2 = ps.getMovingBoxes().get(0);
		Box number3 = ps.getMovingBoxes().get(2);
		
		Solver solver1 = new Solver(ps);
		outPutString += solver1.initiate((MovingBox) number1);
		Solver solver2 = new Solver(ps);
		String initiate2 = solver2.initiate((MovingBox) number2);
		outPutString += initiate2;
		Solver solver3 = new Solver(ps);
		outPutString += solver3.initiate((MovingBox) number3);
		*/
		
		/*for(Box b : ps.getMovingBoxes()) {
			Solver solver = new Solver(ps);
			outPutString += solver.initiate((MovingBox) b);
		}
		*/
		
		int length = outPutString.split("\n").length;
		
		writeToFile(length + "\n" + outPutString, outPutFileName);
	}
	
	private List<Box> getMovingBoxes(ProblemSpec ps){
		List<Box> returnBoxes = new ArrayList<>();
		for(Box b : ps.getMovingBoxes()) {
			returnBoxes.add(b);
		}
		
		return returnBoxes;
	}
	
	private void writeToFile(String outPutString, String fileName ) throws IOException {
		String path = new File("").getAbsolutePath();
		File outputFile = new File(path + "/" + fileName);
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		writer.write(outPutString);
		writer.close();	
	}
	
	private boolean isFinished() {
		MovingBox movingBox;
		for(Box b : ps.getMovingBoxes()) {
			movingBox = (MovingBox) b;
			if(!movingBox.isFinished()) {
				return false;
			}
		}
		return true;
	}
	
	private void setOriginalPositions(ProblemSpec ps) {
		
		RobotConfig robot = ps.getInitialRobotConfig();
		Point2D originalRobotPoint = new Point2D.Double(robotPosition[0], robotPosition[1]);
		robot.setPos(originalRobotPoint);
		robot.setOrientation(robotOrientation);
		
		for(int i = 0; i < ps.getMovingBoxes().size(); i++) {
			
			Box b = ps.getMovingBoxes().get(i);
			Point2D originalPosition = new Point2D.Double(mbPositions[i*2], mbPositions[(i*2)+1]);
			b.setRect(originalPosition);
		}
		
		for(int i = 0; i < ps.getMovingObstacles().size(); i++) {
			Box b = ps.getMovingObstacles().get(i);
			Point2D originalPosition = new Point2D.Double(moPositions[i*2], moPositions[(i*2)+1]);
			b.setRect(originalPosition);
		}
		
	}
	
	private double[] saveMovingBoxPositions(ProblemSpec ps){
		
		int numberOfMovingBoxes = ps.getMovingBoxes().size();
		double[] positions = new double[numberOfMovingBoxes * 2];
		
		for(int i = 0; i < numberOfMovingBoxes * 2; i+= 2) {
			positions[i] = ps.getMovingBoxes().get(i/2).getRect().getCenterX();
			positions[i+1] = ps.getMovingBoxes().get((i/2)).getRect().getCenterY();
		}
		return positions;	
	}
	
	private double saveRobotOrientation(ProblemSpec ps2) {
		return ps2.getInitialRobotConfig().getOrientation();
	}

	private double[] saveRobotPosition(ProblemSpec ps2) {
		double[] positions = new double[2];
		positions[0] = ps2.getInitialRobotConfig().getPos().getX();
		positions[1] = ps2.getInitialRobotConfig().getPos().getY();
		return positions;
	}


	private double[] saveMovingObstaclePositions(ProblemSpec ps2) {
		int numberOfMovingObstacles = ps.getMovingObstacles().size();
		double[] positions = new double[numberOfMovingObstacles * 2];
		
		for(int i = 0; i < numberOfMovingObstacles * 2; i+= 2) {
			positions[i] = ps.getMovingObstacles().get(i/2).getRect().getCenterX();
			positions[i+1] = ps.getMovingObstacles().get((i/2)).getRect().getCenterY();
		}
		return positions;	
	}
	
	
	

}
