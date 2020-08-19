import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import communication.Interpreter;
import communication.Protocol;
import communication.Synchronizer;
import communication.Transmitter;
import communication.TCP.TCPConnectionObserver;
import communication.TCP.TCPParameters;
import company.LightweightEmployee;
import controllers.EmulatorController;
import swing.windows.Window;
import time.CheckingRequest;
import toolbox.observers.ExceptionHandler;
import toolbox.Constants;
import toolbox.Serializor;
import views.EmulatorView;

/**
 * The emulator is the device which physically receives the staff checkings.
 *
 * The different communications with the application are described through its transmitters and interpreters, 
 * but all the technical stuff is handled internally by the synchronizer.
 * 
 * It's also there that the cache is loaded and saved.
 * 
 * @author Charles MECHERIKI
 *
 */
public class Emulator {
	private ArrayList<LightweightEmployee> staff;	/**	The staff used in the emulator		*/ 

	private Window window;							/**	The window where all is displayed	*/

	private EmulatorView view;						/**	The emulator only view			*/ 
	private EmulatorController controller;			/**	The emulator only controller	*/ 

	private Synchronizer synchronizer;				/**	Synchronizer, handling the communication with the application */
	private TCPParameters serverTCPParameters;		/**	TCP parameters of the synchronizer's server */
	private TCPParameters clientTCPParameters;		/** TCP parameters of the synchronizer's client */

	private ArrayList<CheckingRequest> checkingRequestsCache;	/**	Checkings request cache	*/ 
	
	private final String windowTitle = "TimeTrackingEmulator";							/**	Title of the emulator window		*/
	private final Dimension windowDimension = new Dimension(580, 300);					/**	Dimension of the emulator window	*/
	
	private final String TCPServerParametersCacheFilename = "TCPServerParameters.ser";	/**	TCP server parameters cache filename */ 
	private final String staffCacheFilename = "Staff.ser"; 								/**	Staff cache filename				*/ 
	private final String checkingRequestsCacheFilename = "CheckingRequests.ser";		/**	Checking requests filename			*/ 

	/**
	 * Emulator constructor which :
	 * 		1) Loads the cache;
	 * 		2) Creates the HMI;
	 * 		3) Creates and launches the synchronizer.
	 */
	public Emulator() {
		try {
			loadCache();
			
			createHMI();
			createSynchronizer();
			
			launchSynchronizer();
		}
		catch (Exception _exception) {
			fatalError(new Exception("An error occured on intialization", _exception));
		}
	}
	
	//==============//
	//		cache 	//
	//==============//
	
	/**
	 * Loads the last state of the emulator from cache files (called at launch) using the serializor.
	 */
	@SuppressWarnings("unchecked") // Removes the warnings when casting the deserialized files.
	public void loadCache() {
		try {
			staff = (ArrayList<LightweightEmployee>)Serializor.deserializeFromFile(staffCacheFilename);
			serverTCPParameters = (TCPParameters)Serializor.deserializeFromFile(TCPServerParametersCacheFilename);
			checkingRequestsCache = (ArrayList<CheckingRequest>)Serializor.deserializeFromFile(checkingRequestsCacheFilename);
		}
		catch (Exception _exception) {
			fatalError(_exception);
		}
		
		if (staff == null) {
			staff = new ArrayList<LightweightEmployee>();
		}
		if (serverTCPParameters == null) {
			serverTCPParameters = new TCPParameters(Constants.DEFAULT_EMULATOR_SERVER_IP, Constants.DEFAULT_EMULATOR_SERVER_PORT);
		}
		if (checkingRequestsCache == null) {
			checkingRequestsCache = new ArrayList<CheckingRequest>();
		}
	}
	
	/**
	 * Saves the current state of the emulator in cache files (called at closing) using the serializor.
	 */
	public void saveCache() {
		try {
			Serializor.serializeToFile(TCPServerParametersCacheFilename, serverTCPParameters);
			Serializor.serializeToFile(staffCacheFilename, controller.getStaff());
			Serializor.serializeToFile(checkingRequestsCacheFilename, checkingRequestsCache);
		}
		catch (Exception _exception) {
			fatalError(_exception);
		}
	}
	
	//==============//
	//		HMI		//
	//==============//
	
	/**
	 * Creates the HMI :
	 * 		1) Creates the view;
	 * 		2) Creates the controller;
	 *  	3) Sets the transmitters;
	 *  	4) Creates the window.
	 */
	public void createHMI() {
		controller = new EmulatorController(staff, view = new EmulatorView());
		controller.setCheckingRequestsSize(checkingRequestsCache.size());
		setTransmitters();
		createWindow();
	}
	
	/**
	 * Sets the controllers - synchronizer transmitters.
	 */
	public void setTransmitters() {
		controller.setCheckingRequestTransmitter(new CheckingRequestTransmitter());
		controller.setConnectButtonTransmitter(new ConnectButtonTransmitter());
	}
	
	/**
	 * Creates the emulator window and set some event handlers, especially save the cache on close.
	 */
	public void createWindow() {
		window = new Window(windowTitle, windowDimension);
		window.add(view);
		window.display();
		
		window.setWindowPreExitAction(new Runnable() {
			@Override
			public void run() {
				synchronizer.disconnectClient();
				saveCache();
			}
		});
	}
	
	//======================//
	//		synchronizer	//
	//======================//
	
	/**
	 * Creates the synchronizer and prepares its communication protocol.
	 */
	public void createSynchronizer() {
		clientTCPParameters = new TCPParameters(Constants.APPLICATION_SERVER_IP, Constants.APPLICATION_SERVER_PORT);
		
		synchronizer = new Synchronizer(
			new EmulatorServerConnectionObserver(), serverTCPParameters,
			new EmulatorClientConnectionObserver(), clientTCPParameters
		);

		synchronizer.setExceptionHandler(new SynchronizerExceptionHandler());
		
		prepareProtocol();
	}
	
	/**
	 * Prepare the communication protocol for the synchronizer.
	 */
	public void prepareProtocol() {
		synchronizer.newProtocolInterpreter(Protocol.Tag.ALL_STAFF, new AllStaffInterpreter());
		synchronizer.newProtocolInterpreter(Protocol.Tag.RECRUITED_STAFFMEMBER, new RecruitedStaffMemberInterpreter());
		synchronizer.newProtocolInterpreter(Protocol.Tag.UPDATED_STAFFMEMBER, new UpdatedStaffMemberInterpreter());
		synchronizer.newProtocolInterpreter(Protocol.Tag.DISMISSED_STAFFMEMBER_ID, new DismissedStaffMemberIDInterpreter());
		synchronizer.newProtocolInterpreter(Protocol.Tag.TCP_PARAMETERS, new TCPParametersInterpreter());
	}
	
	/**
	 * Launches the synchronizer.
	 */
	public void launchSynchronizer() {
		synchronizer.launchServer();
	}
	
	//======================//
	//		transmitters	//
	//======================//
	
	/**
	 * Transmitter used to handle checking requests (cf. Transmitter class).
	 * 
	 * @author Charles MECHERIKI
	 *
	 */
	private class CheckingRequestTransmitter extends Transmitter<CheckingRequest> {

		@Override
		public void transmit(CheckingRequest _checkingRequest) {
			if (!synchronizer.clientIsClosed()) {
				synchronizer.sendObject(Protocol.Tag.CHECKING_REQUEST, _checkingRequest);
			}
			else {
				checkingRequestsCache.add(_checkingRequest);
				controller.setCheckingRequestsSize(checkingRequestsCache.size());
			}
		}
	}
	
	/**
	 * Transmitter used to handle connection / disconnection (cf. Transmitter class).
	 * 
	 * @author Charles MECHERIKI
	 *
	 */
	private class ConnectButtonTransmitter extends Transmitter<Integer> {

		@Override
		public void transmit(Integer _connectionState) {
			if (_connectionState == 0) {
				synchronizer.connectClient();
			}
			else {
				synchronizer.disconnectClient();
			}
		}
	}
	
	//======================//
	//		interpreters	//
	//======================//

	/**
	 * Interpreter for the application's staff (cf. Interpreter class).
	 * 
	 * @author Charles MECHERIKI
	 *
	 */
	private class AllStaffInterpreter extends Interpreter<ArrayList<LightweightEmployee>> {

		@Override
		public void interpret(ArrayList<LightweightEmployee> _staff) {
			controller.setStaff(_staff);
		}
	}
	
	/**
	 * Interpreter for an application's newly recruited staff member (cf. Interpreter class).
	 * 
	 * @author Charles MECHERIKI
	 *
	 */
	private class RecruitedStaffMemberInterpreter extends Interpreter<LightweightEmployee> {

		@Override
		public void interpret(LightweightEmployee _lightweightEmployee) {
			controller.addStaffMember(_lightweightEmployee);
		}
	}
	
	/**
	 * Interpreter for an application's newly updated staff member (cf. Interpreter class).
	 * 
	 * @author Charles MECHERIKI
	 *
	 */
	private class UpdatedStaffMemberInterpreter extends Interpreter<LightweightEmployee> {

		@Override
		public void interpret(LightweightEmployee _lightweightEmployee) {
			controller.updateStaffMember(_lightweightEmployee);
		}
	}
	
	/**
	 * Interpreter for an application's newly dismissed staff member (cf. Interpreter class).
	 * 
	 * @author Charles MECHERIKI
	 *
	 */
	private class DismissedStaffMemberIDInterpreter extends Interpreter<Integer> {

		@Override
		public void interpret(Integer _dismissedStaffMemberID) {
			controller.removeStaffMemberByID(_dismissedStaffMemberID);
		}
	}
	
	/**
	 * Interpreter for a TCP parameters changement (cf. Interpreter class).
	 * 
	 * @author Charles MECHERIKI
	 *
	 */
	private class TCPParametersInterpreter extends Interpreter<TCPParameters> {
		
		@Override
		public void interpret(TCPParameters _TCPParameters) {
			synchronizer.rebootServerWithNewParameters(_TCPParameters);
			serverTCPParameters = _TCPParameters;
			alert("TCP parameters succesfully changed to " + _TCPParameters.getIPAddress() + ", port " + _TCPParameters.getPort());
		}
	}
	
	//==================//
	//		observers	//
	//==================//
	
	/**
	 * Simple observer for the synchronizer's server connection.
	 * 
	 * When the server accepts a connection, the controller update the view accordingly.
	 * 
	 * @author Charles MECHERIKI
	 *
	 */
	private class EmulatorServerConnectionObserver extends TCPConnectionObserver {
		@Override
		public void onConnection() {
			controller.switchToOnline();
		}
		
		@Override
		public void onDisconnection() {
			controller.switchToOffline();
			controller.setCheckingRequestsSize(checkingRequestsCache.size());
		}
	}
	
	/**
	 * Simple observer for the synchronizer's client connection.
	 * 
	 * Once connected, the client send a 'hey' packet (cf. synchronizer) to initiate the 
	 * reciprocal connection (application client to emulator server).
	 * 
	 * The checking request cache of the emulator is sent every time the emulator connects to the application.
	 * 
	 * @author Charles MECHERIKI
	 *
	 */
	private class EmulatorClientConnectionObserver extends TCPConnectionObserver {
		@Override
		public void onConnection() {
			synchronizer.sendHey();
			
			// Send the checking requests cache
			synchronizer.sendObject(Protocol.Tag.CHECKING_REQUESTS_CACHE, checkingRequestsCache);
			checkingRequestsCache.clear();
		}
		
		@Override
		public void onDisconnection() {
			
		}
	}
	
	/**
	 * Observer used to catch the messages and exceptions from inside the emulator (synchronizer, etc.).
	 *
	 * @author Charles MECHERIKI
	 *
	 */
	private class SynchronizerExceptionHandler extends ExceptionHandler {
		@Override
		public void handleException(Exception _exception) {
			alert(_exception.getMessage());
		}
		
		@Override
		public void handleFatalException(Exception _exception) {
			fatalError(_exception);
		}
	}
	
	//==================//
	//		exceptions	//
	//==================//
	
	/**
	 * Displays the given exception as a fatal error to the user and forces the process to exit.
	 * 
	 * @param _exception	the exception to display
	 */
	public void fatalError(Exception _exception) {
		JOptionPane.showMessageDialog(null, _exception.getMessage(), "Emulator - Fatal error", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}

	/**
	 * Displays the given message to the user.
	 * 
	 * @param _message	the message to display
	 */
	public void alert(String _message) {
		JOptionPane.showMessageDialog(null, _message, "Emulator - Alert message", JOptionPane.INFORMATION_MESSAGE);
	}
}
