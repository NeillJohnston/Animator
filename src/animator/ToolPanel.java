package animator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import animator.Manager.ToolType;

/**
 * ToolPanel is a box that lets the user select between tools easily.
 * User can also change tool settings from here.
 * 
 * @author Neill Johnston
 */
public class ToolPanel extends JPanel {
	private JPanel toolButtons;
	private JPanel toolSettings;
	
	/**
	 * Construct the tool panel.
	 * Initialize the panel with two panels: toolButtons and and toolSettings.
	 * ToolButtons can select the stroke used by the cursor.
	 * ToolSettings can change the options of the currently selected tool.
	 */
	public ToolPanel() {
		super();
		setPreferredSize(new Dimension(150, 150));
		setLayout(new BorderLayout());
		
		// Create and add the buttons grid.
		toolButtons = new JPanel();
		toolButtons.setPreferredSize(new Dimension(200, 200));
		toolButtons.setBorder(BorderFactory.createTitledBorder("Toolbox"));
		toolButtons.setLayout(new GridLayout(5, 2, 5, 5));
		
			// Create and add the pen tool button.
			JButton toolButtonsPen = new JButton("Pen");
			toolButtonsPen.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Manager.tool.put("stroke", Manager.ToolType.PEN);
				}
			});
			toolButtons.add(toolButtonsPen);
	
			// Create and add the line tool button.
			JButton toolButtonsLine = new JButton("Line");
			toolButtonsLine.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Manager.tool.put("stroke", Manager.ToolType.LINE);
				}
			});
			toolButtons.add(toolButtonsLine);
		
		// Create and add the settings panel.
		toolSettings = new JPanel();
		toolSettings.setBorder(BorderFactory.createTitledBorder("Tool settings"));
		
			// Create and add the tool width slider.
			JSlider toolSettingsWidth = new JSlider(JSlider.VERTICAL, 0, 100, 10);
			toolSettingsWidth.setMaximumSize(new Dimension(100, 100));;
			toolSettingsWidth.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					Manager.tool.put("width", ((JSlider) e.getSource()).getValue());
				}
			});
			toolSettingsWidth.setMajorTickSpacing(50);
			toolSettingsWidth.setMinorTickSpacing(10);
			toolSettingsWidth.setPaintLabels(true);
			toolSettingsWidth.setPaintTicks(true);
			toolSettings.add(new JLabel("Width"));
			toolSettings.add(toolSettingsWidth);
		
		add(toolButtons, BorderLayout.NORTH);
		add(toolSettings, BorderLayout.CENTER);
	}
}
