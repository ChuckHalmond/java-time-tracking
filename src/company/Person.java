package company;

import java.io.Serializable;

/**
 * A person, with a firstname and a lastname.
 * 
 * @author Charles MECHERIKI
 * 
 */
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;	/** Recommended when implementing Serializable */
	
	protected String firstname;	/** Firstname of the person */
	protected String lastname;	/** Lastname of the employee */
	
	
	/**
	* Initializes a person with his firstname and lastname.
	* 
	* @param _firstname	the firstname of the person
	* @param _lastname	the lastname of the person
	*/
	public Person(String _firstname, String _lastname) {
		this.firstname = _firstname;
		this.lastname = _lastname;
	}
	
	/**
	* Returns the name of the person.
	* 
	* @return the name of the person
	*/
	public String getName() {
		return firstname + " " + lastname;
	}
	
	/**
	* Returns the firstname of the person.
	* 
	* @return the firstname of the person
	*/
	public String getFirstname() {
		return firstname;
	}
	
	/**
	* Returns the lastname of the person.
	* 
	* @return the lastname of the person
	*/
	public String getLastname() {
		return lastname;
	}
	
	/**
	* Returns the name of the person.
	* 
	* @return the name of the person
	*/
	public String toString() {
		return getName();
	}
}
