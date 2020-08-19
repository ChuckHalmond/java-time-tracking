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

import company.Company;
import company.Department;
import company.Employee;
import company.LightweightEmployee;
import tables.CheckingsTable;
import time.Checking;
import time.CheckingContext;
import toolbox.exceptions.InputException;
import toolbox.filters.CheckingsFilter;
import views.CheckingsView;

/**
 * The checkings controller, handling all the checkings actions from the HMI.
 * This controller allows to :
 * 		- Consult the list of the checkings;
 * 		- Filter the list of the checkings, with spatial or temporal filters;
 *  	- Add a checking;
 * 		- Correct an existing checking.
 *
 * @author Charles MECHERIKI
 *
 */
public class CheckingsController extends Controller {
	
	private Company company;
	private ArrayList<Checking> checkings;
	
	private CheckingsView view;
	private CheckingsTable checkingsTable;
	private CheckingsFilter checkingsFilter;
	
	private Transmitter<LightweightEmployee> updatedStaffMemberTransmitter;
	
	public CheckingsController(Company _company, CheckingsView _view) {
		company = _company;
		checkings = company.getCheckings();
		view = _view;

		buildView();
		launchListeners();
		
		updatedStaffMemberTransmitter = null;
	}

	public void buildView() {
		System.out.println(checkings.size());
		checkingsTable = new CheckingsTable(checkings);
		
		view.writeCheckingsTable(checkingsTable);
		view.writeFilterStaffComboBox(company.getStaff());
		view.writeFilterDepartmentComboBox(company.getDepartments());
		
		view.reset();
		
		(new ApplyFiltersButtonListener()).actionPerformed(null);
	}
	
	public void addCheckingToTable(Checking _checking) {
		if (checkingsFilter != null && checkingsFilter.accept(_checking)) {
			checkingsTable.addChecking(_checking);
		}
	}

	public void launchListeners() {
		view.addDepartmentsComboBoxListener(new DepartmentsComboBoxListener());
		view.addApplyFiltersButtonListener(new ApplyFiltersButtonListener());
		view.addResetFiltersButtonListener(new ResetFiltersButtonListener());
		view.addCheckButtonListener(new CheckButtonListener());
		view.addCorrectCheckingButtonListener( new CorrectCheckingButtonListener());
	}
	
	//************************//
	//**	transmitters	**//
	//************************//
	
	public void setUpdatedStaffMemberTransmitter(Transmitter<LightweightEmployee> _transmitter) {
		updatedStaffMemberTransmitter = _transmitter;
	}
	
	//************************//
	//**	listeners		**//
	//************************//
	
	private class DepartmentsComboBoxListener implements ItemListener {

		@Override
	    public void itemStateChanged(ItemEvent _event) {
			try {
				if (_event.getStateChange() == ItemEvent.SELECTED) {
					Department department = view.readFilterDepartmentComboBox();
					ArrayList<Employee> staff = null;
					
					if (department.equals(Department.ALL_DEPARTMENTS_PLACEHOLDER)) {
						staff = company.getStaff();
					}
					else {
						staff = department.getStaff();
					}
					
					view.writeFilterStaffComboBox(staff);
				}
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}

	private class ApplyFiltersButtonListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent _event) {
			try {
				Employee staffMember = view.readFilterStaffComboBox();
				Department department = view.readFilterDepartmentComboBox();
		        LocalDate fromDate = view.readFilterFromDatePicker();
		        LocalDate toDate = view.readFilterToDatePicker();
		        
		        checkingsFilter = new CheckingsFilter(staffMember, department, fromDate, toDate);

		        if (staffMember.equals(Employee.ALL_STAFF_PLACEHOLDER)) {
			        if (department.equals(Department.ALL_DEPARTMENTS_PLACEHOLDER)) {
						if (toDate.equals(fromDate)) {
							checkings = company.getCheckingsByDate(toDate);
						}
						else {
							checkings = company.getCheckingsBetweenDates(fromDate, toDate);
						}
			        }
			        else {
			        	if (toDate.equals(fromDate)) {
							checkings = department.getCheckingsByDate(toDate);
						}
			        	else {
							checkings = department.getCheckingsBetweenDates(fromDate, toDate);
						}
			        }
		        }
		        else {
		        	if (toDate.equals(fromDate)) {
						checkings = staffMember.getCheckingsByDate(toDate);
					}
					else {
						checkings = staffMember.getCheckingsBetweenDates(fromDate, toDate);
					}
		        }
		        checkingsTable.setCheckings(checkings);
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}

	private class ResetFiltersButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent _event) {
			try {
				view.reset();
				(new ApplyFiltersButtonListener()).actionPerformed(null);
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}
	
	public class CheckButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent _event) {
			try {
				LocalDate checkDate = view.readCheckDatePicker();
				LocalTime checkTime = view.readCheckTimePicker();
				int staffMemberID = view.readStaffMemberIDUnsignedIntField();
				
				LocalDateTime checkDateTime = LocalDateTime.of(checkDate, checkTime);     
	
		        if (staffMemberID < 0) {
		        	throw new InputException("A staff member ID must be entered to achieve this action.");
		        }

		        Employee staffMember = company.getStaffMemberByID(staffMemberID);
		        CheckingContext checkingContext = staffMember.generateCheckingContext(checkDateTime);
		        Checking checking = company.addNewChecking(staffMember, checkingContext);
		        
		        checkingsTable.addChecking(checking);
		        
		        if (updatedStaffMemberTransmitter != null) {
		        	updatedStaffMemberTransmitter.transmit(checking.getStaffMember().lightweight());
		        }
		        
				view.reset();
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}
	
	public class CorrectCheckingButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent _event) {
			try {
				LocalDate correctDate = view.readCorrectCheckingDatePicker();
				LocalTime correctTime = view.readCorrectCheckingTimePicker();
				int correctCheckingID = view.readCorrectCheckingIDUnsignedIntField();
				
				LocalDateTime correctDateTime = LocalDateTime.of(correctDate, correctTime);     
	
		        if (correctCheckingID == -1) {
		        	throw new InputException("A checking ID must be entered to achieve this action.");
		        }
	  
		        Checking checking = company.getCheckingByID(correctCheckingID);
		        Employee staffMember = checking.getStaffMember();
		        staffMember.correctCheckingContext(checking.getContext(), correctDateTime);
		        checkingsTable.replaceCheckingByID(correctCheckingID, checking);
		        
		        if (updatedStaffMemberTransmitter != null) {
		        	updatedStaffMemberTransmitter.transmit(checking.getStaffMember().lightweight());
		        }
		        
				view.reset();
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}
}
