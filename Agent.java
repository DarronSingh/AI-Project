import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Agent {
	
	private int agentID;
	private int x, y, radius, velocity;
	private int frameX, frameY;
	private ArrayList<Node> targets = new ArrayList<Node>();
	private String publicBroadcast, privateBroadcast;
	
	public Agent(int agentID, int frameX, int frameY) {
		this.agentID = agentID;
		this.frameX = frameX;
		this.frameY = frameY;
		radius = 20; // radar distance
		velocity = 1; // 1cm
		spawn();
		
		publicBroadcast = "";
		privateBroadcast = "";
		System.out.println("Agent " + agentID + " created");
	}
	
	public void spawn() {
		// scaled down x and y location
		x = ThreadLocalRandom.current().nextInt(0, frameX/10-radius);
		y = ThreadLocalRandom.current().nextInt(0, frameY/10-radius);
	}
	
	public void update() {
		if (targets.size() == 5)
			publicBroadcast = "win";
	}
	
	public void draw(Graphics2D g2d) {
		
		// color code agents
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
		
		// scale up coordinates when drawing
		// draw radar
		g2d.drawOval(x*10, y*10, radius*10, radius*10);
		g2d.drawOval(x*10, y*10, radius*10-1, radius*10-1);
		g2d.drawOval(x*10, y*10, radius*10-2, radius*10-2);
		
		// draw agent
		g2d.fillRect(x*10+(radius*10/2), y*10+(radius*10/2), 5, 5);
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
