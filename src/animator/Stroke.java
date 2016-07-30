package animator;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

/**
 * An interface for various types of strokes - brush, pencil, etc.
 * 
 * @author Neill Johnston
 */
public abstract class Stroke extends ArrayList<Point> {
	protected Point start;
	protected Point end;
	
	// Stroke settings.
	protected int width;
	protected Color color;
	
	/**
	 * Construct this Stroke object with specified initial coordinates.
	 */
	public Stroke(Point start) {
		super();
		
		this.start = start;
		this.width = (Integer) Animator.getManager().tool.get("width");
		this.color = (Color) Animator.getManager().tool.get("color");
		add(start);
	}
	
	/**
	 * Draw this specified stroke to canvas.
	 * 
	 * @param g		Graphics object to use
	 */
	void paint(Graphics2D g2d) {
		// Fill in with custom code.
	}

	/**
	 * Draw this specified stroke to canvas, using a lower opacity.
	 * 
	 * @param g2d		Graphics2D object to use
	 * @param alpha		opacity (0.0-1.0)
	 */
	void paint(Graphics2D g2d, double alpha) {
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
		paint(g2d);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}
	
	/**
	 * Update the stroke with mouse coordinate information.
	 * 
	 * @param p		mouse coordinates
	 */
	void update(Point p) {
		add(p);
	}
	
	/**
	 * Finish the stroke, update with the last coordinate.
	 * 
	 * @param p		mouse coordinates
	 */
	void end(Point p) {
		add(p);
		end = p;
	}
}
