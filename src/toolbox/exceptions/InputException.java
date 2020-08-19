package toolbox.exceptions;

/**
 * An exception that is thrown when the user launches an action and forgot to specify a mandatory input.
 * 
 * @author Charles MECHERIKI
 *
 */
public class InputException extends Exception {
	private static final long serialVersionUID = 1L;

	public InputException(String _message) {
        super(_message);
    }
	
	public InputException(String _message, Exception _cause) {
        super(_message, _cause);
    }
}