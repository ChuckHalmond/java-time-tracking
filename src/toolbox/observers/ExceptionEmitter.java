package toolbox.observers;

/**
 * A basic subject-like class, used to keep track of exceptions.
 *
 * @author Charles MECHERIKI
 *
 */
public class ExceptionEmitter {
	protected ExceptionHandler handler;
	
	public ExceptionEmitter() {
		handler = null;
	}
	
	protected void notifyFatalException(Exception _exception) {
		if (handler != null) {
			handler.handleFatalException(_exception);
		}
	}
	
	protected void notifyException(Exception _exception) {
		if (handler != null) {
			handler.handleException(_exception);
		}
	}
	
	public void setExceptionHandler(ExceptionHandler _handler) {
		handler = _handler;
	}
}
