package animator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

/**
 * Frame is the basic form of an animation frame.
 * Includes whatever strokes exist to display on the canvas.
 * It is an ArrayList of Stroke objects.
 * 
 * @author Neill Johnston
 */
public class Frame extends ArrayList<Stroke> {
	public Frame() {
	}
	
	/**
	 * Deep-copy an original frame.
	 * TODO: un-break this.
	 * TODO: because it really doesn't work now.
	 * TODO: at all.
	 * 
	 * @param original	frame to be copied
	 * @return a deep copy of Frame original
	 */
	public static Frame copy(Frame original) {
		// TODO: un-break this.
		Frame clone = (Frame) original.clone();
		return clone;
	}

	private class FrameComponent extends JComponent {
		private Frame parent;
		
		FrameComponent(Frame parent) {
			super();
			
			this.parent = parent;
			
			setMaximumSize(new Dimension(15, Integer.MAX_VALUE));
			setBorder(BorderFactory.createLineBorder(Color.black));
		}
	}
}
