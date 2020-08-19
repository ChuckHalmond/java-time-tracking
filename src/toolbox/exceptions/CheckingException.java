package toolbox.exceptions;

/**
 * An exception thrown when a checking is unexpected.
 * 
 * @author Charles MECHERIKI
 *
 */
public class CheckingException extends Exception {
	private static final long serialVersionUID = 1L;

	public CheckingException(String message) {
        super(message);
    }
}