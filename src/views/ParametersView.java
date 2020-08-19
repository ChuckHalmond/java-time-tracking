package views;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import swing.PlaceholdedIPAddressField;
import swing.PlaceholdedPortField;
import swing.PlaceholdedUnsignedIntField;
import swing.panels.FlowPanel;
import swing.panels.GridPanel;

/**
 * The parameters view of the application.
 * This view is no more no less the 'Parameters' tab of the application.
 * Its role is exclusively to handle the writing/reading in/of the swing elements and listen for the user's actions.
 * 
 * Note : see the parameters controller for the list of the possible actions and their implementations.
 *
 * @author Charles MECHERIKI
 *
 */
public class ParametersView extends View {
	private static final long serialVersionUID = 1L;

	private JLabel emulatorIPAddressLabel;
	private PlaceholdedIPAddressField emulatorIPAddressField;
	private JLabel emulatorPortLabel;
	private PlaceholdedPortField emulatorPortField;
	private JLabel incidentThresholdLabel;
	private PlaceholdedUnsignedIntField incidentThresholdUnsignedIntField;
	
	private JButton saveParametersButton;
	private JButton resetToDefaultButton;
	private JButton importFromFileButton;
	private JButton exportToFileButton;
	
	public ParametersView() {
		super("Parameters");
		build();
	}
	
	public void build() {
		// Panels
		GridPanel textfieldsPanel = new GridPanel(3, 2, GridPanel.defaultPreferedComponentsSize, GridPanel.defaultBorder);
		FlowPanel saveResetButtonsPanel = new FlowPanel(FlowPanel.defaultPreferedComponentsSize, FlowPanel.defaultBorder);
		FlowPanel importExportButtonsPanel = new FlowPanel(FlowPanel.defaultPreferedComponentsSize, FlowPanel.defaultBorder);
		
		// Labels
		emulatorIPAddressLabel = new JLabel("Emulator's IP address");
		emulatorPortLabel = new JLabel("Emulator's port");
		incidentThresholdLabel = new JLabel("Incident threshold");
		
		// Fields
		emulatorIPAddressField = new PlaceholdedIPAddressField("IP address");
		emulatorPortField = new PlaceholdedPortField("port");
		incidentThresholdUnsignedIntField = new PlaceholdedUnsignedIntField("min. deficit hours");
		
		// Buttons
		saveParametersButton = new JButton("Save parameters");
		resetToDefaultButton = new JButton("Reset fields");
		importFromFileButton = new JButton("Import from file..");
		exportToFileButton = new JButton("Export to file..");
		
		textfieldsPanel.add(emulatorIPAddressLabel);
		textfieldsPanel.add(emulatorIPAddressField);
		textfieldsPanel.add(emulatorPortLabel);
		textfieldsPanel.add(emulatorPortField);
		textfieldsPanel.add(incidentThresholdLabel);
		textfieldsPanel.add(incidentThresholdUnsignedIntField);

		saveResetButtonsPanel.add(saveParametersButton);
		saveResetButtonsPanel.add(resetToDefaultButton);

		importExportButtonsPanel.add(importFromFileButton);
		importExportButtonsPanel.add(exportToFileButton);
		
		add(textfieldsPanel);
		add(saveResetButtonsPanel);
		add(importExportButtonsPanel);
	}
	
	//==================//
	//		readers		//
	//==================//
	
	public String readEmulatorIPAddress() {
		return emulatorIPAddressField.getIPAddress();
	}
	
	public int readEmulatorPort() {
		return emulatorPortField.getPort();
	}
	
	public int readIncidentThreshold() {
		return incidentThresholdUnsignedIntField.getValue();
	}
	
	//==================//
	//		writers		//
	//==================//
	
	public void writeEmulatorIPAddress(String _IPAddress) {
		emulatorIPAddressField.setText(_IPAddress);
	}
	
	public void writeEmulatorPort(int _port) {
		emulatorPortField.setValue(_port);
	}
	
	public void writeIncidentThreshold(int _threshold) {
		incidentThresholdUnsignedIntField.setValue(_threshold);
	}
	
	//======================//
	//		listeners		//
	//======================//
	
	public void addSaveParametersButtonListener(ActionListener _actionListener) {
		saveParametersButton.addActionListener(_actionListener);
	}
	
	public void addResetToDefaultButtonListener(ActionListener _actionListener) {
		resetToDefaultButton.addActionListener(_actionListener);
	}
	
	public void addImportFromFileButtonListener(ActionListener _actionListener) {
		importFromFileButton.addActionListener(_actionListener);
	}
	
	public void addExportToFileButtonListener(ActionListener _actionListener) {
		exportToFileButton.addActionListener(_actionListener);
	}
}
