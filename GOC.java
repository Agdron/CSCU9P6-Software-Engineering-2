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
 * A Ground Operations Controller Screen:
 * Inputs events from GOC (a person), and displays aircraft and gate information.
 * This class is a controller for the GateInfoDatabase and the AircraftManagementDatabase: sending them messages to change the gate or aircraft status information.
 * This class also registers as an observer of the GateInfoDatabase and the AircraftManagementDatabase, and is notified whenever any change occurs in those <<model>> elements.
 * See written documentation.
 */
public class GOC implements ActionListener, Observer, ListSelectionListener {
	/** The Ground Operations Controller Screen interface has access to the GateInfoDatabase.*/
	private GateInfoDatabase gateInfoDatabase;
	/**
	 * The Ground Operations Controller Screen interface has access to the AircraftManagementDatabase.
	 */
	private JFrame frame;
	private AircraftManagementDatabase aircraftDatabase;

	private JList flightCodesList = new JList(new DefaultListModel());
	private JScrollPane flightCodesScrollList = new JScrollPane(flightCodesList);

	private JList gateStatusList = new JList(new DefaultListModel());
	private JScrollPane gateStatusScrollList = new JScrollPane(gateStatusList);

	private JTextArea planeDetailsTextArea = new JTextArea();
	private JScrollPane planeDetailsScroll = new JScrollPane(planeDetailsTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	private JButton grantGroundClearanceButton = new JButton("Grant Ground Clearance");
	private JButton taxiToSelectedGateButton = new JButton("Taxi to selected gate");
	private JButton grantTaxiRunwayClearanceButton = new JButton("Grant Taxi Runway Clearance");

	public GOC (GateInfoDatabase gateInfoDatabase, AircraftManagementDatabase aircraftDatabase) {


		frame = new JFrame("Ground Operations Controller");
		this.gateInfoDatabase = gateInfoDatabase;
		this.aircraftDatabase = aircraftDatabase;
		aircraftDatabase.addObserver(this);
		gateInfoDatabase.addObserver(this);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(440, 400);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());

		planeDetailsTextArea.setEditable(false);

		// Panel for planes list
		JPanel panel1 = new JPanel(new GridLayout(2, 1));
		panel1.add(new JLabel("Planes:"));
		panel1.add(flightCodesScrollList);


		// Panel for plane details
		JPanel panel2 = new JPanel(new GridLayout(2, 1));
		panel2.add(new JLabel("Plane Details:"));
		panel2.add(planeDetailsScroll);

		// Panel for gate status list
		JPanel panel3 = new JPanel(new GridLayout(2, 1));
		panel3.add(new JLabel("Gate Status:"));
		panel3.add(gateStatusScrollList);

		// Panel for the buttons
		JPanel panel4 = new JPanel(new GridLayout(3, 1));
		panel4.add(grantGroundClearanceButton);
		panel4.add(taxiToSelectedGateButton);
		panel4.add(grantTaxiRunwayClearanceButton);

		grantTaxiRunwayClearanceButton.addActionListener(this);
		grantGroundClearanceButton.addActionListener(this);
		taxiToSelectedGateButton.addActionListener(this);

		flightCodesList.addListSelectionListener(this);

		JPanel mainPanel = new JPanel(new GridLayout(2, 2));

		mainPanel.add(panel1);
		mainPanel.add(panel2);
		mainPanel.add(panel3);
		mainPanel.add(panel4);

		frame.add(mainPanel);
		frame.setVisible(true);  
		frame.setLocation(1100, 425);

		updateGateStatusList();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ManagementRecord selectedFlight = (ManagementRecord) flightCodesList.getSelectedValue();
		if(selectedFlight != null) {
			if (e.getSource() == grantGroundClearanceButton){
				aircraftDatabase.setStatus(selectedFlight.getmCode(), ManagementRecord.GROUND_CLEARANCE_GRANTED); 
			}
			else if (e.getSource() == grantTaxiRunwayClearanceButton){
				aircraftDatabase.setStatus(selectedFlight.getmCode(), ManagementRecord.AWAITING_TAKEOFF);
			}
			else if (e.getSource() == taxiToSelectedGateButton){
				int gateNumber = gateStatusList.getSelectedIndex();
				if(gateNumber != -1 && selectedFlight.getStatus() == ManagementRecord.LANDED) {
					aircraftDatabase.taxiTo(selectedFlight.getmCode(), gateNumber);
					gateInfoDatabase.allocate(gateNumber, selectedFlight.getmCode());
				}
			}
		}



	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
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
		updateGateStatusList();
		DefaultListModel theList = (DefaultListModel) flightCodesList.getModel();
		theList.clear();
		for (int i = 0; i < AircraftManagementDatabase.maxMRs; i++) {
			if(aircraftDatabase.getStatus(i) != ManagementRecord.FREE && aircraftDatabase.getStatus(i) != ManagementRecord.IN_TRANSIT) {
				theList.addElement(aircraftDatabase.getMR(i));
			}
		}
		planeDetailsTextArea.setText("");

	}

	private void updateGateStatusList() {

		int[] gateStatuses = gateInfoDatabase.getStatuses();
		DefaultListModel theList = (DefaultListModel) gateStatusList.getModel();
		theList.clear();

		for (int gateNumber = 0; gateNumber < gateStatuses.length; gateNumber++) {

			String gateStatus = null;

			if(gateStatuses[gateNumber] == Gate.FREE) {
				gateStatus = "Gate " + gateNumber + ": FREE";
			}
			else if(gateStatuses[gateNumber] == Gate.RESERVED) {
				gateStatus = "Gate " + gateNumber + ": RESERVED";
			}
			else if(gateStatuses[gateNumber] == Gate.OCCUPIED) {
				gateStatus = "Gate " + gateNumber + ": OCCUPIED";
			}

			theList.addElement(gateStatus);
		}
	}


}