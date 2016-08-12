package animator;

import javax.sound.sampled.Line;
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
public class LineObject extends CanvasObject {
	/**
	 * Default constructor.
	 */
	public LineObject() {
		System.out.println("Creating new LineObject");
	}

	/**
	 * Construct a new LineObject from specified settings.
	 *
	 * @param start			Corresponds to member field
	 * @param end			"..."
	 * @param strokeWidth	"..."
	 * @param color			"..."
	 */
	public LineObject(Point start, Point end, int strokeWidth, Color color) {
		this.start = start;
		this.end = end;
		this.strokeWidth = strokeWidth;
		this.color = color;
	}

	/**
	 * Draw a line with the current width and color.
	 */
	@Override
	public void paint(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(strokeWidth,
				BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2d.setColor(Color.black);
		g2d.draw(new Line2D.Float(start.x, start.y, end.x, end.y));
	}

	/**
	 * Make a new command when the line is ended.
	 */
	@Override
	public void fin() {
		Manager.addCommand(new NewLineObjectCommand(this.start, this.end, this.strokeWidth, this.color, this.hashCode()));
	}

	// --- Commands ---

	class NewLineObjectCommand implements Command {
		private Point start;
		private Point end;
		private int strokeWidth;
		private Color color;
		private int coHash;

		NewLineObjectCommand(Point start, Point end, int strokeWidth, Color color, int coHash) {
			this.start = start;
			this.end = end;
			this.strokeWidth = strokeWidth;
			this.color = color;
			this.coHash = coHash;
		}

		public void undo() {

		}

		public void redo() {
			LineObject lo = new LineObject(this.start, this.end, this.strokeWidth, this.color);

		}
	}
}
