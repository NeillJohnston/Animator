package animator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Displays keyframes, options for drag-and-drop and other stuff.
 * 
 * @author Neill Johnston
 */
public class FramePanel extends JPanel {
	private TimelineComponent timeline;
	
	public FramePanel() {
		super();
		
		setPreferredSize(new Dimension(200, 200));
		setLayout(new BorderLayout());
		
		// Create the frame settings/management panel.
		JPanel frameSettings = new JPanel();
		frameSettings.setPreferredSize(new Dimension(200, 200));
		frameSettings.setBorder(BorderFactory.createTitledBorder("Frame Management"));
		
			// Create and add the new frame button.
			JButton frameSettingsNew = new JButton("New Frame");
			frameSettingsNew.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					while(Manager.getCurrentFrame() != null)
						Manager.anim.put("current", (int) Manager.anim.get("current") + 1);
					Manager.layers.get(0).put((int) Manager.anim.get("current"), new Frame());
					repaint();
					Animator.getGuiAnimatorCanvas().repaint();
				}
			});
			frameSettings.add(frameSettingsNew);
		
		// Create the layer/frame view panel.
		JPanel frameView = new JPanel();
		frameView.setLayout(new BoxLayout(frameView, BoxLayout.Y_AXIS));
		JScrollPane frameViewWrapper = new JScrollPane(frameView);
		frameViewWrapper.setPreferredSize(new Dimension(200, 200));
		frameViewWrapper.setBorder(BorderFactory.createTitledBorder("Layer/Frame View"));
		frameViewWrapper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		frameViewWrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
			// Create and add the timeline.
			timeline = new TimelineComponent();
			frameView.add(timeline);
			frameView.add(Box.createVerticalGlue());
		
		add(frameSettings, BorderLayout.WEST);
		add(frameViewWrapper, BorderLayout.CENTER);
	}
	
	/**
	 * Paint this and all its components.
	 * 
	 * @param g		Graphics object to use
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		paintComponents(g);
	}
	
	/**
	 * A GUI representation of the timeline.
	 * When clicked, it switches the current frame.
	 */
	private class TimelineComponent extends JComponent {
		// The width (in pixels) of a frame in the timeline GUI.
		private static final int FRAME_WIDTH = 15;
		
		/**
		 * Default constructor, initialize properties and add listener.
		 */
		TimelineComponent() {
			super();

			setPreferredSize(new Dimension(2000, 20));
			setBorder(BorderFactory.createLineBorder(Color.black, 1));
			
			// Add a mouse listener to change the current frame in timeline.
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					Manager.anim.put("current", (int) e.getX() / FRAME_WIDTH);
					Animator.getGuiAnimatorCanvas().repaint();
					repaint();
				}
			});
		}
		
		/**
		 * Paint a filled rectangle where the current frame is, and lines to indicate frames.
		 * 
		 * @param g		Graphics object to use
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			g.setColor(Color.lightGray);
			g.fillRect((int) Manager.anim.get("current") * FRAME_WIDTH, 0, FRAME_WIDTH, getHeight());
			g.setColor(Color.black);
			for(int x = 0; x < getWidth(); x += FRAME_WIDTH)
				g.drawLine(x, 0, x, getHeight());
		}
	}
}
