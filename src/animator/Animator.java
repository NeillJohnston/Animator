package animator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

import java.awt.event.KeyEvent;

/**
 * Animator, the entry class that contains the main method.
 * Initializes main window and starts program.
 * 
 * @author Neill Johnston
 */
public class Animator {
	public static JFrame mainWindow;
	
	/**
	 * Main, entry point into the application.
	 * 
	 * @param args	argument list from console
	 */
	public static void main(String[] args) {
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
			public void run() {
				mainWindow = new JFrame("Animator");
				mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mainWindow.setLayout(new BorderLayout());
				
				// Create and add the menubar.
				JMenuBar menuBar = new JMenuBar();
				menuBar.setOpaque(true);
				menuBar.setPreferredSize(new Dimension(mainWindow.getWidth(), 20));
				
				JMenu fileMenu = new JMenu("File");
				fileMenu.add(new JMenuItem("New", KeyEvent.VK_0));
				menuBar.add(fileMenu);
				
				mainWindow.setJMenuBar(menuBar);
				
				// Create and add the canvas.
				AnimatorCanvas animatorCanvas = new AnimatorCanvas();
				mainWindow.add(animatorCanvas, BorderLayout.CENTER);
				
				// Pack and display the window.
				mainWindow.pack();
				mainWindow.setVisible(true);
			}
		});
	}
}
