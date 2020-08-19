package tables;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import tables.cell_renderers.CheckingStatusCellRenderer;
import tables.cell_renderers.OvertimeCellRendererManager;
import time.Checking;
import time.CheckingStatus;
import time.DateTimeService;
import time.Overtime;

/**
 * A checkings table.
 * It is possible to add a checking, update a checking or update the entire table.
 *
 * @author Charles MECHERIKI
 *
 */
public class CheckingsTable extends Table {
	private static final long serialVersionUID = 1L;

	private DefaultTableModel model;
	private String[] columnNames = {"ID", "Staff", "Eff. time", "Exp. time", "Status", "Overtime"};
	
	public CheckingsTable(ArrayList<Checking> _checkings) {
		setModel(model = new DefaultTableModel() {	
			private static final long serialVersionUID = 1L;

			@Override
	        public Class<?> getColumnClass(int _columnIndex) {
			   switch (_columnIndex) {
			   		case 0:
			   			return Integer.class;
			   		case 1:
			   			return String.class;
			   		case 2:
			   			return String.class;
			   		case 3:
			   			return String.class;
			   		case 4:
			   			return CheckingStatus.class;
			   		case 5:
			   			return Overtime.class;	
			   		default:
			   			return super.getColumnClass(_columnIndex);
			   }
	        }
		});
		
		for (String columnName : columnNames) {
			model.addColumn(columnName); 
		}

		getColumnModel().getColumn(4).setCellRenderer(new CheckingStatusCellRenderer());
		getColumnModel().getColumn(5).setCellRenderer(OvertimeCellRendererManager.getRenderer());
		
		if (_checkings != null) {
			for (Checking checking : _checkings) {
				model.addRow(createRow(checking));
			}
		}
		
		model.fireTableDataChanged();
	}
	
	public Object[] createRow(Checking _checking) {
		if (_checking != null) {
			return new Object[] {
				_checking.getID(),
				_checking.getStaffMember().toString(),
				DateTimeService.toShortString(_checking.getContext().getEffectiveDateTime()),
				DateTimeService.toShortString(_checking.getContext().getExpectedDateTime()),
				_checking.getContext().getStatus(),
				_checking.getContext().getOvertime()
			};
		}
		return null;
	}
	
	public void setCheckings(ArrayList<Checking> _checkings) {
		while (model.getRowCount() > 0) {
			model.removeRow(0);
		}
		
		if (_checkings != null) {
			for (Checking checking : _checkings) {
				model.addRow(createRow(checking));
			}
		}
		
		model.fireTableDataChanged();
	}
	
	public void replaceCheckingByID(int _checkingID, Checking _checking) {
		
		if (_checking != null) {
			Object[] row = createRow(_checking);
			
			int i = 0;
			while (i < model.getRowCount()) {
	        	if (model.getValueAt(i, 0).equals(_checkingID)) {
	        	    model.removeRow(i);
	        	    model.insertRow(i, row);
	        	}
	        	i++;
	        }
			
			model.fireTableDataChanged();
		}
	}
	
	public void addChecking(Checking _checking) {

		if (_checking != null) {
			Object[] row = createRow(_checking);
			
	        model.insertRow(model.getRowCount(), row);
			model.fireTableDataChanged();
		}
	}
}
