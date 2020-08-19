package time;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * A few utility methods for formatting dates, times, and durations.
 * 
 * @author Charles MECHERIKI
 *
 */
public class DateTimeService {
	
	public static Clock clock = Clock.systemDefaultZone();		// Clock used to store an artificial "now" date time
	
	/**
	 * Sets the artificial clock according to the date time given.
	 * 
	 * @param _dateTime	the date time wanted for the clock
	 */
	public static void setClock(LocalDateTime _dateTime) {
		clock = Clock.fixed(_dateTime.toInstant(ZoneOffset.UTC), Clock.systemDefaultZone().getZone());
	}
	
	/**
	 * Resets the clock attribute to its original value : now.
	 */
	public static void setClockToDefault() {
		clock = Clock.systemDefaultZone();
	}
	
	/**
	 * Truncate the date time given to the closest quarter.
	 * 
	 * @param _dateTime	the date time to truncate
	 * @return the date time truncated to the closest quarter
	 */
	public static LocalDateTime roundAtQuarter(LocalDateTime _dateTime) {
		int minutes = _dateTime.getMinute();
		minutes = (minutes % 15 > 7) ? 15 * (minutes / 15) + 15 : 15 * (minutes / 15);

		return _dateTime.truncatedTo(ChronoUnit.HOURS).plusMinutes(minutes);
	}
	
	/**
	 * Returns the given date time in a short format.
	 * 
	 * @param _dateTime	the date time
	 * @return the date time formatted
	 */
	public static String toShortString(LocalDateTime _dateTime) {
		return _dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm").withLocale(Locale.ENGLISH));
	}
	
	/**
	 * Returns the given date time in a long format.
	 * 
	 * @param _dateTime	the date time
	 * @return the date time formatted
	 */
	public static String toLongString(LocalDateTime _dateTime) {
		return _dateTime.format(DateTimeFormatter.ofPattern("EEEE d MMMM yyyy, HH:mm").withLocale(Locale.ENGLISH));
	}
	
	/**
	 * Returns the given date in a particular format.
	 * 
	 * @param _date	the date
	 * @return the date formatted
	 */
	public static String toString(LocalDate _date) {
		return _date.format(DateTimeFormatter.ofPattern("EEEE d MMMM yyyy").withLocale(Locale.ENGLISH));
	}
	
	/**
	 * Returns the given time in a particular format.
	 * 
	 * @param _time	the time
	 * @return the time formatted
	 */
	public static String toString(LocalTime _time) {
		return _time.format(DateTimeFormatter.ofPattern("HH:mm").withLocale(Locale.FRENCH));
	}
}
