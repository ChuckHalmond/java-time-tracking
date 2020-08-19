package communication.TCP;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * A TCP client, able to send and receive packets via its socket.
 *
 * @author Charles MECHERIKI
 *
 */
public class TCPClient extends TCPMessage {
	private TCPParameters TCPParameters;		/**	Client TCP parameters 		*/
	private InetSocketAddress isA;				/** Client inet socket address 	*/
	private Socket s;							/** Client socket				*/

	/**
	 * Constructs and initializes the TCP parameters and inet socket address of the client.
	 * 
	 * @param _TCPParameters	the TCP parameters for the client
	 */
	public TCPClient(TCPParameters _TCPParameters) {
		s = null;
		isA = null;
		setSocketParameters(_TCPParameters);
	}
	
	/**
	 * Returns the TCP parameters of the client.
	 * 
	 * @return the TCP parameters of the client
	 */
	public TCPParameters getParameters() {
		return TCPParameters;
	}
 
	/**
	 * Opens the client socket.
	 * 
	 * @throws IOException	if the client encountered an error opening his socket
	 */
	public void openSocket() throws IOException {
		try {
			s = new Socket(isA.getHostName(), isA.getPort());
			out = s.getOutputStream();
			in = s.getInputStream();
		}
		catch (IOException _exception) {
			if (s != null) {
				s.close();
			}
			throw new IOException("The client encountered an error opening his socket.", _exception);
		}
	}
	
	/**
	 * Closes the client socket.
	 * 
	 * @throws IOException	if the client encountered an error closing his socket
	 */
	public void closeSocket() throws Exception {
		try {
			if (s != null) {
				s.close();
			}
		}
		catch (Exception _exception) {
			throw new Exception("The client encountered an error closing his socket.", _exception);
		}
	}
	
	/**
	 * Sets the client TCP parameters.
	 * 
	 * @param _TCPParameters the TCP parameters to set
	 */
	public void setSocketParameters(TCPParameters _TCPParameters) {
		TCPParameters = _TCPParameters;
		isA = new InetSocketAddress(_TCPParameters.getIPAddress(), _TCPParameters.getPort());
	}
	
	/**
	 * Returns whether the client's connection is closed or not.
	 * 
	 * @return whether the client's connection is closed or not
	 */
	public boolean isClosed() {
		return s == null || s.isClosed();
	}
}
