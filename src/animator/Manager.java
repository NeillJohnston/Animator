package animator;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manager is the class that holds all of the important project data.
 * It includes variables that store
 * - tool options,
 * - the current layers and frames,
 * - preferences,
 * etc. Also loads preferences from file.
 * Many variables here are public so that getter methods are not needed.
 * 
 * @author Neill Johnston
 */
public class Manager {
	// List of layers.
	public static ArrayList<Layer> layers;
	
	// Current frame properties.
	public static HashMap<String, Object> anim = new HashMap<String, Object>();
	
	// Tool properties.
	public static HashMap<String, Object> tool = new HashMap<String, Object>();
	
	// Possible tools used by the cursor.
	public enum ToolType {
		PEN,
		LINE,;
	}
	
	public Manager() {
		// Initialize with a single blank layer.
		layers = new ArrayList<Layer>();
		layers.add(new Layer("untitled"));
		
		// Initialize current layer.
		anim.put("layer", layers.get(0));
		anim.put("current", 0);
		anim.put("fps", 12);
		
		// Initialize tool defaults.
		tool.put("width", new Integer(10));
		tool.put("color", Color.black);
		tool.put("stroke", ToolType.PEN);
	}
	
	/**
	 * Undo the last stroke.
	 */
	public static void undo() {
		Frame current = getCurrentFrame();
		if(current.size() > 0)
			current.remove(current.size() - 1);
	}
	
	/**
	 * Create a new stroke from the current stroke type.
	 * 
	 * @param p		the initial point of the new stroke
	 */
	public static Stroke getNewCurrentStroke(Point p) {
		switch((ToolType) tool.get("stroke")) {
			case PEN:
				return new PenStroke(p);
			case LINE:
				return new LineStroke(p);
		}
		return null;
	}
	
	/**
	 * Return the current frame.
	 * 
	 * @return the frame of the current layer at the current time
	 */
	public static Frame getCurrentFrame() {
		return ((Layer) anim.get("layer")).get((Integer) anim.get("current"));
	}
}
