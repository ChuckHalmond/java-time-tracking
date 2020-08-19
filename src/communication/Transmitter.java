package communication;

/**
 * The role of a transmitter is to execute some code which implies a communication with the other device directly
 * within the controller.
 * The interpreters are defined inside the devices.
 * 
 * For example, a RecruitedStaffMemberTransmitter is created in the application and given to the StaffController
 * to transmit the staff member to the emulator once he is created from the HMI.
 *
 * @author Charles MECHERIKI
 *
 * @param <T>	the type of the object to transmit
 */
public abstract class Transmitter<T> {
	public abstract void transmit(T _object);
}
