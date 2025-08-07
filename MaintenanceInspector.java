import java.util.Observable;
import java.util.Observer;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An interface to SAAMS:
 * Maintenance Inspector Screen:
 * Inputs events from the Maintenance Inspector, and displays aircraft information.
 * This class is a controller for the AircraftManagementDatabase: sending it messages to change the aircraft status information.
 * This class also registers as an observer of the AircraftManagementDatabase, and is notified whenever any change occurs in that <<model>> element.
 */

public class MaintenanceInspector implements ActionListener, Observer{

	private JFrame frame;
	private AircraftManagementDatabase aircraftDatabase;

	private JList flightCodesList = new JList(new DefaultListModel());
	private JScrollPane flightCodesScrollList = new JScrollPane(flightCodesList);

	private JButton maintenanceButton = new JButton("Maintenance Done No Fault");
	private JButton reportfaultButton = new JButton("Report Fault");
	private JButton faultFixedButton = new JButton("Repair Complete");

	private JLabel faultDes = new JLabel("Fault Description");
	
	private JTextArea planeFaultTextInput = new JTextArea();
	private JScrollPane planeFaultScroll = new JScrollPane(planeFaultTextInput, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	public MaintenanceInspector(AircraftManagementDatabase aircraftDatabase) {
		frame = new JFrame("Maintenance Inspector");

		this.aircraftDatabase = aircraftDatabase;
		aircraftDatabase.addObserver(this);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(400, 400);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new GridLayout(1,2));

		JPanel normalPanel = new JPanel(new GridLayout(3, 1));
		normalPanel.add(new JLabel("Planes for Maintenance Check"));
		normalPanel.add(flightCodesScrollList);
		normalPanel.add(maintenanceButton);

		JPanel faultyPanel = new JPanel(new GridLayout(4, 1));
		faultyPanel.add(faultDes);
		faultyPanel.add(planeFaultScroll);
		faultyPanel.add(reportfaultButton);
		faultyPanel.add(faultFixedButton);

		frame.add(normalPanel);
		frame.add(faultyPanel);

		maintenanceButton.addActionListener(this);
		reportfaultButton.addActionListener(this);
		faultFixedButton.addActionListener(this);

		frame.setVisible(true);
		frame.setLocation(0, 205);
		updateMaintenanceList();

	}

	private void updateMaintenanceList() {
		DefaultListModel theList = (DefaultListModel) flightCodesList.getModel();
		theList.clear();
		for (int i = 0; i < AircraftManagementDatabase.maxMRs; i++) {
			if(aircraftDatabase.getStatus(i) == ManagementRecord.READY_CLEAN_AND_MAINT ||
					aircraftDatabase.getStatus(i) == ManagementRecord.CLEAN_AWAIT_MAINT) {
				theList.addElement(aircraftDatabase.getMR(i));
			}
			else if(aircraftDatabase.getStatus(i) == ManagementRecord.AWAIT_REPAIR ||
					aircraftDatabase.getStatus(i) == ManagementRecord.FAULTY_AWAIT_CLEAN) {
				theList.addElement(aircraftDatabase.getMR(i));
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		updateMaintenanceList();

	}

	private int nextStateAfterCheck(int currentState) {
		if(currentState == ManagementRecord.READY_CLEAN_AND_MAINT) {
			return ManagementRecord.OK_AWAIT_CLEAN;
		}
		else if(currentState == ManagementRecord.CLEAN_AWAIT_MAINT) {
			return ManagementRecord.READY_REFUEL;
		}
		return -1;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		ManagementRecord selectedFlight = (ManagementRecord) flightCodesList.getSelectedValue();
		if(selectedFlight != null) {
			if (e.getSource() == maintenanceButton){
				int nextStateAfterCheck = nextStateAfterCheck(selectedFlight.getStatus());
				aircraftDatabase.setStatus(selectedFlight.getmCode(), nextStateAfterCheck); 
			}
			else if (e.getSource() == reportfaultButton){
				if(planeFaultTextInput.getText().equals("")) {
					faultDes.setForeground(Color.red);
				} else {
					aircraftDatabase.faultsFound(selectedFlight.getmCode(), planeFaultTextInput.getText());
					planeFaultTextInput.setText(null);
					faultDes.setForeground(Color.black);
				}
				
			}
			else if (e.getSource() == faultFixedButton){
				aircraftDatabase.setStatus(selectedFlight.getmCode(), ManagementRecord.READY_CLEAN_AND_MAINT);
			}
		}
	}

}

