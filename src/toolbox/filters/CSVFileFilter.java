package toolbox.filters;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * A filter for the CSV files.
 *
 * @author Charles MECHERIKI
 *
 */
public class CSVFileFilter extends FileFilter {
	
	@Override
	public boolean accept(File _file) {
		String path = _file.getName();
		return _file != null && path.length() >= 4 && path.substring(path.length() - 4).equals(".csv");
	}

	@Override
	public String getDescription() {
		return "CSV file";
	}
}
