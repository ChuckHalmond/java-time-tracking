package company;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import time.Checking;
import toolbox.exceptions.CompanyStructureException;

/**
 * A department of the company.
 * All the staff members of the company belongs to exactly one department.
 * A department is composed of employees and managers.
 * There should always be one head manager in every department.
 * 
 * A department must be created from inside the company.
 * 
 * @author Charles MECHERIKI
 *
 */
public class Department implements Serializable {
	private static final long serialVersionUID = 1L; /** Recommended when implementing Serializable */
	
	private int ID;								/** ID of the department 	*/
	private String name;						/** Name of the department 	*/

	private Manager headManager;				/** Head manager of the department (i.e the one who is actually managing the department) */
	private ArrayList<Manager> managers;		/** Managers of the department	*/
	private ArrayList<Employee> employees;		/** Employees of the department	*/
	
	public static final Department SELECT_DEPARTMENT_PLACEHOLDER = new Department(-1, "Select a department"); 	/** Placeholder used inside the views */
	public static final Department ALL_DEPARTMENTS_PLACEHOLDER = new Department(-1, "All the departments");		/** Placeholder used inside the views */
	
	//********************//
	//**	constructor	**//
	//********************//
	
	/**
	 * Creates and initializes a department with its name.
	 * 
	 * @param _ID	the ID of the department
	 * @param _name	the name of the department
	 */
	public Department(int _ID, String _name) {
		ID = _ID;
		name = _name;

		managers = new ArrayList<Manager>();
		employees = new ArrayList<Employee>();
		headManager = null;
	}
	
	//********************//
	//**	destructor	**//
	//********************//
	
	/**
	* Free all the references to the staff from the department.
	*/
	public void freeStaff() {
		managers.clear();
		employees.clear();
		headManager = null;
	}
	
	//************//
	//**	ID	**//
	//************//
	
	/**
	* Returns the ID of the department.
	* 
	* @return the ID of the department
	*/
	public int getID() {
		return ID;
	}
	
	//****************//
	//**	name	**//
	//****************//
	
	/**
	* Returns the name of the department.
	* 
	* @return the name of the department
	*/
	public String toString() {
		return getName();
	}
	
	/**
	* Returns the name of the department.
	* 
	* @return the name of the department
	*/
	public String getName() {
		return name;
	}
	
	/**
	* Rename the department with the given new name.
	* 
	* @param _newName	the new name for the department
	*/
	public void rename(String _newName) {
		name = _newName;
	}
	
	//****************//
	//**	staff	**//
	//****************//
	
	/**
	* Returns the staff of the department (i.e employees plus managers).
	* 
	* @return the staff of the department
	*/
	public ArrayList<Employee> getStaff() {
		ArrayList<Employee> staff = new ArrayList<Employee>();
		
		staff.addAll(getEmployees());
		staff.addAll(getManagers());
		
		return staff;
	}
	
	/**
	* Returns the department staff information.
	* 
	* @return the department staff information
	*/
	public String toLongString() {
		String string = getName() + "\n";
		
		string += "\tHead Manager :\n\t\t- " + ((headManager != null) ? headManager.getName() : "NONE") + "\n";
		string += "\tManagers :\n";
		for (Manager manager : managers) {
			string += "\t\t- " + manager.toString() + "\n";
		}
		
		string += "\tEmployees :\n";
		for (Employee employee : employees) {
			string += "\t\t- " + employee.toString() + "\n";
		}
		
		return string;
	}
	
	//********************//
	//**	employees	**//
	//********************//	
	
	/**
	 * Returns the employees of the department.
	 * 
	 * @return the employees of the department
	 */
	public ArrayList<Employee> getEmployees() {
		return employees;
	}
	
	/**
	 * Returns true if the department contains the employee, false otherwise.
	 * 
	 * @param _employee	the employee to look for
	 * @return	true if the department contains the employee, false otherwise
	 */
	public boolean containsEmployee(Employee _employee) {
		return employees.contains(_employee);
	}
	
	/**
	 * Assigns the given employee the department.
	 * 
	 * @param _employee	the employee to assign
	 * @throws CompanyStructureException	if the employee is already in this department
	 */
	public void assignEmployee(Employee _employee) throws CompanyStructureException {
		if (!containsEmployee(_employee)) {
			if (!_employee.isWorkingIn(null) && !_employee.isWorkingIn(this)) {
				_employee.getDepartment().dismissEmployee(_employee);
			}
			_employee.setDepartment(this);
			employees.add(_employee);
		}
		else {
			throw new CompanyStructureException(_employee.getName() + " is already employee in " + getName() + ".");
		}
	}

	/**
	 * Dismisses an employee from the department.
	 * 
	 * @param _employee	the employee to dismiss
	 * @throws CompanyStructureException	if the employee is not in the department
	 */
	public void dismissEmployee(Employee _employee) throws CompanyStructureException {
		if (employees.remove(_employee)) {
			_employee.setDepartment(null);
		}
		else {
			throw new CompanyStructureException(_employee.getName() + " isn't employee in the department " + getName() + ".");
		}
	}

	//************************//
	//**	head manager	**//
	//************************//
	
	/**
	 * Returns the head manager of the department.
	 * 
	 * @return the head manager of the department
	 */
	public Manager getHeadManager() {
		return headManager;
	}
	
	/**
	 * Returns true if the given manager is the head manager of the department, otherwise false.
	 * 
	 * @param _manager	the manager
	 * @return true if the given manager is the head manager of the department, otherwise false
	 */
	public boolean isManagedBy(Manager _manager) {
		return headManager == _manager;
	}
	
	/**
	 * Assigns the given manager as head manager of the department.
	 * Note : a manager assigned with this method will be set as one of the managers AND the new head manager of the department.
	 * 
	 * @param _manager	the manager to assign
	 * @throws CompanyStructureException	if the manager is already head manager of the department
	 */
	public void assignHeadManager(Manager _manager) throws CompanyStructureException {
		Department managerDepartment = _manager.getDepartment();
		
		if (!isManagedBy(_manager)) {
			if (managerDepartment != this) {
				if (managerDepartment != null) {
					if (_manager.isHead()) {
						managerDepartment.dismissHeadManager();
					}
					managerDepartment.dismissManager(_manager);
				}
				_manager.setDepartment(this);
				managers.add(_manager);
			}
			if (!isManagedBy(null)) {
				dismissHeadManager();
			}
			_manager.isHead(true);
			_manager.setRole(Role.HEADMANAGER);
			headManager = _manager;
		}
		else {
			throw new CompanyStructureException(_manager.getName() + " is already head manager of " + getName() + ".");
		}
	}
	
	/**
	 * Dismisses the current head manager and makes him simple manager.
	 */
	private void dismissHeadManager() {
		headManager.isHead(false);
		headManager.setRole(Role.MANAGER);
		headManager = null;
	}
	
	//*****************//
	//**	managers **//
	//*****************//
	
	/**
	 * Returns the managers of the department.
	 * 
	 * @return the managers of the department
	 */
	public ArrayList<Manager> getManagers() {
		return managers;
	}
	
	/**
	 * Returns true if the department contains the manager, false otherwise.
	 * 
	 * @param _manager	the manager to look for
	 * @return true if the department contains the manager, false otherwise
	 */
	public boolean containsManager(Manager _manager) {
		return managers.contains(_manager);
	}
	
	/**
	 * Assigns the given manager the department.
	 * Note : a manager assigned with this method will be ONLY set as one of the managers of the department.
	 * 
	 * @param _manager	the manager to assign
	 * @throws CompanyStructureException	if the manager is already managing any department
	 */
	public void assignManager(Manager _manager) throws CompanyStructureException {
		if (!containsManager(_manager)) {
			if (!_manager.isWorkingIn(null)) {
				Department department = _manager.getDepartment();
				if (!_manager.isHead() || (getEmployees().size() + getManagers().size() == 1)) {
					department.dismissManager(_manager);
				}
				else {
					throw new CompanyStructureException(_manager.getName() + " can't be assigned as manager : he is head manager in " + department.getName() + ".");
				}
			}
			_manager.setDepartment(this);
			managers.add(_manager);
		}
		else {
			throw new CompanyStructureException(_manager.getName() + " is already manager in " + getName() + ".");
		}
	}
	
	/**
	 * Dismisses the manager from the department.
	 * 
	 * @param _manager	the manager to dismiss
	 * @throws CompanyStructureException	if the manager is currently head of the department or isn't in the department
	 */
	public void dismissManager(Manager _manager) throws CompanyStructureException {
		if (!isManagedBy(_manager)) {
			if (managers.remove(_manager)) {
				_manager.setDepartment(null);
			}
			else {
				throw new CompanyStructureException(_manager.getName() + " isn't manager in " + getName() + ".");
			}
		}
		else {
			if (employees.size() + managers.size() == 1) {
				if (managers.remove(_manager)) {
					dismissHeadManager();
				}
				else {
					throw new CompanyStructureException(_manager.getName() + " isn't manager in " + getName() + ".");
				}
			}
			else {
				throw new CompanyStructureException(_manager.getName() + " can't be dismissed : he is head manager in " + getName() + ".");
			}
		}
	}
	
	//******************//
	//**	checkings **//
	//******************//
	
	/**
	 * Returns the checkings of the department staff.
	 * 
	 * @return the checkings of the department staff
	 */
	public ArrayList<Checking> getCheckings() {
		ArrayList<Checking> checkings = new ArrayList<Checking>();
		
		for (Employee employee : employees) {
			checkings.addAll(employee.getCheckings());
		}
		for (Manager manager : managers) {
			checkings.addAll(manager.getCheckings());
		}
		
		return checkings;
	}
	
	/**
	 * Returns the checkings of the department staff on a precise date.
	 * 
	 * @param _date		the effective date of the checkings wanted
	 * @return the checkings of the department staff on a precise date.
	 */
	public ArrayList<Checking> getCheckingsByDate(LocalDate _date) {
		ArrayList<Checking> checkings = new ArrayList<Checking>();
		
		for (Employee employee : employees) {
			checkings.addAll(employee.getCheckingsByDate(_date));
		}
		for (Manager manager : managers) {
			checkings.addAll(manager.getCheckingsByDate(_date));
		}
		
		return checkings;
	}
	
	/**
	 * Returns the checkings of the department staff between two dates.
	 * 
	 * @param _startDate	the start date of the period
	 * @param _endDate		the end date of the period
	 * @return the checkings of the department staff between two dates
	 */
	public ArrayList<Checking> getCheckingsBetweenDates(LocalDate _startDate,  LocalDate _endDate) {
		ArrayList<Checking> checkings = new ArrayList<Checking>();
		
		for (Employee employee : employees) {
			checkings.addAll(employee.getCheckingsBetweenDates(_startDate, _endDate));
		}
		for (Manager manager : managers) {
			checkings.addAll(manager.getCheckingsBetweenDates(_startDate, _endDate));
		}
		
		return checkings;
	}
}
