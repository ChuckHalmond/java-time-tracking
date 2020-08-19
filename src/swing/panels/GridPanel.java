package swing.panels;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.border.EmptyBorder;

/**
 * A grid panel class, proposing a uniform size for all its components, a border and some default padding.
 *
 * @author Charles MECHERIKI
 *
 */
public class GridPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	private Dimension preferedComponentsSize;
	private Insets borderInsets;

	public GridPanel(int _rows, int _cols, Dimension _preferedComponentSize, EmptyBorder _border) {
		setLayout(new GridLayout(_rows, _cols, defaultHorizontalGap, defaultVerticalGap));
		
		if (_border != null) {
			setBorder(_border);
			borderInsets = _border.getBorderInsets();
		}
	
		if (_preferedComponentSize != null) {
			preferedComponentsSize = _preferedComponentSize;
			setPreferredSize(
				new Dimension(
					_cols * ((int)preferedComponentsSize.getWidth() + ((_cols > 1) ? defaultHorizontalGap : 0)) + borderInsets.left + borderInsets.right,
					_rows * ((int)preferedComponentsSize.getHeight() + ((_rows > 1) ? defaultVerticalGap : 0)) +  borderInsets.top + borderInsets.bottom
				)
			);
		}
	}
	
	public GridPanel(int _rows, int _cols, Dimension _preferedComponentSize) {
		this(_rows, _cols, _preferedComponentSize, new EmptyBorder(0, 0, 0, 0));
	}
}