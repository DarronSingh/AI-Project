import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Agent {
	
	private int agentID;
	private int x, y, radius, velX, velY;
	private int frameX, frameY;
	Font font;
	private ArrayList<Node> targets = new ArrayList<Node>();
	private String publicBroadcast, privateBroadcast;
	
	public Agent(int agentID, int frameX, int frameY) {
		this.agentID = agentID;
		this.frameX = frameX;
		this.frameY = frameY;
		radius = 10; // radar radius
		velX = velY = 1; // 1cm per time
		spawn();
		
		publicBroadcast = "";
		privateBroadcast = "";
		font = new Font("TimesRoman", Font.PLAIN, 16);
		System.out.println("Agent " + agentID + " created");
	}
	
	public void spawn() {
		// scaled down x and y location
		x = ThreadLocalRandom.current().nextInt(0, frameX/10-2*radius);
		y = ThreadLocalRandom.current().nextInt(0, frameY/10-2*radius);
	}
	
	public void move() {
		// check wall collision
		if (x*10 <= 0 && velX<0)
			velX = -velX;
		else if (x*10+2*radius*10 >= frameX && velX>0)
			velX = -velX;
		if (y*10 <= 0 && velY<0)
			velY = -velY;
		else if (y*10+2*radius*10 >= frameY && velY>0)
			velY = -velY;
		
		// move agent
		x+=velX;
		y+=velY;
	}
	
	public void update() {
		if (targets.size() == 5)
			publicBroadcast = "won";
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
		g2d.drawOval(x*10, y*10, 2*radius*10, 2*radius*10);
		g2d.drawOval(x*10, y*10, 2*radius*10-1, 2*radius*10-1);
		g2d.drawOval(x*10, y*10, 2*radius*10-2, 2*radius*10-2);
		
		// draw agent
//		g2d.fillRect(x*10+(2*radius*10/2), y*10+(2*radius*10/2), 5, 5);
		g2d.setFont(font);
		g2d.drawString(String.valueOf(agentID), x*10+(2*radius*10/2), y*10+(2*radius*10/2));
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
	
	public int getRadius() {
		return radius;
	}
	
	public int getDiameter() {
		return 2*radius;
	}
	
	public int getVelX() {
		return velX;
	}
	
	public int getVelY() {
		return velY;
	}
	
	public void setVelX(int velX) {
		this.velX = velX;
	}
	
	public void setVelY(int velY) {
		this.velY = velY;
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
