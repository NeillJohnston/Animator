package animator;

import java.awt.*;

/**
 * CanvasObject is an abstract class that handles objects being drawn to the canvas.
 *
 * @author Neill Johnston
 */
public abstract class CanvasObject {
    Point start;
    Point end;

    int strokeWidth;
    Color color;

    /**
     * Paint the object.
     *
     * @param g2d   Graphics2D object to use
     */
    public void paint(Graphics2D g2d) {
        // Empty method.
    }

    /**
     * Draw this object to canvas, using a lower opacity.
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
     * Initialize the object (and drag motion) with a start point.
     *
     * @param p     Point to start drag
     */
    public void init(Point p) {
        // Begin the initial drag to create the object.
        start = p;
        end = p;
        // Get tool settings.
        this.strokeWidth = (int) Manager.tool.get(Manager.TOOL_WIDTH);
        this.color = (Color) Manager.tool.get(Manager.TOOL_COLOR);

    }

    /**
     *
     */
    public void update(Point p) {
        end = p;
    }

    /**
     * Finish creating the object.
     */
    public void fin() {
        // Empty method.
    }
}
