import java.time.LocalDateTime;

import time.DateTimeService;

/**
 * Main class for the application.
 * 
 * @author Charles MECHERIKI
 *
 */
public class ApplicationMain {
	
	/**
	 * Launches the application.
	 * 
	 * If the user wants to set an alternative date time to be considered as the application's 'now' date time, 
	 * the year, month, day of month, hour and minute have to be passed as arguments to the program.
	 * 
	 * @param _args	the year, month, day of month, hour and minute to consider for the 'now' date time
	 */
	public static void main(String[] _args) {
		
		if (_args.length > 0) {
			try {
				int year = Integer.parseInt(_args[0]);
				int month = Integer.parseInt(_args[1]);
				int dayOfMonth = Integer.parseInt(_args[2]);
				int hour = Integer.parseInt(_args[3]);
				int minute = Integer.parseInt(_args[4]);
				
				LocalDateTime now = LocalDateTime.of(year, month, dayOfMonth, hour, minute);
				
				DateTimeService.setClock(now);	// Sets the given date time as "now" in the Application
			}
			catch (Exception _exception) {
				System.out.println("Argument parsing failed : no custom 'now' date time considered.");
			}
		}
		
		new Application();
	}
}