package communication;
import java.util.HashMap;
import java.util.Map;

/**
 * The protocol defines the communication between the devices.
 * Its role is to reference the different ways the synchronizer should interpret a packet and 
 * to enumerate the different packets tags and flags used by the synchronizer.
 * 
 * @author Charles MECHERIKI
 *
 */
public class Protocol {
	
	private Map<Tag, Interpreter<?>> interpreters;

	/**
	 * A flag, present in the head of each packet, indicate in which way the synchronizer should interpret 
	 * the tag and the data of the packet.
	 * 
	 * @author Charles MECHERIKI
	 *
	 */
	public enum Flag {
		EXC("EXC"),	/** Flag indicating an exception to display			*/
		OBJ("OBJ"),	/** Flag indicating an object, precised by the tag 	*/
		HEY("HEY"),	/** Flag indicating a connection request 			*/
		BYE("BYE");	/** Flag indicating a disconnection request 		*/
		
	    private final String string;	/** Describing string for each flag */

	    /**
	     * Creates a flag from a string.
	     * 
	     * @param _string	the flag string
	     */
	    Flag(final String _string) {
	        string = _string;
	    }

	    /**
	     * Returns the string of the flag.
	     */
	    @Override
	    public String toString() {
	        return string;
	    }
	}
	
	/**
	 * When the flag of a packet is OBJ, the tag role precises the interpreter to call with the object.
	 * However, the tag field could be used with another flag, as long as the correct behavior is defined in the synchronizer.
	 * 
	 * @author Charles MECHERIKI
	 *
	 */
	public enum Tag {
		DEFAULT("DEFAULT"),											/** Default tag																	*/
		ALL_STAFF("ALL_STAFF"),									/** Tag indicating the staff to consider in the emulator						*/
		RECRUITED_STAFFMEMBER("RECRUITED_STAFFMEMBER"),			/** Tag indicating a staff member to add to the emulator						*/
		UPDATED_STAFFMEMBER("UPDATED_STAFFMEMBER"),				/** Tag indicating a staff member to update in the emulator						*/
		DISMISSED_STAFFMEMBER_ID("DISMISSED_STAFFMEMBER_ID"),	/** Tag indicated the ID of a staff member to dismiss in the emulator			*/
		CHECKING_REQUEST("CHECKING_REQUEST"),					/** Tag indicating a checking request to deal with in the application 			*/
		CHECKING_REQUESTS_CACHE("CHECKING_REQUESTS_CACHE"),		/** Tag indicating the checking requests cache to deal with in the application 	*/
		TCP_PARAMETERS("TCP_PARAMETERS");						/** Tag indicating the TCP parameters to consider for the emulator's server 	*/
		
	    private final String string;	// Describing string for the role

	    /**
	     * Creates a tag from a string.
	     * 
	     * @param _string	the tag string
	     */
	    Tag(final String _string) {
	        string = _string;
	    }

	    /**
	     * Returns the string of the tag.
	     */
	    @Override
	    public String toString() {
	        return string;
	    }
	}
	
	/**
	 * Constructor of the protocol, initiating an hashmap of tag and interpreters.
	 */
	public Protocol() {
		interpreters = new HashMap<Tag, Interpreter<?>>();
	}
	
	/**
	 * Binds the given interpreter to the given tag in the protocol's hashmap.
	 * 
	 * @param _tag			the tag
	 * @param _interpreter	the interpreter to bind to the tag
	 */
	public void newInterpreter(Tag _tag, Interpreter<?> _interpreter) {
		interpreters.put(_tag, _interpreter);
	}
	
	/**
	 * Returns the interpreter relative to the hashmap if it exists, null otherwise. 
	 * 
	 * @param _tag	the tag
	 * @return the interpreter relative to the hashmap if it exists, null otherwise. 
	 */
	public Interpreter<?> getInterpreter(Tag _tag) {
		return interpreters.get(_tag);
	}
}
