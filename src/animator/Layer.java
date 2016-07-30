package animator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Layer is the basic form of an animation layer.
 * Holds a bunch of frames in sequence (ArrayList frames).
 * It is an ArrayList of Frame objects.
 * 
 * @author Neill Johnston
 */
public class Layer extends HashMap<Integer, Frame> {
	private String name;
	
	public Layer(String name) {
		this.name = name;
		put(0, new Frame());
	}
	
	public String getName() {
		return name;
	}
	
	private class LayerComponent extends JComponent {
		private Layer parent;

		LayerComponent(Layer parent) {
			super();
			
			this.parent = parent;
			
			setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
			setBorder(BorderFactory.createLineBorder(Color.gray));
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		}

		/**
		 * Paint this and all its components.
		 * 
		 * @param g		Graphics object to use
		 */
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			paintComponents(g);
		}
	}
}
