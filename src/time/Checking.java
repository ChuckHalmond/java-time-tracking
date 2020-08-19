package time;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import company.Employee;

/**
 * The class representing a checking of a staff member.
 * All the information of the checking, except its ID and its owner, are stored in its context.
 * 
 * @author Charles MECHERIKI
 * 
 */
public class Checking implements Serializable {
	private static final long serialVersionUID = 1L; /** Recommended when implementing Serializable */

	private int ID;								/** ID of the checking 												*/
	private Employee staffMember;				/** Staff member who did the checking 								*/
	private CheckingContext context;			/** Context of the checking (expected / effective date time, etc.) 	*/

	/**
	 * Creates and initialize a checking.
	 * 
	 * @param _ID			the ID of the checking
	 * @param _staffMember	the staff member who did the checking
	 * @param _context		the context of the checking
	 */
	public Checking(int _ID, Employee _staffMember, CheckingContext _context) {
		ID = _ID;
		staffMember = _staffMember;
		context = _context;
	}
	
	/**
	 * Updates the checking with the correct values.
	 * 
	 * @param _effectiveDateTime	the effective date time of the checking
	 * @param _status			the status of the checking
	 * @param _expectedDateTime	the expected date time of the checking
	 * @param _overtime			the overtime of the checking
	 */
	public void correctCheckingContext(LocalDateTime _effectiveDateTime, LocalDateTime _expectedDateTime, CheckingStatus _status, Overtime _overtime) {
		context.correct(_effectiveDateTime, _expectedDateTime, _status, _overtime);
	}
	
	/**
	 * Returns the information of the checking.
	 * 
	 * @return the information of the checking
	 */
	public String toString() {
		return "ID : " + ID
		+ ", StaffMember : " + staffMember.getName()
		+ ", " + context.toString();
	}
	
	/**
	 * Returns the ID of the checking.
	 * 
	 * @return the ID of the checking
	 */
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns the staff member who did the checking.
	 * 
	 * @return the staff member who did the checking
	 */
	public Employee getStaffMember() {
		return staffMember;
	}
	
	/**
	 * Returns the context of the checking.
	 * 
	 * @return the context of the checking
	 */
	public CheckingContext getContext() {
		return context;
	}
	
	/**
	 * Returns true if the actual date time of the checking was on the given date, false otherwise.
	 * 
	 * @param _date	the date to check for
	 * @return true if the checking was on the date given, false otherwise
	 */
	public boolean isOn(LocalDate _date) {
		return context.getEffectiveDateTime().toLocalDate().equals(_date);
	}
	
	/**
	 * Returns true if the effective date time of the checking was between the given dates, false otherwise.
	 * 
	 * @param _startDate	the start date of the period
	 * @param _endDate	the end date of the period
	 * @return true if the effective date time of the checking was between the given dates, false otherwise
	 */
	public boolean isBetween(LocalDate _startDate, LocalDate _endDate) {
		return context.getEffectiveDateTime().isAfter(LocalDateTime.of(_startDate, LocalTime.of(00, 00)))
				&& context.getEffectiveDateTime().isBefore(LocalDateTime.of(_endDate, LocalTime.of(23, 59)));
	}

}
