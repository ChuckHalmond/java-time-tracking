package toolbox.filters;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * A filter for the config files.
 *
 * @author Charles MECHERIKI
 *
 */
public class ConfigFileFilter extends FileFilter {
	
	@Override
	public boolean accept(File _file) {
		String path = _file.getName();
		return _file != null && path.length() >= 7 && path.substring(path.length() - 7).equals(".config");
	}

	@Override
	public String getDescription() {
		return "Config file";
	}
}