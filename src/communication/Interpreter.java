package communication;

/**
 * An interpreter is used in the communication between the devices to handle the objects received from the packets.
 * The idea is to concentrate the tasks required to handle each object in just one abstract method.
 * The interpreters are defined inside the devices.
 * 
 * For example, a RecruitedStaffMemberInterpreter is created in the emulator to transfer the recruited staff
 * member received from the application to the controller.
 * 
 * 
 * @author Charles MECHERIKI
 *
 * @param <T>	the type of the object to interpret
 */
public abstract class Interpreter<T> {
	
	public abstract void interpret(T _object);

	@SuppressWarnings("unchecked") // Removes the cast's warning
	public void castAndInterpret(Object _object) {
		interpret((T)_object);
	}
}