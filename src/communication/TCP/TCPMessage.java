package communication.TCP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import communication.Packet;
import communication.Protocol.Flag;
import communication.Protocol.Tag;

/**
 * Class responsible of the transmission of packets through input and output streams.
 * All the frames sent have the same fixed size.
 * 
 * In order to handle the case where the size of the packet data exceeds this value, all the frames
 * transmitted through the streams are indexed. 
 *
 * @author Charles MECHERIKI
 *
 */
public class TCPMessage {
	private final int bufferSize = 1024;			/** Buffer size, i.e the size of the frames sent 	*/
	private byte[] buffer = new byte[bufferSize];	/** Buffer used in reading and writing 				*/
	
	protected OutputStream out;						/** Output stream, used to write data 				*/
	protected InputStream in;						/**	Input stream, used to read data 				*/
	
	/**
	 * Constructor initializing the streams as null.
	 */
	public TCPMessage() {
		out = null;
		in = null;
	}
	
	/**
	 * Clears the buffer.
	 */
	public void clearBuffer() {
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = 0;
		}
	}
	
	/**
	 * Returns the packet successfully read and reassembled from the input stream or null if the stream is closed.
	 * 
	 * @return the packet successfully read and reassembled from the input stream or null if the stream is closed
	 * @throws IOException	if an IO exception occurred during reception
	 */
	public Packet receivePacket() throws IOException {
		if (in != null) {
			try {
				byte[] tempData = new byte[0];
				byte[] packetData = new byte[0];
				int packetDataSize = 0;
				
				clearBuffer();
				if (in.read(buffer) > -1) {
					
					int frameIndexInPacket = byteArrayToInt(Arrays.copyOfRange(buffer, bufferSize - 8, bufferSize - 4));
					int nbOfFramesInPacket = byteArrayToInt(Arrays.copyOfRange(buffer, bufferSize - 4, bufferSize));
					
					int packetFlagSize = byteArrayToInt(Arrays.copyOfRange(buffer, bufferSize - 20, bufferSize - 16));
					int packetTagSize = byteArrayToInt(Arrays.copyOfRange(buffer, bufferSize - 16, bufferSize - 12));
					int packetHeadSize = packetFlagSize + packetTagSize;
					
					Flag packetFlag = Flag.valueOf(new String(Arrays.copyOfRange(buffer, 0, packetFlagSize)));
					Tag packetTag = Tag.valueOf(new String(Arrays.copyOfRange(buffer, packetFlagSize, packetFlagSize + packetTagSize)));
					
					while (frameIndexInPacket <= nbOfFramesInPacket) {
						int frameBodySize = byteArrayToInt(Arrays.copyOfRange(buffer, bufferSize - 12, bufferSize - 8));
						byte[] frameBody = Arrays.copyOfRange(buffer, packetHeadSize, packetHeadSize + frameBodySize);

						tempData = new byte[packetDataSize + frameBodySize];
						System.arraycopy(packetData, 0, tempData, 0, packetDataSize);
						System.arraycopy(frameBody, 0, tempData, packetDataSize, frameBodySize);
						
						packetData = tempData;
						packetDataSize += frameBodySize;

						if (frameIndexInPacket == nbOfFramesInPacket) {
							clearBuffer();
							return new Packet(packetFlag, packetTag, packetData);
						}
						
						clearBuffer();
						in.read(buffer);
						
						frameIndexInPacket = byteArrayToInt(Arrays.copyOfRange(buffer, bufferSize - 8, bufferSize - 4));
					}
				}
			}
			catch (IOException _exception) {
				throw new IOException("An error occured on packet reception.", _exception);
			}
		}
		return null;
	}
	
	/**
	 * Sends a packet through the output stream, segmenting it in frames.
	 * 
	 * @param _packet	the packet to send
	 * @throws IOException	if an IO exception occurred during sending
	 */
	public void sendPacket(Packet _packet) throws IOException {
		if (out != null) {
			try {
				byte[] packetData = _packet.getData();
				String packetFlag = _packet.getFlag().toString();
				String packetTag = _packet.getTag().toString();
				
				int packetDataSize = packetData.length;
				int packetFlagSize = packetFlag.length();
				int packetTagSize = packetTag.length();
				
				int remainingPacketDataSize = packetDataSize;

				int maxFrameBodySize = bufferSize - packetFlag.length() - packetTag.length() - 20;
				int nbOfFramesInPacket = (packetDataSize / maxFrameBodySize) + ((packetDataSize % maxFrameBodySize == 0) ? 0 : 1);
				int frameIndexInPacket = 1;

				while (frameIndexInPacket <= nbOfFramesInPacket) {

					int frameHeadSize = packetFlag.length() + packetTag.length();
					byte[] frameHead = new byte[frameHeadSize];
					System.arraycopy((packetFlag + packetTag).getBytes(), 0, frameHead, 0, frameHeadSize);
					
					
					int frameBodySize = (remainingPacketDataSize < maxFrameBodySize) ? remainingPacketDataSize : maxFrameBodySize;
					byte[] frameBody = new byte[frameBodySize];
					System.arraycopy(packetData, packetDataSize - remainingPacketDataSize, frameBody, 0, frameBodySize);

					int frameQueueSize = 20;
					byte[] frameQueue = new byte[frameQueueSize];
					System.arraycopy(intToByteArray(packetFlagSize), 0, frameQueue, 0, 4);
					System.arraycopy(intToByteArray(packetTagSize), 0, frameQueue, 4, 4);
					System.arraycopy(intToByteArray(frameBodySize), 0, frameQueue, 8, 4);
					System.arraycopy(intToByteArray(frameIndexInPacket), 0, frameQueue, 12, 4);
					System.arraycopy(intToByteArray(nbOfFramesInPacket), 0, frameQueue, 16, 4);
					
					clearBuffer();
					System.arraycopy(frameHead, 0, buffer, 0, frameHeadSize);
					System.arraycopy(frameBody, 0, buffer, frameHeadSize, frameBodySize);
					System.arraycopy(frameQueue, 0, buffer, bufferSize - frameQueueSize, frameQueueSize);
					
					out.write(buffer);
					
					frameIndexInPacket++;
					remainingPacketDataSize -= frameBodySize;
				}
			}
			catch (IOException _exception) {
				throw new IOException("An error occured on sending a packet.", _exception);
			}
		}
	}
	
	/**
	 * Converts an integer into bytes.
	 * 
	 * @param _value	the integer to convert into bytes
	 * @return the integer converted into byte array
	 */
    public static byte[] intToByteArray(int _value) {
    	ByteBuffer byteBuffer = ByteBuffer.allocate(4);
    	byteBuffer.putInt(_value);
    	return byteBuffer.array();
    }

	/**
	 * Converts bytes into integer.
	 * 
	 * @param _bytes	the bytes to convert into integer
	 * @return the bytes converted into integer
	 */
	public static int byteArrayToInt(byte[] _bytes) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(_bytes);
		return byteBuffer.getInt();
	}
}
