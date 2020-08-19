package swing;

/**
 * A placeholded text field which returns a specific result if its value is not a correct port. 
 *
 * @author Charles MECHERIKI
 *
 */
public class PlaceholdedPortField extends PlaceholdedUnsignedIntField {
	private static final long serialVersionUID = 1L;

	public PlaceholdedPortField(String _placeholder) {
		super(_placeholder);
	}

	public int getPort() {
		int value = getValue();
		
		return (value >= 1024 && value <= 49151) ? value : -1;
	}
}
