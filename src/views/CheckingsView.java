package views;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.table.JTableHeader;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import company.Department;
import company.Employee;
import toolbox.formatters.DateLabelFormatter;
import swing.panels.FlowPanel;
import swing.panels.GridPanel;
import swing.panels.TablePanel;
import tables.Table;
import time.DateTimeService;
import swing.TimePicker;
import swing.PlaceholdedUnsignedIntField;

/**
 * The checkings view of the application.
 * This view is no more no less the 'Checkings history' tab of the application.
 * Its role is exclusively to handle the writing/reading in/of the swing elements and listen for the user's actions.
 * 
 * Note : see the checkings controller for the list of the possible actions and their implementations.
 *
 * @author Charles MECHERIKI
 *
 */
public class CheckingsView extends View {

	private TablePanel checkingsTablePanel;
	
	private JComboBox<Employee> filterStaffComboBox;
	private JComboBox<Department> filterDepartmentComboBox;
	
	private PlaceholdedUnsignedIntField staffMemberIDUnsignedIntField;
	private PlaceholdedUnsignedIntField correctCheckingIDUnsignedIntField;
	
	private JDatePickerImpl filterFromDatePicker;
	private JDatePickerImpl filterToDatePicker;
	private JDatePickerImpl checkDatePicker;
	private JDatePickerImpl correctCheckingDatePicker;
	private TimePicker checkTimePicker;
	private TimePicker correctCheckingTimePicker;
	
	private JButton applyFiltersButton;
	private JButton resetFiltersButton;
	private JButton checkButton;
	private JButton correctCheckingButton;
	
	private static final long serialVersionUID = 1L;

	public CheckingsView() {
		super("Checkings history");
		build();
	}
	
	public void build() {
		// Properties
		Properties datePickerProperties = new Properties();
		datePickerProperties.put("text.today", "Today");
		datePickerProperties.put("text.month", "Month");
		datePickerProperties.put("text.year", "Year");
		
		// Panels
		GridPanel filtersPanel = new GridPanel(2, 3, GridPanel.defaultPreferedComponentsSize, GridPanel.defaultBorder);
		FlowPanel filterButtonsPanel = new FlowPanel(FlowPanel.defaultPreferedComponentsSize, FlowPanel.defaultBorder);
		GridPanel checkPanel = new GridPanel(3, 3, GridPanel.defaultPreferedComponentsSize, GridPanel.defaultBorder);
		GridPanel correctCheckingPanel = new GridPanel(3, 3, GridPanel.defaultPreferedComponentsSize, GridPanel.defaultBorder);
		checkingsTablePanel = new TablePanel();
		
		// Labels
		JLabel spatialFiltersLabel = new JLabel("Spatial filters");
		JLabel temporalFiltersLabel = new JLabel("Temporal filters");
		JLabel checkLabel = new JLabel("Manual check..");
		JLabel checkingCorrectionLabel = new JLabel("Correct a checking..");
		
		// ComboBoxes
		filterDepartmentComboBox = new JComboBox<Department>();
		filterStaffComboBox = new JComboBox<Employee>();
		
		// Fields
		staffMemberIDUnsignedIntField = new PlaceholdedUnsignedIntField("staff member ID");
		correctCheckingIDUnsignedIntField = new PlaceholdedUnsignedIntField("checking ID");
		
		// Date and time pickers
		filterFromDatePicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(), datePickerProperties), new DateLabelFormatter());
		filterToDatePicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(), datePickerProperties), new DateLabelFormatter());
		checkDatePicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(), datePickerProperties), new DateLabelFormatter());
		checkTimePicker = new TimePicker();
		correctCheckingDatePicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(), datePickerProperties), new DateLabelFormatter());
		correctCheckingTimePicker = new TimePicker();
		
		// Buttons
		applyFiltersButton = new JButton("Apply filters");
		resetFiltersButton = new JButton("Reset filters");
		checkButton = new JButton("Check");
		correctCheckingButton = new JButton("Correct");
		
		filtersPanel.add(spatialFiltersLabel);
		filtersPanel.add(filterDepartmentComboBox);
		filtersPanel.add(filterStaffComboBox);
		filtersPanel.add(temporalFiltersLabel);
		filtersPanel.add(filterFromDatePicker);
		filtersPanel.add(filterToDatePicker);
		
		filterButtonsPanel.add(applyFiltersButton);
		filterButtonsPanel.add(resetFiltersButton);
		
		checkPanel.add(checkLabel);
		checkPanel.add(new JLabel());
		checkPanel.add(new JLabel());
		checkPanel.add(staffMemberIDUnsignedIntField);
		checkPanel.add(checkDatePicker);
		checkPanel.add(checkTimePicker);
		checkPanel.add(new JLabel());
		checkPanel.add(checkButton);
		checkPanel.add(new JLabel());
		
		correctCheckingPanel.add(checkingCorrectionLabel);
		correctCheckingPanel.add(new JLabel());
		correctCheckingPanel.add(new JLabel());
		correctCheckingPanel.add(correctCheckingIDUnsignedIntField);
		correctCheckingPanel.add(correctCheckingDatePicker);
		correctCheckingPanel.add(correctCheckingTimePicker);
		correctCheckingPanel.add(new JLabel());
		correctCheckingPanel.add(correctCheckingButton);
		correctCheckingPanel.add(new JLabel());
		
		add(filtersPanel);
		add(filterButtonsPanel);
		add(checkingsTablePanel);
		add(checkPanel);
		add(correctCheckingPanel);
	}
	
	public void reset() {
		LocalDate today = LocalDate.now(DateTimeService.clock);
		
		filterDepartmentComboBox.setSelectedIndex(0);
		filterStaffComboBox.setSelectedIndex(0);
		
		filterFromDatePicker.getModel().setDate(today.getYear(), 1, 1);
		filterFromDatePicker.getModel().setSelected(true);
		filterToDatePicker.getModel().setDate(today.getYear(), 12, 31);
		filterToDatePicker.getModel().setSelected(true);
		
		correctCheckingDatePicker.getModel().setDate(today.getYear(), today.getMonthValue() - 1, today.getDayOfMonth());
		correctCheckingDatePicker.getModel().setSelected(true);
		correctCheckingTimePicker.setSelectedIndex(34);
		correctCheckingIDUnsignedIntField.reset();
		
		checkDatePicker.getModel().setDate(today.getYear(), today.getMonthValue() - 1, today.getDayOfMonth());
		checkDatePicker.getModel().setSelected(true);
		checkTimePicker.setSelectedIndex(34);
		staffMemberIDUnsignedIntField.reset();
	}

	//==================//
	//		readers		//
	//==================//
	
	public Employee readFilterStaffComboBox() {
		return (Employee)filterStaffComboBox.getSelectedItem();
	}
	
	public Department readFilterDepartmentComboBox() {
		return (Department)filterDepartmentComboBox.getSelectedItem();
	}

	public LocalDate readFilterFromDatePicker() {
		return (LocalDate)((Date)filterFromDatePicker.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public LocalDate readFilterToDatePicker() {
		return (LocalDate)((Date)filterToDatePicker.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public int readStaffMemberIDUnsignedIntField() {
		return staffMemberIDUnsignedIntField.getValue();
	}
	
	public LocalDate readCheckDatePicker() {
		return (LocalDate)((Date)checkDatePicker.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public LocalTime readCheckTimePicker() {
		return (LocalTime)checkTimePicker.getSelectedItem();
	}
	
	public int readCorrectCheckingIDUnsignedIntField() {
		return correctCheckingIDUnsignedIntField.getValue();
	}
	
	public LocalDate readCorrectCheckingDatePicker() {
		return (LocalDate)((Date)correctCheckingDatePicker.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public LocalTime readCorrectCheckingTimePicker() {
		return (LocalTime)correctCheckingTimePicker.getSelectedItem();
	}
	
	//==================//
	//		writers		//
	//==================//
	
	public void writeFilterStaffComboBox(ArrayList<Employee> _staff) {
		filterStaffComboBox.removeAllItems();

		filterStaffComboBox.addItem(Employee.ALL_STAFF_PLACEHOLDER);
		
		for (Employee staffMember : _staff) {
			filterStaffComboBox.addItem(staffMember);
		}
	}
	
	public void writeFilterDepartmentComboBox(ArrayList<Department> _departments) {
		filterDepartmentComboBox.removeAllItems();

		filterDepartmentComboBox.addItem(Department.ALL_DEPARTMENTS_PLACEHOLDER);
		
		for (Department department : _departments) {
			filterDepartmentComboBox.addItem(department);
		}
	}
	
	//==============//
	//		table	//
	//==============//
	
	public void writeCheckingsTable(Table _checkingsTable) {
		_checkingsTable.prepare();
		_checkingsTable.getColumnModel().getColumn(0).setPreferredWidth(1);
		_checkingsTable.getColumnModel().getColumn(5).setPreferredWidth(25);
		
		JScrollPane checkingsTableScrollPane = new JScrollPane(_checkingsTable);
		checkingsTableScrollPane.setPreferredSize(new Dimension(580, 116));
		
		JTableHeader checkingsTableHeader = _checkingsTable.getTableHeader();
		checkingsTableHeader.setPreferredSize(new Dimension(580, 26));

		checkingsTablePanel.removeAll();
		checkingsTablePanel.add(checkingsTableHeader);
		checkingsTablePanel.add(checkingsTableScrollPane);
	}
	
	//==================//
	//		listeners	//
	//==================//
	
	public void addDepartmentsComboBoxListener(ItemListener _itemListener) {
		filterDepartmentComboBox.addItemListener(_itemListener);
	}

	public void addApplyFiltersButtonListener(ActionListener _actionListener) {
		applyFiltersButton.addActionListener(_actionListener);
	}
	
	public void addResetFiltersButtonListener(ActionListener _actionListener) {
		resetFiltersButton.addActionListener(_actionListener);
	}

	public void addCorrectCheckingButtonListener(ActionListener _actionListener) {
		correctCheckingButton.addActionListener(_actionListener);
	}
	
	public void addCheckButtonListener(ActionListener _actionListener) {
		checkButton.addActionListener(_actionListener);
	}
}
