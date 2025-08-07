import java.util.Observable;
import java.util.Observer;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An interface to SAAMS:
 * Refuelling Supervisor Screen:
 * Inputs events from the Refuelling Supervisor, and displays aircraft information.
 * This class is a controller for the AircraftManagementDatabase: sending it messages to change the aircraft status information.
 * This class also registers as an observer of the AircraftManagementDatabase, and is notified whenever any change occurs in that <<model>> element.
 * See written documentation.
 */
public class RefuellingSupervisor implements ActionListener, Observer {
	/**
	 * The Refuelling Supervisor Screen interface has access to the AircraftManagementDatabase.*/

	private JFrame frame;
	private AircraftManagementDatabase aircraftDatabase;

	private JList flightCodesList = new JList(new DefaultListModel());
	private JScrollPane flightCodesScrollList = new JScrollPane(flightCodesList);

	private JButton reFuelButton = new JButton("Plane is Refuelled");

	public RefuellingSupervisor(AircraftManagementDatabase aircraftDatabase) {
		frame = new JFrame("Refuelling Supervisor");

		this.aircraftDatabase = aircraftDatabase;
		aircraftDatabase.addObserver(this);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(290, 400);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new GridLayout(3, 1));

		frame.add(new JLabel("Planes to Clean"));
		frame.add(flightCodesScrollList);
		frame.add(reFuelButton);

		reFuelButton.addActionListener(this);

		frame.setVisible(true);
		frame.setLocation(827, 20);
		updateRefuelList();

	}

	private void updateRefuelList() {
		DefaultListModel theList = (DefaultListModel) flightCodesList.getModel();
		theList.clear();
		for (int i = 0; i < AircraftManagementDatabase.maxMRs; i++) {
			if(aircraftDatabase.getStatus(i) == ManagementRecord.READY_REFUEL) {
				theList.addElement(aircraftDatabase.getMR(i));
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		updateRefuelList();
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == reFuelButton){
			ManagementRecord selectedFlight = (ManagementRecord) flightCodesList.getSelectedValue();
			if(selectedFlight != null) {
				aircraftDatabase.setStatus(selectedFlight.getmCode(), ManagementRecord.READY_PASSENGERS); 
			}
		}

	}

}
