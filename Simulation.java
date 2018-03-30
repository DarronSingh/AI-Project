import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Simulation extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static final int FRAMEX = 1000, FRAMEY = 1000, SIZE = 100;
	private static final String SIMNAME = "Simulation v1.0";
	public boolean isSimulating;
	private static int mode, wins; // scenario and win count
	
	private static Node[][] nodes = new Node[SIZE][SIZE];
	private static Agent[] agents = new Agent[5];

	public Simulation() {
		setup();
		setFocusable(true);
		isSimulating = true;
		System.out.println("Simulation started in scenario " + mode);
	}
	
	public void setup() {
		
		// initialize agents
		for (int i=0; i<agents.length; i++)
			agents[i] = new Agent(i, FRAMEX, FRAMEY);
		
		// initialize nodes
		for (int i=0; i<SIZE; i++) {
			for (int j=0; j<SIZE; j++)
				nodes[i][j] = new Node(i, j, FRAMEX, FRAMEY);
		}
		
		// pick locations for target nodes
		Integer[] xls = new Integer[100];
		Integer[] yls = new Integer[100];
		for (int i=0; i<SIZE; i++) {
			xls[i] = i;
			yls[i] = i;
		}
		Collections.shuffle(Arrays.asList(xls)); // x coordinates for targets
		Collections.shuffle(Arrays.asList(yls)); // y coordinates for targets
		
		// setup targets for agents 0 - 4
		for (int i=0; i<agents.length*5; i++) {
			nodes[xls[i]][yls[i]].setTargetID(i);
			nodes[xls[i]][yls[i]].setAgentID(i/5);
			System.out.println("Target " + i + " for Agent " + i/5 + " created at x:" + nodes[xls[i]][yls[i]].getX() + ", y:" + nodes[xls[i]][yls[i]].getY());
		}
	}
	
	public void detectAgentCollision() {
		for (int i=0; i<agents.length; i++) {
			for (int j=0; j<agents.length; j++) {
				
				// don't check collision with self
				if (agents[i].getAgentID() == agents[j].getAgentID())
					continue;
				
				// if its already diverting, leave alone
				if (agents[i].isDiverting || agents[j].isDiverting)
					continue;
				
				// get center radius, coordinates, velocities
				int x1 = agents[i].getX();
				int y1 = agents[i].getY();
				int x2 = agents[j].getX();
				int y2 = agents[j].getY();
				int velX1 = agents[i].getVelX();
				int velY1 = agents[i].getVelY();
				
				
				// check if agents are within 1 unit of each other
				if (Math.abs(x1-x2) <= 2 && Math.abs(y1-y2) <= 2) {
					
					// check if agent 1 is moving horizontally
					if (velX1 != 0) {
						
						// check if also moving vertically (diagonal case)
						if (velY1 != 0) {
							agents[i].divertPath(new Coordinate(x1+2, y1)); // divert to the right
						}
						
						// (horizontal only case)
						else {
							agents[i].divertPath(new Coordinate(x1, y1-2)); //divert up
						}
					}
					
					// check if agent 1 is moving vertically (vertical case)
					else if (velY1 != 0) {
						agents[i].divertPath(new Coordinate(x1+2, y1)); // divert to the right
					}
					
					// non moving collision
					else {
						System.out.println("error: not moving");
					}
				}
			}
		}
	}
	
	public void detectTargetCollision() {
		for (int k=0; k<agents.length; k++) {
			for (int i=0; i<SIZE; i++) {
				for (int j=0; j<SIZE; j++) {
					if (nodes[i][j].getAgentID() == agents[k].getAgentID()) {
						int aX = agents[k].getX();
						int aY = agents[k].getY();
						int tX = nodes[i][j].getX();
						int tY = nodes[i][j].getY();
						
						if (Math.pow(tX - aX, 2) + Math.pow(tY - aY, 2) <= Math.pow(agents[k].getRadius(), 2)) {
							System.out.println("Agent: " + agents[k].getAgentID() + " (" + agents[k].getColor() +") found Target: " + nodes[i][j].getTargetID());
							agents[k].addTarget(nodes[i][j]); // add to list of targets
							nodes[i][j].clearTarget(); // remove targetID and agentID
							continue;
						}
					}
				}
			}
		}
	}

	public void update() {
		detectAgentCollision();
		detectTargetCollision();
		
		// loop through all agents
		for (int i=0; i<agents.length; i++) {
			if (agents[i].isActive) {
				checkBroadcast(agents[i]);
				agents[i].update();
			}
		}
	}
	
	public void checkBroadcast(Agent a) {
		if (a.getBroadcast() != null) {
			
			// check for win condition, on win stop simulation
			if (a.getBroadcast().body.equals("won")) {
				a.clearBroadcast();
				wins++;
				System.out.println(String.valueOf("Agent: " + a.getAgentID()) + " (" + a.getColor() +") found all targets.");
				
				// scenario 1 = 5 wins, else 1 win
				if ((mode == 1 && wins == agents.length) || mode != 1)
					isSimulating = false;
			}
			
			// check for path completion
			else if (a.getBroadcast().body.equals("done path")) {
				System.out.println(String.valueOf("Agent: " + a.getAgentID()) + " (" + a.getColor() +") has comepleted it's path.");
			}
		}
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		super.paint(g);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		
		// paint the targets
		for (int i=0; i<SIZE; i++) {
			for (int j=0; j<SIZE; j++)
				nodes[i][j].draw(g2d);
		}
		
		// paint the agents
		for (int i=0; i<agents.length; i++)
			agents[i].draw(g2d);
	}
	
	public static void main(String args[]) throws InterruptedException {
		
		mode = 0;
		
		JFrame frame = new JFrame(SIMNAME);
		Simulation sim = new Simulation();
		frame.add(sim);
		frame.setSize(FRAMEX+15, FRAMEY+40); // offset so all cells show in window
		frame.setResizable(false);
		frame.setLocation(900, 0);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		while(sim.isSimulating) {
			sim.update();
			sim.repaint();
			Thread.sleep(25);
		}
		
		System.out.println("Simulation complete in scenario " + mode);
	}
}
