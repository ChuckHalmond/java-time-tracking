package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import communication.Transmitter;
import company.LightweightEmployee;
import time.CheckingRequest;
import time.DateTimeService;
import toolbox.exceptions.InputException;
import views.EmulatorView;

/**
 * The emulator controller, handling all the emulator actions from the HMI.
 * This controller allows to :
 *  	- Connect/disconnect the emulator to/from the application;
 * 		- Emit a checking request to the application.
 *
 * @author Charles MECHERIKI
 *
 */
public class EmulatorController extends Controller {
	
	private ArrayList<LightweightEmployee> staff;
	
	private EmulatorView view;
	
	private Transmitter<CheckingRequest> checkingRequestTransmitter;
	private Transmitter<Integer> connectionRequestTransmitter;
	
	private int connectionState = 0;
	
	public EmulatorController(ArrayList<LightweightEmployee> _staff, EmulatorView _view) {
		view = _view;
		staff = _staff;
		
		buildView();
		launchListeners();
		
		checkingRequestTransmitter = null;
		connectionRequestTransmitter = null;

		new Thread(new Clock()).start();
	}
	
	public void buildView() {
		LocalDateTime now = LocalDateTime.now(DateTimeService.clock);
		
		view.writeStaffComboBox(staff);
		view.writeCurrentDateTextField(now.toLocalDate());
		view.writeCurrentTimeTextField(now.toLocalTime());
		view.writeRoundedCurrentTimeTextField(DateTimeService.roundAtQuarter(now).toLocalTime());
		
		view.reset();
	}

	public void launchListeners() {
		view.addStaffComboBoxListener(new StaffComboBoxListener());
		view.addCheckButtonListener(new CheckButtonListener());
		view.addConnectButtonListener(new ConnectButtonListener());
	}
	
	//======================//
	//		transmitters	//
	//======================//
	
	
	public void setCheckingRequestTransmitter(Transmitter<CheckingRequest> _transmitter) {
		checkingRequestTransmitter = _transmitter;
	}
	
	public void setConnectButtonTransmitter(Transmitter<Integer> _transmitter) {
		connectionRequestTransmitter = _transmitter;
	}
	
	//======================//
	//		staff methods	//
	//======================//
	
	
	public void switchToOnline() {
		view.switchToOnline();
		view.clearCheckingRequestsLabel();
		connectionState = 1;
	}
	
	public void switchToOffline() {
		view.switchToOffline();
		connectionState = 0;
	}

	public void setCheckingRequestsSize(int _checkingRequestsSize) {
		view.writeCheckingRequestsLabel(_checkingRequestsSize);
	}

	public ArrayList<LightweightEmployee> getStaff() {
		return staff;
	}
	
	public void setStaff(ArrayList<LightweightEmployee> _staff) {
		staff = _staff;
		view.writeStaffComboBox(staff);
	}

	public void addStaffMember(LightweightEmployee _staffMember) {
		staff.add(_staffMember);
		view.writeStaffComboBox(staff);
	}

	public void removeStaffMemberByID(int _staffMemberID) {
		for (LightweightEmployee staffMember : staff) {
			if (staffMember.getID() == _staffMemberID) {
				staff.remove(staffMember);
				break;
			}
		}
		view.writeStaffComboBox(staff);
	}
	
	public void updateStaffMember(LightweightEmployee _staffMember) {
		for (LightweightEmployee staffMember : staff) {
			if (staffMember.getID() == _staffMember.getID()) {
				staffMember = _staffMember;
				break;
			}
		}
		
		if (view.readStaffComboBox().getID() == _staffMember.getID()) {
			view.writeNextExpectedCheckingTextField(_staffMember.getNextExpectedCheckingDateTime());
		}
	}

	
	//==================//
	//		listeners	//
	//==================//
	
	private class StaffComboBoxListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent _event) {
			try {
				if (_event.getStateChange() == ItemEvent.SELECTED) {
					LightweightEmployee staffMember = view.readStaffComboBox();
					
					if (!staffMember.equals(LightweightEmployee.SELECT_STAFFMEMBER_PLACEHOLDER)) {
						view.writeNextExpectedCheckingTextField(staffMember.getNextExpectedCheckingDateTime());
					}
				}
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}

	private class CheckButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent _event) {
			try {
				LightweightEmployee staffMember = view.readStaffComboBox();
				
				if (staffMember.equals(LightweightEmployee.SELECT_STAFFMEMBER_PLACEHOLDER)) {
					throw new InputException("A staff member must be selected to achieve this action.");
				}
				
				CheckingRequest checkingRequest = new CheckingRequest(
					staffMember.getID(), DateTimeService.roundAtQuarter(LocalDateTime.now(DateTimeService.clock))
				);
				
				if (checkingRequestTransmitter != null) {
					checkingRequestTransmitter.transmit(checkingRequest);
				}
	
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}
	
	private class ConnectButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent _event) {
			try {
				if (connectionRequestTransmitter != null) {
					connectionRequestTransmitter.transmit(connectionState);
				}
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}
	
	//==============//
	//		clock	//
	//==============//
	
	private class Clock implements Runnable {
		
		@Override
		public void run() {
			LocalDateTime now = LocalDateTime.now(DateTimeService.clock);
			LocalTime time = now.toLocalTime();
			LocalDate date = now.toLocalDate();
			
			long nextMinute = (long)(Math.ceil(System.currentTimeMillis() / 60000.0) * 60000);
			
			while (true) {
				try {
					Thread.sleep(nextMinute - System.currentTimeMillis());
				}
				catch (Exception _exception) {
					view.alert(_exception.getMessage());
				}
				
				now = now.plusMinutes(1);
				time = now.toLocalTime();

				if (now.toLocalDate().isAfter(date)) {
					date = now.toLocalDate();
					view.writeCurrentDateTextField(date);
				}

				view.writeCurrentTimeTextField(time);
				view.writeRoundedCurrentTimeTextField(DateTimeService.roundAtQuarter(now).toLocalTime());
				
				nextMinute += 60000;
			}
		}
	}
}