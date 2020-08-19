package tables;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import company.Department;
import tables.cell_renderers.DepartmentHeadCellRenderer;

/**
 * A departments table.
 * It is possible to add a department, update a department or remove a department from the table.
 *
 * @author Charles MECHERIKI
 *
 */
public class DepartmentsTable extends Table {
	private static final long serialVersionUID = 1L;

	private DefaultTableModel model;
	private String[] columnNames = {"ID", "Name", "Head Manager", "Managers", "Employees"};
	
	public DepartmentsTable(ArrayList<Department> _departments) {
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
			   			return Integer.class;
			   		case 4:
			   			return Integer.class;	
			   		default:
			   			return super.getColumnClass(_columnIndex);
			   }
	        }
		});
		
		for (String columnName : columnNames) {
			model.addColumn(columnName); 
		}
		
		getColumnModel().getColumn(2).setCellRenderer(new DepartmentHeadCellRenderer());
		
		if (_departments != null) {
			for (Department department : _departments) {
				model.addRow(createRow(department));
			}
		}
		
		model.fireTableDataChanged();
	}
	
	public Object[] createRow(Department _department) {
		if (_department != null) {
			return new Object[] {
				_department.getID(),
				_department.getName(),
				(_department.getHeadManager() == null) ? "NONE" : _department.getHeadManager().getName(),
				_department.getManagers().size(),
				_department.getEmployees().size()
			};
		}
		return null;
	}
	
	public void addDepartment(Department _department) {
		
		if (_department != null) {
			Object[] row = createRow(_department);
			
		    model.addRow(row);
		    model.fireTableDataChanged();
		}
	}
	
	public void removeDepartmentByID(int _departmentID) {

		int i = 0;
        while (i < model.getRowCount()) {
        	if (model.getValueAt(i, 0).equals(_departmentID)) {
        	    model.removeRow(i);
        	    return;
        	}
        	i++;
        }
	}
	
	public void updateDepartment(Department _department) {	
		Object[] row = createRow(_department);
		
		int i = 0;
		while (i < model.getRowCount()) {
        	if (model.getValueAt(i, 0).equals(_department.getID())) {
        	    model.removeRow(i);
        	    model.insertRow(i, row);
        	}
        	i++;
        }
	    
	    model.fireTableDataChanged();
	}
}
