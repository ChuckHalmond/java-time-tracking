package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;

import tables.cell_renderers.OvertimeCellRendererManager;
import communication.Transmitter;
import communication.TCP.TCPParameters;
import toolbox.Constants;
import toolbox.exceptions.InputException;
import toolbox.filters.ConfigFileFilter;
import parameters.Parameters;
import views.ParametersView;

/**
 * The checkings controller, handling all the departments actions from the HMI.
 * This controller allows to :
 *  	- Change the parameters of the application (emulator's server TCP parameters, incident threshold);
 * 		- Import/export parameters from/to a config file.
 *
 * @author Charles MECHERIKI
 *
 */
public class ParametersController extends Controller {
	private ParametersView view;
	private Parameters parameters;
	private Transmitter<TCPParameters> parametersTransmitter;
	
	public ParametersController(Parameters _parameters, ParametersView _view) {
		parameters = _parameters;
		view = _view;
		
		parametersTransmitter = null;
		
		buildView();
		addListeners();
	}
	
	public void buildView() {
		view.writeEmulatorIPAddress(parameters.getEmulatorIPAddress());
		view.writeEmulatorPort(parameters.getEmulatorPort());
		view.writeIncidentThreshold(parameters.getIncidentThreshold());
	}
	
	public void addListeners() {
		view.addSaveParametersButtonListener(new SaveParametersButtonListener());
		view.addResetToDefaultButtonListener(new ResetToDefaultButtonListener());
		view.addImportFromFileButtonListener(new ImportFromFileButtonListener());
		view.addExportToFileButtonListener(new ExportToFileButtonListener());
	}
	
	public void setSavedParametersTransmitter(Transmitter<TCPParameters> _parametersTransmitter) {
		parametersTransmitter = _parametersTransmitter;
	}

	private class SaveParametersButtonListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent _event) {
			try {
				String emulatorIPAddress = view.readEmulatorIPAddress();
				int emulatorPort = view.readEmulatorPort();
				int incidentThreshold = view.readIncidentThreshold();
				
				if (emulatorIPAddress.equals("")) {
					throw new InputException("The given IP address is not valid for communication.");
				}
				else if (emulatorPort < 0 || emulatorPort == Constants.APPLICATION_SERVER_PORT) {
					throw new InputException("The given port is not allowed for communication (" + Constants.APPLICATION_SERVER_PORT + " is reserved).");
				}
				else if (incidentThreshold < 0) {
					throw new InputException("The incident threshold must be a positive number.");
				}

				parameters.setIncidentThreshold(incidentThreshold);
				OvertimeCellRendererManager.updateIncidentThreshold(incidentThreshold);
				parameters.saveParameters();
				
				if (!emulatorIPAddress.equals(parameters.getEmulatorIPAddress()) || emulatorPort != parameters.getEmulatorPort()) {
					if (parametersTransmitter != null) {
						parametersTransmitter.transmit(new TCPParameters(emulatorIPAddress, emulatorPort));
					}
				}
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}
	
	public void setTCPParameters(TCPParameters _TCPParameters) throws Exception {
		parameters.setEmulatorIPAddress(_TCPParameters.getIPAddress());
		parameters.setEmulatorPort(_TCPParameters.getPort());
		parameters.saveParameters();
	}

	private class ResetToDefaultButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent _event) {
			try {
				parameters.loadParameters();
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}

			view.writeEmulatorIPAddress(parameters.getEmulatorIPAddress());
			view.writeEmulatorPort(parameters.getEmulatorPort());
			view.writeIncidentThreshold(parameters.getIncidentThreshold());
		}

	}
	
	private class ImportFromFileButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent _event) {
			try {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new ConfigFileFilter());
				fileChooser.setCurrentDirectory(new File("config"));

				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					parameters.importParametersFromFile(fileChooser.getSelectedFile());
				}
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}

			view.writeEmulatorIPAddress(parameters.getEmulatorIPAddress());
			view.writeEmulatorPort(parameters.getEmulatorPort());
			view.writeIncidentThreshold(parameters.getIncidentThreshold());
		}

	}
	
	private class ExportToFileButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent _event) {
			parameters.setEmulatorIPAddress(view.readEmulatorIPAddress());
			parameters.setEmulatorPort(view.readEmulatorPort());
			parameters.setIncidentThreshold(view.readIncidentThreshold());
			
			try {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new ConfigFileFilter());
				fileChooser.setCurrentDirectory(new File("config"));

				if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					parameters.exportParametersToFile(fileChooser.getSelectedFile());
				}
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}
}
