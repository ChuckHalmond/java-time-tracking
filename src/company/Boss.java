package company;

import java.io.Serializable;

/**
 * The boss of the company.
 * The boss is not considered as a staff member and as just a symbolic role in the company.
 * There should be only one existing boss in the program, created when the company is built.
 * 
 * @author Charles MECHERIKI
 * 
 */
public class Boss extends Person implements Serializable {
	private static final long serialVersionUID = 1L; /** Recommended when implementing Serializable */
	
	/**
	* Initializes the boss as a simple person, with a firstname and a lastname.
	* 
	* @param _firstname		the boss's firstname
	* @param _lastname		the boss's lastname
	*/
	public Boss(String _firstname, String _lastname) {
		super(_firstname, _lastname);
	}
	
	/**
	* Returns the name of the boss.
	* 
	* @return the name of the boss
	*/
	public String toString() {
		return getName();
	}
}