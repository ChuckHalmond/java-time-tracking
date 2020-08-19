package tables.cell_renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import time.Overtime;
import toolbox.Constants;

/**
 * A cell renderer which highlights the checkings table 'Overtime' row inconsistencies, like an overtime beyond the incident threshold.
 *
 * @author Charles MECHERIKI
 *
 */
public class OvertimeCellRenderer extends DefaultTableCellRenderer {

	private int incidentThreshold = Constants.DEFAULT_INCIDENT_THRESHOLD;
	
	private static final long serialVersionUID = 1L;
	
	public Component getTableCellRendererComponent(JTable _table, Object _value, boolean _isSelected, boolean _hasFocus, int _row, int _column) {
		Component component = super.getTableCellRendererComponent(_table, _value, _isSelected, _hasFocus, _row, _column);

        int modelRow = _table.getRowSorter().convertRowIndexToModel(_row);
		
		Overtime overtime = (Overtime)_table.getModel().getValueAt(modelRow, _column);

	    try {
	    	long overtimeMinutes = overtime.toMinutes();
	    	
		    if (overtimeMinutes >= incidentThreshold) {
		    	component.setForeground(Color.BLUE);
		    }
		    else if (overtimeMinutes <= -incidentThreshold) {
		    	component.setForeground(Color.RED);
		    }
		    else {
		    	component.setForeground(Color.BLACK);
		    }
	    }
	    catch (Exception _exception) {
	    	// The overtime cannot be converted to number, styling it is useless.
	    }

	    return component;
	}

	public void setIncidentThreshold(int _incidentThreshold) {
		incidentThreshold = _incidentThreshold;
	}
}
