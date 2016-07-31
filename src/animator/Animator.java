package animator;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Animator, the entry class that contains the main method.
 * Initializes main window and starts program.
 * 
 * @author Neill Johnston
 */
public class Animator {
	public static JFrame mainWindow;
	
	private static AnimatorCanvas animatorCanvas;
	private static FramePanel framePanel;
	
	/**
	 * Main, entry point into the application.
	 * 
	 * @param args	argument list from console
	 */
	public static void main(String[] args) {
		// Get a new Manager.
		newManager();
		
		// Set up the native look and feel.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Schedule the main window creation.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				mainWindow = new JFrame("Animator");
				mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mainWindow.setLayout(new BorderLayout());
				
				// Create and add the menu bar.
				JMenuBar menuBar = new JMenuBar();
				menuBar.setOpaque(true);
				menuBar.setPreferredSize(new Dimension(mainWindow.getWidth(), 20));
				
				JMenu fileMenu = new JMenu("File");
				fileMenu.add(new JMenuItem("New", KeyEvent.VK_0));
				menuBar.add(fileMenu);
				
				JMenu editMenu = new JMenu("Edit");
				JMenuItem editMenuUndo = new JMenuItem("Undo");
				editMenuUndo.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Manager.undo();
						animatorCanvas.repaint();
						System.out.println("undo");
					}
				});
				editMenu.add(editMenuUndo);
				menuBar.add(editMenu);
				
				mainWindow.setJMenuBar(menuBar);
				
				// Create and add the canvas.
				animatorCanvas = new AnimatorCanvas();
				mainWindow.add(animatorCanvas, BorderLayout.CENTER);
				
				// Create and add the tool panel.
				ToolPanel toolPanel = new ToolPanel();
				mainWindow.add(toolPanel, BorderLayout.WEST);
				
				// Create and add the frame panel.
				framePanel = new FramePanel();
				mainWindow.add(framePanel, BorderLayout.SOUTH);
				
				// Pack and display the window.
				mainWindow.pack();
				mainWindow.setSize(new Dimension(1280, 720));
				mainWindow.setVisible(true);
				mainWindow.setFocusable(true);
				
				Manager.refreshKeyBindings();
			}
		});
	}
	
	/**
	 * Return the animator canvas from GUI.
	 * 
	 * @return	animatorCanvas
	 */
	public static AnimatorCanvas getGuiAnimatorCanvas() {
		return animatorCanvas;
	}
	
	/**
	 * Return the frame panel from GUI.
	 * 
	 * @return	framePanel
	 */
	public static FramePanel getGuiFramePanel() {
		return framePanel;
	}
	
	/**
	 * Create a new Manager to represent a new file/animation.
	 */
	public static void newManager() {
		// Load the Manager object.
		new Manager();
	}
}
