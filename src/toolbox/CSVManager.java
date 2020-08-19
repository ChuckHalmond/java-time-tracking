package toolbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import toolbox.filters.CSVFileFilter;


/**
 * A static utility class handling the export/import to/from CSV file.
 * 
 * This class is actually only used to store staff members in CSV files and read staff members from CSV files.
 * The actual CSV - data structure conversions are made from the company/employee classes.
 * Only the staff firstnames, lastnames, departments and roles are saved and can be reinterpreted.
 *
 * @author Charles MECHERIKI
 *
 */
public class CSVManager {
	private static BufferedReader bufferedReader = null;	/**	The buffered reader 				*/

	private static String encoding = "UTF-8";				/**	Encoding used 						*/
	private static String directory = "csv/";				/** Default directory for the csv files */
	
	/**
	 * Returns the default directory for the CSV files, and creates its if it doesn't exists
	 * 
	 * @return the default directory for the CSV files
	 */
	public static File directory() {
		File file;
		
		if (!(file = new File(directory)).exists()) {
			file.mkdir();
		}
		
		return file;
	}

	
	/***
	 * Writes the given string in the given CSV file.
	 * 
	 * @param _contentCSVFile	the string representing the content of the CSV file
	 * @param _file				the CSV file
	 * @throws Exception if the given file is not a CSV file
	 */
	public static void writeCSVFileContent(String _contentCSVFile, File _file) throws Exception {
		
		if (!(new CSVFileFilter().accept(_file))) {
			throw new IOException("The extension must be .csv.");
		}

		PrintWriter writer = new PrintWriter(_file, encoding);
		
		writer.println(_contentCSVFile);
		
		writer.close();
	}
	
	/**
	 * Sets the CSV file to read in the buffer reader.
	 * 
	 * @param _file	the file to read
	 * @throws Exception	if the file is not a CSV file
	 */
	public static void setFileToRead(File _file) throws Exception {
		
		if (!(new CSVFileFilter().accept(_file))) {
			throw new IOException("The extension must be .csv.");
		}
		
		bufferedReader = new BufferedReader(new FileReader(_file));
	}
	
	/**
	 * Reads one line of the file stored in the buffer reader.
	 * 
	 * @return the line read
	 * @throws IOException	if an error occurred on reading
	 */
	public static String readLine() throws IOException  {
	    String line;
	    
	    if ((line = bufferedReader.readLine()) != null) {
	    	return line;
	    }
	    else {
	    	return null;
	    }
	}
	
	/**
	 * Closes the buffer reader.
	 * 
	 * @throws Exception	if the close operation failed
	 */
	public static void closeReader() throws Exception {
		if (bufferedReader != null) {
			bufferedReader.close();
		}
	}
}
