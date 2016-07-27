package animator;

import java.awt.Dimension;

import javax.swing.*;

import java.awt.event.KeyEvent;

public class Animator {
	public static JFrame mainWindow;
	
	/**
	 * Main.
	 * @param args 
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
				mainWindow.setPreferredSize(new Dimension(640, 480));
				
				JMenuBar menuBar = new JMenuBar();
				menuBar.setOpaque(true);
				menuBar.setPreferredSize(new Dimension(mainWindow.getWidth(), 25));
				
				JMenu fileMenu = new JMenu("File");
				fileMenu.add(new JMenuItem("New", KeyEvent.VK_0));
				menuBar.add(fileMenu);
				
				mainWindow.setJMenuBar(menuBar);
				
				mainWindow.pack();
				mainWindow.setVisible(true);
			}
		});
	}
}
