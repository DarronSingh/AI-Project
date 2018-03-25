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
			nodes[xls[i]][yls[i]].setAgentID(i%5);
			System.out.println("target " + i + " created at x:" + nodes[xls[i]][yls[i]].getX() + ", y:" + nodes[xls[i]][yls[i]].getY());
		}
	}
	
	public void detectAgentCollision() {
		
	}
	
	public void detectTargetCollision() {
		// collect target
	}

	public void update() {
		// check for agent broadcasts
		// handle them, then reset to ""
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
			Thread.sleep(15);
		}
	}
}
