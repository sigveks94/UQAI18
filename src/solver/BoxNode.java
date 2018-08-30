package solver;
import problem.Box;
import java.awt.geom.Point2D;

public class BoxNode extends Node{
	private Box connectedBox;

	public BoxNode(Point2D pos) {
		super(pos);
	}

	public Box getConnectedBox() {
		return connectedBox;
	}

	public void setConnectedBox(Box connectedBox) {
		this.connectedBox = connectedBox;
	}
	
	
}
