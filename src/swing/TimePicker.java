package swing;

import java.time.LocalTime;

import javax.swing.JComboBox;

/**
 * A custom time picker, proposing all the quarters of a day.
 *
 * @author Charles MECHERIKI
 *
 */
public class TimePicker extends JComboBox<LocalTime> {
	private static final long serialVersionUID = 1L;

	public TimePicker() {
		for (int i = 0; i < 24; i++) {
			for (int j = 0; j < 60; j+= 15) {
				addItem(LocalTime.of(i, j));
			}
		}
	}
	
	public LocalTime getTime() {
		int[] selectedTime = (int[])getSelectedItem();
		return LocalTime.of(selectedTime[0], selectedTime[1]);
	}
}
