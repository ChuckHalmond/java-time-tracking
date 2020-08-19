import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import communication.Interpreter;
import communication.Protocol;
import communication.Transmitter;
import communication.TCP.TCPConnectionObserver;
import communication.TCP.TCPParameters;
import communication.Synchronizer;
import company.Company;
import company.Employee;
import company.LightweightEmployee;
import controllers.CheckingsController;
import controllers.Controller;
import controllers.DepartmentsController;
import controllers.ParametersController;
import controllers.StaffController;
import parameters.Parameters;
import swing.windows.TabbedWindow;
import toolbox.exceptions.CompanyStructureException;
import toolbox.exceptions.CheckingException;
import toolbox.observers.ExceptionHandler;
import toolbox.CompanyGenerator;
import toolbox.Constants;
import toolbox.Serializor;
import time.Checking;
import time.CheckingContext;
import time.CheckingRequest;
import views.CheckingsView;
import views.DepartmentsView;
import views.ParametersView;
import views.StaffView;
import views.View;

/**
 * The application is the main device, where all the company is being managed.
 * 
 * The role of this structure is to coordinate the HMI with the emulator, yet it doesn't requires the emulator to work properly.
 * 
 * The different communications with the emulator are described through its transmitters and interpreters, 
 * but all the technical stuff is handled internally by the synchronizer.
 * 
 * It's also there that the cache is loaded and saved.
 * 
 * @author Charles MECHERIKI
 *
 */
public class Application {
	private Company company;		/**	The company used in the application	*/ 
	private Parameters parameters;	/**	Parameters of the application 		*/

	private TabbedWindow window;	/**	The window where all is displayed	*/
	
	private StaffView staffView;				/**	The application staff tab view 			*/
	private CheckingsView checkingsView;		/**	The application checkings tab view 		*/
	private DepartmentsView departmentsView;	/**	The application departments tab view 	*/
	private ParametersView parametersView;		/**	The application parameters tab view 	*/
	
	private StaffController staffController;			/**	Controller managing the company staff 	*/
	private CheckingsController checkingsController;	/**	Controller managing the checkings	 	*/
	private DepartmentsController departmentController;	/**	Controller managing the departments		*/
	private ParametersController parametersController;	/**	Controller managing the parameters		*/
	
	private ArrayList<Controller> controllers;	/**	All the controllers	*/
	private ArrayList<View> views;				/** All the views		*/

	private Synchronizer synchronizer;			/**	Synchronizer, handling the communication with the emulator 	*/
	private TCPParameters serverTCPParameters;	/**	TCP parameters of the synchronizer's server */
	private TCPParameters clientTCPParameters;	/** TCP parameters of the synchronizer's client */
	
	private final String windowTitle = "TimeTrackingApplication";		/**	Title of the application window		*/
	private final Dimension windowDimension = new Dimension(650, 600);	/**	Dimension of the application window	*/
	
	public static final String companyCacheFile = "Company.ser";		/**	Company cache filename				*/
	
	/**
	 * Application constructor which :
	 * 		1) Loads the cache;
	 * 		2) Creates the HMI;
	 * 		3) Creates and launches the synchronizer.
	 */
	public Application() {
		try {
			loadCache();
			
			createHMI();
			createSynchronizer();
			
			launchSynchronizer();
		}
		catch (Exception _exception) {
			fatalError(new Exception("An error occured on intialization.", _exception));
		}
	}

	//==============//
	//		cache	//
	//==============//
	
	/**
	 * Loads the last state of the application from cache files (called at launch) using the serializor.
	 */
	public void loadCache() {
		try {
			company = (Company)Serializor.deserializeFromFile(companyCacheFile);
			parameters = new Parameters();
			parameters.loadParameters();
		}
		catch (Exception _exception) {
			fatalError(_exception);
		}
		
		if (company == null) {
			company = CompanyGenerator.generate();
		}
	}
	
	/**
	 * Saves the current state of the application in cache files (called at closing) using the serializor.
	 */
	public void saveCache() {
		try {
			parameters.saveParameters();
			Serializor.serializeToFile(companyCacheFile, company);
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
	 * 		1) Creates the views;
	 * 		2) Creates the controllers;
	 *  	3) Sets the transmitters;
	 *  	4) Creates the window.
	 */
	public void createHMI() {
		createViews();
		createControllers();
		setTransmitters();
		createWindow();
	}
	
	/**
	 * Creates the views.
	 */
	public void createViews() {
		views = new ArrayList<View>();
		
		staffView = new StaffView();
		checkingsView = new CheckingsView();
		departmentsView = new DepartmentsView();
		parametersView = new ParametersView();
		
		views.add(staffView);
		views.add(checkingsView);
		views.add(departmentsView);
		views.add(parametersView);
	}
	
	/**
	 * Creates the controllers.
	 */
	public void createControllers() {
		controllers = new ArrayList<Controller>();
		
		staffController = new StaffController(company, staffView);
		checkingsController = new CheckingsController(company, checkingsView);
		departmentController = new DepartmentsController(company, departmentsView);
		parametersController = new ParametersController(parameters, parametersView);

		controllers.add(staffController);
		controllers.add(checkingsController);
		controllers.add(departmentController);
		controllers.add(parametersController);
	}
	
	/**
	 * Sets the transmitters (cf. Transmitter class).
	 */
	public void setTransmitters() {
		staffController.setRecruitedStaffMemberTransmitter(new RecruitedStaffMemberTransmitter());
		staffController.setDismissedStaffMemberIDTransmitter(new DismissedStaffMemberIDTransmitter());
		checkingsController.setUpdatedStaffMemberTransmitter(new UpdatedStaffMemberTransmitter());
		parametersController.setSavedParametersTransmitter(new SavedParametersTransmitter());
	}
	
	/**
	 * Creates the application window and set some event handlers, especially save the cache on close.
	 */
	public void createWindow() {
		window = new TabbedWindow(windowTitle, windowDimension);
		
		window.integrateViews(views);
		
		window.addTabChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent _event) {
				controllers.get(window.getTabbedPane().getSelectedIndex()).buildView();
			}
		});
		
		window.setWindowPreExitAction(new Runnable() {
			@Override
			public void run() {
				synchronizer.disconnectClient();
				saveCache();
			}
		});
		
		window.display();
	}
	
	//======================//
	//		synchronizer	//
	//======================//
	
	/**
	 * Creates the synchronizer and prepares its communication protocol.
	 */
	public void createSynchronizer() {
		clientTCPParameters = new TCPParameters(parameters.getEmulatorIPAddress(), parameters.getEmulatorPort());
		serverTCPParameters = new TCPParameters(Constants.APPLICATION_SERVER_IP, Constants.APPLICATION_SERVER_PORT);

		synchronizer = new Synchronizer(
				new ApplicationServerConnectionObserver(), serverTCPParameters,
				new ApplicationClientConnectionObserver(), clientTCPParameters
		);

		synchronizer.setExceptionHandler(new SynchronizerExceptionHandler());
		
		prepareProtocol();
	}
	
	
	/**
	 * Prepares the communication protocol for the synchronizer.
	 */
	public void prepareProtocol() {
		synchronizer.newProtocolInterpreter(Protocol.Tag.CHECKING_REQUEST, new CheckingRequestInterpreter());
		synchronizer.newProtocolInterpreter(Protocol.Tag.CHECKING_REQUESTS_CACHE, new CheckingRequestsCacheInterpreter());
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
	 * Transmitter used to handle added staff member (cf. Transmitter class).
	 * 
	 * @author Charles MECHERIKI
	 *
	 */
	private class RecruitedStaffMemberTransmitter extends Transmitter<LightweightEmployee> {

		@Override
		public void transmit(LightweightEmployee _lightweightStaffMember) {
			if (_lightweightStaffMember != null) {
				synchronizer.sendObject(Protocol.Tag.RECRUITED_STAFFMEMBER, _lightweightStaffMember);
			}
		}
	}
	
	/**
	 * Transmitter used to handle dismissed staff member (cf. Transmitter class).
	 * 
	 * @author Charles MECHERIKI
	 *
	 */
	private class DismissedStaffMemberIDTransmitter extends Transmitter<Integer> {

		@Override
		public void transmit(Integer _staffMemberID) {
			if (_staffMemberID != 0) {
				synchronizer.sendObject(Protocol.Tag.DISMISSED_STAFFMEMBER_ID, _staffMemberID);
			}
		}
	}
	
	/**
	 * Transmitter used to handle updated staff member (for example a staff member who checked)(cf. Transmitter class).
	 * 
	 * @author Charles MECHERIKI
	 *
	 */
	private class UpdatedStaffMemberTransmitter extends Transmitter<LightweightEmployee> {

		public void transmit(LightweightEmployee _lightweightStaffMember) {
			if (_lightweightStaffMember != null) {
				synchronizer.sendObject(Protocol.Tag.UPDATED_STAFFMEMBER, _lightweightStaffMember);
			}
		}
	}
	
	/**
	 * Transmitter used to handle parameters change (cf. Transmitter class).
	 * 
	 * @author Charles MECHERIKI
	 *
	 */
	private class SavedParametersTransmitter extends Transmitter<TCPParameters> {

		public void transmit(TCPParameters _TCPParameters) {
			try {
				if (_TCPParameters != null) {
					if (!synchronizer.clientIsClosed()) {
						parametersController.setTCPParameters(_TCPParameters);
						synchronizer.sendObject(Protocol.Tag.TCP_PARAMETERS, _TCPParameters);
						synchronizer.rebootClientWithNewParameters(_TCPParameters);
					}
					else {
						alert("The emulator must be connected to change its TCP parameters.");
					}
				}
			}
			catch (Exception _exception) {
				fatalError(_exception);
			}
		}
	}
	
	//======================//
	//		interpreters	//
	//======================//
	
	/**
	 * Interpreter for the emulator's checking requests (cf. Interpreter class).
	 * 
	 * @author Charles MECHERIKI
	 *
	 */
	private class CheckingRequestInterpreter extends Interpreter<CheckingRequest> {

		@Override
		public void interpret(CheckingRequest _checkingRequest) {
			try {
				Employee staffMember = company.getStaffMemberByID(_checkingRequest.getStaffMemberID());
				
				CheckingContext checkingContext = staffMember.generateCheckingContext(_checkingRequest.getCheckingDateTime());
				Checking checking = company.addNewChecking(staffMember, checkingContext);
				
				checkingsController.addCheckingToTable(checking);
				staffController.updateStaffMemberInTable(staffMember);
				
				synchronizer.sendObject(Protocol.Tag.UPDATED_STAFFMEMBER, staffMember.lightweight());
			}
			catch (Exception _exception) {
				if (!synchronizer.clientIsClosed()) {
					synchronizer.sendException(_exception);
				}
			}
		}
	}
	
	/**
	 * Interpreter for the emulator's checking requests cache (cf. Interpreter class).
	 * 
	 * @author Charles MECHERIKI
	 *
	 */
	private class CheckingRequestsCacheInterpreter extends Interpreter<ArrayList<CheckingRequest>> {

		@Override
		public void interpret(ArrayList<CheckingRequest> _checkingRequests) {
			Employee staffMember = null;
			int unexpectedCheckings = 0;
			int unknownStaffMemberIDs = 0;
			for (CheckingRequest checkingRequest : _checkingRequests) {
				try {
					if ((staffMember = company.getStaffMemberByID(checkingRequest.getStaffMemberID())) != null) {
						
						CheckingContext checkingContext = staffMember.generateCheckingContext(checkingRequest.getCheckingDateTime());
						Checking checking = company.addNewChecking(staffMember, checkingContext);
						
						checkingsController.addCheckingToTable(checking);
						staffController.updateStaffMemberInTable(staffMember);
					}
				}
				catch (CheckingException _exception) {
					unexpectedCheckings++;
				}
				catch (CompanyStructureException _exception) {
					unknownStaffMemberIDs++;
				}
			}
			if (_checkingRequests.size() > 0) {
				alert("Checking requests report :\n"
					+ "     - " + _checkingRequests.size() + " checking requests processed ;\n"
					+ "     - " + (_checkingRequests.size() - unexpectedCheckings - unknownStaffMemberIDs) + " succeeded "
					+ "and " + (unexpectedCheckings + unknownStaffMemberIDs) + " failed ;\n" 
					+ ((unexpectedCheckings > 0) ? "     - " + unexpectedCheckings + " of them were unexpected ;\n" : "")
					+ ((unknownStaffMemberIDs > 0) ? "     - " + unknownStaffMemberIDs + " of them were from an unknown staff member ;\n" : "")
				);
			}
			synchronizer.sendObject(Protocol.Tag.ALL_STAFF, company.getLightweightStaff());
		}
	}
	
	//==================//
	//		observers	//
	//==================//
	
	/**
	 * Simple observer for the synchronizer's server connection.
	 * 
	 * Unlike the emulator, no action is launched when the server accepts a connection.
	 * 
	 * @author Charles MECHERIKI
	 *
	 */
	private class ApplicationServerConnectionObserver extends TCPConnectionObserver {
		@Override
		public void onConnection() {

		}		
		@Override
		public void onDisconnection() {

		}
	}
	
	/**
	 * Simple observer for the synchronizer's client connection.
	 * 
	 * Once connected, the client send a 'hey' packet (cf. synchronizer) to initiate the 
	 * reciprocal connection (emulator client to application server).
	 * 
	 * @author Charles MECHERIKI
	 *
	 */
	private class ApplicationClientConnectionObserver extends TCPConnectionObserver {
		@Override
		public void onConnection() {
			synchronizer.sendHey();
		}
		
		@Override
		public void onDisconnection() {

		}
	}
	
	/**
	 * Observer used to catch the messages and exceptions from inside the application (synchronizer, etc.).
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
		_exception.printStackTrace();
		JOptionPane.showMessageDialog(null, _exception.getMessage(), "Application - Fatal error", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}

	/**
	 * Displays the given message to the user.
	 * 
	 * @param _message	the message to display
	 */
	public void alert(String _message) {
		JOptionPane.showMessageDialog(null, _message, "Application - Alert message", JOptionPane.INFORMATION_MESSAGE);
	}
}