package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import company.Company;
import company.Department;
import tables.DepartmentStaffTable;
import tables.DepartmentsTable;
import toolbox.exceptions.InputException;
import views.DepartmentsView;

/**
 * The departments controller, handling all the departments actions from the HMI.
 * This controller allows to :
 * 		- Add a department;
 * 		- Rename a department;
 * 		- Remove a department;
 * 		- Consult the list of the departments;
 * 		- Consult the staff of a department.
 *
 * @author Charles MECHERIKI
 *
 */
public class DepartmentsController extends Controller {
	
	private DepartmentsView view;

	private DepartmentsTable departmentsTable;
	private DepartmentStaffTable departmentStaffTable;
	
	private Company company;
	
	public DepartmentsController(Company _company, DepartmentsView _view) {
		company = _company;
		view = _view;
		
		buildView();
		launchListeners();
	}

	public void buildView() {
		departmentsTable = new DepartmentsTable(company.getDepartments());
		departmentStaffTable = new DepartmentStaffTable(null);
		
		view.writeDepartmentsTable(departmentsTable);
		view.writeDepartmentsComboBox(company.getDepartments());
		view.writeDepartmentStaffTable(departmentStaffTable);
		
		view.reset();
	}

	public void launchListeners() {
		view.addButtonListener(new AddButtonListener());
		view.departmentsComboBoxListener(new DepartmentComboBoxListener());
		view.renameButtonListener(new RenameButtonListener());
		view.removeButtonListener(new RemoveButtonListener());
	}
	
	//************************//
	//**	listeners		**//
	//************************//
	
	private class AddButtonListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent _event) {
			try {
				String name = view.readNameTextField();
		        
		        if (name.toString().equals("")) {
		        	throw new InputException("A name must be entered to achieve this action.");
		        }
		        
		        Department department = company.addNewDepartment(name);
		        departmentsTable.addDepartment(department);
		        view.writeDepartmentsComboBox(company.getDepartments());
		        view.reset();
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}
	
	private class DepartmentComboBoxListener implements ItemListener {

		@Override
	    public void itemStateChanged(ItemEvent _event) {
			try {
				if (_event.getStateChange() == ItemEvent.SELECTED) {
					Department department = view.readDepartmentComboBox();

					if (department.equals(Department.SELECT_DEPARTMENT_PLACEHOLDER)) {
						department = null;
					}
					
					departmentStaffTable.setDepartment(department);
				}
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}

	private class RenameButtonListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent _event) {
			try {
				Department department = view.readDepartmentComboBox();
				String name = view.readRenameTextField();
		        
		        if (department.equals(Department.SELECT_DEPARTMENT_PLACEHOLDER)) {
		        	throw new InputException("A department must be selected to achieve this action.");
		        }
		        else if (name.equals("")) {
		        	throw new InputException("A new name must be entered to achieve this action.");
		        }
		        company.renameDepartment(department, name);
		        departmentsTable.updateDepartment(department);
		        view.writeDepartmentsComboBox(company.getDepartments());
		        view.reset();
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}

	private class RemoveButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent _event) {
			try {
				Department department = view.readDepartmentComboBox();
		        
		        if (department.equals(Department.SELECT_DEPARTMENT_PLACEHOLDER)) {
		        	throw new InputException("A department must be selected to achieve this action.");
		        }
		        
		        company.removeDepartment(department);
		        departmentsTable.removeDepartmentByID(department.getID());
		        view.writeDepartmentsComboBox(company.getDepartments());
		        view.reset();
			}
			catch (Exception _exception) {
				view.alert(_exception.getMessage());
			}
		}
	}
}
