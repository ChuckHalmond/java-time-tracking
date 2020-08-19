package parameters;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import toolbox.Constants;
import toolbox.filters.ConfigFileFilter;

/**
 * The parameters of the application.
 *
 * @author Charles MECHERIKI
 *
 */
public class Parameters {
	
	private Properties config;														/**	Current configuration of the parameters 	*/
	
	private static final String directory = "config/";								/**	Directory where to store the config file(s)	*/
	private static final String filename = "parameters.config";						/** Name of the default config file				*/
	
	private static final String emulatorIPAddressFieldName = "emulatorIPAddress";	/**	Field name for the emulator IP address in the config	*/
	private static final String emulatorPortFieldName = "emulatorPort";				/**	Field name for the emulator port in the config			*/
	private static final String incidentThresholdFieldName = "incidentThresholed";	/**	Field name for the incident threshold in the config 	*/
	
	/**
	 * Constructor.
	 */
	public Parameters() {
		config = new Properties();
	}
	
	/**
	 * Saves the parameters in a default config file.
	 * 
	 * @throws IOException	if an error occurred on file writing
	 */
	public void saveParameters() throws IOException {
		exportParametersToFile(new File(directory + filename));
	}
	
	/**
	 * Exports the parameters to the given file.
	 * 
	 * @param _file	the file where to save the configuration
	 * @throws IOException	if an error occurred on file writing
	 */
	public void exportParametersToFile(File _file) throws IOException {
		FileOutputStream outputStream = null;
		
		if (!(new ConfigFileFilter().accept(_file))) {
			throw new IOException("The extension must be .config.");
		}

		try {
			outputStream = new FileOutputStream(_file);
			config.store(outputStream, "Custom configuration");
		}
		catch (IOException _exception) {
			throw new IOException("An error occured on parameters export.", _exception);
		}
		finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}
	
	/**
	 * Import the parameters from a given config file.
	 * 
	 * @param _file		the config file to import
	 * @throws IOException	if an error occurred on file reading
	 */
	public void importParametersFromFile(File _file) throws IOException {
		FileInputStream inputStream = null;
		
		if (!(new ConfigFileFilter().accept(_file))) {
			throw new IOException("The extension must be .config");
		}
		
		try {
			inputStream = new FileInputStream(_file);
			config.load(inputStream);
		}
		catch (IOException _exception) {
			throw new IOException("An error occured on parameters import.", _exception);
		}
		finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}
	
	/**
	 * Loads the parameters from the default config file.
	 * 
	 * @throws IOException	if an error occurred on file reading
	 */
	public void loadParameters() throws IOException {
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		
		try {
			File file = null;
			
			if (!(file = new File(directory)).exists()) {
				file.mkdir();
			}
			
			if ((file = new File(directory + filename)).exists()) { 
				inputStream = new FileInputStream(file);
				config.load(inputStream);
			}
			else {
				config.setProperty(emulatorIPAddressFieldName, Constants.DEFAULT_EMULATOR_SERVER_IP);
				config.setProperty(emulatorPortFieldName, String.valueOf(Constants.DEFAULT_EMULATOR_SERVER_PORT));
				config.setProperty(incidentThresholdFieldName, String.valueOf(Constants.DEFAULT_INCIDENT_THRESHOLD));
				
				outputStream = new FileOutputStream(directory + filename);
				config.store(outputStream, "Default configuration");
			}
		}
		catch (IOException _exception) {
			throw new IOException("An error occured on parameters loading.", _exception);
		}
		finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}
	
	/**
	 * Sets the emulator IP address used in the communication.
	 * 
	 * @param _IPAddress	the emulator IP address used in the communication
	 */
	public void setEmulatorIPAddress(String _IPAddress) {
		config.setProperty(emulatorIPAddressFieldName, _IPAddress);
	}
	
	/**
	 * Returns the emulator IP address used in the communication.
	 * 
	 * @return the emulator IP address used in the communication
	 */
	public String getEmulatorIPAddress() {
		return config.getProperty(emulatorIPAddressFieldName);
	}
	
	/**
	 * Sets the emulator port used in the communication.
	 * 
	 * @param _port	the emulator port used in the communication
	 */
	public void setEmulatorPort(int _port) {
		config.setProperty(emulatorPortFieldName, String.valueOf(_port));
	}
	
	/**
	 * Returns the emulator port used in the communication.
	 * 
	 * @return the emulator port used in the communication
	 */
	public int getEmulatorPort() {
		return Integer.parseInt(config.getProperty(emulatorPortFieldName));
	}

	/**
	 * Sets the incident threshold.
	 * 
	 * @param _threshold	the incident threshold
	 */
	public void setIncidentThreshold(int _threshold) {
		config.setProperty(incidentThresholdFieldName, String.valueOf(_threshold));
	}
	
	/**
	 * Returns the incident threshold.
	 * 
	 * @return the incident threshold
	 */
	public int getIncidentThreshold() {
		return Integer.parseInt(config.getProperty(incidentThresholdFieldName));
	}
}
