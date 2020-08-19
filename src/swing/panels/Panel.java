package swing.panels;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * A panel base class, with default padding, border and components size values.
 *
 * @author Charles MECHERIKI
 *
 */
public class Panel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static int defaultHorizontalGap = 8;
	public static int defaultVerticalGap = 8;
	public static Dimension defaultPreferedComponentsSize = new Dimension(144, 26);
	public static EmptyBorder defaultBorder = new EmptyBorder(5, 20, 5, 20);
	
	public static EmptyBorder defaultTablePanelBorder = new EmptyBorder(5, 20, 5, 20);
}
