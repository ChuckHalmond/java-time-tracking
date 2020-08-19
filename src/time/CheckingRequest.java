package time;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A checking request, emitted from the emulator whenever a staff member checks.
 * A checking request is then processed by the application as a real checking.
 *
 * @author Charles MECHERIKI
 *
 */
public class CheckingRequest implements Serializable {
	private static final long serialVersionUID = 1L;	/** Recommended when implementing Serializable */

	private int staffMemberID;							/** ID of the staff member who did the checking */
	private LocalDateTime checkingDateTime;				/** Date time of the checking 					*/
	
	/**
	 * Creates an initialize a checking.
	 * 
	 * @param _staffMemberID		the ID of the staff member who did the checking
	 * @param _checkingDateTime		date time of the checking
	 */
	public CheckingRequest(int _staffMemberID, LocalDateTime _checkingDateTime) {
		staffMemberID = _staffMemberID;
		checkingDateTime = _checkingDateTime;
	}
	
	/** 
	 * Returns the ID of the staff member who did the checking.
	 * 
	 * @return the ID of the staff member who did the checking
	 */
	public int getStaffMemberID() {
		return staffMemberID;
	}
	
	/**
	 * Returns the date time of the cheking.
	 * 
	 * @return the date time of the cheking
	 */
	public LocalDateTime getCheckingDateTime() {
		return checkingDateTime;
	}
}
