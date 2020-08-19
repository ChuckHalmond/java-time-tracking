package views;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * A simple base class for the views, with a name and the possibility to display error messages.
 *
 * @author Charles MECHERIKI
 *
 */
public class View extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	public View(String _name) {
		name = _name;
	}
	
	public String getName() {
		return name;
	}
	
	public void alert(String _mesage) {
		JOptionPane.showMessageDialog(null, _mesage, name + " - Alert message", JOptionPane.INFORMATION_MESSAGE);
	}
}