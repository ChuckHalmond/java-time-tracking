package tables.cell_renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import time.CheckingStatus;

/**
 * A cell renderer which highlights the checkings table 'Status' row inconsistencies, like a checking with a 'unknown leaving' status.
 *
 * @author Charles MECHERIKI
 *
 */
public class CheckingStatusCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;
	
	public Component getTableCellRendererComponent(JTable _table, Object _value, boolean _isSelected, boolean _hasFocus, int _row, int _column) {
		Component component = super.getTableCellRendererComponent(_table, _value, _isSelected, _hasFocus, _row, _column);

        int modelRow = _table.getRowSorter().convertRowIndexToModel(_row);
		
		CheckingStatus checkingStatus = (CheckingStatus)_table.getModel().getValueAt(modelRow, _column);

	    if (checkingStatus == CheckingStatus.UNKNOWN_LEAVING) {
	    	component.setForeground(Color.ORANGE);
	    }
	    else {
	    	component.setForeground(Color.BLACK);
	    }

	    return component;
	}
}
