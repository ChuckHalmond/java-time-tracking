package toolbox;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * A static utility class responsible of serialization/deserialization. 
 *
 * @author Charles MECHERIKI
 *
 */
public class Serializor {
	private static FileOutputStream fos;		/** File output stream 		*/
	private static ObjectOutputStream oos;		/** Object output stream 	*/
	private static FileInputStream fis;			/** File input stream 		*/
	private static ObjectInputStream ois;		/** Object output stream 	*/
	
	private static final String directory = "cache/";	/**	Directory where the serialized files are stored */
	
	/**
	 * Serializes in a file with the given name the given object.
	 * 
	 * @param _filename		name for the file
	 * @param _object		object to serialize in the file
	 * @throws Exception	if an error occured during serialization
	 */
	public static void serializeToFile(String _filename, Object _object) throws Exception {
		String filepath = directory + _filename;
		File file = null;
		
		if (!(file = new File(directory)).exists()) {
			file.mkdir();
		}
		
		if (!(file = new File(filepath)).exists()) {
			file.delete();
		}
		
		try {
			fos = new FileOutputStream(filepath);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(_object);
		}
		catch (Exception _exception) {
			throw new Exception("The serializor encountered an error on serializing the file '" + filepath + "',"
				+ " please restart the device.", _exception);
		}
		finally {
			try {
				if (oos != null) {
					oos.close();
				}
			}
			catch (Exception _exception) {
				throw new Exception("The serializor encountered an error on closing the file '" + filepath + "',"
					+ " please remove it if it exists and restart the device.", _exception);
			}
		}
	}
	
	/**
	 * Serializes the given object into bytes.
	 * 
	 * @param _object	the object to serialize
	 * @return	the bytes of the object
	 * @throws Exception	if an error occured during serialization
	 */
	public static byte[] serializeObjectToData(Object _object) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = null;

	    try {
			os = new ObjectOutputStream(out);
			os.writeObject(_object);
	    }
	    catch (Exception _exception) {
	    	throw new Exception("The serializor encountered an error on serializing data, please restart the device.", _exception);
	    }
	    finally {
	    	try {
	    		if (os != null)
	    			os.close();
		    	if (out != null)
		    		out.close();
	    	}
	    	catch (Exception _exception) {
	    		throw new Exception("The serializor encountered an error on closing its streams after serializing data.", _exception);
	    	}
	    }
	    
	    return out.toByteArray();
	}
	
	/**
	 * Deserializes the file with the given filename.
	 * 
	 * @param _filename	the name of the file to deserialize.
	 * @return	the object which was contained in the file
	 * @throws Exception	if an error occured during deserialization
	 */
	public static Object deserializeFromFile(String _filename) throws Exception {
		String filepath = directory + _filename;
		Object object = null;
		
		if (!(new File(filepath)).exists()) {
			return null;
		}
		
		try {
			fis = new FileInputStream(filepath);
			ois = new ObjectInputStream(fis);
			object = ois.readObject();
		}
		catch (Exception _exception) {
			throw new Exception("The serializor encountered an error on deserializing the file '" + filepath + "',"
				+ " please remove it and restart the device.", _exception);
		}
		finally {
			if (ois != null) {
				try {
					ois.close();
				}
				catch (Exception _exception) {
					throw new IOException("The serializor encountered an error on closing the file '" + filepath + "',"
						+ " please remove it and restart the device.", _exception);
				}
			}
		}

		return object;
	}
	
	/**
	 * Deserializes the given data into object.
	 * 
	 * @param _data		the data to deserizalize
	 * @return	the object equivalent of the data
	 * @throws Exception	if an error occured during deserialization
	 */
	public static Object deserializeDataToObject(byte[] _data) throws Exception {
	    ByteArrayInputStream in = new ByteArrayInputStream(_data);
	    ObjectInputStream is = null;
	    Object object = null;

	    try {
	    	is = new ObjectInputStream(in);
	    	object = is.readObject();
	    	is.close();
	    }
	    catch (EOFException _exception) {
	    	return object;
	    }
	    catch (Exception _exception) {
	    	throw new Exception("The serializor encountered an error on deserializing data.", _exception);
	    }
	    return object;
	}
}
