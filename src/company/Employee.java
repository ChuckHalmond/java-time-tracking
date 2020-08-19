package company;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;

import time.DateTimeService;
import time.Overtime;
import time.Checking;
import time.CheckingContext;
import time.CheckingStatus;

import toolbox.exceptions.CheckingException;

/**
 * An employee is the most basic role for a staff member.
 * Every staff member of the company is an employee.
 * 
 * The different staff roles are employee, manager and head manager.
 * 
 * An employee has to check every working day accordingly to his schedule.
 * An employee can have excess or deficit hours, considered respectively as positive and negative overtime.
 * 
 * @author Charles MECHERIKI
 *
 */
public class Employee extends Person implements Serializable {
	private static final long serialVersionUID = 1L;	/** Recommended when implementing Serializable */

	protected int ID;									/** ID of the employee */

	protected Department department;					/** Department of the employee */

	protected Overtime overtime;						/** Overtime of the employee : a negative overtime represents deficit hours, 
															whereas a positive one represents excess hours */
	protected LocalTime[][] schedule;					/** Schedule of the employee */
														
	protected Role role;								/** The role of the employee (employee or manager or head manager) */
	
	public static LocalTime[][] defaultSchedule = {		/** Default schedule for every employee */
		{LocalTime.of(8, 30, 0), LocalTime.of(17, 0, 0)},
		{LocalTime.of(8, 30, 0), LocalTime.of(17, 0, 0)},
		{LocalTime.of(8, 30, 0), LocalTime.of(17, 0, 0)},
		{LocalTime.of(8, 30, 0), LocalTime.of(17, 0, 0)},
		{LocalTime.of(8, 30, 0), LocalTime.of(17, 0, 0)}
	};
	
	protected ArrayList<Checking> checkings;			/** Checkings of the employee */
	
	protected LocalDateTime nextExpectedCheckingDateTime;		/** Next expected checking date time : helps in determining the checking conditions */
	protected LocalDateTime previousExpectedCheckingDateTime;	/** Previous expected checking date time : helps in determining the checking conditions */

	public static final Employee SELECT_STAFFMEMBER_PLACEHOLDER = new Employee(-1, "Select a", "staff member");	/** Placeholder used inside the views */
	public static final Employee ALL_STAFF_PLACEHOLDER = new Employee(-1, "All the", "staff");					/** Placeholder used inside the views */
	
	//************************//
	//**	constructors	**//
	//************************//
	
	/**
	* Creates a staff member and initializes its attributes.
	* 
	* @param _ID		the ID of the staff member
	* @param _firstname	the firstname of the staff member
	* @param _lastname	the lastname of the staff member
	* @param _role		the role of the staff member
	*/
	public Employee(int _ID, String _firstname, String _lastname, Role _role) {
		super(_firstname, _lastname);

		ID = _ID;
		role = _role;
		department = null;
		
		schedule = defaultSchedule.clone();
		overtime = new Overtime(Duration.ZERO);
		checkings = new ArrayList<Checking>();

		initializeCheckingAttributes();
	}
	
	/**
	* Creates an employee and initializes its attributes.
	* 
	* @param _ID		the ID of the employee	
	* @param _firstname	the firstname of the employee
	* @param _lastname	the lastname of the employee
	*/
	public Employee(int _ID, String _firstname, String _lastname) {
		this(_ID, _firstname, _lastname, Role.EMPLOYEE);
	}
	
	/**
	* Creates an employee from a given manager.
	*
	* @param _manager	the manager to transform in employee
	*/
	public Employee(Manager _manager) {
		super(_manager.firstname, _manager.lastname);

		ID = _manager.ID;
		
		department = _manager.department;
		role = Role.EMPLOYEE;
		
		schedule = _manager.schedule;
		overtime = _manager.overtime;
		checkings = _manager.checkings;

		nextExpectedCheckingDateTime = _manager.nextExpectedCheckingDateTime;
		previousExpectedCheckingDateTime = _manager.previousExpectedCheckingDateTime;
	}
	
	/**
	 * Returns the employee but stored in a lightweight data structure (used in the emulator).
	 * 
	 * @return the lightweight employee
	 */
	public LightweightEmployee lightweight() {
		return new LightweightEmployee(this);
	}
	
	//************//
	//**	ID	**//
	//************//
	
	/**
	* Returns the ID of the employee.
	* 
	* @return the ID of the employee
	*/
	public int getID() {
		return ID;
	}
	
	//****************//
	//**	role	**//
	//****************//
	
	/**
	* Returns the role of the employee.
	* 
	* @return the role of the employee
	*/
	public Role getRole() {
		return role;
	}
	
	/**
	* Sets the role of the employee
	* Note : This method should only be used from the company during an assignment.
	* 
	* @param _role	the role
	*/
	public void setRole(Role _role) {
		role = _role;
	}
	
	
	//********************//
	//**	department	**//
	//********************//
	
	
	/**
	* Returns the department of the employee.
	* 
	* @return the department of the employee
	*/
	public Department getDepartment() {
		return department;
	}
	
	/**
	 * Returns true if the employee is working in the given department, false otherwise
	 * 
	 * @param _department	the department
	 * @return true if the employee is working in the given department, false otherwise 
	 */
	public boolean isWorkingIn(Department _department) {
		return department == _department;
	}

	/**
	* Assigns the employee a department
	* 
	* @param _department	the department for the employee
	*/
	public void setDepartment(Department _department) {
		this.department = _department;
	}

	//********************//
	//**	schedule	**//
	//********************//

	/**
	 * Returns the schedule of the employee.
	 * 
	 * @return the schedule of the employee
	 */
	public LocalTime[][] getSchedule() {
		return schedule;
	}
	
	/**
	 * Returns the employee's schedule entry for the given day.
	 * 
	 * @param _dayOfWeekValue	the day of the schedule entry
	 * @return the employee's schedule entry for the given day.
	 */
	public LocalTime[] getScheduleEntry(int _dayOfWeekValue) {
		return schedule[_dayOfWeekValue - 1];
	}
	
	/**
	 * Changes the entire schedule of an employee.
	 * 
	 * @param _schedule	the new schedule of the employee
	 */
	public void changeSchedule(LocalTime[][] _schedule) {
		schedule = _schedule;
	}

	/**
	 * Changes the employee's schedule entry for the given day.
	 * 
	 * @param _dayOfWeekIdx	the day of the schedule entry
	 * @param _scheduleEntry	the value for the schedule entry
	 */
	public void changeScheduleEntry(int _dayOfWeekIdx, LocalTime[] _scheduleEntry) {
		schedule[_dayOfWeekIdx - 1] = _scheduleEntry;
	}
	
	//********************//
	//**	checkings	**//
	//********************//
	

	/**
	 * Returns the overtime of the employee. A negative overtime represents deficit hours, whereas
	 * a positive one represents excess hours.
	 * 
	 * @return	the overtime of the employee
	 */
	public Overtime getOvertime() {
		return overtime;
	}

	/**
	 * Returns the next expected checking date time of the employee.
	 * 
	 * @return the next expected checking date time of the employee
	 */
	public LocalDateTime getNextExpectedCheckingDateTime() {
		return nextExpectedCheckingDateTime;
	}
	
	/**
	* Initializes the previous next expected checking date time and the next expected checking date time.
	* Note : the employee is supposed to start work the next working day after his hiring (i.e his creation).
	*/
	public void initializeCheckingAttributes() {
		LocalDate hiringDate = LocalDate.now(DateTimeService.clock);
		
		int hiringDayOfWeekValue = hiringDate.getDayOfWeek().getValue();
		
		if (hiringDayOfWeekValue < 5) {
			nextExpectedCheckingDateTime = LocalDateTime.of(hiringDate.plusDays(1), getScheduleEntry(hiringDayOfWeekValue + 1)[0]);
			previousExpectedCheckingDateTime = LocalDateTime.now(DateTimeService.clock);
		}
		else {
			nextExpectedCheckingDateTime = LocalDateTime.of(hiringDate.plusDays(8 - hiringDayOfWeekValue), getScheduleEntry(1)[0]);
			previousExpectedCheckingDateTime = LocalDateTime.now(DateTimeService.clock);
		}
		
	}
	
	/**
	 * Adds a checking to the employee's checkings
	 * 
	 * @param _checking	the checking to add
	 */
	public void addChecking(Checking _checking) {
		checkings.add(_checking);
	}
	
	/**
	 * Returns all the checkings of the employee.
	 * 
	 * @return all the checkings of the employee
	 */
	public ArrayList<Checking> getCheckings() {
		return checkings;
	}
	
	/**
	 * Returns the last checking of the employee, and null if the employee hasn't checked yet.
	 * 
	 * @return the last checking of the employee, and null if the employee hasn't checked yet
	 */
	public Checking getLastChecking() {
		if (checkings.size() > 0) {
			return checkings.get(checkings.size() - 1);
		}
		return null;
	}
	
	/**
	 * Returns the checkings of the employee on a precise date.
	 * 
	 * @param _date	the date of the checkings
	 * @return the checkings of the employee on a precise date
	 */
	public ArrayList<Checking> getCheckingsByDate(LocalDate _date) {
		ArrayList<Checking> tempCheckings = new ArrayList<Checking>();
		
		for (Checking checking : checkings) {
			if (checking.isOn(_date)) {
				tempCheckings.add(checking);
			}
		}
		
		return tempCheckings;
	}
	
	/**
	 * Returns the checkings of the employee between two dates.
	 * 
	 * @param _startDate		the start date of the period
	 * @param _endDate		the end date of the period
	 * @return the checkings of the employee between two dates
	 */
	public ArrayList<Checking> getCheckingsBetweenDates(LocalDate _startDate, LocalDate _endDate) {
		ArrayList<Checking> tempCheckings = new ArrayList<Checking>();
		
		for (Checking checking : checkings) {
			if (checking.isBetween(_startDate, _endDate)) {
				tempCheckings.add(checking);
			}
		}
		
		return tempCheckings;
	}
	
	/**
	 * Returns the checking with the given ID.
	 * 
	 * @param _checkingID	the id of the checking
	 * @return the checking with the given ID
	 * @throws CheckingException	if no checking has the given ID
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
	
	/**
	* Generates a checking context for the employee at a certain date time.
	* This method determines if the employee was supposed to check at this moment (according to his schedule and previous checkings), 
	* and updates its different date time attributes and total overtime.
	* 
	* @param _checkingDateTime the date time of the checking context
	* @return the checking context generated
	* @throws CheckingException	if the employee wasn't supposed to check (i.e during the week-end or after having already checked out)
	*/
	public CheckingContext generateCheckingContext(LocalDateTime _checkingDateTime) throws CheckingException {
		
		// Preparing all the information we'll need
		_checkingDateTime = DateTimeService.roundAtQuarter(_checkingDateTime);
		
		LocalDate checkingDate = _checkingDateTime.toLocalDate();
		LocalTime checkingTime = _checkingDateTime.toLocalTime();
		int checkingDateDayOfWeekValue = _checkingDateTime.getDayOfWeek().getValue();
		
		LocalDate nextExpectedCheckingDate = nextExpectedCheckingDateTime.toLocalDate();
		LocalTime nextExpectedCheckingTime = nextExpectedCheckingDateTime.toLocalTime();
		int nextExpectedCheckingDayOfWeekValue = nextExpectedCheckingDateTime.getDayOfWeek().getValue();

		LocalDate previousCheckingDate = previousExpectedCheckingDateTime.toLocalDate();

		Duration duration = Duration.ZERO;
		CheckingStatus status;

		/* 
		 * If it's a working day and the next checking is expected for tomorrow or later, the employee can check
		 */
		if (checkingDateDayOfWeekValue <= 5 && nextExpectedCheckingDate.isBefore(checkingDate) || nextExpectedCheckingDate.equals(checkingDate)) {

			previousExpectedCheckingDateTime = nextExpectedCheckingDateTime;

			LocalDate tempDate = nextExpectedCheckingDate;
			int tempDateDayOfWeekValue = tempDate.getDayOfWeek().getValue();
			
			/* Determination of the 'working' delay between the actual datetime and the expected datetime */
			if (nextExpectedCheckingDate.isBefore(checkingDate)) {
				while (tempDate.isBefore(checkingDate)) {
					if (tempDateDayOfWeekValue <= 5) { // Working days
						duration = duration.plus(Duration.between(getScheduleEntry(tempDateDayOfWeekValue)[0], getScheduleEntry(tempDateDayOfWeekValue)[1]));
					}
					tempDate = tempDate.plusDays(1);
					tempDateDayOfWeekValue = (tempDateDayOfWeekValue == 7) ? 1 : tempDateDayOfWeekValue + 1;
				}
			}
			duration = duration.plus((Duration.between(nextExpectedCheckingTime, checkingTime)));
			
			/* 
			 * Missed last day's checking case : the next checking's date was expected before today and was an evening
			 */
			if (nextExpectedCheckingDate.isBefore(checkingDate) && nextExpectedCheckingTime.equals(getScheduleEntry(nextExpectedCheckingDate.getDayOfWeek().getValue())[1])) {
				
				duration = Duration.ZERO;
				
				status = CheckingStatus.UNKNOWN_LEAVING;
				
				nextExpectedCheckingDateTime = LocalDateTime.of(checkingDate, getScheduleEntry(checkingDateDayOfWeekValue)[0]);
			}
			/* 
			 * Regular arrival case : the last expected checking's date was before today
			 */
			else if (previousCheckingDate.isBefore(checkingDate)) {

				duration = duration.negated();
				
				if (duration.isNegative()) {
					status = CheckingStatus.LATE_ARRIVAL;
				}
				else if (!duration.isZero()){
					status = CheckingStatus.EARLY_ARRIVAL;
				}
				else {
					status = CheckingStatus.AT_TIME_ARRIVAL;
				}
				
				nextExpectedCheckingDateTime = LocalDateTime.of(checkingDate, getScheduleEntry(nextExpectedCheckingDayOfWeekValue)[1]);
			}
			/* 
			 * Regular leaving case : the last expected checking's date was today 
			 */
			else {
				if (checkingDateDayOfWeekValue == 5) { // Next Monday
					nextExpectedCheckingDateTime = LocalDateTime.of(checkingDate.plusDays(3), getScheduleEntry(1)[0]);
				}
				else { // Tomorrow
					nextExpectedCheckingDateTime = LocalDateTime.of(checkingDate.plusDays(1), getScheduleEntry(nextExpectedCheckingDayOfWeekValue + 1)[0]);
				}
	
				if (duration.isNegative()) {
					status = CheckingStatus.EARLY_LEAVING;
				}
				else if (!duration.isZero()){
					status = CheckingStatus.LATE_LEAVING;
				}
				else {
					status = CheckingStatus.AT_TIME_LEAVING;
				}
			}

			overtime.add(duration);
			
			return new CheckingContext(_checkingDateTime, previousExpectedCheckingDateTime, status, Overtime.fromDuration(duration));
		}
		else {
			throw new CheckingException("Unexpected checking.");
		}
	}

	/**
	 * Correct the given checking context of the employee, updating all its information accordingly.
	 * 
	 * @param _checkingContext	the checking context to correct
	 * @param _correctDateTime	the correct date time for the checking
	 */
	public void correctCheckingContext(CheckingContext _checkingContext, LocalDateTime _correctDateTime) {
		_correctDateTime = DateTimeService.roundAtQuarter(_correctDateTime);

		if (_checkingContext != null) {
			LocalDateTime expectedDateTime = _checkingContext.getExpectedDateTime();

			Duration correctOvertimeDuration = Duration.between(expectedDateTime, _correctDateTime);
			CheckingStatus correctStatus;

			if (_checkingContext.getStatus().isLeaving()) {
				correctStatus = (correctOvertimeDuration.isNegative()) ? CheckingStatus.EARLY_LEAVING : (!correctOvertimeDuration.isZero()) ? CheckingStatus.LATE_LEAVING : CheckingStatus.AT_TIME_LEAVING;
			}
			else {
				correctOvertimeDuration = correctOvertimeDuration.negated();
				correctStatus = (correctOvertimeDuration.isNegative()) ? CheckingStatus.LATE_ARRIVAL : (!correctOvertimeDuration.isZero()) ? CheckingStatus.EARLY_ARRIVAL : CheckingStatus.AT_TIME_ARRIVAL;
			}
			
			overtime.withdraw(_checkingContext.getOvertime());
			overtime.add(correctOvertimeDuration);
			
			_checkingContext.correct(_correctDateTime, expectedDateTime, correctStatus, Overtime.fromDuration(correctOvertimeDuration));
		}
	}
	
	//****************//
	//**	CSV		**//
	//****************//
	
	/**
	 * Returns the CSV line representing the staff member.
	 * Only the firstname, lastname, department and role are saved.
	 * 
	 * @return the CSV line representing the staff member.
	 */
	public String toCSVLine() {
		String line = "";
		
		line += getFirstname() + ",";
		line += getLastname() + ",";
		line += getDepartment().getName() + ",";
		line += getRole().toString();

		return line;
	}
}
