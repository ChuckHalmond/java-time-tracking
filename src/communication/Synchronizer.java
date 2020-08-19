package communication;

import communication.Protocol.Flag;
import communication.Protocol.Tag;
import communication.TCP.TCPClient;
import communication.TCP.TCPConnectionObserver;
import communication.TCP.TCPParameters;
import communication.TCP.TCPServer;
import toolbox.observers.ExceptionEmitter;
import toolbox.Serializor;

/**
 * The class managing the communication between the devices.
 * 
 * The application and the emulator have each a synchronizer, which is responsible of the coordination of their respective 
 * clients and servers.
 *
 * @author Charles MECHERIKI
 *
 */
public class Synchronizer extends ExceptionEmitter {
	private Protocol protocol;			/**	The protocol to follow */
	
	private TCPConnectionObserver serverConnectionObserver;		/**	Server connection observer form the device */
	private TCPConnectionObserver clientConnectionObserver;		/**	Client connection observer form the device */
	
	private Server server;	/**	Server */
	private Client client;	/** Client */

	/**
	 * Constructs and initialize the synchronizer.
	 * 
	 * @param _serverConnectionObserver		the server connection observer from the device
	 * @param _serverTCPParameters				the TCP parameters for the server
	 * @param _clientConnectionObserver		the client connection observer from the device
	 * @param _clientTCPParameters				the TCP parameters for the client
	 */
	public Synchronizer(TCPConnectionObserver _serverConnectionObserver, TCPParameters _serverTCPParameters, 
			TCPConnectionObserver _clientConnectionObserver, TCPParameters _clientTCPParameters) {
		
		protocol = new Protocol();
		
		serverConnectionObserver = _serverConnectionObserver;
		clientConnectionObserver = _clientConnectionObserver;
		
		server = new Server(_serverTCPParameters);
		client = new Client(_clientTCPParameters);
	}

	//==================//
	//		protocol 	//
	//==================//
	
	/**
	 * Adds a new interpreter to the synchronizer's protocol with the given tag key.
	 * 
	 * @param _tag			the tag key for the interpreter
	 * @param _interpreter	the interpreter to add
	 */
	public void newProtocolInterpreter(Tag _tag, Interpreter<?> _interpreter) {
		protocol.newInterpreter(_tag, _interpreter);
	}
	
	//==================================//
	//		communication methods		//
	//==================================//
	
	/**
	 * Sends a 'hey' packet to the other device.
	 * This packet is interpreted as a request to connect its client (cf. the server's routine method).
	 */
	public void sendHey() {
		send(Protocol.Flag.HEY, Tag.DEFAULT, new byte[0]);
	}
	
	/**
	 * Sends a 'bye' packet to the other device.
	 * This packet is interpreted as a request to disconnect its client  (cf. the server's routine method).
	 */
	public void sendBye() {
		send(Protocol.Flag.BYE, Tag.DEFAULT, new byte[0]);
	}
	
	/**
	 * Sends an exception packet to the other device.
	 * This packet is interpreted as an internal exception (cf. the server's routine method).
	 * 
	 * @param _exception	the exception to send
	 */
	public void sendException(Exception _exception) {
		send(Protocol.Flag.EXC, Tag.DEFAULT, _exception);
	}

	/**
	 * Sends to send an object packet, with a particular tag, interpreted accordingly by the other device (cf. the server's routine method).
	 * 
	 * @param _tag		the tag for the packet to send
	 * @param _object	the object for the packet
	 * 
	 */
	public void sendObject(Tag _tag, Object _object) {
		send(Protocol.Flag.OBJ, _tag, _object);
	}
	
	/**
	 * Sends a packet (created on the fly) from the device's client to the other device's server.
	 * 
	 * @param _flag		the flag for the packet
	 * @param _tag		the tag for the packet
	 * @param _object	the object for the packet
	 */
	public void send(Flag _flag, Tag _tag, Object _object) {
		try {
			byte[] objectData = Serializor.serializeObjectToData(_object);

			try {
				if (!client.isClosed()) {
					client.sendPacket(new Packet(_flag, _tag, objectData));
				}
			}
			catch (NullPointerException _npException) {
				try {
					clientConnectionObserver.onDisconnection();
				}
				catch (Exception _exception) {
					notifyFatalException(_exception);
				}
			}
		}
		catch (Exception _exception) {
			notifyFatalException(_exception);
		}
	}
	
	//==================//
	//		server		//
	//==================//

	
	/**
	 * The server of the synchronizer, which exclusively receives and interprets packets.
	 *
	 * @author Charles MECHERIKI
	 *
	 */
	private class Server extends TCPServer implements Runnable {
		
		/**
		 * Constructor, which initializes the server's TCP parameters.
		 * @param _TCPParameters	the server's TCP parameters
		 */
		public Server(TCPParameters _TCPParameters) {
			super(_TCPParameters);
		}

		/**
		 * Cycle of life of a server.
		 * A server wait for a client connection and then triggers its routine.
		 * If the client disconnects, the server wait for another connection.
		 */
		@Override
		public void run() {
			try {
				openPassiveSocket();
			}
			catch (Exception _exception) {
				notifyFatalException(_exception);
			}
			
			while (true) {
				try {
					openActiveSocket();
					routine();
					closeActiveSocket();
				}
				catch (Exception _exception) {
					notifyException(new Exception("The connection was unexpectedly lost.", _exception));
				}
				finally {
					try {
						closeActiveSocket();
						serverConnectionObserver.onDisconnection();
					}
					catch (Exception _exception) {
						notifyFatalException(_exception);
					}
				}
			}
		}
		
		/**
		 * The routine of the server.
		 * The server interprets every packet received according to the synchronizer's protocol.
		 * 
		 * @throws Exception
		 */
		public void routine() throws Exception {
			Packet packet = null;

			while ((packet = receivePacket()) != null) {
				Flag flag = packet.getFlag();
				Tag tag = packet.getTag();
				
				if (flag.equals(Protocol.Flag.OBJ)) {
					protocol.getInterpreter(tag).castAndInterpret(Serializor.deserializeDataToObject(packet.getData()));
				}
				else if (flag.equals(Protocol.Flag.EXC)) {
					notifyException((Exception)Serializor.deserializeDataToObject(packet.getData()));
				}
				else if (flag.equals(Protocol.Flag.HEY)) {
					serverConnectionObserver.onConnection();
					connectClient();
				}
				else if (flag.equals(Protocol.Flag.BYE)) {
					serverConnectionObserver.onDisconnection();
					disconnectClient();
					
					return;
				}
			}
		}
	}
	
	/**
	 * Launches the server. 
	 */
	public void launchServer() {
		new Thread(server).start();
	}
	
	/**
	 * Reboots the server with new TCP parameters.
	 * 
	 * @param _TCPParameters	the new TCP parameters for the server.
	 */
	public void rebootServerWithNewParameters(TCPParameters _TCPParameters) {
		try {
			server.closePassiveSocket();
			server.setSocketParameters(_TCPParameters);
			server.openPassiveSocket();
		}
		catch (Exception _exception) {
			notifyFatalException(_exception);
		}
	}
	
	//==================//
	//		client		//
	//==================//

	/**
	 * The client of the synchronizer, which exclusively send packets.
	 *
	 * @author Charles MECHERIKI
	 *
	 */
	private class Client extends TCPClient {

		/**
		 * Constructor, which initializes the client's TCP parameters.
		 * @param _TCPParameters	the client's TCP parameters
		 */
		public Client(TCPParameters _TCPParameters) {
			super(_TCPParameters);
		}
	}
	
	/**
	 * Tries to connect the client to the other device's server.
	 */
	public void connectClient() {
		try {
			if (client.isClosed()) {
				client.openSocket();
				sendHey();
				clientConnectionObserver.onConnection();
			}
		}
		catch (Exception _exception) {
			notifyException(new Exception("The connection couldn't be established. Please connect the other device.", _exception));
		}
	}
	
	/**
	 * Disconnects the client from the other device's server.
	 */
	public void disconnectClient() {
		try {
			if (!client.isClosed()) {
				sendBye();
				client.closeSocket();
				clientConnectionObserver.onDisconnection();
			}
		}
		catch (Exception _exception) {
			notifyFatalException(_exception);
		}
	}
	
	/**
	 * Returns whether the client's connection is closed.
	 * 
	 * @return whether the client's connection is closed.
	 */
	public boolean clientIsClosed() {
		return client.isClosed();
	}
	
	/**
	 * Reboots the client with new TCP parameters.
	 * 
	 * @param _TCPParameters	the new TCP parameters for the client.
	 */
	public void rebootClientWithNewParameters(TCPParameters _TCPParameters) {
		try {
			client.closeSocket();
			client.setSocketParameters(_TCPParameters);
			connectClient();
		}
		catch (Exception _exception) {
			notifyFatalException(_exception);
		}
	}
}