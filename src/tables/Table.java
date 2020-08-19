package tables;

import java.awt.Dimension;

import javax.swing.JTable;

/**
 * A common table base class for the views tables, gathering some usefull styling.
 *
 * @author Charles MECHERIKI
 *
 */
public class Table extends JTable {
	private static final long serialVersionUID = 1L;
	
	public void prepare() {
		super.setEnabled(false);
		setAutoCreateRowSorter(true);	
		setIntercellSpacing(new Dimension(6, 6));
		setRowHeight(getRowHeight() + 6);
	}
}