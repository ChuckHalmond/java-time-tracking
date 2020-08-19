package communication;

import communication.Protocol.Flag;
import communication.Protocol.Tag;

/**
 * A packet is a data structure used by the synchronizer's TCPserver and TCP client, which respects a protocol.
 * 
 * The original idea of sending a flag and a tag with the data is to embed the indications about the interpretation of the packet's data.
 * That way, there are no problems of order reliability or 'artificial synchronization' instead of a real protocol.
 *  
 * @author Charles MECHERIKI
 *
 */
public class Packet {
	private Flag flag;		/**	The flag of the packet, indicating the way to handle the tag and / or the data 	*/
	private Tag tag;		/** The tag, which precises the way the interpreter to use with the data			*/
	private byte[] data;	/** The data of the object sent														*/
	
	/**
	 * Constructs a packet from its flag, tag and data.
	 * 
	 * @param _flag	the flag of the packet
	 * @param _tag	the tag of the packet
	 * @param _data	the data of the packet
	 */
	public Packet(Flag _flag, Tag _tag, byte[] _data) {
		flag = _flag;
		tag = _tag;
		data = _data;
	}
	
	/**
	 * Returns the data of the packet.
	 * 
	 * @return the data of the packet
	 */
	public byte[] getData() {
		return data;
	}
	
	/**
	 * Returns the flag of the packet.
	 * 
	 * @return the flag of the packet
	 */
	public Flag getFlag() {
		return flag;
	}
	
	/**
	 * Returns the tag of the packet.
	 * 
	 * @return the tag of the packet
	 */
	public Tag getTag() {
		return tag;
	}
	
	/**
	 * Sets the data of the packet.
	 * 
	 * @param _data	the data of the packet
	 */
	public void setData(byte[] _data) {
		data = _data;
	}
	
	/**
	 * Sets the flag of the packet.
	 * 
	 * @param _flag	the flag of the packet
	 */
	public void setFlag(Flag _flag) {
		flag = _flag;
	}
	
	/**
	 * Sets the tag of the packet.
	 * 
	 * @param _tag	the tag of the packet
	 */
	public void setTag(Tag _tag) {
		tag = _tag;
	}
}
