package animator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * AnimatorCanvas is a custom JPanel that holds all of the frame drawing.
 * It has methods to draw strokes on the canvas, which later will be saved.
 * 
 * @author Neill Johnston
 */
public class AnimatorCanvas extends JPanel {
	private static double ONION_ALPHA = 0.25;
	
	private double zoom;
	private double x;
	private double y;
	
	/**
	 * Main constructor for the canvas.
	 * Initializes the mouse listeners and invokes necessary JPanel methods
	 * to display itself.
	 */
	public AnimatorCanvas() {
		super();
		
		zoom = 1.0;
		x = 0;
		y = 0;
		setBackground(Color.white);
		
		// Add the mouse listener.
		AnimatorCanvasAdapter adapter = new AnimatorCanvasAdapter(this);
		this.addMouseListener(adapter);
		this.addMouseMotionListener(adapter);
		this.addMouseWheelListener(adapter);
		
		this.setVisible(true);
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
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(x, y);
		g2d.scale(zoom, zoom);
		
		// If a previous frame exists, onion-skin it.
		Frame lastFrame = Manager.layers.get(0).get((int) Manager.anim.get("current") - 1);
		if(lastFrame != null) {
			for(Stroke s : lastFrame)
				s.paint(g2d, ONION_ALPHA);
		}
		
		// Draw the current frame in full color.
		if(Manager.getCurrentFrame() != null) {
			for(Stroke s : Manager.getCurrentFrame())
				s.paint(g2d);
		}
	}
	
	/**
	 * Change the zoom level using a (2^x)-type function.
	 * 
	 * @param dzoom		change in zoom
	 */
	public void zoom(double dzoom) {
		zoom *= Math.pow(0.5, dzoom / 5);
		repaint();
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
			double mousex = e.getPoint().getX();
			double mousey = e.getPoint().getY();
			Point finalPoint = new Point((int)((mousex - x) / zoom), (int)((mousey - y) / zoom));
			
			if(SwingUtilities.isLeftMouseButton(e)) {
				currentStroke = Manager.getNewCurrentStroke(finalPoint);
				
				if(Manager.getCurrentFrame() != null)
					Manager.getCurrentFrame().add(currentStroke);
			}
			
			lastPoint = e.getPoint();
			parent.repaint();
		}
		
		/**
		 * Continue the stroke when the mouse is dragged.
		 * 
		 * @param e		the event
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			double mousex = e.getPoint().getX();
			double mousey = e.getPoint().getY();
			Point finalPoint = new Point((int)((mousex - x) / zoom), (int)((mousey - y) / zoom));
			
			// If within the tolerable range, make a new point.
			if(SwingUtilities.isLeftMouseButton(e) &&
					e.getPoint().distance(lastPoint) > STROKE_TOLERANCE) {
				currentStroke.update(finalPoint);
				
				lastPoint = e.getPoint();
			}
			// If the middle mouse button is the one dragging, change x and y.
			else if(SwingUtilities.isMiddleMouseButton(e)) {
				parent.x += (e.getPoint().getX() - lastPoint.getX());
				parent.y += (e.getPoint().getY() - lastPoint.getY());
				
				lastPoint = e.getPoint();
			}
			parent.repaint();
		}
		
		/**
		 * Zoom when the mouse wheel is used.
		 * 
		 * @param e		the event
		 */
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			double oldZoom = parent.zoom;
			parent.zoom(e.getPreciseWheelRotation());
			// Shift x and y over so that the zoom effect happens at the center of the canvas.
			parent.x += (parent.zoom - oldZoom) * (parent.x - parent.getWidth() / 2) / oldZoom;
			parent.y += (parent.zoom - oldZoom) * (parent.y - parent.getHeight() / 2) / oldZoom;
		}
	}
}


