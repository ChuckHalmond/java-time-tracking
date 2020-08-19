package company;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import time.Checking;
import time.CheckingContext;
import toolbox.exceptions.CheckingException;
import toolbox.exceptions.CompanyStructureException;

/**
 * A company, which includs a boss, some departments and some staff (employees or managers).
 * 
 * When the terms staff and staff member are used, they designate either a manager or a simple employee,
 * but stored in the employee data structure (a manager is a specialized employee).
 * 
 * A manager can be head of a department, and each department should have one manager as head.
 * 
 * Note : A head is not required for a department to be created, but once it has a head, this manager
 * can not leave the department until he is the last staff member working in it.
 * 
 * All the major tasks (assignments, dismissal, department creation, etc.) should be launched from here, as
 * it is THE main business data structure of the program.
 * 
 * @author Charles MECHERIKI
 * 
 */
public class Company implements Serializable {
	private static final long serialVersionUID = 1L;	/** Recommended when implementing Serializable */
	
	private String name;							/** Name of the company */
	private Boss boss;								/** Boss of the company */

	private ArrayList<Department> departments;		/** Departments of the company 	*/
	private ArrayList<Manager> managers;			/** Managers of the company 	*/
	private ArrayList<Employee> employees;			/** Employees of the company 	*/
	
	//************************//
	//**	constructor		**//
	//************************//
	
	/***
	 * Creates a company with its boss information.
	 * 
	 * @param _name				the name of the company
	 * @param _bossFirstname	the firsname of the company boss
	 * @param _bossLastname		the lastname of the company boss
	 */
	public Company(String _name, String _bossFirstname, String _bossLastname) {
		name = _name;
		
		boss = new Boss(_bossFirstname, _bossLastname);
		departments = new ArrayList<Department>();
		employees = new ArrayList<Employee>();
		managers = new ArrayList<Manager>();
	}
	
	//************************//
	//**	IDs generation	**//
	//************************//

	/**
	 * Generates the ID for the newly created staff member.
	 * This method is used in replacement of a static integer (which wouldn't be serializable) to generate unique IDs.
	 * 
	 * @return the ID for the newly created staff member
	 */
	public int generateNewStaffMemberID() {
		int maxID = 0;
		
		for (Employee employee : employees) {
			if (employee.getID() > maxID) {
				maxID = employee.getID();
			}
		}
		for (Manager manager : managers) {
			if (manager.getID() > maxID) {
				maxID = manager.getID();
			}
		}
		
		return maxID + 1;
	}
	
	/**
	 * Generates the ID for the newly created department.
	 * This method is used in replacement of a static integer (which wouldn't be serializable) to generate unique IDs.
	 * 
	 * @return the ID for the newly created department
	 */
	public int generateNewDepartmentID() {
		int maxID = 0;
		
		for (Department department : departments) {
			if (department.getID() > maxID) {
				maxID = department.getID();
			}
		}
		
		return maxID + 1;
	}
	
	/**
	 * Generates the ID for the newly created checking.
	 * This method is used in replacement of a static integer (which wouldn't be serializable) to generate unique IDs.
	 * 
	 * @return the ID for the newly created checking
	 */
	public int generateNewCheckingID() {
		int maxID = 0;

		for (Checking checking : getCheckings()) {
			if (checking.getID() > maxID) {
				maxID = checking.getID();
			}
		}
		
		return maxID + 1;
	}
	
	//****************//
	//**	name 	**//
	//****************//
	
	/**
	* Returns the name of the company.
	* 
	* @return the name of the company
	*/
	public String getName() {
		return name;
	}
	
	/**
	* Renames the company.
	* 
	* @param _name	the new name of the company
	*/
	public void rename(String _name) {
		name = _name;
	}
	
	/**
	* Returns the name of the company
	* 
	* @return the name of the company
	*/
	public String toString() {
		return getName();
	}
	
	/**
	* Returns a string describing the entire company (boss, departments and respective staff).
	* 
	* @return a string describing the entire company (boss, departments and respective staff)
	*/
	public String toLongString() {
		String string = "";
		
		string += "Company " + toString() + "\n";
		string += "Boss : " + boss.toString() + "\n";
		string += "Departments :\n";
		for (Department department : departments) {
			string += department.toLongString();
		}
		
		return string;
	}
	
	/**
	* Returns the boss of the company.
	* 
	* @return the boss of the company
	*/
	public Boss getBoss() {
		return boss;
	}
	
	//************************//
	//**	departments 	**//
	//************************//
	
	/**
	 * Returns the departments of the company.
	 * 
	 * @return the departments of the company
	 */
	public ArrayList<Department> getDepartments() {
		return departments;
	}
	
	/**
	 * Returns the department of the company with the given name if it exists, null otherwise.
	 * 
	 * @param _departmentName	the name of the department to look for
	 * @return the department of the company with the given name if it exists, null otherwise
	 */
	public Department getDepartmentByName(String _departmentName) {
		for (Department department : departments) {
			if (department.getName().equals(_departmentName)) {
				return department;
			}
		}
		return null;
	}
	
	/**
	 * Returns true if the company contains the department, false otherwise.
	 * 
	 * @param _department	the department
	 * @return	true if the company contains the department, false otherwise 
	 */
	public boolean containsDepartment(Department _department) {
		return departments.contains(_department);
	}
	
	/**
	 * Creates and adds a new department to the company with the given name and returns it.
	 * 
	 * @param _name		the name for the new department
	 * @return the newly created department
	 * @throws CompanyStructureException	if a department with the same already exists
	 */
	public Department addNewDepartment(String _name) throws CompanyStructureException {
		if (getDepartmentByName(_name) == null) {
			Department newDepartment = new Department(generateNewDepartmentID(), _name);
			departments.add(newDepartment);
			
			return newDepartment;
		}
		else {
			throw new CompanyStructureException("Department " + _name + " already exists in the company.");
		}
	}
	
	/**
	 * Renames the given department with the given new name.
	 * 
	 * @param _department	the department to rename
	 * @param _newName		the new name for the department
	 * @throws CompanyStructureException	if a department with the same name already exists
	 */
	public void renameDepartment(Department _department, String _newName) throws CompanyStructureException {
		if (getDepartmentByName(_newName) == null) {
			_department.rename(_newName);
		}
		else {
			throw new CompanyStructureException("Department " + _newName + " already exists in the company.");
		}
	}
	
	/**
	 * Removes the given department from the company.
	 * 
	 * @param _department	the department to remove
	 * @throws CompanyStructureException	if the department does not exists in the company
	 */
	public void removeDepartment(Department _department) throws CompanyStructureException {
		if (containsDepartment(_department)) {
			if (_department.getManagers().size() == 0 && _department.getEmployees().size() == 0) {
				departments.remove(_department);
				_department.freeStaff();
			}
			else {
				throw new CompanyStructureException(_department.getName() + " must have no staff inside to be removed.");
			}
		}
		else {
			throw new CompanyStructureException(_department.getName() + " doesn't exist in the company.");
		}
	}
	
	//****************//
	//**	staff 	**//
	//****************//
	
	/**
	* Returns the staff of the company (i.e the employees and managers).
	* 
	* @return the staff of the company (i.e the employees and managers)
	*/
	public ArrayList<Employee> getStaff() {
		ArrayList<Employee> staff = new ArrayList<Employee>();
		
		staff.addAll(getEmployees());
		staff.addAll(getManagers());
		
		return staff;
	}
	
	/**
	* Returns the staff of the company but in a lightweight data structure (suited for the emulator).
	* 
	* @return the staff of the company but in a lightweight data structure (suited for the emulator)
	*/
	public ArrayList<LightweightEmployee> getLightweightStaff() {
		ArrayList<LightweightEmployee> lightweightStaff = new ArrayList<LightweightEmployee>();

		for (Employee employee : employees) {
			lightweightStaff.add(new LightweightEmployee(employee));
		}
		for (Manager manager : managers) {
			lightweightStaff.add(new LightweightEmployee(manager));
		}
		
		return lightweightStaff;
	}
	
	/**
	 * Returns true if the company contains a staff member with the given name, false otherwise
	 * 
	 * @param _staffMemberName	the name of the staff member to look for
	 * @return	true if the company contains a staff member with the given name, false otherwise
	 */
	public boolean containsStaffMemberWithName(String _staffMemberName) {
		for (Manager manager : managers) {
			if (manager.getName().equals(_staffMemberName)) {
				return true;
			}
		}
		for (Employee employee : employees) {
			if (employee.getName().equals(_staffMemberName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	* Returns the staff member who has the given ID.
	* 
	* @param _ID	the ID of the staff member to look for
	* @return the staff member who has the given ID
	* @throws CompanyStructureException	if no staff member in the company has the given ID
	*/
	public Employee getStaffMemberByID(int _ID) throws CompanyStructureException {
		for (Manager manager : managers) {
			if (manager.getID() == _ID) {
				return manager;
			}
		}
		for (Employee employee : employees) {
			if (employee.getID() == _ID) {
				return employee;
			}
		}
		throw new CompanyStructureException(_ID + " doesn't reference any staff member of the company.");
	}
	
	//********************//
	//**	employees	**//
	//********************//
	
	/**
	 * Returns all the employees of the company.
	 * 
	 * @return all the employees of the company
	 */
	public ArrayList<Employee> getEmployees() {
		return employees;
	}
	
	/**
	 * Returns true if the company contains the given employee, false otherwise.
	 * 
	 * @param _employee	the employee to look for
	 * @return	true if the company contains the given employee, false otherwise 
	 */
	public boolean containsEmployee(Employee _employee) {
		return employees.contains(_employee);
	}
	
	/**
	 * Creates and assigns a new employee a department of the company and returns him.
	 * 
	 * @param _firstname	the firstname of the new employee
	 * @param _lastname		the lastname of the new employee
	 * @param _department	the department the employee is assigned to
	 * @return the newly created employee
	 * @throws CompanyStructureException	if the department doesn't exist in the company
	 */
	public Employee assignNewEmployee(String _firstname, String _lastname, Department _department) throws CompanyStructureException {
		Employee newEmployee = new Employee(generateNewStaffMemberID(), _firstname, _lastname);
		
		if (containsDepartment(_department)) {
			if (!containsStaffMemberWithName(newEmployee.getName())) {
				employees.add(newEmployee);
				_department.assignEmployee(newEmployee);
				
				return newEmployee;
			}
			else {
				throw new CompanyStructureException(newEmployee.getName() + " already exists in the company.");
			}
		}
		else {
			throw new CompanyStructureException(_department.getName() + " doesn't exist in the company.");
		}
	}
	
	/**
	 * Assigns an existing employee a department of the company.
	 * 
	 * @param _employee		the employee to assign
	 * @param _department	the department the employee is assigned to
	 * 
	 * @throws CompanyStructureException	if the department or the employee doesn't exist in the company or 
	 * the employee is already in this department
	 */
	public void assignEmployee(Employee _employee, Department _department) throws CompanyStructureException {
		if (containsDepartment(_department)) {
			_department.assignEmployee(_employee);
		}
		else {
			throw new CompanyStructureException(_department.getName() + " doesn't exist in the company.");
		}
	}
	
	/**
	 * Dismisses the given employee from the company.
	 * 
	 * @param _employee		the employee to dismiss
	 * @throws CompanyStructureException	if the employee isn't in the company
	 */
	public void dismissEmployee(Employee _employee) throws CompanyStructureException {
		if (containsEmployee(_employee)) {
			if (_employee.getDepartment() != null) {
				_employee.getDepartment().dismissEmployee(_employee);
			}
			employees.remove(_employee);
		}
		else {
			throw new CompanyStructureException(_employee.getName() + " doesn't exist in the company.");
		}
	}
	
	/**
	* Promotes the given employee to the role of manager and returns the result.
	* 
	* @param _employee	the employee to promote
	* @return the employee once promoted to the role of manager
	* @throws CompanyStructureException	if the given employee is not in the company
	*/
	public Manager promoteEmployee(Employee _employee) throws CompanyStructureException {
		Department department = _employee.getDepartment();
		Manager manager = new Manager(_employee);
		managers.add(manager);
		dismissEmployee(_employee);
		
		if (department != null) {
			manager.setDepartment(null);
			assignManager(manager, department);
		}
		
		return manager;
	}
	
	//********************//
	//**	managers	**//
	//********************//

	/**
	 * Returns all the managers of the company.
	 * 
	 * @return all the managers of the company
	 */
	public ArrayList<Manager> getManagers() {
		return managers;
	}
	
	/**
	 * Returns true if the company contains the given manager, false otherwise.
	 * 
	 * @param _manager	the manager to look for
	 * @return true if the company contains the given manager, false otherwise.
	 */
	public boolean containsManager(Manager _manager) {
		return managers.contains(_manager);
	}
	
	/**
	 * Returns the manager of the company who has the given ID if he exists, null otherwise.
	 * 
	 * @param _managerID	the ID of the manager to look for
	 * @return the manager of the company who has the given ID if he exists, null otherwise
	 */
	public Manager getManagerByID(int _managerID) {
		for (Manager manager : managers) {
			if (manager.getID() == _managerID) {
				return manager;
			}
		}
		return null;
	}
	
	/**
	 * Creates and assigns a new manager as head of a department of the company and returns him.
	 * Note : a manager assigned with this method will be set as one of the managers AND the new head manager of the department.
	 * 
	 * @param _firstname	the firstname of the new manager
	 * @param _lastname		the lastname of the new manager
	 * @param _department	the department of the company
	 * @return the newly created manager
	 * @throws CompanyStructureException	if the department doesn't exist in the company
	 */
	public Manager assignNewHeadManager(String _firstname, String _lastname, Department _department) throws CompanyStructureException {
		Manager newHeadManager = new Manager(generateNewStaffMemberID(), _firstname, _lastname);
		
		if (containsDepartment(_department)) {
			if (!containsStaffMemberWithName(newHeadManager.getName())) {
				managers.add(newHeadManager);
				_department.assignHeadManager(newHeadManager);
				
				return newHeadManager;
			}
			else {
				throw new CompanyStructureException(newHeadManager.getName() + " is already exists in the company.");
			}
		}
		else {
			throw new CompanyStructureException(_department.getName() + " doesn't exist in the company.");
		}
	}
	
	/**
	 * Assigns an existing manager as head of a department of the company.
	 * Note : a manager assigned with this method will be set as one of the managers AND the new head manager of the department.
	 * 
	 * @param _manager		the manager to assign
	 * @param _department	the department the manager is assigned to
	 * @throws CompanyStructureException	if the department isn't in the company or the manager is already managing this department
	 */
	public void assignHeadManager(Manager _manager, Department _department) throws CompanyStructureException {
		if (containsDepartment(_department)) {
			_department.assignHeadManager(_manager);
		}
		else {
			throw new CompanyStructureException(_department.getName() + " doesn't exist in the company.");
		}
	}
	
	/**
	 * Creates and assigns a new manager a department of the company and returns him.
	 * Note : a manager assigned with this method will be ONLY set as one of the managers of the department.
	 * 
	 * @param _firstname	the firstname of the new manager
	 * @param _lastname		the lastname of the new manager
	 * @param _department	the department the manager is assigned to
	 * @return the newly created head manager
	 * @throws CompanyStructureException	if the department isn't in the company or the manager is already in this department
	 */
	public Manager assignNewManager(String _firstname, String _lastname, Department _department) throws CompanyStructureException {
		Manager newManager = new Manager(generateNewStaffMemberID(), _firstname, _lastname);

		if (containsDepartment(_department)) {
			if (!containsStaffMemberWithName(newManager.getName())) {
				managers.add(newManager);
				_department.assignManager(newManager);
				
				return newManager;
			}
			else {
				throw new CompanyStructureException(newManager.getName() + " already exists in the company.");
			}
		}
		else {
			throw new CompanyStructureException(_department.getName() + " doesn't exist in the compnay.");
		}
	}
	
	/**
	 * Assigns an already existing manager a department of the company.
	 * Note : a manager assigned with this method will be ONLY set as one of the managers of the department.
	 * 
	 * @param _manager		the manager to assign
	 * @param _department	the department the manager is assigned to
	 * @throws CompanyStructureException	if the department isn't in the company or the manager is already in this department
	 */
	public void assignManager(Manager _manager, Department _department) throws CompanyStructureException {
		if (containsDepartment(_department)) {
			_department.assignManager(_manager);
		}
		else {
			throw new CompanyStructureException(_department.getName() + " doesn't exist in the company.");
		}
	}
	
	/**
	 * Dismisses a manager from the company
	 * 
	 * @param _manager	the manager to dismiss
	 * @throws CompanyStructureException	if the manager is not in the company or is managing a department
	 */
	public void dismissManager(Manager _manager) throws CompanyStructureException {
		if (containsManager(_manager)) {
			if (_manager.getDepartment() != null) {
				_manager.getDepartment().dismissManager(_manager);
			}
			managers.remove(_manager);
		}
		else {
			throw new CompanyStructureException(_manager.getName() + " doesn't exist in the company.");
		}
	}
	
	/**
	* Demotes the given manager to the role of employee.
	* 
	* @param _manager	the manager to demote
	* @return the manager once demoted to the role of employee
	* @throws CompanyStructureException	if the given manager is not in the company or is currently head of a department
	*/
	public Employee demoteManager(Manager _manager) throws CompanyStructureException {
		if (containsManager(_manager)) {
			Department department = _manager.getDepartment();
			if (!_manager.isHead() || (department.getEmployees().size() + department.getManagers().size() == 1)) {	
				
				Employee employee = new Employee(_manager);
				employees.add(employee);
				dismissManager(_manager);
				
				if (department != null) {
					employee.setDepartment(null);
					assignEmployee(employee, department);
				}

				return employee;
			}
			else {
				throw new CompanyStructureException(_manager.getName() + " can't be demoted : he is head manager in " + department.getName() + ".");
			}
		}
		else {
			throw new CompanyStructureException(_manager.getName() + " doesn't exist in the company.");	
		}
	}
	
	//********************//
	//**	checkings	**//
	//********************//
	
	/**
	 * Creates and returns a new checking to the company checkings.
	 * 
	 * @param _staffMember	the staff member who initiated the checking
	 * @param _context		the context of the checking
	 * @return the new checking
	 */
	public Checking addNewChecking(Employee _staffMember, CheckingContext _context) {
		Checking checking = new Checking(generateNewCheckingID(), _staffMember, _context);
		_staffMember.addChecking(checking);
		
		return checking;
	}
	
	/**
	 * Returns the checkings of the staff.
	 * 
	 * @return the checkings of the staff
	 */
	public ArrayList<Checking> getCheckings() {
		ArrayList<Checking> checkings = new ArrayList<Checking>();

		for (Department department : departments) {
			checkings.addAll(department.getCheckings());
		}
		
		return checkings;
	}
	
	/**
	 * Returns the checkings of the staff on a precise date.
	 * 
	 * @param _date		the effective date of the checkings wanted
	 * @return the checkings of the staff on a precise date.
	 */
	public ArrayList<Checking> getCheckingsByDate(LocalDate _date) {
		ArrayList<Checking> checkings = new ArrayList<Checking>();
		
		for (Department department : departments) {
			checkings.addAll(department.getCheckingsByDate(_date));
		}
		
		return checkings;
	}
	
	/**
	 * Returns the checkings of the staff between two dates.
	 * 
	 * @param _startDate	the start date of the period
	 * @param _endDate		the end date of the period
	 * @return the checkings of the staff between two dates
	 */
	public ArrayList<Checking> getCheckingsBetweenDates(LocalDate _startDate,  LocalDate _endDate) {
		ArrayList<Checking> checkings = new ArrayList<Checking>();
		
		for (Department department : departments) {
			checkings.addAll(department.getCheckingsBetweenDates(_startDate, _endDate));
		}
		
		return checkings;
	}
	
	/**
	 * Returns the checking with the given ID.
	 * 
	 * @param _checkingID		the ID of the checking
	 * @return 	the checking with the given ID
	 * @throws CheckingException	if there is no checking with the given ID
	 */
	public Checking getCheckingByID(int _checkingID) throws CheckingException {
		ArrayList<Checking> checkings = getCheckings();
		
		for (Checking checking : checkings) {
			if (checking.getID() == _checkingID) {
				return checking;
			}
		}
		
		throw new CheckingException(_checkingID + " doesn't reference any checking.");
	}
	
	//****************//
	//**	CSV		**//
	//****************//
	
	/**
	 * Interprets a staff member CSV line.
	 * If the staff member/department encountered doesn't exist, it will try to recruit/add it to the company.
	 * 
	 * @param _line			the line to interpret
	 * @throws CompanyStructureException	if the interpretation threw an error
	 */
	public void interpretStaffMemberCSVLine(String _line) throws CompanyStructureException {
		if (!_line.equals("")) {
			String[] lineComponents = _line.split(",");
			
			Department department = getDepartmentByName(lineComponents[2]);
			if (department == null) {
				department = addNewDepartment(lineComponents[2]);
			}
		
			if (lineComponents[3].equals(Role.MANAGER.toString())) {
				assignNewManager(lineComponents[0], lineComponents[1], department);
			}
			else if (lineComponents[3].equals(Role.HEADMANAGER.toString())) {
				assignNewHeadManager(lineComponents[0], lineComponents[1], department);
				
			}
			else if (lineComponents[3].equals(Role.EMPLOYEE.toString())) {
				assignNewEmployee(lineComponents[0], lineComponents[1], department);
			}
		}
	}
}
