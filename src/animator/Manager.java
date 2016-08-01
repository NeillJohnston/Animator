package animator;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;

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
	// Preferences file location.
	private static Path prefsPath;
	
	// List of layers.
	public static ArrayList<Layer> layers;
	
	// Global actions.
	public static HashMap<String, AbstractAction> actions = new HashMap<String, AbstractAction>();
	public static final String ACTION_UNDO = "action_undo";
	public static final String ACTION_NEWFRAME = "action_newFrame";
	public static final String ACTION_NEXTFRAME = "action_nextFrame";
	public static final String ACTION_PREVFRAME = "action_prefFrame";
	public static final String ACTION_DUPLICATEFRAME = "action_duplicateFrame";
	public static final String ACTION_COMBINEFRAME = "action_combineFrame";
	public static final String ACTION_PLAY = "action_play";
	
	// Current frame properties.
	public static HashMap<String, Object> anim = new HashMap<String, Object>();
	public static final String ANIM_LAYER = "anim_layer";
	public static final String ANIM_CURRENT = "anim_current";
	public static final String ANIM_FPS = "anim_fps";
	public static final String ANIM_PLAYINGFLAG = "anim_playingFlag";
	
	// Tool properties.
	public static HashMap<String, Object> tool = new HashMap<String, Object>();
	
	// Preferences loaded from file.
	public static HashMap<String, String> pref = new HashMap<String, String>();
	
	// Hotkeys loaded from file.
	public static HashMap<String, KeyStroke> keys = new HashMap<String, KeyStroke>();
	public static final String HOTKEY_UNDO = "hotkey_undo";
	public static final String HOTKEY_NEWFRAME = "hotkey_newFrame";
	public static final String HOTKEY_NEXTFRAME = "hotkey_nextFrame";
	public static final String HOTKEY_PREVFRAME = "hotkey_prevFrame";
	public static final String HOTKEY_DUPLICATEFRAME = "hotkey_duplicateFrame";
	
	// Possible tools used by the cursor.
	public enum ToolType {
		EDIT,
		PEN,
		LINE,;
	}
	
	public static void init() {
		// Initialize global actions.
		actions.put(ACTION_UNDO, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 undo();
			}
		});
		actions.put(ACTION_NEWFRAME, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				while(Manager.getCurrentFrame() != null)
					anim.put(ANIM_CURRENT, (int) Manager.anim.get(ANIM_CURRENT) + 1);
				layers.get(0).put((int) Manager.anim.get(ANIM_CURRENT), new Frame());
				Animator.getGuiFramePanel().repaint();
				Animator.getGuiAnimatorCanvas().repaint();
			}
		});
		actions.put(ACTION_NEXTFRAME, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				anim.put(ANIM_CURRENT, (int) anim.get(ANIM_CURRENT) + 1);
				Animator.getGuiFramePanel().repaint();
				Animator.getGuiAnimatorCanvas().repaint();
			}
		});
		actions.put(ACTION_PREVFRAME, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((int) anim.get(ANIM_CURRENT) > 0)
					anim.put(ANIM_CURRENT, (int) anim.get(ANIM_CURRENT) - 1);
				Animator.getGuiFramePanel().repaint();
				Animator.getGuiAnimatorCanvas().repaint();
			}
		});
		actions.put(ACTION_DUPLICATEFRAME, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Frame original = getCurrentFrame();
				while(Manager.getCurrentFrame() != null)
					anim.put(ANIM_CURRENT, (int) Manager.anim.get(ANIM_CURRENT) + 1);
				layers.get(0).put((int) Manager.anim.get(ANIM_CURRENT), Frame.copy(original));
				Animator.getGuiFramePanel().repaint();
				Animator.getGuiAnimatorCanvas().repaint();
			}
		});
		actions.put(ACTION_COMBINEFRAME, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Frame current = getCurrentFrame();
				if(current != null) {
					PenStroke combined = new PenStroke(current.get(0).start);
					for(Stroke s : current) {
						for(Point p : s.points) {
							if(p == s.start) {
								combined.update(combined.new JoinPoint(p));
							} else {
								combined.update(p);
							}
						}
					}
					current.clear();
					current.add(combined);
					Animator.getGuiFramePanel().repaint();
					Animator.getGuiAnimatorCanvas().repaint();
				}
			}
		});
		actions.put(ACTION_PLAY, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(getCurrentFrame() == null) {
					Manager.anim.put(ANIM_CURRENT, 0);
				}
				Timer t = new Timer(1000 / (int) Manager.anim.get(Manager.ANIM_FPS), new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Timer t = (Timer) e.getSource();
						Manager.actions.get(Manager.ACTION_NEXTFRAME).actionPerformed(e);
						if(getCurrentFrame() == null) {
							t.stop();
							Manager.anim.put(ANIM_PLAYINGFLAG, false);
						}
					}
				});
				t.start();
				Manager.anim.put(ANIM_PLAYINGFLAG, true);
			}
		});
		
		// Initialize with a single blank layer.
		layers = new ArrayList<Layer>();
		layers.add(new Layer("untitled"));
		
		// Initialize current animation properties.
		anim.put(ANIM_LAYER, layers.get(0));
		anim.put(ANIM_CURRENT, 0);
		anim.put(ANIM_FPS, 12);
		anim.put(ANIM_PLAYINGFLAG, false);
		
		// Initialize tool defaults.
		tool.put("width", new Integer(10));
		tool.put("color", Color.black);
		tool.put("stroke", ToolType.PEN);
		
		// Initialize hotkey defaults.
		keys.put("undo", KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
		keys.put("newFrame", KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.CTRL_DOWN_MASK));
		keys.put("nextFrame", KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
		keys.put("prevFrame", KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
		
		// Get the preferences file and load user preferences.
		prefsPath = Paths.get("preferences.txt");
		refreshPreferences();
	}
	
	/**
	 * Refresh the preferences.
	 */
	public static void refreshPreferences() {
		// Try-with-resources to open the file.
		try(BufferedReader reader = Files.newBufferedReader(prefsPath)) {
			String line;
			while((line = reader.readLine()) != null) {
				String[] pair = line.split(":", 1);
				if(pair.length == 2)
					pref.put(pair[0], pair[1]);
			}
		}
		// TODO: better exception handling.
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Refresh the hotkey bindings.
	 */
	public static void refreshKeyBindings() {
		// Put the correct keys into the input map.
		JRootPane root = Animator.mainWindow.getRootPane();
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keys.get("undo"), HOTKEY_UNDO);
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keys.get("newFrame"), HOTKEY_NEWFRAME);
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keys.get("nextFrame"), HOTKEY_NEXTFRAME);
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keys.get("prevFrame"), HOTKEY_PREVFRAME);
		
		// Create the correct actions.
		root.getActionMap().put(HOTKEY_UNDO, actions.get(ACTION_UNDO));
		root.getActionMap().put(HOTKEY_NEWFRAME, actions.get(ACTION_NEWFRAME));
		root.getActionMap().put(HOTKEY_NEXTFRAME, actions.get(ACTION_NEXTFRAME));
		root.getActionMap().put(HOTKEY_PREVFRAME, actions.get(ACTION_PREVFRAME));
	}
	
	/**
	 * Undo the last stroke.
	 */
	public static void undo() {
		Frame current = getCurrentFrame();
		if(current.size() > 0)
			current.remove(current.size() - 1);
		Animator.getGuiAnimatorCanvas().repaint();
	}
	
	/**
	 * Create a new stroke from the current stroke type.
	 * 
	 * @param p		the initial point of the new stroke
	 */
	public static Stroke getNewCurrentStroke(Point p) {
		switch((ToolType) tool.get("stroke")) {
			case EDIT:
				return null;
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
		return ((Layer) anim.get(ANIM_LAYER)).get((Integer) anim.get(ANIM_CURRENT));
	}
}
