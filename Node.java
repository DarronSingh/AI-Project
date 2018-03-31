import java.awt.Color;
import java.awt.Graphics2D;

public class Node {

	private int x, y, width; // scaled down coordinates
	private int agentID, targetID; // if -1, not a target
	private boolean isFound = false;

	public Node(int x, int y, int frameX, int frameY) {
		this.x = x;
		this.y = y;
		clearTarget(); // no target by default
		width = frameX / 100; // scale width for board size
	}

	public void draw(Graphics2D g2d) {
		// draw cells
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawRect(x*10, y*10, width, width);
		
		// draw targets
		if (agentID>=0) {
			switch (agentID) {
			case 0:
				g2d.setColor(Color.GREEN);
				break;
			case 1:
				g2d.setColor(Color.BLUE);
				break;
			case 2:
				g2d.setColor(Color.BLACK);
				break;
			case 3:
				g2d.setColor(Color.ORANGE);
				break;
			case 4:
				g2d.setColor(Color.RED);
				break;
			}
			
			// scale up coordinates for drawing
			g2d.fillRect(x*10, y*10, width, width);
		}
	}

	public void setAgentID(int agentID) {
		this.agentID = agentID;
	}

	public int getAgentID() {
		return agentID;
	}
	
	public void setTargetID(int targetID) {
		this.targetID = targetID;
	}

	public int getTargetID() {
		return targetID;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public void clearTarget() {
		agentID = -1;
		targetID = -1;
	}
	
	public void setIsFound(boolean isFound) {
		this.isFound = isFound;
	}
	
	public boolean getIsFound() {
		return isFound;
	}
}
