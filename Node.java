
public class Node {

	private int x, y, width;
	private int agentID; // if -1, not a target

	public Node(int x, int y, int frameX, int frameY) {
		this.x = x;
		this.y = y;
		width = frameX / 100; // scale width for board size
//		System.out.println("Node x:" + x + ", y:" + y + " created");
	}

	// public void draw(Graphics2D g2d) {
	//
	// }

	public void setAgentID(int agentID) {
		this.agentID = agentID;
	}

	public int getAgentID() {
		return agentID;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
