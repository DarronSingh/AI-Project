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
	
	private static Node[][] nodes = new Node[SIZE][SIZE];
	private static Agent[] agents = new Agent[5];

	public Simulation() {
		setup();
		setFocusable(true);
		isSimulating = true;
		System.out.println("Simulation started");
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
				// get center coordinates
				int cX1 = agents[i].getX()+agents[i].getRadius();
				int cY1 = agents[i].getY()+agents[i].getRadius();
				int cX2 = agents[j].getX()+agents[j].getRadius();
				int cY2 = agents[j].getY()+agents[j].getRadius();
				
				// get distance squared and radius squared
				int dSqr = (int) (Math.pow((cX2 - cX1), 2) + Math.pow((cY2 - cY1), 2));
				int rSqr = (int) Math.pow(agents[i].getRadius() + agents[j].getRadius(), 2);
				
				if (dSqr <= rSqr) {
					// swap velocities
					int tempVelX = agents[i].getVelX();
					int tempVelY = agents[i].getVelY();
					
					agents[i].setVelX(agents[j].getVelX());
					agents[i].setVelY(agents[j].getVelY());
//					agents[i].move();
					
					agents[j].setVelX(tempVelX);
					agents[j].setVelY(tempVelY);
//					agents[j].move();
				}
			}
			agents[i].move();
		}
	}
	
	public void detectTargetCollision() {
		for (int k=0; k<agents.length; k++) {
			for (int i=0; i<SIZE; i++) {
				for (int j=0; j<SIZE; j++) {
					if (nodes[i][j].getAgentID() == agents[k].getAgentID()) {
						int aX = agents[k].getX()+agents[k].getRadius();
						int aY = agents[k].getY()+agents[k].getRadius();
						int tX = nodes[i][j].getX();
						int tY = nodes[i][j].getY();
						
						if (Math.pow(tX - aX, 2) + Math.pow(tY - aY, 2) <= Math.pow(agents[k].getRadius(), 2)) {
							System.out.println("Agent: " + agents[k].getAgentID() + " found Target: " + nodes[i][j].getTargetID());
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
//		detectAgentCollision(); // fix this
		detectTargetCollision();
		
		// loop through all agents
		for (int i=0; i<agents.length; i++) {
			
			// check if the agent is still active
			if (agents[i].isActive) {
				agents[i].update(); // update the agent
				
				// check if public broadcast exists
				if (agents[i].getPublicBroadcast() != "") {
					
					// check for win condition
					if (agents[i].getPublicBroadcast().equals("won")) {
						System.out.println(String.valueOf("Agent " + agents[i].getAgentID()) + " won!");
						isSimulating = false;
					}
					
					// check for path completion
					else if (agents[i].getPublicBroadcast().equals("done path")) {
						System.out.println(String.valueOf("Agent " + agents[i].getAgentID()) + " has comepleted it's path.");
					}
					
					agents[i].resetPublicBroadcast(); // clear broadcast
				}
				
				if (agents[i].getPrivateaBroadcast() != "") {
					// handle private broadcasts
					agents[i].resetPrivateaBroadcast(); // clear broadcast
				}
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
		JFrame frame = new JFrame(SIMNAME);
		Simulation sim = new Simulation();
		frame.add(sim);
		frame.setSize(FRAMEX+15, FRAMEY+40); // offset so all cells show in window
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		while(sim.isSimulating) {
			sim.update();
			sim.repaint();
			Thread.sleep(30);
		}
	}
}
