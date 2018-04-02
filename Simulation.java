import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Simulation extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static final int FRAMEX = 1000, FRAMEY = 1000, SIZE = 100;
	private static final String SIMNAME = "Simulation v1.0";
	private static final DecimalFormat TIMEFORMAT = new DecimalFormat("#.00");
	private static int wins, mostFound;
	public static int mode;
	public boolean isSimulating;
	
	private Node[][] nodes = new Node[SIZE][SIZE];
	private Agent[] agents = new Agent[5];

	public Simulation(int iteration) {
		setup();
		setFocusable(true);
		isSimulating = true;
		System.out.println("Simulation #" + iteration + " started in scenario " + mode);
	}
	
	public void setup() {
		
		// initialize counters
		wins = mostFound = 0;
		
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
//			System.out.println("Target " + i + " for Agent " + i/5 + " created at x:" + nodes[xls[i]][yls[i]].getX() + ", y:" + nodes[xls[i]][yls[i]].getY());
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
				if (Math.hypot(x1-x2, y1-y2) <= 10) {
					
					// check if agent 1 is moving horizontally
					if (velX1 != 0) {
						
						// check if also moving vertically (diagonal case)
						if (velY1 != 0) {
							 
							// check which side of window is farther
							if (FRAMEX - x1 > x1)
								agents[i].divertPath(new Coordinate(x1+5, y1)); // divert right
							else
								agents[i].divertPath(new Coordinate(x1-5, y1)); // divert left
						}
						
						// (horizontal only case)
						else {
							
							// check which side of window is farther
							if (FRAMEY - y1 > y1)
								agents[i].divertPath(new Coordinate(x1, y1-5)); // divert up
							else
								agents[i].divertPath(new Coordinate(x1, y1+5)); // divert down
						}
					}
					
					// check if agent 1 is moving vertically (vertical case)
					else if (velY1 != 0) {
						
						// check which side of window is farther
						if (FRAMEX - x1 > x1)
							agents[i].divertPath(new Coordinate(x1+5, y1)); // divert right
						else
							agents[i].divertPath(new Coordinate(x1-5, y1)); // divert left
					}
					
					else {/*non moving collision, should never happen*/}
				}
			}
		}
	}
	
	public void detectTargetCollision() {
		for (int k=0; k<agents.length; k++) {
			for (int i=0; i<SIZE; i++) {
				for (int j=0; j<SIZE; j++) {
					
					// get values for calculation
					int aX = agents[k].getX();
					int aY = agents[k].getY();
					int tX = nodes[i][j].getX();
					int tY = nodes[i][j].getY();
					boolean isWithinRadius = false;
					
					// calculate if target is within agent's search radius
					if (Math.pow(tX - aX, 2) + Math.pow(tY - aY, 2) <= Math.pow(agents[k].getRadius(), 2))
						isWithinRadius = true;
					else
						continue;
					
					// if target belongs to this agent
					if (nodes[i][j].getAgentID() == agents[k].getAgentID()) {
						
						// check if target is within search radius
						if (isWithinRadius) {
//							System.out.println("Agent: " + agents[k].getAgentID() + " (" + agents[k].getColor() +") found Target: " + nodes[i][j].getTargetID());
							agents[k].addTarget(nodes[i][j]); // add to list of targets
							nodes[i][j].clearTarget(); // remove targetID and agentID
							nodes[i][j].setIsFound(true);
							
							// check if new highest amount of targets found
							if (agents[k].getNumFound() > mostFound)
								mostFound = agents[k].getNumFound();
						}
					}
					
					// if in mode 1 or 2, if there is still a target there, but belongs to a different agent
					else if (mode != 0 && nodes[i][j].getAgentID() != -1) {
						
						// check if target is within search radius and is not already found
						if (isWithinRadius && !nodes[i][j].getIsFound()) {
							nodes[i][j].setIsFound(true);
							
							// if scenario 3, check if recipients has less than mostFound-2 targets found
							if (mode == 2 && agents[nodes[i][j].getAgentID()].getNumFound() < mostFound-2)
								continue;
							agents[nodes[i][j].getAgentID()].addMessage(new Message(nodes[i][j].getAgentID(), "target found", new Coordinate(nodes[i][j].getX(), nodes[i][j].getY())));
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
				System.out.println(String.valueOf("Agent: " + a.getAgentID()) + " (" + a.getColor() +") found all targets. Step count: " + a.getStepCount());
				
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
	
	public void generateCSV(int iteration) throws IOException {
		FileWriter fw = new FileWriter("G13_1.csv", true);
		StringBuilder sb = new StringBuilder();
		
//		sb.append("a,b,c,d,e,f,g,h,i,j,k\n"); // columns
		
		for (Agent a : agents) {
			sb.append(String.valueOf(mode)); // a
			sb.append(",");
			sb.append(String.valueOf(iteration)); // b
			sb.append(",");
			sb.append(String.valueOf(a.getAgentID())); // c
			sb.append(",");
			sb.append(String.valueOf(a.getNumFound())); // d
			sb.append(",");
			sb.append(String.valueOf(a.getStepCount())); // e
			sb.append(",");
			sb.append(String.valueOf(a.getHappiness())); // f
			sb.append(",");
			sb.append(String.valueOf(a.getMaxHappiness())); // g
			sb.append(",");
			sb.append(String.valueOf(a.getMinHappiness())); // h
			sb.append(",");
			sb.append(String.valueOf(a.getAverageHappiness())); // i
			sb.append(",");
			sb.append(String.valueOf(a.getSTDHappiness())); // j
			sb.append(",");
			sb.append(String.valueOf(a.getCompetitiveness())); // k
			sb.append("\n");
		}
		
		fw.write(sb.toString());
		fw.close();
	}
	
	public void log(String s) {
	}
	
	public static void main(String args[]) throws InterruptedException, IOException {
		
		Scanner in = new Scanner(System.in);
		int iterations, simSpeed;
		
		// get scenario number
		do {
			System.out.print("Enter the scenario you wish to simulate (1-3): ");
			mode = in.nextInt() - 1;
		} while (mode < 0 || mode > 2);
		
		// get number of iterations
		do {
			System.out.print("Enter the number of iterations: ");
			iterations = in.nextInt();
		} while (iterations <= 0);
		
		// get simulation speed
		do {
			System.out.print("Enter simulation speed (0-4) or 5 for no animations: ");
			simSpeed = in.nextInt();
			simSpeed = ((5-simSpeed)*10);
		} while (simSpeed != 0 && (simSpeed < 10 || simSpeed > 50));
		
		double totalRuntime = System.nanoTime();
		
		for (int i=0; i<iterations; i++) {
			System.out.println();
			
			JFrame frame = new JFrame(SIMNAME + " - scenario " + (mode+1));
			Simulation sim = new Simulation(i);
			
			// decide whether to open window or not
			if (simSpeed > 0) {
				frame.add(sim);
				frame.setSize(FRAMEX+15, FRAMEY+40); // offset so all cells show in window
				frame.setResizable(false);
				frame.setLocation(900, 10); // moved window to right to see command window
				frame.setAlwaysOnTop(true);
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
			
			while(sim.isSimulating) {
				sim.update();
				
				// if no window, then don't do these
				if (simSpeed>0 ) {
					sim.repaint();
					Thread.sleep(simSpeed);
				}
			}
			
			sim.generateCSV(i); // output csv
			
			if (simSpeed > 0) {
				frame.setVisible(false);
				frame.dispose();
			}
		}
		
		// calculate time elapsed for all iterations
		totalRuntime = (System.nanoTime() - totalRuntime)/1000000000.0;
		
		System.out.println();
		if (simSpeed == 0)
			System.out.println("Simulation complete in scenario " + (mode+1) + " with " + iterations + " iterations and no animation.");
		else
			System.out.println("Simulation complete in scenario " + (mode+1) + " with " + iterations + " iterations and animation speed " + simSpeed + ".");
		System.out.println("Total runtime: " + TIMEFORMAT.format(totalRuntime) + "s");
		in.close();
	}
}
