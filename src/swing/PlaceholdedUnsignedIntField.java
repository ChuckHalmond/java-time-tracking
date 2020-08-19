package swing;

/**
 * A placeholded text field which returns a specific result if its value is not an unsigned integer. 
 *
 * @author Charles MECHERIKI
 *
 */
public class PlaceholdedUnsignedIntField extends PlaceholdedTextField {
	private static final long serialVersionUID = 1L;

	public PlaceholdedUnsignedIntField(String _placeholder) {
		super(_placeholder);
	}
	
	public int getValue() {
		String text = super.getText();
		if (text != "") {
			try {
				return Integer.parseInt(text);
			}
			catch (NumberFormatException _numberFormatException) {
				return -1;
			}
		}
		else {
			return -1;
		}
	}

	public void setValue(int _value) {
		setText(String.valueOf(_value));
	}
}
