package tables.cell_renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * A cell renderer which highlights the departments table 'Head manager' row inconsistencies, like a department without head.
 *
 * @author Charles MECHERIKI
 *
 */
public class DepartmentHeadCellRenderer extends DefaultTableCellRenderer {
	
	private static final long serialVersionUID = 1L;
	
	public Component getTableCellRendererComponent(JTable _table, Object _value, boolean _isSelected, boolean _hasFocus, int _row, int _column) {
		Component component = super.getTableCellRendererComponent(_table, _value, _isSelected, _hasFocus, _row, _column);

        int modelRow = _table.getRowSorter().convertRowIndexToModel(_row);

		String head = (String)_table.getModel().getValueAt(modelRow, _column);

	    if (head.equals("NONE")) {
	    	component.setForeground(Color.RED);
	    }
	    else {
	    	component.setForeground(Color.BLACK);
	    }

	    return component;
	}
}