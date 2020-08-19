package tests;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import time.CheckingContext;
import time.CheckingStatus;
import time.DateTimeService;
import toolbox.exceptions.CheckingException;
import toolbox.exceptions.CompanyStructureException;
import company.Company;
import company.Department;
import company.Employee;

/**
 * Just a few tests on the checking system.
 * 
 * @author Charles MECHERIKI
 *
 */
public class CheckingTest {
	private Company williamPikardInc;	/**	The company			*/
	private Employee jean;				/** Jean, an employee 	*/
	
	private LocalDate nextWorkingDay;	/** Next working day of the employee, just after his recruitment */
	private int nextWorkingDayIdx;		/** Index of the next working day (DayOfWeek index) */
	
	private LocalDate nextNextWorkingDay;	/** Next working day after the next working day of the employee */ 
	private int nextNextWorkingDayIdx;		/** Index of the next working day after the next working day (DayOfWeek index) */
	
	/**
	 * Initialiazes the attributes for the tests.
	 */
    @Before
    public void initialization() {
    	williamPikardInc = new Company("WilliamPikardInc", "William", "Pikard");
    	
    	try {
	    	Department logisticDepartment = williamPikardInc.addNewDepartment("Logistic");
	    	jean = williamPikardInc.assignNewEmployee("Jean",  "Bon", logisticDepartment);
    	}
    	catch (CompanyStructureException _exception) {
    		System.out.println("An unexpected error occured.");
    	}

    	LocalDate today = LocalDate.now(DateTimeService.clock);
    	
		nextWorkingDay = (today.getDayOfWeek().getValue() < 5) ? today.plusDays(1) : today.plusDays(8 - today.getDayOfWeek().getValue());
		nextWorkingDayIdx = nextWorkingDay.getDayOfWeek().getValue();
		
		nextNextWorkingDay = (nextWorkingDayIdx < 5) ? nextWorkingDay.plusDays(1) : nextWorkingDay.plusDays(8 - nextWorkingDayIdx);
		nextNextWorkingDayIdx = nextNextWorkingDay.getDayOfWeek().getValue();
    }
   
    /**
     *  Jean checks three times the same day : a CheckingException should be thrown
     */
	@Test
	public void testThreeCheckingsADay_checkingExceptionThrown() {
        try {
        	jean.generateCheckingContext(LocalDateTime.of(nextWorkingDay, jean.getScheduleEntry(nextWorkingDayIdx)[0]));
        	jean.generateCheckingContext(LocalDateTime.of(nextWorkingDay, jean.getScheduleEntry(nextWorkingDayIdx)[0].plusHours(5)));
        	jean.generateCheckingContext(LocalDateTime.of(nextWorkingDay, jean.getScheduleEntry(nextWorkingDayIdx)[1]));
        	
        	Assert.assertTrue(false);
        }
        catch (CheckingException e) {
        	Assert.assertTrue(true);
        }
        catch (Exception e) {
        	Assert.assertTrue("Something went wrong : " + e.getMessage(), false);
        }
	}
	
    /**
     *  Jean comes early but leaves early : he has no deficit or excess hours.
     */
	@Test
	public void testDeficitHoursPlusExcessHours_overtimeEqualsZero() {
		try {
			CheckingContext checkingContext = null;
			
			checkingContext = jean.generateCheckingContext(LocalDateTime.of(nextWorkingDay, jean.getScheduleEntry(nextWorkingDayIdx)[0].minusMinutes(25)));
			williamPikardInc.addNewChecking(jean, checkingContext);
			
			checkingContext = jean.generateCheckingContext(LocalDateTime.of(nextWorkingDay, jean.getScheduleEntry(nextWorkingDayIdx)[1].minusMinutes(35)));
			williamPikardInc.addNewChecking(jean, checkingContext);
			
        	Assert.assertTrue(jean.getOvertime().getDuration() == Duration.ZERO);
        } catch (Exception e) {
        	Assert.assertTrue("Something went wrong : " + e.getMessage(), false);
        }
	}
	
    /**
     *  Jean missed one entire day of work : he has a negative overtime of the duration of the day missed.
     */
	@Test
	public void testMissingOneWholeDay_overtimeEqualsTheDayLengthNegated() {
		try {
			CheckingContext checkingContext = null;
			
			// JeanBon didn't come the next working day; but only the day after
			
			checkingContext = jean.generateCheckingContext(LocalDateTime.of(nextNextWorkingDay, jean.getScheduleEntry(nextNextWorkingDayIdx)[0]));
			williamPikardInc.addNewChecking(jean, checkingContext);
			
			Duration durationOfTheMissedDay = Duration.between(jean.getScheduleEntry(nextWorkingDayIdx)[0], jean.getScheduleEntry(nextWorkingDayIdx)[1]);
 
			Assert.assertTrue(jean.getOvertime().getDuration().negated().equals(durationOfTheMissedDay));
        } catch (Exception e) {
        	Assert.assertTrue("Something went wrong : " + e.getMessage(), false);
        }
	}
	
    /**
     *  Jean forgot to check out the first day and asked to correct it : his check is corrected.
     */
	@Test
	public void testCorrectMissingCheckOut_overtimeEqualsZero() {
		try {
			CheckingContext checkingContext = null;
			
			checkingContext = jean.generateCheckingContext(LocalDateTime.of(nextWorkingDay, jean.getScheduleEntry(nextWorkingDayIdx)[0]));
			williamPikardInc.addNewChecking(jean, checkingContext);
			
			// Jean realises only the next morning that he forgot to check
			
			checkingContext = jean.generateCheckingContext(LocalDateTime.of(nextNextWorkingDay, jean.getScheduleEntry(nextNextWorkingDayIdx)[0]));
			williamPikardInc.addNewChecking(jean, checkingContext);
			
			// Jean made it corrected
			
			jean.correctCheckingContext(checkingContext, LocalDateTime.of(nextWorkingDay, jean.getScheduleEntry(nextWorkingDayIdx)[1]));
			
        	Assert.assertTrue(jean.getOvertime().getDuration() == Duration.ZERO && checkingContext.getStatus() == CheckingStatus.AT_TIME_LEAVING);
        } catch (Exception e) {
        	Assert.assertTrue("Something went wrong : " + e.getMessage(), false);
        }
	}
	
    /**
     *  Tests the methods supposed to retrieve checkings partially (from department, between dates, etc.).
     */
	@Test
	public void testGetPartialCheckings_partialCheckingsCorrectlyRetrieved() {
		try {
			boolean bool1, bool2, bool3, bool4, bool5;
			CheckingContext checkingContext = null;

			/* We add a department and an employee to the company */
			Department qualityDepartment =  williamPikardInc.addNewDepartment("Qualité");
			Employee guy =  williamPikardInc.assignNewEmployee("Guy", "Ligili", qualityDepartment);
			
			/* Guy, from the quality department, checks once the next working day */
			checkingContext = guy.generateCheckingContext(LocalDateTime.of(nextWorkingDay, guy.getScheduleEntry(nextWorkingDayIdx)[0]));
			williamPikardInc.addNewChecking(guy, checkingContext);

			/* Jean, from the quality department, checks twice the next working day and once the day after */
			checkingContext = jean.generateCheckingContext(LocalDateTime.of(nextWorkingDay, jean.getScheduleEntry(nextWorkingDayIdx)[0]));
			williamPikardInc.addNewChecking(jean, checkingContext);
			checkingContext = jean.generateCheckingContext(LocalDateTime.of(nextNextWorkingDay, jean.getScheduleEntry(nextNextWorkingDayIdx)[0]));
			williamPikardInc.addNewChecking(jean, checkingContext);
			checkingContext = jean.generateCheckingContext(LocalDateTime.of(nextNextWorkingDay, jean.getScheduleEntry(nextNextWorkingDayIdx)[1]));
			williamPikardInc.addNewChecking(jean, checkingContext);
			
			bool1 = williamPikardInc.getCheckings().size() == 4;
			bool3 = williamPikardInc.getCheckingsByDate(nextWorkingDay).size() == 2;
			bool4 = williamPikardInc.getCheckingsBetweenDates(nextWorkingDay, nextNextWorkingDay).size() == 4;
			bool2 = qualityDepartment.getCheckings().size() == 1;
			bool5 = guy.getCheckings().size() == 1;
			
			Assert.assertTrue(bool1 && bool2 && bool3 && bool4 && bool5);
		}
		catch (Exception e) {
			Assert.assertTrue("Something went wrong : " + e.getMessage(), false);
		}
	}
}
