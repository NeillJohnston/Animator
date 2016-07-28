package animator;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 * An interface for various types of strokes - brush, pencil, etc.
 * 
 * @author Neill Johnston
 */
public abstract class Stroke {
	ArrayList<Point> strokePoints;
	Point start;
	Point end;
	
	/**
	 * Construct this Stroke object with specified initial coordinates.
	 */
	public Stroke(Point start) {
		this.start = start;
		strokePoints = new ArrayList<Point>();
		strokePoints.add(start);
	}
	
	/**
	 * Draw this specified stroke to canvas.
	 * 
	 * @param g		Graphics object to use
	 */
	void paint(Graphics g) {
		// Fill in with custom code.
	}
	
	/**
	 * Update the stroke with mouse coordinate information.
	 * 
	 * @param p		mouse coordinates
	 */
	void update(Point p) {
		this.strokePoints.add(p);
	}
	
	/**
	 * Finish the stroke, update with the last coordinate.
	 * 
	 * @param p		mouse coordinates
	 */
	void end(Point p) {
		strokePoints.add(p);
		end = p;
	}
}
