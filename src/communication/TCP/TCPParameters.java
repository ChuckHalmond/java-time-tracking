package communication.TCP;

import java.io.Serializable;

/**
 * A simple data structure storing an IP address and a port number, used by the
 * TCP servers and TCP clients.
 *
 * @author Charles MECHERIKI
 *
 */
public class TCPParameters implements Serializable {
	private static final long serialVersionUID = 1L;	/** Recommended when implementing Serializable */
	
	private String IPAddress;	/**	IP address	*/
	private int port;			/** Port number	*/
	
	/**
	 * Creates and initializes the TCP parameters.
	 * 
	 * @param _IPAddress	the IP address
	 * @param _port			the port number
	 */
	public TCPParameters(String _IPAddress, int _port) {
		IPAddress = _IPAddress;
		port = _port;
	}
	
	/**
	 * Returns the IP address.
	 * @return the IP address
	 */
	public String getIPAddress() {
		return IPAddress;
	}
	
	/**
	 * Returns the port number.
	 * 
	 * @return the port number
	 */
	public int getPort() {
		return port;
	}
}