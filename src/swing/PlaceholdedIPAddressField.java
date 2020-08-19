package swing;

import org.apache.commons.validator.routines.InetAddressValidator;

/**
 * A placeholded text field which returns a specific result if its value is not a correct IP address. 
 *
 * @author Charles MECHERIKI
 *
 */
public class PlaceholdedIPAddressField extends PlaceholdedTextField {
	private static final long serialVersionUID = 1L;

	public PlaceholdedIPAddressField(String _placeholder) {
		super(_placeholder);
	}
	
	public String getIPAddress() {
		String IPAdress = super.getText();
		
		return (InetAddressValidator.getInstance().isValid(IPAdress)) ? IPAdress : "";
	}
}