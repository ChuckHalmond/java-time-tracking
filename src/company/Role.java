package company;

import java.util.ArrayList;

/**
 * The different roles for a staff member.
 * 
 * @author Charles
 *
 */
public enum Role {
	EMPLOYEE("Employee"),						/** Employee role */
	MANAGER("Manager"),							/** Manager role */
	HEADMANAGER("Head manager"),				/** Head manager role */
	SELECT_ROLE_PLACEHOLDER("Select a role");	/** Placeholder used inside the views */
	
    private final String string; /** Describing string for each role */

    /**
     * Creates a role with a descriptive string.
     * 
     * @param _string	the string which describes the role
     */
    Role(final String _string) {
        string = _string;
    }

    /**
     * Returns the describing string of the role.
     */
    @Override
    public String toString() {
        return string;
    }
   
	/**
	 * Returns all the possible roles.
	 * 
	 * @return all the possible roles
	 */
	public static ArrayList<Role> getAllRoles() {
		ArrayList<Role> roles = new ArrayList<Role>();

		roles.add(EMPLOYEE);
		roles.add(MANAGER);
		roles.add(HEADMANAGER);
		
		return roles;
	}

	/**
	 * Returns the assignable roles for the given role.
	 * 
	 * @param _role	the role to check for
	 * @return	the assignable roles for the given role
	 */
	public static ArrayList<Role> getAssignableRolesFor(Role _role) {
		ArrayList<Role> roles = new ArrayList<Role>();
		
		if (_role.equals(EMPLOYEE)) {
			roles.add(EMPLOYEE);
		}
		else {
			roles.add(MANAGER);
			roles.add(HEADMANAGER);
		}
		
		return roles;
	}
	
	/**
	 * Returns the possible gradings for the given role.
	 * 
	 * @param _role	the role
	 * @return the possible gradings for the given role
	 */
	public static ArrayList<Role> getPossibleGradeRolesFor(Role _role) {
		ArrayList<Role> roles = new ArrayList<Role>();
		
		if (_role.equals(EMPLOYEE)) {
			roles.add(MANAGER);
		}
		else {
			roles.add(EMPLOYEE);
		}

		return roles;
	}
}
