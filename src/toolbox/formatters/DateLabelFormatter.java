package toolbox.formatters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFormattedTextField.AbstractFormatter;

/**
 * A date label formatter, required by the JDatePicker components to work correctly.
 *
 * @author Charles MECHERIKI
 *
 */
public class DateLabelFormatter extends AbstractFormatter {
	private static final long serialVersionUID = 1L;

	private String datePattern = "yyyy-MM-dd";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    @Override
    public Object stringToValue(String _text) throws ParseException {
        return dateFormatter.parseObject(_text);
    }

    @Override
    public String valueToString(Object _value) throws ParseException {
        if (_value != null) {
            return dateFormatter.format(((Calendar)_value).getTime());
        }
        return "";
    }

}
