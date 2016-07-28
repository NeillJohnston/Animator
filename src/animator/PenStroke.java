package animator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;

public class PenStroke extends Stroke {
	int DEFAULT_WIDTH = 25;
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
	public void paint(Graphics g) {
		Point last = start;
		// Cast Graphics g as a Graphics2D object to increase control.
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(DEFAULT_WIDTH,
				BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2d.setColor(DEFAULT_COLOR);
		for(Point p : this.strokePoints) {
			g2d.draw(new Line2D.Float(last.x, last.y, p.x, p.y));
			last = p;
		}
	}
}
