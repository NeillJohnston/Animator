package animator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;

/**
 * A simple single line, two points worth of information.
 * 
 * @author Neill Johnston
 */
public class LineStroke extends Stroke {
	public LineStroke(Point start) {
		super(start);
		end = start;
	}
	
	/**
	 * Override update to just reset the end.
	 */
	@Override
	public void update(Point p) {
		end = p;
	}
	
	/**
	 * Draw a line with the current width and color.
	 */
	@Override
	public void paint(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(width,
				BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2d.setColor(Color.black);
		g2d.draw(new Line2D.Float(start.x, start.y, end.x, end.y));
	}
}
