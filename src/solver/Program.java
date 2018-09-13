package solver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
		for(int i = 0; i < ps.getMovingBoxes().size(); i++) {
			Solver solver = new Solver(ps);
			MovingBox mb = (MovingBox) ps.getMovingBoxes().get(i);
			outPutString += solver.initiate(mb);
		}
		
		int length = outPutString.split("\n").length;
		
		writeToFile(length + "\n" + outPutString);
	}
	
	public void writeToFile(String string) throws IOException {
		File outputFile = new File("/Users/ErlendHjelleStrandkleiv/Projects/UQAI18/" + "finalOutput.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		writer.write(string);
		writer.close();	
	}
	
	
	
	
	
	

}
