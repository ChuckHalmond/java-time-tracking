package time;

import java.io.Serializable;
import java.time.Duration;

/**
 * Class used to faciliate the utilization of overtime in the tables.
 *
 * @author Charles MECHERIKI
 *
 */
public class Overtime implements Comparable<Overtime>, Serializable{
	private static final long serialVersionUID = 1L;	/** Recommended when implementing Serializable */
	
	private Duration duration;							/**	Duration value of the overtime	*/
	
	/**
	 * Creates an overtime from a duration.
	 * 
	 * @param _duration	the duration of the overtime
	 */
	public Overtime(Duration _duration) {
		duration = _duration;
	}
	
	/**
	 * Creates an overtime from a duration.
	 * 
	 * @param _duration	the duration of the overtime
	 * @return the overtime object created from the duration
	 */
	public static Overtime fromDuration(Duration _duration) {
		return new Overtime(_duration);
	}
	
	/**
	 * Returns the duration of the overtime.
	 * 
	 * @return the duration of the overtime
	 */
	public Duration getDuration() {
		return duration;
	}
	
	/**
	 * Returns the duration of the overtime in minutes.
	 * 
	 * @return the duration of the overtime in minutes
	 */
	public long toMinutes() {
		return duration.toMinutes();
	}
	
	/**
	 * Adds a duration to the overtime's current duration.
	 * 
	 * @param _duration	the duration to add
	 */
	public void add(Duration _duration) {
		duration = duration.plus(_duration);
	}
	
	/**
	 * Adds the given overtime's duration to the overtime's current duration.
	 * 
	 * @param _overtime	the overtime whose duration is added
	 */
	public void add(Overtime _overtime) {
		duration = duration.plus(_overtime.getDuration());
	}
	
	/**
	 * Withdraws the given overtime's duration from the overtime's current duration.
	 * 
	 * @param _duration the duration to withdraw
	 */
	public void withdraw(Duration _duration) {
		duration = duration.minus(_duration);
	}
	
	/**
	 * Withdraws the given overtime's duration from the overtime's current duration.
	 * 
	 * @param _overtime	the overtime whose duration is withdrawn
	 */
	public void withdraw(Overtime _overtime) {
		duration = duration.minus(_overtime.getDuration());
	}
	
	/**
	 * Returns the overtime in a small string format.
	 * 
	 * @return the overtime in a small string format
	 */
	public String toString() {
		long hours = duration.abs().toHours();
		long minutes = duration.abs().toMinutes() % 60;
		String string = (duration.isNegative()) ? "-" : "";
		
		if (!duration.isZero()) {
			if (hours > 0) {
				string += hours + "h";
			}
			if (minutes > 0) {
				string += minutes + "m";
			}
		}
		else {
			string = "";
		}

		return string;
	}

	/**
	 * Required to automatically sort the overtime in the tables.
	 */
	@Override
	public int compareTo(Overtime _overtime) {
		return Long.compare(toMinutes(), _overtime.toMinutes());
	}
}
