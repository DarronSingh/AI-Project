import java.util.ArrayList;

public class Agent {
	
	private int agentID;
	private int x, y, width;
	private int frameX, frameY;
	private ArrayList<Node> targets = new ArrayList<Node>();
	private String publicBroadcast, privateBroadcast;
	
	public Agent(int agentID, int frameX, int frameY) {
		this.agentID = agentID;
		this.frameX = frameX;
		this.frameY = frameY;
		width = frameX/100; // scale width for board size
		
		publicBroadcast = "";
		privateBroadcast = "";
		System.out.println("Agent " + agentID + " created");
	}
	
	public void update() {
		if (targets.size() == 5)
			publicBroadcast = "win";
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public ArrayList<Node> getTargets() {
		return targets;
	}
	
	public void addTarget(Node target) {
		targets.add(target);
	}
	
	public String getPublicBroadcast () {
		return publicBroadcast;
	}
	
	public String getPrivateaBroadcast () {
		return privateBroadcast;
	}
	
	public void resetPublicBroadcast () {
		publicBroadcast = "";
	}
	
	public void resetPrivateaBroadcast () {
		privateBroadcast = "";
	}
}
