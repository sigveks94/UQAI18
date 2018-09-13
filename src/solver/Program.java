package solver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import problem.Box;
import problem.MovingBox;
import problem.ProblemSpec;

public class Program {
	
	ProblemSpec ps;
	State state;
	public Program(ProblemSpec ps) {
		this.ps = ps;
		this.state = new State(ps);
	}
	
	
	public void run() throws IOException {
		String outPutString = state.returnCompleteLineState() + "\n";
		while(!isFinished()){
			int numberOfMovingBoxes = ps.getMovingBoxes().size();
			Random rand = new Random();
			int i = rand.nextInt(numberOfMovingBoxes);
			Solver solver = new Solver(ps);
			MovingBox mb = (MovingBox) ps.getMovingBoxes().get(i);
			String returnString = solver.initiate(mb);
			if(returnString.equals("")) {
				continue;
			}
			outPutString += returnString;
		}
		
		int length = outPutString.split("\n").length;
		
		writeToFile(length + "\n" + outPutString);
	}
	
	private void writeToFile(String string) throws IOException {
		File outputFile = new File("/Users/ErlendHjelleStrandkleiv/Projects/UQAI18/" + "finalOutput.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		writer.write(string);
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
	
	
	

}
