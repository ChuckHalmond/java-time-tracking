package communication.TCP;

/**
 * A basic observer-like class, used to execute code on the TCP clients/servers connection/disconnection.
 * 
 * @author Charles MECHERIKI
 *
 */
public abstract class TCPConnectionObserver {
	public abstract void onConnection();
	public abstract void onDisconnection();
}
