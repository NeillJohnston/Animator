package animator;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JComponent;

/**
 * An interface for various types of strokes - brush, pencil, etc.
 * Extension of JComponent so that it can be transformed and viewed easily.
 * 
 * @author Neill Johnston
 */
public abstract class Stroke extends Rectangle {
	//
	protected Point start;
	protected Point end;
	protected ArrayList<Point> points;
	protected Rectangle dragHandle;
	
	// Stroke settings.
	protected int strokeWidth;
	protected Color color;
	
	/**
	 * Construct this Stroke object with specified initial coordinates.
	 */
	public Stroke(Point start) {
		super();
		
		this.start = start;
		this.points = new ArrayList<Point>();
		this.strokeWidth = (Integer) Manager.tool.get("width");
		this.color = (Color) Manager.tool.get("color");
		this.points.add(start);
		this.dragHandle = new Rectangle();
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
	 * Draw this stroke to canvas, using a lower opacity.
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
	 * Draw this stroke's editor box.
	 * 
	 * @param g2d Graphics2D object to use
	 */
	void paintEditor(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(2,
				BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
		g2d.setColor(Color.green);
		g2d.draw(this);
		g2d.setColor(Color.orange);
		g2d.draw(dragHandle);
	}
	
	/**
	 * Update the stroke in general.
	 */
	void update() {
		double minX = start.x; double maxX = start.x;
		double minY = start.y; double maxY = start.y;
		for(Point q : this.points) {
			if(q.x < minX)
				minX = q.x;
			else if(q.x > maxX)
				maxX = q.x;
			if(q.y < minY)
				minY = q.y;
			else if(q.y > maxY)
				maxY = q.y;
		}
		this.setBounds((int) minX, (int) minY, (int) (maxX - minX), (int) (maxY - minY));
		this.dragHandle.setBounds((int) this.getCenterX() - 10, (int) this.getCenterY() - 10, 20, 20);
	}
	
	/**
	 * Update the stroke with mouse coordinate information.
	 * 
	 * @param p		mouse coordinates
	 */
	void update(Point p) {
		update();
		points.add(p);
	}
	
	/**
	 * Finish the stroke, update with the last coordinate.
	 * Update the Rectangle bounds to fit the whole Stroke.
	 * 
	 * @param p		mouse coordinates
	 */
	void end(Point p) {
		update(p);
		end = p;
	}
	
	/**
	 * Use a MouseEvent to figure out how to edit this stroke.
	 * 
	 * @param e		MouseEvent information of what happened
	 */
	void edit(MouseEvent e) {
		if(this.dragHandle.contains(e.getPoint()))
			this.translate((int) (e.getX() - this.dragHandle.x - this.dragHandle.width / 2),
					(int) (e.getY() - this.dragHandle.y - this.dragHandle.height / 2));
	}
	
	/**
	 * Override translate to also translate all points and handles.
	 */
	@Override
	public void translate(int dx, int dy) {
		super.translate(dx, dy);
		for(Point p : this.points) {
			p.translate(dx, dy);
		}
		dragHandle.translate(dx, dy);
	}
}
