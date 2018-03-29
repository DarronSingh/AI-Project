import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class Agent {
	
	private int agentID;
	private int x, y, radius, velX, velY;
	private int frameX, frameY;
	private String color, publicBroadcast, privateBroadcast;
	boolean isActive, isDiverting;
	
	private ArrayList<Node> targets = new ArrayList<Node>();
	private Stack<Coordinate> path = new Stack<Coordinate>();
	private Coordinate currentTarget;
	
	public Agent(int agentID, int frameX, int frameY) {
		this.agentID = agentID;
		this.frameX = frameX;
		this.frameY = frameY;
		radius = 10; // radar radius
		spawn();
		setupPath();
		
		publicBroadcast = "";
		privateBroadcast = "";
		System.out.println("Agent " + agentID + " created");
		isActive = true;
	}
	
	public void spawn() {
		// randomly generate location
		x = ThreadLocalRandom.current().nextInt(0, frameX/10-2*radius);
		y = ThreadLocalRandom.current().nextInt(0, frameY/10-2*radius);
	}
	
	public void setupPath() {
		// set color and create path in reverse
		switch (agentID) {
		case 0:
			color = "GREEN";
			currentTarget = new Coordinate(0, 0); // set start
			path.push(new Coordinate(0, 0)); // end
			generatePath();
			break;
		case 1:
			color = "BLUE";
			currentTarget = new Coordinate(0, 20); // set start
			path.push(new Coordinate(0, 20)); // end
			generatePath();
			break;
		case 2:
			color = "BLACK";
			currentTarget = new Coordinate(0, 40); // set start
			path.push(new Coordinate(0, 40)); // end
			generatePath();
			break;
		case 3:
			color = "ORANGE";
			currentTarget = new Coordinate(0, 60); // set start
			path.add(new Coordinate(0, 60)); // end
			generatePath();
			break;
		case 4:
			color = "RED";
			currentTarget = new Coordinate(0, 80); // start
			path.add(new Coordinate(0, 80)); // end
			generatePath();
			break;
		}
	}
	
	public void generatePath() {
		// path in reverse
		path.add(new Coordinate(0, 0)); // top left
		path.add(new Coordinate(80, 0));
		path.add(new Coordinate(80, 10));
		path.add(new Coordinate(20, 10));
		path.add(new Coordinate(20, 20));
		path.add(new Coordinate(80, 20));
		path.add(new Coordinate(80, 40));
		path.add(new Coordinate(20, 40));
		path.add(new Coordinate(20, 60));
		path.add(new Coordinate(80, 60));
		path.add(new Coordinate(80, 80));
		path.add(new Coordinate(0, 80)); // bottom left
	}
	
	public void setDirection() {
		// decide left, right or none
		if (x > currentTarget.getX())
			velX = -1;
		else if (x < currentTarget.getX())
			velX = 1;
		else
			velX = 0;
		
		// decide up, down or none
		if (y > currentTarget.getY())
			velY = -1;
		else if (y < currentTarget.getY())
			velY = 1;
		else
			velY = 0;
	}
	
	public void move() {
		// if we have reached the current target
		if (x == currentTarget.getX() && y == currentTarget.getY()) {
			if (path.isEmpty()) {
				publicBroadcast = "done path";
				isActive = false;
			} else {
				// get next in line, pop it from stack
				currentTarget = path.pop();
				if (isDiverting)
					isDiverting = false;
			}
		}		
		setDirection(); // set the direction
		
		// move agent
		x+=velX;
		y+=velY;
	}
	
	public void update() {
		move();
		
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
		g2d.fillRect(x*10+(2*radius*10/2)-radius/2, y*10+(2*radius*10/2)-radius/2, 10, 10);
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
	
	public String locationToString() {
		return "(" + String.valueOf(x) + ", " + String.valueOf(y) + ")";
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
	
	public void addPath(Coordinate c) {
		path.add(c);
	}
	
	public void divertPath(Coordinate c) {
		isDiverting = true;
		this.path.add(currentTarget); // add current target to path again
		this.currentTarget = c; // change current target to diverting path
		this.setDirection(); // change direction according to new target
//		move();
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
	
	public String getColor() {
		return color;
	}
}
