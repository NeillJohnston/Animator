package animator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

/**
 * AnimatorCanvas is a custom JPanel that holds all of the frame drawing.
 * It has methods to draw strokes on the canvas, which later will be saved.
 * 
 * @author Neill Johnston
 */
public class AnimatorCanvas extends JPanel {
	ArrayList<Stroke> strokes = new ArrayList<Stroke>();
	
	/**
	 * Main constructor for the canvas.
	 * Initializes the mouse listeners and invokes necessary JPanel methods
	 * to display itself.
	 */
	public AnimatorCanvas() {
		super();
		
		// Add the mouse listener.
		AnimatorCanvasAdapter adapter = new AnimatorCanvasAdapter(this);
		this.addMouseListener(adapter);
		this.addMouseMotionListener(adapter);
		
		this.setPreferredSize(new Dimension(640, 480));
		this.setVisible(true);;
	}
	
	/**
	 * Override JPanel's paint to call paint on all of the strokes.
	 * 
	 * @param g		Graphics object being used
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		// Draw all of the strokes from the Set strokes.
		Iterator<Stroke> i = strokes.iterator();
		while(i.hasNext())
			i.next().paint(g);
	}
	
	/**
	 * A custom MouseAdapter to handle events from AnimatorCanvas.
	 * This will create new strokes and add them to the canvas.
	 */
	private class AnimatorCanvasAdapter extends MouseAdapter {
		static final int STROKE_TOLERANCE = 5;
		AnimatorCanvas parent;
		Point lastPoint;
		
		Stroke currentStroke;
		
		/**
		 * Constructor including the adapter's parent canvas.
		 * 
		 * @param parent	the canvas that uses this adapter
		 */
		public AnimatorCanvasAdapter(AnimatorCanvas parent) {
			super();
			this.parent = parent;
			this.lastPoint = new Point(-100, -100);
		}
		
		/**
		 * Start a new stroke when the mouse is pressed.
		 * 
		 * @param e		the event
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			System.out.println(e.getPoint().toString());
			currentStroke = new PenStroke(e.getPoint());
			
			lastPoint = e.getPoint();
			parent.strokes.add(currentStroke);
			parent.repaint();
		}
		
		/**
		 * Continue the stroke when the mouse is dragged.
		 * 
		 * @param e		the event
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			// If within the tolerable range, make a new point.
			if(e.getPoint().distance(lastPoint) > STROKE_TOLERANCE) {
				currentStroke.update(e.getPoint());
				
				lastPoint = e.getPoint();
				parent.repaint();
			}
		}
	}
}


