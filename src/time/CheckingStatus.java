package time;

/**
 * Enumeration for the different checkings status.
 * 
 * @author Charles MECHERIKI
 * 
 */
public enum CheckingStatus {
	AT_TIME_ARRIVAL,	/** An at time arrival checking */
	AT_TIME_LEAVING,	/** An at time leaving checking */
	EARLY_ARRIVAL,		/** An early arrival checking 	*/
	EARLY_LEAVING,		/** An early leaving checking	*/
	LATE_ARRIVAL,		/** A late arrival checking		*/
	LATE_LEAVING,		/** A late leaving checking 	*/
	UNKNOWN_LEAVING;	/** An unknown leaving checking	*/

	/**
	 * Returns true if the status is early, false otherwise.
	 * 
	 * @return true if the status is early, false otherwise
	 */
    public boolean isEarly() {
    	return this == EARLY_ARRIVAL || this == EARLY_LEAVING;
    }
    
	/**
	 * Returns true if the status is late, false otherwise.
	 * 
	 * @return true if the status is late, false otherwise
	 */
    public boolean isLate() {
    	return this == LATE_ARRIVAL || this == LATE_LEAVING;
    }
    
	/**
	 * Returns true if the status is at time, false otherwise.
	 * 
	 * @return true if the status is at time, false otherwise
	 */
    public boolean isAtTime() {
        return this == AT_TIME_ARRIVAL || this == AT_TIME_LEAVING;
    }
    
	/**
	 * Returns true if the status is an arrival, false otherwise.
	 * 
	 * @return true if the status is an arrival, false otherwise
	 */
    public boolean isArrival() {
    	return this == AT_TIME_ARRIVAL || this == EARLY_ARRIVAL || this == LATE_ARRIVAL;
    }
    
	/**
	 * Returns true if the status is a leaving, false otherwise.
	 * 
	 * @return true if the status is a leaving, false otherwise
	 */
    public boolean isLeaving() {
    	return this == AT_TIME_LEAVING || this == EARLY_LEAVING || this == LATE_LEAVING || this == UNKNOWN_LEAVING;
    }
}
