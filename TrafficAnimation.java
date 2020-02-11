import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * CS 121 Project 1: Traffic Animation
 *
 * Animated ocean scene.
 *
 * @author BSU CS 121 Instructors
 * @author Jason Dudley
 * @version CS121-002, Spring 2020
 * 
 */
@SuppressWarnings("serial")
public class TrafficAnimation extends JPanel {
	// CONSTANTS
	private final int DELAY = 100; // milliseconds
	private int xOffset = 0; // initial offset
	private int stepSize = 4; // global increment
	private int yOffset = 0; // y offset for fish
	private int yStepSize = 1; // global y increment
	private boolean tailFlip = true; // Boolean for tail flip
	private boolean hooked = false; // Boolean for if fish hooked
	private final Color BACKGROUND_COLOR = new Color(204, 255, 255);

	public void paintComponent(Graphics g) {
		// Get the current width and height of the window.
		int width = getWidth(); // panel width
		int height = getHeight(); // panel height
		// Fill the graphics page with the background color.
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, width, height);
		if (!hooked) { // Keep moving everything with offsets
			// Offset increased to allow for smooth transition on and off screen
			xOffset = (xOffset + stepSize) % (width * 3 / 2);
			// Offset for vertical fish movement
			yOffset = (yOffset + yStepSize) % (height / 2);			
		}
		else { // Fish hooked
			g.setColor(Color.black);
			g.drawString("FISH ON!", width / 4, height / 4);
		}
		// Flip tail
		tailFlip = !tailFlip;
		/**
		 * Background
		 */
		// Sun
		g.setColor(Color.yellow);
		g.fillOval(width * 3 / 4, height / 12, height / 6, height / 6);
		// Clouds
		int cloud = width / 48;
		int cloudX = width * 2 / 3;
		int cloudY = height / 6;
		g.setColor(Color.white);
		// Generate cloud set
		for (int x = 2; x < 7; x++) {
			int cloudSize = cloud * x;
			if (x > 4)
				cloudSize = cloud * (8 - x);
			g.fillOval(cloudX + cloud * x, cloudY - cloud * x, cloudSize, cloudSize);		
		}
		// Water colors
		Color waterColor = new Color(0, 51, 102);
		Color waterAccent = new Color(0, 0, 204);
		Color waveColor = Color.white;
		// Draw horizon water
		g.setColor(waterColor);
		g.fillRect(0, height / 3, width, height); // IMPORTANT: y Offset based on 1/3 height
		// Draw island
		Color landColor = new Color(204, 102, 0);
		g.setColor(landColor);
		g.fillArc(width / 12, height / 4, width / 4, height / 4, 0, 180);
		g.fillRect(0, height / 4, width / 5, height / 8);
		// Beach
		Color beachColor = new Color(255, 229, 204);
		g.setColor(beachColor);
		g.fillArc(width / -6, height * 5 / 16 - 1, width / 2, height / 8, 0, -180);
		// Palm Trees
		Color palmTrunk = new Color(102, 51, 0);
		Color palmFrond = new Color(0, 153, 0);
		int palmHeight = height / 12;
		int palmWidth = width / 64;
		int palmY = height * 3 / 8 - palmHeight * 2 / 3;
		for (int x = 0; x < width / 4; x += width / 16) {
			g.setColor(palmTrunk);
			g.fillRect(x, palmY + 1, palmWidth, palmHeight); // +1 for truncation fix
			g.fillOval(x - 1, palmY + palmHeight - palmWidth / 2 - 1, palmWidth, palmWidth);
			g.setColor(palmFrond);
			int frondHeight = palmWidth * 2;
			if (x % 2 == 0) // vary height for even trees
				frondHeight = palmWidth * 3;
			g.fillArc(x - palmWidth * 3 / 2, palmY, palmWidth * 4, frondHeight, 0, 180);
		}
		// Accent water as lane for main boat with waves
		int waveLength = width / 24;
		int waveHeight = height / 24;
		g.setColor(waterAccent);
		g.fillRect(0, height * 7 / 12 - waveHeight * 3 / 2, width, waveHeight * 4);
		// Waves
		for (int x = 0; x < width * 2; x += waveLength) {
			g.setColor(waterColor); // backfill top waves
			g.fillArc(x - xOffset / 4, height * 7 / 12 - waveHeight * 2, waveLength, waveHeight, 0, -180);
			g.setColor(waterAccent); // backfill bottom waves
			g.fillArc(x - xOffset / 4, height * 7 / 12 + waveHeight * 2, waveLength, waveHeight, 0, -180);
			g.setColor(waveColor); // outline/border lines
			g.drawArc(x - xOffset / 4, height * 7 / 12 - waveHeight * 2, waveLength, waveHeight, 0, -180);
			g.drawArc(x - xOffset / 4, height * 7 / 12 + waveHeight * 2, waveLength, waveHeight, 0, -180);
		}
		/**
		 * CALCULATIONS
		 */
		// Splash height (vertical motion from rolling, used for boat)
		int splashHeight = xOffset % 20;
		if (splashHeight > 10) // start decreasing height
			splashHeight = (10 - splashHeight % 10) / 5;
		else
			splashHeight = splashHeight / 5;
		// Hull
		int boatLength = width * 3 / 8;
		int hullLength = width / 4 + 1; // +1 accounts for truncation, fixes gaps at large scale
		int hullHeight = hullLength / 3;
		int hullX = xOffset - boatLength;
		int hullY = height / 2 - splashHeight;
		// Cabin
		int cabinLength = hullLength * 2 / 3;
		int cabinHeight = hullHeight / 2;
		int cabinX = hullX + hullLength / 3;
		/**
		 * ANGLER
		 */
		int anglerHeight = cabinHeight;
		int anglerX = xOffset - boatLength + cabinLength / 4;
		int anglerY = hullY - anglerHeight;
		Color skin = new Color(255, 204, 153);
		// Body
		g.setColor(skin);
		g.fillRect(anglerX, anglerY + anglerHeight / 4, anglerHeight / 3, anglerHeight);
		g.fillOval(anglerX - anglerHeight / 6, anglerY, anglerHeight / 2, anglerHeight / 2);
		g.fillArc(anglerX - anglerHeight / 3, anglerY + anglerHeight / 2, anglerHeight / 2, anglerHeight / 2, 0, 180);
		// Eye
		g.setColor(Color.black);
		g.fillOval(anglerX - anglerHeight / 12, anglerY + anglerHeight / 4, anglerHeight / 8, anglerHeight / 8);
		// Pole
		g.setColor(Color.CYAN);
		g.drawLine(anglerX - anglerHeight / 3, anglerY + anglerHeight * 2 / 3, hullX - anglerHeight / 3,
				hullY - cabinHeight * 2);
		// Fishing Line
		g.setColor(Color.white);
		g.drawLine(hullX - anglerHeight / 3, hullY - cabinHeight * 2, hullX - cabinLength, height * 3 / 4);
		g.setColor(Color.CYAN);
		g.drawArc(hullX - cabinLength - anglerHeight / 4, height * 3 / 4 - anglerHeight / 4,anglerHeight / 4,anglerHeight / 4,0,-180);
		g.drawArc(hullX - cabinLength, height * 3 / 4 - anglerHeight / 4,anglerHeight / 4,anglerHeight / 4,0,-180);
		// Hat
		g.setColor(Color.red);
		g.fillRect(anglerX - anglerHeight / 3, anglerY + anglerHeight / 8, anglerHeight * 3 / 4, anglerHeight / 6);
		g.fillArc(anglerX - anglerHeight / 6, anglerY - anglerHeight / 12, anglerHeight / 2, anglerHeight / 2, 0, 180);
		/**
		 * MAIN BOAT
		 */
		// Draw hull
		g.setColor(Color.white);
		g.fillRect(hullX, hullY, hullLength, hullHeight);
		g.fillArc(xOffset - hullLength, hullY - hullHeight, hullLength, hullHeight * 2, 0, -90);
		// Draw cabin
		g.fillRect(cabinX, hullY - cabinHeight, cabinLength, cabinHeight);
		int cabinFrontX[] = { xOffset - hullLength / 2, xOffset - hullLength / 2,
				xOffset - hullLength / 2 + cabinHeight };
		int cabinFrontY[] = { hullY - cabinHeight, hullY, hullY };
		g.fillPolygon(cabinFrontX, cabinFrontY, 3);
		// Bridge
		int bridgeLength = cabinLength * 3 / 4;
		int bridgeHeight = cabinHeight;
		int bridgeX = cabinX;
		int bridgeY = hullY - cabinHeight;
		g.fillRect(bridgeX, hullY - cabinHeight - bridgeHeight, bridgeLength, bridgeHeight);
		// Tower
		int towerLength = bridgeLength * 3 / 4;
		int towerHeight = cabinHeight / 2;
		int towerX = bridgeX + cabinHeight / 4;
		int towerY = bridgeY - bridgeHeight - towerHeight;
		g.drawRect(towerX, towerY, towerLength, towerHeight);
		for (int x = towerX; x < towerX + towerLength; x += towerLength / 3)
			g.drawLine(x, towerY, x, towerY + towerHeight);
		// Outriggers
		int outRiggerLength = hullHeight * 2;
		int outRiggerX = xOffset - hullLength;
		int outRiggerY = bridgeY;
		g.drawLine(outRiggerX, outRiggerY, outRiggerX - towerLength, outRiggerY - outRiggerLength);
		// Hull windows
		int portDiameter = hullHeight / 4;
		g.setColor(Color.black);
		for (int x = 0; x < 4; x++)
			g.fillOval(cabinX + x * 4 * portDiameter, hullY + portDiameter, portDiameter, portDiameter);
		// Cabin windows
		g.fillRect(cabinX + cabinHeight / 4, hullY - cabinHeight * 3 / 4, cabinLength - cabinHeight / 4,
				cabinHeight * 3 / 4);
		int cabinFrontWindowX[] = { xOffset - hullLength / 2, xOffset - hullLength / 2,
				xOffset - hullLength / 2 + cabinHeight * 3 / 4 };
		int cabinFrontWindowY[] = { hullY - cabinHeight * 3 / 4, hullY, hullY };
		g.fillPolygon(cabinFrontWindowX, cabinFrontWindowY, 3);
		// Bridge window
		int bridgeFrontX[] = { bridgeX + bridgeLength, bridgeX + bridgeLength, bridgeX + bridgeLength * 7 / 6 };
		int bridgeFrontY[] = { bridgeY, bridgeY - bridgeHeight, bridgeY };
		g.fillPolygon(bridgeFrontX, bridgeFrontY, 3);
		// Boat naming
		Font hullFont = new Font(g.getFont().getFontName(), g.getFont().getStyle(),
				g.getFont().getSize() * width / 800);
		g.setFont(hullFont);
		g.drawString("Janet Lou", hullX + hullHeight / 6, hullY + hullHeight / 6);
		// Railing
		g.setColor(Color.white);
		g.drawRect(xOffset - boatLength, hullY - hullHeight / 6, boatLength, hullHeight / 6);
		for (int x = xOffset - boatLength; x < xOffset; x += boatLength / 6)
			g.drawLine(x, hullY - hullHeight / 6, x, hullY);
		// Waves splashing at surface
		splashHeight = xOffset % 60; // Splash height recalculated for waves on hull
		if (splashHeight > 30) // start decreasing height
			splashHeight = 30 - splashHeight % 30;
		g.setColor(waterAccent);
		for (int x = 1; x <= 6; x++) { // 6 waves against boat
			g.fillOval(xOffset - (boatLength * x / 6), hullY + hullHeight - splashHeight / 2, hullLength / 3,
					splashHeight);
		}
		/**
		 * Fish
		 */
		int fishWidth = width / 4;
		int fishHeight = fishWidth / 6;
		int fishOffset = xOffset * 2 - fishWidth * 2;
		int fishX = width - fishOffset;
		int fishY = height * 3 / 4;
		int fishScale = fishWidth / 6;
		if (fishOffset > width + fishWidth) { // Swimming left
			fishX = fishOffset - (width + fishWidth);
			fishScale = fishScale * - 1;
		}
		else { // Swimming left
			fishX = width - fishOffset;
		}
		// Vertical Movement & Angle Fish
		int fishYScale = fishHeight / 6;
		int fishYOffset = yOffset + height * 3 / 4;
		if (fishYOffset > height) { // Swimming up
			fishY = height - (fishYOffset % (height / 2));
		}
		else { // Swimming Down
			fishY = fishYOffset;
			fishYScale = fishYScale * - 1;			
		}
		// Draw fish
		Color fishAccent = new Color(0, 255, 255);
		Color fishColor = new Color(51, 255, 153);
		g.setColor(fishColor);
		// Fin
		int finX[] = { fishX + fishScale, fishX + fishScale * 2, fishX + fishScale * 3};
		int finY[] = { fishY + fishYScale, fishY + fishYScale * 2 - fishHeight, fishY + fishYScale * 3 };
		int finN = 3;
		g.fillPolygon(finX, finY, finN);
		g.setColor(fishAccent);
		g.drawPolygon(finX, finY, finN);
		// Tail
		int tailOffset = (tailFlip) ? fishYScale : -fishYScale;
		int tailX[] = {fishX + fishScale * 5, fishX + fishScale * 6, fishX + fishScale * 6};
		int tailY[] = {fishY + fishYScale * 5, fishY + fishYScale * 6 - fishHeight + tailOffset, 
				fishY + fishYScale * 6 + tailOffset};
		int tailN = 3;
		g.setColor(fishColor);
		g.fillPolygon(tailX, tailY, tailN);
		g.setColor(fishAccent);
		g.drawPolygon(tailX, tailY, tailN);
		g.setColor(fishAccent);
		// Check if fish hooked
		int baitX = hullX - cabinLength;
		int baitY = height * 3 / 4;
		if (baitX - fishX > -8 && baitX - fishX < 8 && 
				baitY - fishY > -8 && baitY - fishY < 8) 
			hooked = true;
		/**
		 * END DRAWING
		 */
		// Put your code above this line. This makes the drawing smoother.
		Toolkit.getDefaultToolkit().sync();
	}

	// ==============================================================
	// You don't need to modify anything beyond this point.
	// ==============================================================

	/**
	 * Starting point for this program. Your code will not go in the main method for
	 * this program. It will go in the paintComponent method above.
	 *
	 * DO NOT MODIFY this method!
	 *
	 * @param args unused
	 */
	public static void main(String[] args) {
		// DO NOT MODIFY THIS CODE.
		JFrame frame = new JFrame("Traffic Animation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new TrafficAnimation());
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Constructor for the display panel initializes necessary variables. Only
	 * called once, when the program first begins. This method also sets up a Timer
	 * that will call paint() with frequency specified by the DELAY constant.
	 */
	public TrafficAnimation() {
		// Do not initialize larger than 800x600. I won't be able to
		// grade your project if you do.
		int initWidth = 600;
		int initHeight = 400;
		setPreferredSize(new Dimension(initWidth, initHeight));
		this.setDoubleBuffered(true);

		// Start the animation - DO NOT REMOVE
		startAnimation();
	}

	/**
	 * Create an animation thread that runs periodically. DO NOT MODIFY this method!
	 */
	private void startAnimation() {
		ActionListener timerListener = new TimerListener();
		Timer timer = new Timer(DELAY, timerListener);
		timer.start();
	}

	/**
	 * Repaints the graphics panel every time the timer fires. DO NOT MODIFY this
	 * class!
	 */
	private class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			repaint();
		}
	}
}
