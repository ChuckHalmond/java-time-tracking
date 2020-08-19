package toolbox.observers;

/**
 * A basic observer-like class, used to keep track of exceptions.
 *
 * @author Charles MECHERIKI
 *
 */
public abstract class ExceptionHandler {
	public abstract void handleFatalException(Exception _exception);
	public abstract void handleException(Exception _exception);
}
