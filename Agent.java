import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class Agent {
	
	private int agentID;
	private int x, y, radius, velX, velY, stepCount, numFound;
	private int frameX, frameY;
	private String color;
	private Message broadcast;
	boolean isActive, won, isDiverting, isHV;
	
	private ArrayList<Node> targets = new ArrayList<Node>(); // found targets go here
	private Stack<Coordinate> path = new Stack<Coordinate>(); // path coordinates go here
	private Queue<Message> inbox = new LinkedList<Message>(); // direct messages go here
	private Coordinate currentTarget;
	
	public Agent(int agentID, int frameX, int frameY) {
		this.agentID = agentID;
		this.frameX = frameX;
		this.frameY = frameY;
		radius = 10; // radar radius
		numFound = stepCount = 0;
		spawn();
		setupPath();
		
//		System.out.println("Agent " + agentID + " created");
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
			path.push(new Coordinate(0, 10)); // end
			generatePath();
			break;
		case 1:
			color = "BLUE";
			currentTarget = new Coordinate(0, 20); // set start
			path.push(new Coordinate(0, 30)); // end
			generatePath();
			break;
		case 2:
			color = "BLACK";
			currentTarget = new Coordinate(0, 40); // set start
			path.push(new Coordinate(0, 50)); // end
			generatePath();
			break;
		case 3:
			color = "ORANGE";
			currentTarget = new Coordinate(0, 60); // set start
			path.add(new Coordinate(0, 70)); // end
			generatePath();
			break;
		case 4:
			color = "RED";
			currentTarget = new Coordinate(0, 80); // start
			path.add(new Coordinate(0, 90)); // end
			generatePath();
			break;
		}
	}
	
	public void generatePath() {
		// add path to stack in reverse so we can unstack it normally
		path.add(new Coordinate(0, 0)); // top left
		path.add(new Coordinate(100, 0));
		path.add(new Coordinate(100, 20));
		path.add(new Coordinate(20, 20));
		path.add(new Coordinate(20, 40));
		path.add(new Coordinate(100, 40));
		path.add(new Coordinate(100, 60));
		path.add(new Coordinate(20, 60));
		path.add(new Coordinate(20, 80));
		path.add(new Coordinate(100, 80));
		path.add(new Coordinate(100, 100));
		path.add(new Coordinate(0, 100)); // bottom left
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
				broadcast = new Message(-1, "done path", new Coordinate(x, y));
				isActive = false;
			} else {
				// get next in line, pop it from stack
				currentTarget = path.pop();
				if (isDiverting)
					isDiverting = false;
			}
		}		
		setDirection(); // set the direction
		
		// move agent normally
		x+=velX;
		y+=velY;
		
		// move agent based on how prof wanted
		// if (isHV && velX!=0)
		// x += velX;
		// else if (!isHV && velY!=0)
		// y += velY;
		//
		// isHV = !isHV;
		
		if (velX!=0) stepCount++;
		if (velY!=0) stepCount++;
	}
	
	// if 2 agents collide, 1 diverts
	public void divertPath(Coordinate c) {
		isDiverting = true;
		path.add(currentTarget); // add current target to path again
		currentTarget = c; // change current target to diverting path
		setDirection(); // change direction according to new target
	}
	
	// if agent learns of a target location, sidetrack
	public void sideTrack(Coordinate c) {
		path.add(currentTarget); // add current target to path again
		path.add(new Coordinate(x, y)); // add current location to path
		currentTarget = c; // change current target to target that was given
		setDirection();
	}
	
	public void checkInbox() {
		// if inbox isn't empty, side track current path to new target
		if (!inbox.isEmpty()) {
			for (int i=0; i<inbox.size(); i++) {
				sideTrack(inbox.remove().coordinate);
			}
		}
	}
	
	public void update() {
		checkInbox();
		move();
		
		if (!won && targets.size() == 5) {
			broadcast = new Message(-1, "won", new Coordinate(x, y));
			won = true;
		}
	}
	
	public void draw(Graphics2D g2d) {
		// color code agents by rgb, 25% transparent
		switch (agentID) {
		case 0:
			g2d.setColor(new Color(0, 153, 51, 63));
			break;
		case 1:
			g2d.setColor(new Color(0, 102, 255, 63));
			break;
		case 2:
			g2d.setColor(new Color(0, 0, 0, 63));
			break;
		case 3:
			g2d.setColor(new Color(255, 102, 0, 63));
			break;
		case 4:
			g2d.setColor(new Color(255, 0, 0, 63));
			break;
		}
		
		// scale up coordinates when drawing
		// draw radar
		g2d.fillOval(x*10-radius*10, y*10-radius*10, 2*radius*10, 2*radius*10);
		
		// draw agent
		g2d.fillRect(x*10-radius/2, y*10-radius/2, 10, 10);
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
	
	public int getStepCount() {
		return stepCount;
	}
	
	public int getNumFound() {
		return numFound;
	}
	
	public String getColor() {
		return color;
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
		numFound = targets.size();
	}
	
	public void addPath(Coordinate c) {
		path.add(c);
	}
	
	public Message getBroadcast() {
		return broadcast;
	}
	
	public void clearBroadcast() {
		broadcast = null;
	}
	
	public void addMessage(Message m) {
		inbox.add(m);
	}
}
