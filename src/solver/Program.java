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
	String outPutFileName;
	public Program(ProblemSpec ps, String fileName) {
		this.ps = ps;
		this.state = new State(ps);
		this.outPutFileName = fileName;
	}
	
	
	public void run() throws IOException {
		String outPutString = state.returnCompleteLineState() + "\n";
		/*while(!isFinished()){
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
		*/
		
		for(Box b : ps.getMovingBoxes()) {
			Solver solver = new Solver(ps);
			outPutString += solver.initiate((MovingBox) b);
		}
		
		int length = outPutString.split("\n").length;
		
		writeToFile(length + "\n" + outPutString, outPutFileName);
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
	
	
	

}
