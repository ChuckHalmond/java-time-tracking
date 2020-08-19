package company;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A lightweight structure representing an employee.
 * 
 * The advantage of using such a class is to manipulate only the core information of a staff member in the emulator
 * and during the communications with the application (for example, to handle low memory).
 * 
 * @author Charles MECHERIKI
 *
 */
public class LightweightEmployee implements Serializable {
	private static final long serialVersionUID = 1L;		/** Recommended when implementing Serializable */
	
	private int ID;											/**	ID of the employee	*/
	private String name;									/** Name of the employee */
	private LocalDateTime lastCheckingEffectiveDateTime;	/** Date time of the last checking of the employee */
	private LocalDateTime nextExpectedCheckingDateTime;		/** Expected date time for the next checking of the employee */
	
	public static final LightweightEmployee SELECT_STAFFMEMBER_PLACEHOLDER = new LightweightEmployee("Select a staff member");
	
	/**
	 * Private constructor, only used to create the static placeholder attribute (probably shouldn't exist).
	 * 
	 * @param _name		the name of the employee
	 */
	public LightweightEmployee(String _name) {
		name = _name;
	}
	
	/**
	 * Constructs a lightweight employee from a regular employee.
	 * 
	 * @param _employee		the employee to reduce
	 */
	public LightweightEmployee(Employee _employee) {
		ID = _employee.getID();
		name = _employee.getName();
		lastCheckingEffectiveDateTime = (_employee.getLastChecking() != null) ? _employee.getLastChecking().getContext().getEffectiveDateTime() : null;
		nextExpectedCheckingDateTime = _employee.getNextExpectedCheckingDateTime();
	}
	
	/**
	 * Returns the ID of the employee.
	 * 
	 * @return the ID of the employee
	 */
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns the name of the employee.
	 * 
	 * @return the name of the employee
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the name of the employee.
	 * 
	 * @return	the name of the employee
	 */
	public String toString() {
		return getName();
	}
	
	/**
	 * Returns the last checking effective date time of the employee.
	 * 
	 * @return the last checking effective date time of the employee
	 */
	public LocalDateTime getLastCheckingEffectiveDateTime() {
		return lastCheckingEffectiveDateTime;
	}
	
	
	/**
	 * Returns the next expected checking date time of the employee.
	 * 
	 * @return the next expected checking date time of the employee
	 */
	public LocalDateTime getNextExpectedCheckingDateTime() {
		return nextExpectedCheckingDateTime;
	}
}
