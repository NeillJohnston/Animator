package animator;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
	protected ArrayList<Handle> handles;
	protected DragHandle dragHandle;
	protected ScaleHandle scaleHandleTL;
	protected ScaleHandle scaleHandleTR;
	protected ScaleHandle scaleHandleBL;
	protected ScaleHandle scaleHandleBR;
	protected Handle selectedHandle;
	
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
		this.strokeWidth = (Integer) Manager.tool.get(Manager.TOOL_WIDTH);
		this.color = (Color) Manager.tool.get(Manager.TOOL_COLOR);
		this.points.add(start);
		this.handles = new ArrayList<Handle>();
		this.dragHandle = new DragHandle(this, 20, Handle.POS_CENTER);
		this.scaleHandleTL = new ScaleHandle(this, 10, Handle.POS_TOPLEFT);
		this.scaleHandleTR = new ScaleHandle(this, 10, Handle.POS_TOPRIGHT);
		this.scaleHandleBL = new ScaleHandle(this, 10, Handle.POS_BOTTOMLEFT);
		this.scaleHandleBR = new ScaleHandle(this, 10, Handle.POS_BOTTOMRIGHT);
		handles.add(dragHandle);
		handles.add(scaleHandleTL);
		handles.add(scaleHandleTR);
		handles.add(scaleHandleBL);
		handles.add(scaleHandleBR);
	}
	
	/**
	 * Draw this specified stroke to canvas.
	 * 
	 * @param g2d	Graphics object to use
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
				BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
		g2d.setColor(Color.green);
		g2d.draw(this);
		for(Handle h : this.handles)
			h.paint(g2d);
	}
	
	/**
	 * Update the stroke in general.
	 */
	void update() {
		double minX = start.x; double maxX = start.x;
		double minY = start.y; double maxY = start.y;
		for(Point q : this.points) {
			minX = Math.min(q.x, minX);
			maxX = Math.max(q.x, maxX);
			minY = Math.min(q.y, minY);
			maxY = Math.max(q.y, maxY);
		}
		this.setBounds(
				(int) minX - strokeWidth / 2,
				(int) minY - strokeWidth / 2,
				(int) (maxX - minX) + strokeWidth,
				(int) (maxY - minY) + strokeWidth);
		
	}
	
	/**
	 * Update the stroke with mouse coordinate information.
	 * 
	 * @param p		mouse coordinates
	 */
	void update(Point p) {
		update();
		for(Handle h : this.handles)
			h.update();
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
	 * TODO: currently this just translates.
	 * 
	 * @param p		Point to translate to.
	 */
	void edit(Point p) {
		if(this.dragHandle.contains(p))
			this.translate((int) (p.getX() - this.dragHandle.x - this.dragHandle.width / 2),
					(int) (p.getY() - this.dragHandle.y - this.dragHandle.height / 2));
		this.update();
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
	
	// --- Drag tracking methods. ---
	
	public void startDrag(Point p) {
		for(Handle h : this.handles) {
			if(h.contains(p)) {
				this.selectedHandle = h;
			}
		}
	}
	
	public void drag(Point p) {
		if(this.selectedHandle != null)
			this.selectedHandle.drag(p);
	}
	
	public void endDrag(Point p) {
		this.selectedHandle = null;
	}
	
	/**
	 * Handle is an abstract helper class that makes it easy to transform strokes.
	 */
	abstract class Handle extends Rectangle {
		// Different positions.
		protected static final int POS_TOPLEFT = 1;
		protected static final int POS_TOPRIGHT = 2;
		protected static final int POS_BOTTOMLEFT = 3;
		protected static final int POS_BOTTOMRIGHT = 4;
		protected static final int POS_CENTER= 5;
		
		// Stroke that owns this handle.
		protected Stroke parent;
		// Color of the handle.
		protected Color color;
		// Side length of the square handle.
		protected int side;
		// Position of the handle.
		protected int pos;
		
		Handle(Stroke parent, int side, int pos) {
			this.parent = parent;
			this.side = side;
			this.pos = pos;
			this.color = Color.black;
		}
		
		/**
		 * Update to set proper bounds.
		 */
		public void update() {
			switch(pos) {
				case POS_TOPLEFT:
					this.setBounds(parent.x, parent.y, side, side);
					break;
				case POS_TOPRIGHT:
					this.setBounds(parent.x + parent.width - side, parent.y, side, side);
					break;
				case POS_BOTTOMLEFT:
					this.setBounds(parent.x, parent.y + parent.height - side, side, side);
					break;
				case POS_BOTTOMRIGHT:
					this.setBounds(parent.x + parent.width - side, parent.y + parent.height - side, side, side);
					break;
				case POS_CENTER:
					this.setBounds((int) (parent.getCenterX() - side / 2), (int) (parent.getCenterY() - side / 2), side, side);
					break;
			}
		}
		
		/**
		 * Paint this handle.
		 * 
		 * @param g2d		Graphics2D object to use
		 */
		public void paint(Graphics2D g2d) {
			g2d.setColor(color);
			g2d.draw(this);
		}
		
		// --- Drag tracking methods. ---
		
		/**
		 * Start tracking a drag.
		 */
		
		/**
		 * Use the handle to edit the stroke.
		 * 
		 * @param p		New point
		 */
		public void drag(Point p) {
			Point start = this.getLocation();
			int dx = p.x - start.x;
			int dy = p.y - start.y;
			this.setBounds(this.x + dx, this.y + dy, this.side, this.side);
		}
	}
	
	/**
	 * 
	 */
	class DragHandle extends Handle {
		DragHandle(Stroke parent, int side, int pos) {
			super(parent, side, pos);
			this.color = Color.blue;
		}
		
		/**
		 * Move the parent stroke with the handle.
		 */
		@Override
		public void drag(Point p) {
			super.drag(p);
		}
		
	}
	
	/**
	 * 
	 */
	class ScaleHandle extends Handle {
		ScaleHandle(Stroke parent, int side, int pos) {
			super(parent, side, pos);
			this.color = Color.red;
		}
		
	}
}
