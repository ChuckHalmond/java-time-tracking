package views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import company.LightweightEmployee;
import swing.panels.GridPanel;
import time.DateTimeService;

/**
 * The unique emulator view.
 * Its role is exclusively to handle the writing/reading in/of the swing elements and listen for the user's actions.
 * 
 * Note : see the emulator controller for the list of the possible actions and their implementations.
 *
 * @author Charles MECHERIKI
 *
 */
public class EmulatorView extends View {
	private static final long serialVersionUID = 1L;
	
	private JComboBox<LightweightEmployee> staffComboBox;
	private JButton checkButton;
	private JButton connectButton;

	private JLabel checkingRequestsLabel;
	private JLabel connectionStateLabel;
	private JLabel currentDateLabel;
	private JLabel currentTimeLabel;
	private JLabel roundedCurrentTimeLabel;
	private JLabel nextExpectedCheckingLabel;

	private final String onlineConnectionStateLabelText = "Online";
	private final String offlineConnectionStateLabelText = "Offline";
	
	private final String onlineConnectButtonText = "Disconnect";
	private final String offlineConnectButtonText = "Connect";
	
	public EmulatorView() {
		super("Emulator view");
		build();
	}
	
	public void build() {
		// Panels
		GridPanel topPanel = new GridPanel(2, 3, new Dimension(175, 26), GridPanel.defaultBorder);
		GridPanel midPanel = new GridPanel(2, 1, new Dimension(560, 26), GridPanel.defaultBorder);
		GridPanel botInfoPanel = new GridPanel(1, 1, new Dimension(535, 26));
		GridPanel botPanel = new GridPanel(1, 2, new Dimension(260, 26), GridPanel.defaultBorder);
		GridPanel innerLeftBotPanel = new GridPanel(1, 2, new Dimension(260, 26));
		GridPanel innerRightBotPanel = new GridPanel(1, 2, new Dimension(180, 26));
		
		// ComboBoxes
		staffComboBox = new JComboBox<LightweightEmployee>();

		// Labels
		checkingRequestsLabel = new JLabel("Checkings on cache : 0");
		connectionStateLabel = new JLabel(offlineConnectionStateLabelText, SwingConstants.CENTER);
		currentDateLabel = new JLabel("", SwingConstants.CENTER);
		currentTimeLabel = new JLabel("", SwingConstants.CENTER);
		roundedCurrentTimeLabel = new JLabel();
		nextExpectedCheckingLabel = new JLabel();
		currentDateLabel.setFont(new Font("Lucida", Font.BOLD, 16));
		currentTimeLabel.setFont(new Font("Lucida", Font.PLAIN, 16));

		// Buttons
		checkButton = new JButton("Check");
		connectButton = new JButton(offlineConnectButtonText);
		
		topPanel.add(checkingRequestsLabel);
		topPanel.add(new JLabel());
		topPanel.add(connectButton);
		
		topPanel.add(new JLabel());
		topPanel.add(new JLabel());
		topPanel.add(connectionStateLabel);
		
		midPanel.add(currentDateLabel);
		midPanel.add(currentTimeLabel);

		innerLeftBotPanel.add(staffComboBox);
		
		botInfoPanel.add(nextExpectedCheckingLabel);
		
		innerRightBotPanel.add(checkButton);
		innerRightBotPanel.add(roundedCurrentTimeLabel);

		botPanel.add(innerLeftBotPanel);
		botPanel.add(innerRightBotPanel);
		
		add(topPanel);
		add(midPanel);
		add(botInfoPanel);
		add(botPanel);
	}
	
	public void reset() {
		staffComboBox.setSelectedIndex(0);
		nextExpectedCheckingLabel.setText("");
	}

	//==================//
	//		readers		//
	//==================//
	
	public LightweightEmployee readStaffComboBox() {
		return (LightweightEmployee)staffComboBox.getSelectedItem();
	}
	
	//==================//
	//		writers		//
	//==================//
	
	public void switchToOnline() {
		connectionStateLabel.setText(onlineConnectionStateLabelText);
		connectButton.setText(onlineConnectButtonText);
	}
	
	public void switchToOffline() {
		connectionStateLabel.setText(offlineConnectionStateLabelText);
		connectButton.setText(offlineConnectButtonText);
	}
	
	public void writeCheckingRequestsLabel(int _checkingRequestsSize) {
		checkingRequestsLabel.setText("Checkings on cache : " + _checkingRequestsSize);
	}
	
	public void clearCheckingRequestsLabel() {
		checkingRequestsLabel.setText("");
	}

	public void writeStaffComboBox(ArrayList<LightweightEmployee> _staff) {
		staffComboBox.removeAllItems();
		
		staffComboBox.addItem(LightweightEmployee.SELECT_STAFFMEMBER_PLACEHOLDER);
		for (LightweightEmployee staffMember : _staff) {
			staffComboBox.addItem(staffMember);
		}
	}
	
	public void writeCurrentDateTextField(LocalDate _date) {
		currentDateLabel.setText(DateTimeService.toString(_date));
	}
	
	public void writeCurrentTimeTextField(LocalTime _time) {
		currentTimeLabel.setText(DateTimeService.toString(_time));
	}
	
	public void writeRoundedCurrentTimeTextField(LocalTime _roundedTime) {
		roundedCurrentTimeLabel.setText("Effective time : " + DateTimeService.toString(_roundedTime));
	}
	
	public void writeNextExpectedCheckingTextField(LocalDateTime _dateTime) {
		nextExpectedCheckingLabel.setText("Next expected checking : " + DateTimeService.toLongString(_dateTime));
	}
	
	//==================//
	//		listeners	//
	//==================//
	
	public void addStaffComboBoxListener(ItemListener _itemListener) {
		staffComboBox.addItemListener(_itemListener);
	}
	
	public void addCheckButtonListener(ActionListener _actionListener) {
		checkButton.addActionListener(_actionListener);
	}
	
	public void addConnectButtonListener(ActionListener _actionListener) {
		connectButton.addActionListener(_actionListener);
	}
}
