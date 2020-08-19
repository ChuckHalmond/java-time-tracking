package communication.TCP;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A TCP server, able to send and receive packets via its socket.
 *
 * @author Charles MECHERIKI
 *
 */
public class TCPServer extends TCPMessage {
	private TCPParameters TCPParameters;		/**	Server TCP parameters 		*/
	private InetSocketAddress isA;				/** Server inet socket address 	*/
	private ServerSocket ss;					/** Server passive socket		*/
	private Socket s;							/** Server active socket		*/
	
	/**
	 * Constructs and initializes the TCP parameters and inet socket address of the server.
	 * 
	 * @param _TCPParameters	the TCP parameters for the server
	 */
	public TCPServer(TCPParameters _TCPParameters) {
		s = null;
		ss = null;
		setSocketParameters(_TCPParameters);
	}
	
	/**
	 * Returns the TCP parameters of the server.
	 * 
	 * @return the TCP parameters of the server
	 */
	public TCPParameters getTCPParameters() {
		return TCPParameters;
	}
	
	
	/**
	 * Opens the server passive socket.
	 * 
	 * @throws IOException	if the server encountered an error opening his passive socket
	 */
	public void openPassiveSocket() throws Exception {
		try {
			ss = new ServerSocket(isA.getPort());
		}
		catch (Exception _exception) {
			if (ss != null) {
				ss.close();
			}
			throw new Exception("The server encountered an error setting his passive socket, "
					+ "this socket is probably open by another process.", _exception);
		}
	}
	
	/**
	 * Opens the server active socket.
	 * 
	 * @throws IOException	if the server encountered an error opening his active socket
	 */
	public void openActiveSocket() throws Exception {
		try {
			s = ss.accept();
			out = s.getOutputStream();
			in = s.getInputStream();
		}
		catch (Exception _exception) {
			if (s != null) {
				s.close();
			}
			if (ss != null) {
				ss.close();
			}
			throw new Exception("The server encountered an error when setting his active socket.", _exception);
		}
	}
	
	/**
	 * Closes the server active socket.
	 * 
	 * @throws IOException	if the server encountered an error closing his active socket
	 */
	public void closeActiveSocket() throws Exception {
		try {
			if (s != null) {
				s.close();
			}
		}
		catch (Exception _exception) {
			throw new Exception("The server encountered an error when closing his active socket.", _exception);
		}
	}
	
	/**
	 * Closes the server passive socket.
	 * 
	 * @throws IOException	if the client encountered an error closing his passive socket
	 */
	public void closePassiveSocket() throws Exception {
		try {
			if (ss != null) {
				ss.close();
			}
		}
		catch (Exception _exception) {
			throw new Exception("The server encountered an error when closing his passive socket.", _exception);
		}
	}	
	
	/**
	 * Sets the server TCP parameters.
	 * 
	 * @param _TCPParameters the TCP parameters to set
	 */
	public void setSocketParameters(TCPParameters _TCPParameters) {
		TCPParameters = _TCPParameters;
		isA = new InetSocketAddress(_TCPParameters.getIPAddress(), _TCPParameters.getPort());
	}

	/**
	 * Returns whether the server's connection is closed or not.
	 * 
	 * @return whether the server's connection is closed or not
	 */
	public boolean isClosed() {
		return s == null || s.isClosed();
	}
}
