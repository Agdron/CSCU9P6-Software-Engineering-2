import java.util.Observable;
import java.util.Observer;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An interface to SAAMS:
 * Cleaning Supervisor Screen:
 * Inputs events from the Cleaning Supervisor, and displays aircraft information.
 * This class is a controller for the AircraftManagementDatabase: sending it messages to change the aircraft status information.
 * This class also registers as an observer of the AircraftManagementDatabase, and is notified whenever any change occurs in that <<model>> element.
 * See written documentation.
 */
public class CleaningSupervisor implements ActionListener, Observer {

	private JFrame frame;
	private AircraftManagementDatabase aircraftDatabase;

	private JList flightCodesList = new JList(new DefaultListModel());
	private JScrollPane flightCodesScrollList = new JScrollPane(flightCodesList);

	private JButton cleanButton = new JButton("Plane is Cleaned");

	public CleaningSupervisor(AircraftManagementDatabase aircraftDatabase) {
		frame = new JFrame("Cleaning Supervisor");

		this.aircraftDatabase = aircraftDatabase;
		aircraftDatabase.addObserver(this);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(290, 400);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new GridLayout(3, 1));

		frame.add(new JLabel("Planes to Clean"));
		frame.add(flightCodesScrollList);
		frame.add(cleanButton);

		cleanButton.addActionListener(this);

		frame.setVisible(true);
		frame.setLocation(390, 205);
		updateToCleanList();

	}

	private void updateToCleanList() {
		DefaultListModel theList = (DefaultListModel) flightCodesList.getModel();
		theList.clear();
		for (int i = 0; i < AircraftManagementDatabase.maxMRs; i++) {
			if(aircraftDatabase.getStatus(i) == ManagementRecord.READY_CLEAN_AND_MAINT ||
					aircraftDatabase.getStatus(i) == ManagementRecord.OK_AWAIT_CLEAN ||
					aircraftDatabase.getStatus(i) == ManagementRecord.FAULTY_AWAIT_CLEAN) {
				theList.addElement(aircraftDatabase.getMR(i));
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		updateToCleanList();
	}

	private int nextStateAfterCleaning(int currentState) {
		if(currentState == ManagementRecord.READY_CLEAN_AND_MAINT) {
			return ManagementRecord.CLEAN_AWAIT_MAINT;
		}
		else if(currentState == ManagementRecord.OK_AWAIT_CLEAN) {
			return ManagementRecord.READY_REFUEL;
		}
		else if(currentState == ManagementRecord.FAULTY_AWAIT_CLEAN) {
			return ManagementRecord.AWAIT_REPAIR;
		}
		return -1;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cleanButton){
			ManagementRecord selectedFlight = (ManagementRecord) flightCodesList.getSelectedValue();
			if(selectedFlight != null) {
				int nextStateAfterCleaning = nextStateAfterCleaning(selectedFlight.getStatus());
				aircraftDatabase.setStatus(selectedFlight.getmCode(), nextStateAfterCleaning); 
			}
		}

	}
	/**
	 * The Cleaning Supervisor Screen interface has access to the AircraftManagementDatabase.
	 */

}

