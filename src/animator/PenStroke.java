package animator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;

/**
 * PenStroke is an extension of Stroke.
 * This type of Stroke forms a stroke by connecting multiple lines.
 * 
 * @author Neill Johnston
 */
public class PenStroke extends Stroke {
	Color DEFAULT_COLOR = Color.black;

	public PenStroke(Point start) {
		super(start);
	}

	/**
	 * Draw a pen stroke as a series of lines.
	 * 
	 * @param g		Graphics object to use
	 */
	@Override
	public void paint(Graphics2D g2d) {
		Point last = start;
		g2d.setStroke(new BasicStroke(strokeWidth,
				BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2d.setColor(DEFAULT_COLOR);
		for(Point p : this.points) {
			g2d.draw(new Line2D.Float(last.x, last.y, p.x, p.y));
			last = p;
		}
	}

	/**
	 * Transform the Stroke according to a transformation.
	 * TODO: figure out how to better manage transformation types.
	 * 
	 * @param dx	change in x
	 * @param dy	change in y
	 */
	@Override
	public void transform(double dx, double dy) {
	}
}
