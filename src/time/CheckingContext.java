package time;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Context of a checking, i.e its effective date time, expected date time, status and overtime.
 * A positive overtime means excess hours, whereas a negative overtime means deficit hours.
 * 
 * @author Charles MECHERIKI
 * 
 */
public class CheckingContext implements Serializable {
	private static final long serialVersionUID = 1L;	/** Recommended when implementing Serializable */

	private LocalDateTime effectiveDateTime;	/** Effective date time of the checking */
	private LocalDateTime expectedDateTime;		/** Expected date time of the checking 	*/
	
	private CheckingStatus status;				/** Status of the checking 				*/
	
	private Overtime overtime;					/** Overtime relative to the checking : a negative overtime represents 
												deficit hours, whereas a positive one represents excess hours */

	/**
	 * Creates and initialize a checking
	 * 
	 * @param _effectiveDateTime	the effective date time of the checking
	 * @param _expectedDateTime		the expected date time of the checking
	 * @param _status				the status of the checking
	 * @param _overtime				the overtime of the checking
	 */
	public CheckingContext(LocalDateTime _effectiveDateTime, LocalDateTime _expectedDateTime, CheckingStatus _status, Overtime _overtime) {
		effectiveDateTime = _effectiveDateTime;
		expectedDateTime = _expectedDateTime;
		status = _status;
		overtime = _overtime;
	}
	
	/**
	 * Updates the checking context with the correct values
	 * 
	 * @param _effectiveDateTime	the effective date time of the checking
	 * @param _expectedDateTime		the expected date time of the checking
	 * @param _status				the status of the checking
	 * @param _overtime				the overtime of the checking
	 */
	public void correct(LocalDateTime _effectiveDateTime, LocalDateTime _expectedDateTime, CheckingStatus _status, Overtime _overtime) {
		effectiveDateTime = _effectiveDateTime;
		expectedDateTime = _expectedDateTime;
		status = _status;
		overtime = _overtime;
	}
	
	/**
	 * Returns the information of the checking context.
	 * 
	 * @return the information of the checking context
	 */
	public String toString() {
		return "ActualDateTime : " + DateTimeService.toShortString(effectiveDateTime)
		+ ", ExpectedDateTime : " + DateTimeService.toShortString(expectedDateTime)
		+ ", Overtime : " + overtime.toString()
		+ ", Status : " + status.toString();
	}
	
	/**
	 * Returns the status of the checking.
	 * 
	 * @return the status of the checking
	 */
	public CheckingStatus getStatus() {
		return status;
	}
	
	/**
	 * Returns the effective date time of the checking.
	 * 
	 * @return the effective date time of the checking
	 */
	public LocalDateTime getEffectiveDateTime() {
		return effectiveDateTime;
	}
	
	/**
	 * Returns the expected date time of the checking.
	 * 
	 * @return the expected date time of the checking
	 */
	public LocalDateTime getExpectedDateTime() {
		return expectedDateTime;
	}
	
	/**
	 * Returns the overtime of the checking.
	 * 
	 * @return the overtime of the checking
	 */
	public Overtime getOvertime() {
		return overtime;
	}
}
