package views;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.table.JTableHeader;

import company.Department;
import swing.PlaceholdedTextField;
import swing.panels.GridPanel;
import swing.panels.TablePanel;
import tables.Table;

/**
 * The departments view of the application.
 * This view is no more no less the 'Departments management' tab of the application.
 * Its role is exclusively to handle the writing/reading in/of the swing elements and listen for the user's actions.
 * 
 * Note : see the departments controller for the list of the possible actions and their implementations.
 *
 * @author Charles MECHERIKI
 *
 */
public class DepartmentsView extends View {
	private static final long serialVersionUID = 1L;
	
	private PlaceholdedTextField nameTextField;
	private PlaceholdedTextField renameTextField;
	
	private JComboBox<Department> departmentComboBox;
	
	private JButton addButton;
	private JButton renameButton;
	private JButton removeButton;
	
	private TablePanel departmentsTablePanel;
	private TablePanel departmentStaffTablePanel;
	
	public DepartmentsView() {
		super("Departments management");
		build();
	}

	public void build() {
		// Panels
		GridPanel addDepartmentPanel = new GridPanel(1, 3, GridPanel.defaultPreferedComponentsSize, GridPanel.defaultBorder);
		GridPanel updateDepartmentPanel = new GridPanel(3, 3, GridPanel.defaultPreferedComponentsSize, GridPanel.defaultBorder);
		departmentsTablePanel = new TablePanel();
		departmentStaffTablePanel = new TablePanel();
		
		// Labels
		JLabel addDepartmentLabel = new JLabel("Add a department");
		JLabel updateDepartmentLabel = new JLabel("Update a department..");
		
		// Fields
		nameTextField = new PlaceholdedTextField("name");
		renameTextField = new PlaceholdedTextField("new name");
		
		// ComboBoxes
		departmentComboBox = new JComboBox<Department>();
		
		// Buttons
		addButton = new JButton("Add");
		renameButton = new JButton("Rename");
		removeButton = new JButton("Remove");

		addDepartmentPanel.add(addDepartmentLabel);
		addDepartmentPanel.add(nameTextField);
		addDepartmentPanel.add(addButton);	

		updateDepartmentPanel.add(updateDepartmentLabel);
		updateDepartmentPanel.add(new JLabel());
		updateDepartmentPanel.add(new JLabel());
		updateDepartmentPanel.add(departmentComboBox);
		updateDepartmentPanel.add(renameTextField);
		updateDepartmentPanel.add(renameButton);
		updateDepartmentPanel.add(new JLabel());
		updateDepartmentPanel.add(removeButton);
		updateDepartmentPanel.add(new JLabel());
		
		add(addDepartmentPanel);
		add(departmentsTablePanel);
		add(updateDepartmentPanel);
		add(departmentStaffTablePanel);
	}
	
	public void reset() {
		nameTextField.reset();
		departmentComboBox.setSelectedIndex(0);
		renameTextField.reset();
	}
	
	//==================//
	//		readers		//
	//==================//
	
	public String readNameTextField() {
		return nameTextField.getText();
	}
	
	public String readRenameTextField() {
		return renameTextField.getText();
	}
	
	public Department readDepartmentComboBox() {
		return (Department)departmentComboBox.getSelectedItem();
	}
	
	//==================//
	//		writers		//
	//==================//
	
	public void writeDepartmentsComboBox(ArrayList<Department> _departments) {
		departmentComboBox.removeAllItems();
		
		departmentComboBox.addItem(Department.SELECT_DEPARTMENT_PLACEHOLDER);
		for (Department department : _departments) {
			departmentComboBox.addItem(department);
		}
	}
	
	//==========================//
	//		departments table	//
	//==========================//
	
	public void writeDepartmentsTable(Table _departmentsTable) {
		_departmentsTable.prepare();
		_departmentsTable.getColumnModel().getColumn(0).setPreferredWidth(1);
		_departmentsTable.getColumnModel().getColumn(3).setPreferredWidth(1);
		_departmentsTable.getColumnModel().getColumn(4).setPreferredWidth(1);
		
		JScrollPane departmentsTableScrollPane = new JScrollPane(_departmentsTable);
		departmentsTableScrollPane.setPreferredSize(new Dimension(500, 116));
		
		JTableHeader departmentsTableHeader = _departmentsTable.getTableHeader();
		departmentsTableHeader.setPreferredSize(new Dimension(500, 26));

		departmentsTablePanel.removeAll();
		departmentsTablePanel.add(departmentsTableHeader);
		departmentsTablePanel.add(departmentsTableScrollPane);
	}
	
	//==============================//
	//		staff department table	//
	//==============================//
	
	public void writeDepartmentStaffTable(Table _departmentStaffTable) {
		_departmentStaffTable.prepare();
		_departmentStaffTable.getColumnModel().getColumn(0).setPreferredWidth(1);
		_departmentStaffTable.getColumnModel().getColumn(4).setPreferredWidth(1);
		
		JScrollPane departmentStaffTableScrollPane = new JScrollPane(_departmentStaffTable);
		departmentStaffTableScrollPane.setPreferredSize(new Dimension(500, 116));
		
		JTableHeader departmentStaffTableHeader = _departmentStaffTable.getTableHeader();
		departmentStaffTableHeader.setPreferredSize(new Dimension(500, 26));

		departmentStaffTablePanel.removeAll();
		departmentStaffTablePanel.add(departmentStaffTableHeader);
		departmentStaffTablePanel.add(departmentStaffTableScrollPane);
	}
	
	//==================//
	//		listeners	//
	//==================//

	public void addButtonListener(ActionListener _actionListener) {
		addButton.addActionListener(_actionListener);
	}

	public void departmentsComboBoxListener(ItemListener _itemListener) {
		departmentComboBox.addItemListener(_itemListener);
	}
	
	public void renameButtonListener(ActionListener _actionListener) {
		renameButton.addActionListener(_actionListener);
	}

	public void removeButtonListener(ActionListener _actionListener) {
		removeButton.addActionListener(_actionListener);
	}
}