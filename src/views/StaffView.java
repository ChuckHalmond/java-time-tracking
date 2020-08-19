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
import company.Employee;
import company.Role;
import swing.PlaceholdedTextField;
import swing.panels.FlowPanel;
import swing.panels.GridPanel;
import swing.panels.TablePanel;
import tables.Table;

/**
 * The staff view of the application.
 * This view is no more no less the 'Staff management' tab of the application.
 * Its role is exclusively to handle the writing/reading in/of the swing elements and listen for the user's actions.
 * 
 * Note : see the staff controller for the list of the possible actions and their implementations.
 *
 * @author Charles MECHERIKI
 *
 */
public class StaffView extends View {
	private static final long serialVersionUID = 1L;
	
	private PlaceholdedTextField firstnameTextField;
	private PlaceholdedTextField lastNameTextField;
	
	private JComboBox<Employee> staffComboBox;
	private JComboBox<Department> assignDepartmentsComboBox;
	private JComboBox<Role> assignRolesComboBox;
	private JComboBox<Role> gradeRolesComboBox;
	private JComboBox<Department> recruitDepartmentsComboBox;
	private JComboBox<Role> recruitRolesComboBox;
	
	private JButton recruitButton;
	private JButton resetButton;
	private JButton importFromCSVButton;
	private JButton exportToCSVButton;
	private JButton assignButton;
	private JButton gradeButton;
	private JButton dismissButton;
	
	private TablePanel staffTablePanel;
	
	public StaffView() {
		super("Staff management");
		build();
	}

	public void build() {
		// Panels
		GridPanel addStaffPanel = new GridPanel(3, 3, GridPanel.defaultPreferedComponentsSize, GridPanel.defaultBorder);
		FlowPanel importExportPanel = new FlowPanel(FlowPanel.defaultPreferedComponentsSize, FlowPanel.defaultBorder);
		GridPanel updateStaffPanel = new GridPanel(5, 3, GridPanel.defaultPreferedComponentsSize, GridPanel.defaultBorder);
		staffTablePanel = new TablePanel();
		
		// Labels
		JLabel recruitStaffLabel = new JLabel("Recruit a staff member..");
		JLabel assignLabel = new JLabel("Assign a staff member..");
		JLabel gradeLabel = new JLabel("Grade a staff member..");
		
		// Fields
		firstnameTextField = new PlaceholdedTextField("firstname");
		lastNameTextField = new PlaceholdedTextField("lastname");
		
		// ComboBoxes
		recruitDepartmentsComboBox = new JComboBox<Department>();
		recruitRolesComboBox = new JComboBox<Role>();
		staffComboBox = new JComboBox<Employee>();
		assignDepartmentsComboBox = new JComboBox<Department>();
		assignRolesComboBox = new JComboBox<Role>();
		gradeRolesComboBox = new JComboBox<Role>();
		
		// Buttons
		resetButton = new JButton("Reset fields");
		recruitButton = new JButton("Recruit");
		importFromCSVButton = new JButton("Import from CSV");
		exportToCSVButton = new JButton("Export to CSV");
		assignButton = new JButton("Assign");	
		gradeButton = new JButton("Grade");
		dismissButton = new JButton("Dismiss");
		
		addStaffPanel.add(recruitStaffLabel);
		addStaffPanel.add(new JLabel());
		addStaffPanel.add(new JLabel());
		addStaffPanel.add(firstnameTextField);
		addStaffPanel.add(lastNameTextField);
		addStaffPanel.add(resetButton);
		addStaffPanel.add(recruitDepartmentsComboBox);
		addStaffPanel.add(recruitRolesComboBox);
		addStaffPanel.add(recruitButton);
		
		importExportPanel.add(importFromCSVButton);
		importExportPanel.add(exportToCSVButton);
		
		updateStaffPanel.add(new JLabel());
		updateStaffPanel.add(staffComboBox);
		updateStaffPanel.add(new JLabel(""));
		updateStaffPanel.add(assignLabel);
		updateStaffPanel.add(new JLabel());
		updateStaffPanel.add(new JLabel());
		updateStaffPanel.add(assignDepartmentsComboBox);
		updateStaffPanel.add(assignRolesComboBox);
		updateStaffPanel.add(assignButton);
		updateStaffPanel.add(gradeLabel);
		updateStaffPanel.add(new JLabel());
		updateStaffPanel.add(new JLabel());
		updateStaffPanel.add(gradeRolesComboBox);
		updateStaffPanel.add(gradeButton);
		updateStaffPanel.add(dismissButton);
		
		add(addStaffPanel);	
		add(importExportPanel);
		add(staffTablePanel);
		add(updateStaffPanel);
	}
	
	public void reset() {
		firstnameTextField.reset();
		lastNameTextField.reset();
		recruitDepartmentsComboBox.setSelectedIndex(0);
		recruitRolesComboBox.setSelectedIndex(0);
		staffComboBox.setSelectedIndex(0);
		assignDepartmentsComboBox.setSelectedIndex(0);
		assignRolesComboBox.setSelectedIndex(0);
		gradeRolesComboBox.setSelectedIndex(0);
	}
	
	//==================//
	//		readers		//
	//==================//
	
	public String readRecruitFirstnameTextField() {
		return firstnameTextField.getText();
	}
	
	public String readRecruitLastnameTextField() {
		return lastNameTextField.getText();
	}
	
	public Department readRecruitDepartmentComboBox() {
		return (Department)recruitDepartmentsComboBox.getSelectedItem();
	}
	
	public Role readRecruitRoleComboBox() {
		return (Role)recruitRolesComboBox.getSelectedItem();
	}
	
	public Employee readStaffComboBox() {
		return (Employee)staffComboBox.getSelectedItem();
	}
	
	public Department readAssignDepartmentsComboBox() {
		return (Department)assignDepartmentsComboBox.getSelectedItem();
	}
	
	public Role readAssignRolesComboBox() {
		return (Role)assignRolesComboBox.getSelectedItem();
	}
	
	public Role readGradeRolesComboBox() {
		return (Role)gradeRolesComboBox.getSelectedItem();
	}
	
	//==================//
	//		writers		//
	//==================//
	
	public void writeRecruitDepartmentsComboBox(ArrayList<Department> _departments) {
		recruitDepartmentsComboBox.removeAllItems();
		
		recruitDepartmentsComboBox.addItem(Department.SELECT_DEPARTMENT_PLACEHOLDER);
		for (Department department : _departments) {
			recruitDepartmentsComboBox.addItem(department);
		}
	}

	public void writeAddRolesComboBox(ArrayList<Role> _roles) {
		recruitRolesComboBox.removeAllItems();
		
		recruitRolesComboBox.addItem(Role.SELECT_ROLE_PLACEHOLDER);
		for (Role role : _roles) {
			recruitRolesComboBox.addItem(role);
		}
	}
	
	public void writeStaffComboBox(ArrayList<Employee> _employees) {
		staffComboBox.removeAllItems();
		
		staffComboBox.addItem(Employee.SELECT_STAFFMEMBER_PLACEHOLDER);
		for (Employee employee : _employees) {
			staffComboBox.addItem(employee);
		}
	}
	
	public void writeAssignDepartmentsComboBox(ArrayList<Department> _departments) {
		assignDepartmentsComboBox.removeAllItems();
		
		assignDepartmentsComboBox.addItem(Department.SELECT_DEPARTMENT_PLACEHOLDER);
		for (Department department : _departments) {
			assignDepartmentsComboBox.addItem(department);
		}
	}
	
	public void writeAssignRolesComboBox(ArrayList<Role> _roles) {
		assignRolesComboBox.removeAllItems();

		assignRolesComboBox.addItem(Role.SELECT_ROLE_PLACEHOLDER);
		for (Role role : _roles) {
			assignRolesComboBox.addItem(role);
		}
	}
	
	public void writeGradeRolesComboBox(ArrayList<Role> _roles) {
		gradeRolesComboBox.removeAllItems();
		
		gradeRolesComboBox.addItem(Role.SELECT_ROLE_PLACEHOLDER);
		for (Role role : _roles) {
			gradeRolesComboBox.addItem(role);
		}
	}
	
	public void writeStaffTable(Table staffTable) {
		staffTable.prepare();
		staffTable.getColumnModel().getColumn(0).setPreferredWidth(1);
		
		JScrollPane staffTableScrollPane = new JScrollPane(staffTable);
		staffTableScrollPane.setPreferredSize(new Dimension(500, 116));
		
		JTableHeader staffTableHeader = staffTable.getTableHeader();
		staffTableHeader.setPreferredSize(new Dimension(500, 26));

		staffTablePanel.removeAll();
		staffTablePanel.add(staffTableHeader);
		staffTablePanel.add(staffTableScrollPane);
	}
	
	//==================//
	//		listeners	//
	//==================//
	
	public void addRecruitButtonListener(ActionListener _actionListener) {
		recruitButton.addActionListener(_actionListener);
	}

	public void addResetButtonListener(ActionListener _actionListener) {
		resetButton.addActionListener(_actionListener);
	}
	
	public void addImportFromCSVButtonListener(ActionListener _actionListener) {
		importFromCSVButton.addActionListener(_actionListener);
	}
	
	public void addExportToCSVButtonListener(ActionListener _actionListener) {
		exportToCSVButton.addActionListener(_actionListener);
	}
	
	public void addStaffComboBoxListener(ItemListener _itemListener) {
		staffComboBox.addItemListener(_itemListener);
	}

	public void addAssignButtonListener(ActionListener _actionListener) {
		assignButton.addActionListener(_actionListener);
	}
	
	public void addGradeButtonListener(ActionListener _actionListener) {
		gradeButton.addActionListener(_actionListener);
	}
	
	public void addDismissButtonListener(ActionListener _actionListener) {
		dismissButton.addActionListener(_actionListener);
	}
}
