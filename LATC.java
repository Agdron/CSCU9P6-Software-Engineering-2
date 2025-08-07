import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;


/**
 * An interface to SAAMS:
 * Local Air Traffic Controller Screen:
 * Inputs events from LATC (a person), and displays aircraft information.
 * This class is a controller for the AircraftManagementDatabase: sending it messages to change the aircraft status information.
 * This class also registers as an observer of the AircraftManagementDatabase, and is notified whenever any change occurs in that <<model>> element.
 * See written documentation.
 */
public class LATC implements ActionListener, Observer, ListSelectionListener {
	/**
	 *  The Local Air Traffic Controller Screen interface has access to the AircraftManagementDatabase.*/
	private JFrame frame;
	private AircraftManagementDatabase aircraftDatabase;

	private JList flightCodesList = new JList(new DefaultListModel());
	private JScrollPane flightCodesScrollList = new JScrollPane(flightCodesList);

	private JTextArea planeDetailsTextArea = new JTextArea();
	private JScrollPane planeDetailsScroll = new JScrollPane(planeDetailsTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	private JButton clearToApproachButton = new JButton("Clear to approach & land");
	private JButton planeLandedButton = new JButton("Confirm plane has landed");
	private JButton allocateDepartureButton = new JButton("Allocated Departure Slot");
	private JButton permitTakeoffButton = new JButton("Permit Takeoff");

	public LATC(AircraftManagementDatabase aircraftDatabase) {
		frame = new JFrame("LATC Controller");
		this.aircraftDatabase = aircraftDatabase;
		aircraftDatabase.addObserver(this);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(440, 400);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());

		planeDetailsTextArea.setEditable(false);

		// panel for planes
		JPanel panel1 = new JPanel(new GridLayout(2, 1));
		panel1.add(new JLabel("Planes:"));
		panel1.add(flightCodesScrollList);


		// panel for plane details
		JPanel panel2 = new JPanel(new GridLayout(2, 1));
		panel2.add(new JLabel("Plane Details:"));
		panel2.add(planeDetailsScroll);


		JPanel panel3 = new JPanel(new GridLayout(2, 1));

		panel3.add(clearToApproachButton);
		panel3.add(planeLandedButton);

		JPanel panel4 = new JPanel(new GridLayout(2, 1));
		panel4.add(allocateDepartureButton);
		panel4.add(permitTakeoffButton);


		clearToApproachButton.addActionListener(this);
		planeLandedButton.addActionListener(this);
		allocateDepartureButton.addActionListener(this);
		permitTakeoffButton.addActionListener(this);

		flightCodesList.addListSelectionListener(this);

		JPanel mainPanel = new JPanel(new GridLayout(2, 2));

		mainPanel.add(panel1);
		mainPanel.add(panel2);
		mainPanel.add(panel3);
		mainPanel.add(panel4);

		frame.add(mainPanel);
		frame.setVisible(true);  
		frame.setLocation(670, 425);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		System.out.println("yo");
		if(e.getSource() == flightCodesList) {
			ManagementRecord selectedMR = (ManagementRecord) flightCodesList.getSelectedValue();
			if(selectedMR != null) {
				planeDetailsTextArea.setText(selectedMR.getDetails());
				System.out.println(selectedMR.toString());
			}
		}

	}

	@Override
	public void update(Observable o, Object arg) {
		DefaultListModel theList = (DefaultListModel) flightCodesList.getModel();
		theList.clear();
		for (int i = 0; i < AircraftManagementDatabase.maxMRs; i++) {
			if(aircraftDatabase.getStatus(i) != ManagementRecord.FREE) {
				theList.addElement(aircraftDatabase.getMR(i));
			}
		}
		planeDetailsTextArea.setText("");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ManagementRecord selectedFlight = (ManagementRecord) flightCodesList.getSelectedValue();
		if(selectedFlight != null) {
			if (e.getSource() == clearToApproachButton){
				aircraftDatabase.setStatus(selectedFlight.getmCode(), ManagementRecord.LANDING); 
			}
			else if (e.getSource() == planeLandedButton){
				aircraftDatabase.setStatus(selectedFlight.getmCode(), ManagementRecord.LANDED);
			}
			else if (e.getSource() == allocateDepartureButton){
				aircraftDatabase.setStatus(selectedFlight.getmCode(), ManagementRecord.AWAITING_TAXI); 
			}
			else if (e.getSource() == permitTakeoffButton){
				aircraftDatabase.setStatus(selectedFlight.getmCode(), ManagementRecord.DEPARTING_THROUGH_LOCAL_AIRSPACE);
			}
		}
	}

}