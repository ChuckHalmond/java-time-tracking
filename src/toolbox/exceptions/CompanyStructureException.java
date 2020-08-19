package toolbox.exceptions;

/**
 * An exception thrown when an illegal operation on the company structure is attempted.
 * 
 * @author Charles MECHERIKI
 *
 */
public class CompanyStructureException extends Exception {
	private static final long serialVersionUID = 1L;

	public CompanyStructureException(String message) {
        super(message);
    }
}
