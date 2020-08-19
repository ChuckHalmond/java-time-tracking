package tables;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import company.Employee;
import company.Role;
import tables.cell_renderers.OvertimeCellRendererManager;
import time.Overtime;

/**
 * A departments table.
 * It is possible to add a staff member, update a staff member or remove a staff member from the table.
 *
 * @author Charles MECHERIKI
 *
 */
public class StaffTable extends Table {
	private static final long serialVersionUID = 1L;

	private DefaultTableModel model;
	private String[] columnNames = {"ID", "Name", "Department", "Role", "Overtime"};
	
	
	public StaffTable(ArrayList<Employee> staff) {
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
			   			return Role.class;
			   		case 4:
			   			return Overtime.class;	
			   		default:
			   			return super.getColumnClass(_columnIndex);
			   }
	        }
		});
		
		for (String columnName : columnNames) {
			model.addColumn(columnName); 
		}
		
		getColumnModel().getColumn(4).setCellRenderer(OvertimeCellRendererManager.getRenderer());

		for (Employee staffMember : staff) {
			model.addRow(createRow(staffMember));
		}
		
		model.fireTableDataChanged();
	}
	
	public Object[] createRow(Employee _staffMember) {
		if (_staffMember != null) {
			return new Object[] {
				_staffMember.getID(),
				_staffMember.getName(),
				_staffMember.getDepartment().toString(),
				_staffMember.getRole().toString(),
				_staffMember.getOvertime()
			};
		}
		return null;
	}
	
	public void updateStaffMember(Employee _staffMember) {
		Object[] row = createRow(_staffMember);

		int i = 0;
        while (i < model.getRowCount()) {
        	if (model.getValueAt(i, 0).equals(_staffMember.getID())) {
        	    model.removeRow(i);
        	    model.insertRow(i, row);
        	    return;
        	}
        	i++;
        }
	    
	    model.fireTableDataChanged();
	}
	
	public void addStaffMember(Employee _staffMember) {
		Object[] row = createRow(_staffMember);
		
	    model.addRow(row);
	    model.fireTableDataChanged();
	}
	
	public void removeStaffMemberByID(int _staffMemberID) {
		int i = 0;
        while (i < model.getRowCount()) {
        	if (model.getValueAt(i, 0).equals(_staffMemberID)) {
        	    model.removeRow(i);
        	    return;
        	}
        	i++;
        }
	}
}
