package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import communication.Transmitter;
import company.Company;
import company.Department;
import company.Employee;
import company.LightweightEmployee;
import company.Manager;
import company.Role;
import tables.StaffTable;
import toolbox.CSVManager;
import toolbox.exceptions.CompanyStructureException;
import toolbox.exceptions.InputException;
import toolbox.filters.CSVFileFilter;
import views.StaffView;

/**
 * The staff controller, handling all the staff actions from the HMI.
 * This controller allows to :
 *  	- Add a new staff member to the company;
 * 		- Consult the list of the company staff;
 * 		- Assign a staff member to a department;
 * 		- Promote/demote an employee/manager to manager/employee;
 * 		- Dismiss a staff member from the company.
 *
 * @author Charles MECHERIKI
 *
 */
public class StaffController extends Controller {
	
	private StaffView view;
	
	private Company company;
	private ArrayList<Employee> staff;

	private StaffTable staffTable;
	
	private Transmitter<LightweightEmployee> recruitedStaffMemberTransmitter;
	private Transmitter<Integer> dismissedStaffMemberIDTransmitter;
	
	public StaffController(Company _company, StaffView _view) {
		company = _company;
		view = _view;
		
		buildView();
		launchListeners();
		
		
		recruitedStaffMemberTransmitter = null;
		dismissedStaffMemberIDTransmitter = null;
	}

	public void buildView() {
		staff = company.getStaff();
		staffTable = new StaffTable(staff);
		
		view.writeRecruitDepartmentsComboBox(company.getDepartments());
		view.writeAddRolesComboBox(Role.getAllRoles());
		view.writeStaffTable(staffTable);
		view.writeStaffComboBox(staff);
		view.writeAssignDepartmentsComboBox(company.getDepartments());
		view.writeAssignRolesComboBox(Role.getAllRoles());
		view.writeGradeRolesComboBox(Role.getAllRoles());
		
		view.reset();
	}

	public void launchListeners() {
		view.addRecruitButtonListener(new RecruitStaffMemberListener());
		view.addResetButtonListener(new ResetButtonListener());
		view.addImportFromCSVButtonListener(new ImportFromCSVButtonListener());
		view.addExportToCSVButtonListener(new ExportToCSVButtonListener());
		view.addStaffComboBoxListener(new StaffComboBoxListener());
		view.addAssignButtonListener(new AssignButtonListener());
		view.addGradeButtonListener(new GradeButtonListener());
		view.addDismissButtonListener(new DismissButtonListener());
	}
	
	public void updateStaffMemberInTable(Employee _staffMember) {
		staffTable.updateStaffMember(_staffMember);
	}

	public void setDismissedStaffMemberIDTransmitter(Transmitter<Integer> _transmitter) {
		dismissedStaffMemberIDTransmitter = _transmitter;
	}
	
	public void setRecruitedStaffMemberTransmitter(Transmitter<LightweightEmployee> _transmitter) {
		recruitedStaffMemberTransmitter = _transmitter;
	}
	
	public class RecruitStaffMemberListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent _event) {
			try {
		        String firstname = view.readRecruitFirstnameTextField();
		        String lastname = view.readRecruitLastnameTextField();
		        Role role = view.readRecruitRoleComboBox();
		        Department department = view.readRecruitDepartmentComboBox();
		        
		        if (firstname.equals("")) {
		        	throw new InputException("A firstname must be entered to achieve this action.");
		        }
		        else if (lastname.equals("")) {
		        	throw new InputException("A lastname must be entered to achieve this action.");
		        }
		        else if (department.equals(Department.SELECT_DEPARTMENT_PLACEHOLDER)) {
		        	throw new InputException("A department must be selected to achieve this action.");
		        }
		        else if (role.equals(Role.SELECT_ROLE_PLACEHOLDER)) {
		        	throw new InputException("A role must be selected to achieve this action.");
		        }
		        
		        Employee newStaffMember = null;
		        if (role.equals(Role.HEADMANAGER)) {
		        	Manager lastHeadManager = department.getHeadManager();
			        newStaffMember = company.assignNewHeadManager(firstname, lastname, department);
			        if (lastHeadManager != null) {
			        	staffTable.updateStaffMember(lastHeadManager);
			        }
		        }
		        else if (role.equals(Role.MANAGER)) {
		        	newStaffMember = company.assignNewManager(firstname, lastname, department);
		        }
		        else {
		        	newStaffMember = company.assignNewEmployee(firstname, lastname, department);
		        }
		        staff.add(newStaffMember);
		        staffTable.addStaffMember(newStaffMember);
				view.writeStaffComboBox(staff);
				view.reset();
				
				if (recruitedStaffMemberTransmitter != null) {
					recruitedStaffMemberTransmitter.transmit(newStaffMember.lightweight());
				}
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}
	
	private class ResetButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent _event) {
			try {
				view.reset();
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}
	
	private class ImportFromCSVButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent _event) {
			try {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new CSVFileFilter());
				fileChooser.setCurrentDirectory(CSVManager.directory());

				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					CSVManager.setFileToRead(fileChooser.getSelectedFile());
					
					String line = null;
					while ((line = CSVManager.readLine()) != null) {
						try {
							company.interpretStaffMemberCSVLine(line);
						}
						catch (CompanyStructureException _exception) {
							view.alert(_exception.getMessage());
						}
					}
					
					CSVManager.closeReader();
					buildView();
				}
			}
			catch (Exception _exception) {
				_exception.printStackTrace();
				view.alert(_exception.getMessage());
			}
		}
	}
	
	private class ExportToCSVButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent _event) {
			try {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new CSVFileFilter());
				fileChooser.setCurrentDirectory(CSVManager.directory());

				if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					
					String contentCSVFile = "";
					for (Employee staffMember : company.getStaff()) {
						contentCSVFile += staffMember.toCSVLine() + "\n";
					}
					
					CSVManager.writeCSVFileContent(contentCSVFile, fileChooser.getSelectedFile());
				}
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}
	
	private class StaffComboBoxListener implements ItemListener {

		@Override
	    public void itemStateChanged(ItemEvent _event) {
			try {
				if (_event.getStateChange() == ItemEvent.SELECTED) {
					Employee staffMember = view.readStaffComboBox();
					Role role = staffMember.getRole();
				
					if (!staffMember.equals(Employee.SELECT_STAFFMEMBER_PLACEHOLDER)) {
					    view.writeAssignRolesComboBox(Role.getAssignableRolesFor(role));
					    view.writeGradeRolesComboBox(Role.getPossibleGradeRolesFor(role));
					}
		       }
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}
	
	private class AssignButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent _event) {
			try {
				Employee staffMember = view.readStaffComboBox();
		        Role role = view.readAssignRolesComboBox();
		        Department department = view.readAssignDepartmentsComboBox();		        

		        if (staffMember.equals(Employee.SELECT_STAFFMEMBER_PLACEHOLDER)) {
		        	throw new InputException("A staff member must be selected to achieve this action.");
		        }
		        else if (department.equals(Department.SELECT_DEPARTMENT_PLACEHOLDER)) {
		        	throw new InputException("A department must be selected to achieve this action.");
		        }
		        else if (role.equals(Role.SELECT_ROLE_PLACEHOLDER)) {
		        	throw new InputException("A role must be selected to achieve this action.");
		        }
		        
		        Employee newStaffMember = null;
		        if (role.equals(Role.MANAGER)) {
		        	Manager manager =  company.getManagerByID(staffMember.getID());
		        	company.assignManager(manager, department);
		        	newStaffMember = manager;
		        }
		        else if (role.equals(Role.HEADMANAGER)) {
		        	Manager manager =  company.getManagerByID(staffMember.getID());
	        		Manager lastHeadManager = department.getHeadManager();
	        		company.assignHeadManager(manager, department);
	        		if (lastHeadManager != null) {
	    		        staffTable.updateStaffMember(lastHeadManager);
	        		}
		        	newStaffMember = manager;
		        }
		        else {
		        	company.assignEmployee(staffMember, department);
		        	newStaffMember = staffMember;
		        }
		        
		        staff.remove(staffMember);
		        staff.add(newStaffMember);
		        staffTable.updateStaffMember(newStaffMember);
				view.writeStaffComboBox(staff);
				view.reset();
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}
	
	private class GradeButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent _event) {
			try {
				Employee staffMember = view.readStaffComboBox();
		        Role role = view.readGradeRolesComboBox();

		        if (staffMember.equals(Employee.SELECT_STAFFMEMBER_PLACEHOLDER)) {
		        	throw new InputException("A staff member must be selected to achieve this action.");
		        }
		        else if (role.equals(Role.SELECT_ROLE_PLACEHOLDER)) {
		        	throw new InputException("A role must be selected to achieve this action.");
		        }
		        
		        Employee newStaffMember = null;
		        if (role.equals(Role.MANAGER)) {
		        	Manager manager = company.promoteEmployee(staffMember);
		        	newStaffMember = manager;
		        }
		        else {
		        	Manager manager = company.getManagerByID(staffMember.getID());
		        	Employee employee = company.demoteManager(manager);
		        	newStaffMember = employee;
		        }
		        staff.remove(staffMember);
		        staff.add(newStaffMember);
		        staffTable.updateStaffMember(newStaffMember);
				view.writeStaffComboBox(staff);
				view.reset();
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}
	
	public class DismissButtonListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent _event) {
			try {
				Employee staffMember = view.readStaffComboBox();
				Role role = staffMember.getRole();
	
		        if (staffMember.equals(Employee.SELECT_STAFFMEMBER_PLACEHOLDER)) {
		        	throw new InputException("A staff member must be selected to achieve this action.");
		        }
		        
		        if (role.equals(Role.EMPLOYEE)) {
		        	company.dismissEmployee(staffMember);
		        }
		        else {
		        	Manager manager = company.getManagerByID(staffMember.getID());
		        	company.dismissManager(manager);
		        }
	
		        int staffMemberID = staffMember.getID();
		        staff.remove(staffMember);
		        staffTable.removeStaffMemberByID(staffMemberID);
				view.writeStaffComboBox(staff);
				view.reset();
				
				if (dismissedStaffMemberIDTransmitter != null) {
					dismissedStaffMemberIDTransmitter.transmit(staffMemberID);
				}
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}
}
