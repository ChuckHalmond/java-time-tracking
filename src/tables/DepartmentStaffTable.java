package tables;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import company.Department;
import company.Employee;
import company.Role;
import tables.cell_renderers.OvertimeCellRendererManager;
import time.Overtime;

/**
 * A department staff table.
 * It is only possible to update the entire table.
 *
 * @author Charles MECHERIKI
 *
 */
public class DepartmentStaffTable extends Table {
	private static final long serialVersionUID = 1L;

	private DefaultTableModel model;
	private String[] columnNames = {"ID", "Name", "Department", "Role", "Overtime"};
	
	public DepartmentStaffTable(Department department) {
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
		
		if (department != null) {
			ArrayList<Employee> staff = department.getStaff();
			for (Employee staffMember : staff) {
				model.addRow(createRow(staffMember));
			}
		}
		
		model.fireTableDataChanged();
	}
	
	private Object[] createRow(Employee _staffMember) {
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
	
	public void setDepartment(Department _department) {
		while (model.getRowCount() > 0) {
			model.removeRow(0);
		}
		
		if (_department != null) {
			ArrayList<Employee> staff = _department.getStaff();
			for (Employee staffMember : staff) {
				model.addRow(createRow(staffMember));
			}
		}
		
		model.fireTableDataChanged();
	}
}
