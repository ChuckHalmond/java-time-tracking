package swing.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.border.EmptyBorder;

/**
 * A flow panel class, proposing a uniform size for all its components, a border and some default padding.
 *
 * @author Charles MECHERIKI
 *
 */
public class FlowPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	private Dimension preferedComponentsSize;

	public FlowPanel(Dimension _preferedComponentSize, EmptyBorder _border) {
		setLayout(new FlowLayout(FlowLayout.CENTER, defaultHorizontalGap, defaultVerticalGap));
		
		if (_border != null) {
			setBorder(_border);
		}
	
		if (_preferedComponentSize != null) {
			preferedComponentsSize = _preferedComponentSize;
		}
	}
	
	public FlowPanel(Dimension _preferedComponentSize) {
		this(_preferedComponentSize, new EmptyBorder(0, 0, 0, 0));
	}
	
	public Component add(Component comp) {
		super.add(comp);
		comp.setPreferredSize(preferedComponentsSize);
		return comp;
	}
}