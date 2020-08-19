package company;

import java.io.Serializable;

/**
 * A manager is a specialized employee, who can manage (i.e be head manager of) a department.
 * Only one manager is head manager per department, and there should always be one.
 * 
 * A head manager keep his position until he is replaced by another, or until he is the only left in his department.
 * 
 * @author MECHERIKI Charles
 * 
 */
public class Manager extends Employee implements Serializable {
	private static final long serialVersionUID = 1L;	/** Recommended when implementing Serializable */
	
	private boolean isHead;			/** Boolean indicating if the manager his head of his department */
	
	/**
	 * Constructs a manager object with his identity and schedule (using the Employee constructor)
	 * 
	 * @param _ID			the ID of the manager
	 * @param _firstname 	the firstname of the manager
	 * @param _lastname 	the lastname of the manager
	 */
	public Manager(int _ID, String _firstname, String _lastname) {
		super(_ID, _firstname, _lastname, Role.MANAGER);
		isHead = false;
	}
	
	/**
	* Creates a manager from a given employee.
	*
	* @param _employee	the employee to copy
	*/
	public Manager(Employee _employee) {
		super(_employee.ID, _employee.firstname, _employee.lastname);
		
		department = _employee.department;
		role = Role.MANAGER;
		isHead = false;
		
		schedule = _employee.schedule;
		overtime = _employee.overtime;
		checkings = _employee.checkings;

		nextExpectedCheckingDateTime = _employee.nextExpectedCheckingDateTime;
		previousExpectedCheckingDateTime = _employee.previousExpectedCheckingDateTime;
	}
	
	
	/**
	* Sets whether the manager is head of his department.
	* 
	* @param _isHead	if the manager is head of his department or not 
	*/
	public void isHead(boolean _isHead) {
		isHead = _isHead;
	}

	/**
	* Returns whether the manager is head of his department.
	* 
	* @return whether the manager is head of his department.
	*/
	public boolean isHead() {
		return isHead;
	}
}